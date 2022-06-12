
package swing.stroke;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;

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