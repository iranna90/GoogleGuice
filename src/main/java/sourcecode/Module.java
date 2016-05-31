package sourcecode;

import com.google.inject.AbstractModule;

import annotation.CircleAnnotation;
import annotation.RectanggleAnnotation;

public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(Shape.class).annotatedWith(CircleAnnotation.class).to(Circle.class);
		bind(Shape.class).annotatedWith(RectanggleAnnotation.class).to(Rectangle.class);
	}

}
