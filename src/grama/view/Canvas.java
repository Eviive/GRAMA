package grama.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JPanel;
import grama.model.Link;
import grama.model.LinkType;
import grama.model.Node;
import grama.model.NodeType;
import java.awt.BasicStroke;

/**
 * The class of the JPanel we will use to do the graph visualization
 * @author BAUDRY Lilian
 * @author VAILLON Albert
 * @version JDK 11.0.13
 */
public class Canvas extends JPanel {
	
	private Graphics2D graphic;
	
	private List<Node> nodesList = new ArrayList<>();
	
	private List<Node> nodesDisplay = new ArrayList<>();
	private List<NodeType> nodesType = new ArrayList<>();
	
	private List<Link> linksDisplay = new ArrayList<>();
	private List<LinkType> linksType = new ArrayList<>();
	
	private HashMap<String, Point> positions = new HashMap<>();
	
	private Object hover = null;
	private Node[] selected = new Node[2];
	
	public Canvas() {
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Object PreviousHover = hover;
				
				hover = getNode(e.getPoint());
				if (hover == null)
					hover = getLink(e.getPoint());
				
				if (PreviousHover != hover) {
					repaint();
				}
			}
		});
	}
	
	@Override
	public void paint(Graphics g){
		graphic = (Graphics2D)g;
		super.paint(graphic);
		
		setNodesLocation();
		
		for (Link link: linksDisplay) {
			if (linksType.contains(link.getType()))
				drawLink(link);
		}
		
		for (Node node: nodesDisplay) {
			if (nodesType.contains(node.getType()))
				drawNode(node);
		}
	}
	
	/**
	 * The method used to draw a single <code>Link</code>
	 * @param link The <code>Link</code> we'll draw
	 */
	public void drawLink(Link link) {		
		Point coords = positions.get(link.getDeparture().getName());

		Point destination = positions.get(link.getDestination().getName());

		graphic.setColor(link.getType().getColor());
		graphic.setStroke(link.getType().getStroke());

		graphic.drawLine(coords.x, coords.y, destination.x, destination.y);
		graphic.setStroke(new BasicStroke());

		Point center = new Point((coords.x + destination.x)/2 , (coords.y + destination.y)/2);
		if (link == hover)
			graphic.setFont(new Font("sans serif", Font.BOLD, 12));
		else
			graphic.setFont(new Font("sans serif", Font.PLAIN, 12));

		graphic.setColor(Color.BLACK);
		String info = Integer.toString(link.getDistance());
		graphic.drawString(info, center.x - graphic.getFontMetrics().getDescent()*info.length()/2, center.y-10);

		if (link == hover)
			graphic.setFont(new Font("sans serif", Font.PLAIN, 12));
	}
	
	/**
	 * The method used to draw a single <code>Node</code>
	 * @param node The <code>Node</code> we'll draw
	 */
	public void drawNode(Node node) {
		Point coords = positions.get(node.getName());
		
		graphic.setColor(new Color(248, 244, 244));
		graphic.fillOval(coords.x - 15,coords.y - 15, 30, 30);
		
		graphic.setColor(Color.BLACK);
		if (selected[0] == node || selected[1] == node) {
			if (node == hover) {
				graphic.setColor(new Color(248, 244, 244));
				graphic.fillOval(coords.x - 20,coords.y - 20, 40, 40);
				graphic.setColor(Color.BLACK);
				graphic.drawOval(coords.x - 20,coords.y - 20, 40, 40);
			} else {
				graphic.drawOval(coords.x - 15,coords.y - 15, 30, 30);
			}
		}
		
		if (node == hover)
			graphic.drawImage(node.getType().getImage(), coords.x - 20, coords.y - 20, 40, 40, null);
		else
			graphic.drawImage(node.getType().getImage(), coords.x - 15, coords.y - 15, 30, 30, null);
		
		graphic.setFont(new Font("sans serif", Font.PLAIN, 12));
		graphic.drawString(node.getName(), coords.x - graphic.getFontMetrics().getDescent()*node.getName().length(), coords.y-20);
	}
	
	/**
	 * @param pos The coordinates of the mouse
	 * @return Returns the <code>Node</code> at the position at the position <code>pos</code> or <code>null</code> if there's nothing
	 */
	public Node getNode(Point pos){
		for(Node node : nodesDisplay){
			Point coords = positions.get(node.getName());
			if (coords.distance(pos.x, pos.y) < 20 && nodesType.contains(node.getType())){
				return node;
			}
		}
		return null;
	}
	
	/**
	 * @param pos The coordinates of the mouse
	 * @return Returns the <code>Link</code> at the position at the position <code>pos</code> or <code>null</code> if there's nothing
	 */
	public Link getLink(Point pos){
		for(Link link : linksDisplay){
			
			Point departure = positions.get(link.getDeparture().getName());
			Point destination = positions.get(link.getDestination().getName());
			
			Point center = new Point((departure.x + destination.x)/2 , (departure.y + destination.y)/2);
			if (center.distance(pos.x, pos.y) < 30){
				return link;
			}
		}
		return null;
	}	
	
	/**
	 * @param nodes The <code>List</code> of <code>Nodes</code> we'll display in the visualization
	 */
	public void initNodes(List<Node> nodes){
		nodesList = nodes;
		setNodesLocation();
	}
	
	/**
	 * Determines the position of all the <code>Nodes</code> we have to display according to their ratios
	 */
	public void setNodesLocation(){
		for (Node node : nodesList) {
			Point center = new Point((int)(getWidth() * node.getRatioX()) , (int)(getHeight() * node.getRatioY()));
			positions.put(node.getName(), center);
		}
	}
	
	/**
	 * @param nodes The <code>List</code> of <code>Nodes</code> we'll display in the visualization
	 * @param links The <code>List</code> of <code>Links</code> we'll display in the visualization
	 */
	public void setDisplay(List<Node> nodes, List<Link> links) {
		nodesDisplay = nodes;
		linksDisplay = links;
		repaint();
	}
	
	/**
	 * @param nodes The <code>List</code> of <code>Nodes</code> we'll display in the visualization
	 */
	public void setDisplayNodes(List<Node> nodes){
		nodesDisplay = nodes;
		repaint();
	}
	
	/**
	 * @param links The <code>List</code> of <code>Links</code> we'll display in the visualization
	 */
	public void setDisplayLinks(List<Link> links){
		linksDisplay = links;
		repaint();
	}
	
	/**
	 * @param nodesType The <code>List</code> of <code>Node</code> types we'll display in the visualization
	 */
	public void setNodesType(List<NodeType> nodesType) {
		this.nodesType = nodesType;
	}
	
	/**
	 * @param linksType The <code>List</code> of <code>Link</code> types we'll display in the visualization
	 */
	public void setLinksType(List<LinkType> linksType) {
		this.linksType = linksType;
	}
	
	/**
	 * @return Returns the currently hovered element
	 */
	public Object getHover() {
		return hover;
	}
	
	/**
	 * Resets the graph visualization
	 */
	public void reset() {
		resetSelected();
		nodesList.clear();
		nodesDisplay.clear();
		linksDisplay.clear();
		positions.clear();
		repaint();
	}
	
	/**
	 * @param i An integer equals to either 0 or 1
	 * @return Returns the selected element at index <code>i</code>
	 */
	public Node getSelected(int i) {
		return selected[i];
	}
	
	/**
	 * @param i An integer equals to either 0 or 1
	 * @param node The <code>Node</code> we want to add at index <code>i</code>
	 */
	public void addSelected(int i, Node node) {
		selected[i] = node;
		repaint();
	}
	
	/**
	 * @param node The <code>Node</code> we want to deselect
	 */
	public void removeSelected(Node node) {
		for (int i = 0; i < selected.length; i++)
			if (node.equals(selected[i]))
				selected[i] = null;
		repaint();
	}
	
	/**
	 * Resets the selection
	 */
	public void resetSelected() {
		selected[0] = null;
		selected[1] = null;
	}
	
	/**
	 * @param node The <code>Node</code> we want to check
	 * @return Returns <code>true</code> if the <code>Node</code> is selected otherwise <code>false</code>
	 */
	public boolean isSelected(Node node) {
		for (Node item: selected)
			if (node.equals(item))
				return true;
		return false;
	}
	
}