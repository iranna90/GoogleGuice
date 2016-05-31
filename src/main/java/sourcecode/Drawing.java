package sourcecode;

import com.google.inject.Inject;

import annotation.CircleAnnotation;
import annotation.RectanggleAnnotation;

public class Drawing {

	@Inject
	@RectanggleAnnotation
	public Shape shape;

	public void startDrawing() {
		System.out.println("started drawing.");
		shape.draw();
		System.out.println("done");
	}
}
