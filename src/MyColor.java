

import java.awt.Color;

public class MyColor extends Color {

	private static final long serialVersionUID = 1L;
	public String name;

	public MyColor(int arg0,String name) {
		super(arg0);
		this.name = name;
	}
	
	public MyColor setColor(int i)
	{
		return new MyColor(i,name);
	}

}
