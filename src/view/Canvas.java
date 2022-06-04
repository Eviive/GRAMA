/*

 */
package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import structure.Link;
import structure.Node;

/**
 * @author BAUDRY Lilian
 * @version JDK 11.0.13
 */
public class Canvas extends JPanel {
	
	Graphics2D graphic;
	
	private List<Node> nodesList = new ArrayList<>();
	private List<Node> nodesDisplay = new ArrayList<>();
	private List<Link> linksDisplay = new ArrayList<>();
	private Map<String , Point> positions = new HashMap<>();
	
	private Object hover = null;
	
	public Canvas(){
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				
				Object PreviousHover = hover;
				
				hover = getNode(e.getPoint());
				if (hover == null)
					hover = getLink(e.getPoint());
				
				if (PreviousHover != hover){
					PreviousHover = hover;
					repaint();
				}
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.out.println(hover);
			}
		});
	}
	
	@Override
	public void paint(Graphics g){
		
		graphic = (Graphics2D)g;
		super.paint(graphic);
		
		setNodesLocation();
		
		for(Link link : linksDisplay){
			drawLink(link);
		}
		
		for(Node node : nodesDisplay){
			drawNode(node);
		}	
	}
	
	public void drawLink(Link link){
		Point coords = positions.get(link.getDeparture().getName());

		if (nodesDisplay.contains(link.getDestination())){
			Point destination = positions.get(link.getDestination().getName());
			graphic.setColor(link.getType().getColor());
			graphic.drawLine(coords.x, coords.y, destination.x, destination.y);

			Point center = new Point((coords.x + destination.x)/2 , (coords.y + destination.y)/2);
			if (link == hover)
				graphic.setFont(new Font("sans serif",Font.BOLD,12));
			else
				graphic.setFont(new Font("sans serif",Font.PLAIN,12));
			
			graphic.setColor(Color.BLACK);
			String info = String.format("%c : %d", link.getType().getCharID(), link.getDistance());
			graphic.drawString(info, center.x - graphic.getFontMetrics().getDescent()*info.length()/2, center.y-10);
			
			if (link == hover)
				graphic.setFont(new Font("sans serif",Font.PLAIN,12));

		}else{
			System.err.println("Warning: Destination node not displayed!");
		}
	}
	
	public void drawNode(Node node){
		Point coords = positions.get(node.getName());
			
		graphic.setColor(node.getType().getColor());
		
		if (node == hover)
			graphic.fillOval(coords.x-7,coords.y-7, 14, 14);
		else
			graphic.fillOval(coords.x-5,coords.y-5, 10, 10);

		graphic.setColor(Color.BLACK);
		graphic.drawString(node.getName(), coords.x - graphic.getFontMetrics().getDescent()*node.getName().length(), coords.y-10);
	}
	
	public Node getNode(Point pos){
		for(Node node : nodesDisplay){
			Point coords = positions.get(node.getName());
			if (Point.distance(pos.x, pos.y, coords.x, coords.y) < 20){
				return node;
			}
		}
		return null;
	}
	
	public Link getLink(Point pos){
		for(Link link : linksDisplay){
			
			Point departure = positions.get(link.getDeparture().getName());
			Point destination = positions.get(link.getDestination().getName());;
			
			Point center = new Point((departure.x + destination.x)/2 , (departure.y + destination.y)/2);
			if (Point.distance(pos.x, pos.y, center.x, center.y) < 30){
				return link;
			}
		}
		return null;
	}	
	
	public void initNodes(List<Node> nodes){
		nodesList = nodes;
		setNodesLocation();
	}
	
	public void setNodesLocation(){
		double angle;
		for (int i = 0 ; i<nodesList.size() ; i++){
			angle = PI * 2 / nodesList.size() * i ;
			positions.put(nodesList.get(i).getName() , CirclePosition(angle));
		}
	}
	
	public Point CirclePosition(double theta) {
		
		Double scaleFactor;
		
		if (getWidth()>getHeight())
			scaleFactor = getHeight()/2.2;
		else
			scaleFactor = getWidth()/2.2;
		
		int x = (int) (getWidth()/2 + scaleFactor * Math.cos(theta));
		int y = (int) (getHeight()/2 + scaleFactor * Math.sin(theta));
		
		return new Point(x,y);
	}
	
	public void setDisplayNodes(List<Node> nodes){
		nodesDisplay = nodes;
		repaint();
	}
	
	public void setDisplayLinks(List<Link> links){
		linksDisplay = links;
		repaint();
	}
	
}
