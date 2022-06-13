
package swing.stroke;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

/**
 * The class that we will use to represent highways in the graph visualization
 * @version JDK 11.0.13
 * @author BAUDRY Lilian
 */
public class DoubleStroke implements Stroke {
	
	BasicStroke stroke1;
	BasicStroke stroke2;
	
	public DoubleStroke(float width, float spacing) {
		stroke1 = new BasicStroke(width);
		stroke2 = new BasicStroke(spacing);
	}
	
	@Override
	public Shape createStrokedShape(Shape s) {
		Shape outline = stroke1.createStrokedShape(s);
		return stroke2.createStrokedShape(outline);
	}
	
}