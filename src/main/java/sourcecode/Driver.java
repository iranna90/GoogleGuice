package sourcecode;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Driver {

	public static void main(String[] args){
		Injector in = Guice.createInjector(new Module());
		System.out.print("Started");
		Drawing drawing = in.getInstance(Drawing.class);
		drawing.startDrawing();
	}
}
