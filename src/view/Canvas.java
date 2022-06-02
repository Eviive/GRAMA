/*

 */
package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import static java.lang.Math.PI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import structure.Link;
import structure.Node;

/**
 *
 * @author lilia
 */
public class Canvas extends JPanel {
	
	private List<Node> nodesList = new ArrayList<>();
	private List<Node> nodesDisplay = new ArrayList<>();
	private Map<String , Point> positions = new HashMap<>();
	
	@Override
	public void paint(Graphics g){
		
		Graphics2D graphic = (Graphics2D)g;
		super.paint(graphic);
		
		double angle;
		for (int i = 0 ; i<nodesList.size() ; i++){
			angle = PI * 2 / nodesList.size() * i ;
			positions.put(nodesList.get(i).getName() , CirclePosition(angle));
		}
	
		for(Node node : nodesDisplay){
			Point coords = positions.get(node.getName());
			
			graphic.setColor(node.getType().getColor());
			graphic.fillRect(coords.x-10,coords.y-10, 20, 20);
			
			for(Link link : node.getNodeLinks()){
				
				if (nodesDisplay.contains(link.getDestination())){
					Point destination = positions.get(link.getDestination().getName());
					graphic.setColor(link.getType().getColor());
					graphic.drawLine(coords.x, coords.y, destination.x, destination.y);
				}
			}
		}
	}
	
	public void initNodes(List<Node> nodes){
		nodesList = nodes;
	}
	
	public void setNodes(List<Node> nodes){
		nodesDisplay = nodes;
		repaint();
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
	
}
