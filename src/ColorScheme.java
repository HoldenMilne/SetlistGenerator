

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;


public class ColorScheme implements ActionListener	{
	
	public Window window;
	public static ArrayList<ColorScheme> schemes = new ArrayList<ColorScheme>();
	public static ArrayList<ColorScheme> builtInSchemes = new ArrayList<ColorScheme>();
	
	
	final static String RIGHT = "right";
	final static String LEFT = "left";
	final static String ACCENT1 = "accent1";
	final static String BAR = "bar";
	final static String ACCENT2 = "accent2";
	final static String FONT = "font";
	ArrayList<MyColor> colors = new ArrayList<MyColor>();
	public MyColor c_right = new MyColor(0xD2DD7E,RIGHT); // Green
	public MyColor c_left = new MyColor(0x75C7B2,LEFT); // Teal
	public MyColor c_accent1 = new MyColor(0x304463,ACCENT1); // Blue
	public MyColor c_bar = new MyColor(0xFAE9C7,BAR); // Soft Pink
	public MyColor c_accent2 = new MyColor(0xE0647B,ACCENT2); // Hard Pink
	public MyColor c_font = new MyColor(0xE0647B,FONT); // Hard Pink
	public Color c_clear = new Color(255,255,255,0); // Hard Pink
	
	public static int colorCount = 6;
	
	public Font f1,f2,f3;
	public String name;
	
	public static File defaultSchemesFile = null;
	public static File customSchemesFile = null;
	
	public ColorScheme(String s, MyColor c1,MyColor c2,MyColor c3,MyColor c4, MyColor c5, MyColor c6)
	{
		name = s;
		colors.add(c1);
		colors.add(c2);
		colors.add(c3);
		colors.add(c4);
		colors.add(c5);
		colors.add(c6);
	}
	
	public ColorScheme(String s, Iterable<MyColor> color_array)
	{
		name = s;
		for(MyColor e : color_array) {
			colors.add(e);
		}
	}
	
	public static ColorScheme SchemeFactory(String s)
	{
		for(ColorScheme c : builtInSchemes)
		{
			if(c.name.equalsIgnoreCase(s))
			{
				return c;
			}
		}
		
		for(ColorScheme c : schemes)
		{
			if(c.name.equalsIgnoreCase(s))
			{
				return c;
			}
		}
		
		return builtInSchemes.get(0);
	}
	
	public static void LoadDefaultSchemes()
	{
		//Right Panel, Left Panel, Accent1, Bar, Accent2,Font e2b03d 71acc1
		//db - 2c3f5a tc -AF7o5c g - 1e6347 y - f7d755 b -569588 AF705c
		if(defaultSchemesFile == null) 
		{
			builtInSchemes.add(new ColorScheme("Default",new MyColor(0x26a033,RIGHT),new MyColor(0x066922,LEFT),new MyColor(0xffffff,ACCENT1),new MyColor(0x066922,BAR),new MyColor(0xffd455,ACCENT2),new MyColor(0xf1f1f1,FONT)));
			builtInSchemes.add(new ColorScheme("Geometrix",new MyColor(0xEEF0F8,RIGHT),new MyColor(0x71acc1,LEFT),new MyColor(0xa0a2af,ACCENT1),new MyColor(0x71acc1,BAR),new MyColor(0x58857e,ACCENT2),new MyColor(0xFAFAFA,FONT)));
			builtInSchemes.add(new ColorScheme("Space",new MyColor(0xffd6fa,RIGHT),new MyColor(0xfafafa,LEFT),new MyColor(0x6dc8f2,ACCENT1),new MyColor(0xffd6fa,BAR),new MyColor(0xf27db2,ACCENT2),new MyColor(0x1,FONT)));
			builtInSchemes.add(new ColorScheme("Simple",new MyColor(0xFEFBFC,RIGHT),new MyColor(0xef9b34,LEFT),new MyColor(0xffd6fa,ACCENT1),new MyColor(0xef9b34,BAR),new MyColor(0xef9b34,ACCENT2),new MyColor(0xffffff,FONT)));
			builtInSchemes.add(new ColorScheme("BW",new MyColor(0xFFFFFF,RIGHT),new MyColor(0x000000,LEFT),new MyColor(0xFFFFFF,ACCENT1),new MyColor(0x000000,BAR),new MyColor(0x000000,ACCENT2),new MyColor(0xFFFFFF,FONT)));
			builtInSchemes.add(new ColorScheme("Night",new MyColor(0x5f6c77,RIGHT),new MyColor(0xeeeeee,LEFT),new MyColor(0x6666b0,ACCENT1),new MyColor(0xeeeeee,BAR),new MyColor(0xaba0FF,ACCENT2),new MyColor(0x555555,FONT)));
			builtInSchemes.add(new ColorScheme("Forest",new MyColor(0x667666,RIGHT),new MyColor(0xeeeeee,LEFT),new MyColor(0xeeeeee,ACCENT1),new MyColor(0x74645f/*bfc2bb*/,BAR),new MyColor(0x55443f,ACCENT2),new MyColor(0x33554f,FONT)));
		}	
	}
	
	public static void LoadSchemes()
	{
		// NOT IN USE
		if(schemes.isEmpty())
			return;
		if(!schemes.isEmpty())
			return;
		BufferedReader br = null;
		try {
			//if(!customSchemesFile.isDirectory())
				//customSchemesFile.createDirectory();
			
			DirectoryStream<Path> dir = Files.newDirectoryStream(Paths.get(customSchemesFile.getPath()));
		
			for(Path p : dir)
			{
				String name = p.getFileName().toString();
				
				if(name.endsWith(".thm"))
				{
					br = new BufferedReader(new FileReader(p.toString()));
					MyColor[] arr = new MyColor[7];
					int i = 0;
					
					while(br.ready())
					{
						String s = br.readLine();
						if(i == arr.length)
							break;
						String n = "";
						switch(i)
						{
							case 0:
								n = RIGHT;
								break;
							case 1:
								n = LEFT;
								break;
							case 2:
								n = ACCENT1;
								break;
							case 3:
								n = BAR;
								break;
							case 4:
								n = ACCENT2;
								break;
							case 5:
								n = FONT;
								break;
						}
						arr[i] = new MyColor(Integer.valueOf(s,16),n);
						
						i++;
					}
					schemes.add(new ColorScheme(name.substring(0,name.indexOf(".")),arr[0],arr[1],arr[2],arr[3],arr[4], arr[5]));
				}
			}
		
		} catch (IOException e) {
				e.printStackTrace();
			}finally
		{
				if(br!=null)
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		}
		
	}
	
	public MyColor FindColor(String c_name)
	{
		for(MyColor c : colors)
		{
			if(c.name.equals(c_name))
				return c;
		}
		
		try {
			throw new ColorNotFoundException();
		} catch (ColorNotFoundException e) {
			e.printStackTrace();
		}
		
		return FindColor(0);
	}
	
	public MyColor FindColor(int i)
	{
		if(i<colors.size())
			return colors.get(i);
		
		try {
			throw new ColorNotFoundException();
		} catch (ColorNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * Usage: To add a color to an existing color scheme.
	 * @param name - Name of the new color.  On Conflict an error will be thrown
	 * @param hex - Hex code of the color.  This is the color that will be displayed
	 * @throws - If the name parameter is already in use by a color, an exception will be thrown.
	 */
	public void AddColor(String name, int hex)
	{
		for(MyColor c : colors)
		{
			if(c.name.equals(name))
			{
				System.out.println("COLOR NAME ALREADY EXISTS");
				return;
			}
		}
		colors.add(new MyColor(hex,name));
	}
	
	private void UpdateColors(Component component, ColorScheme newScheme) {
		SetColor(component,newScheme);
		if(component instanceof Container)
		{
			for(Component c : ((Container) component).getComponents())
			{
				UpdateColors(c,newScheme);
			}
		}
		if(component instanceof Container)
		{
			for(Component c : ((Container) component).getComponents())
			{
				UpdateColors(c,newScheme);
			}
		}
	}
	
	private void SetColor(Component component, ColorScheme newScheme) {

		boolean set = false;
		System.out.print(component.getClass() +" : ");
		if(component instanceof Canvas || component instanceof JLabel)
			return;
		System.out.print(component.isBackgroundSet()+" "+component.getBackground().getClass()+" : \n");
		if(component.isBackgroundSet() && component.getBackground() instanceof MyColor)
		{
			//TODO: THis is the solution
			component.setBackground(newScheme.FindColor(((MyColor)component.getBackground()).name));
			
		}
		
		if(component.isForegroundSet()&& component.getForeground() instanceof MyColor)
		{	
			component.setForeground(newScheme.FindColor(((MyColor)component.getForeground()).name));
		}
		
		if(component instanceof JComponent && ((JComponent) component).getBorder()!=null)
		{
			Border b = ((JComponent)component).getBorder();
			if(b instanceof LineBorder)
			{
				// Get the color of the line border
				((JComponent)component).setBorder(BorderFactory.createLineBorder(newScheme.FindColor(((MyColor)component.getBackground()).name),((LineBorder) b).getThickness()));
				 
				 
			} else if(b instanceof BevelBorder)
			{
				System.out.println(((BevelBorder)b).getHighlightOuterColor());
				System.out.println(((BevelBorder)b).getHighlightInnerColor());
				System.out.println(((BevelBorder)b).getShadowOuterColor());
				System.out.println(((BevelBorder)b).getShadowInnerColor());
				Color hi;
				Color ho;
				Color si;
				Color so;
				
				if(((BevelBorder)b).getHighlightInnerColor() instanceof MyColor && ((BevelBorder)b).getHighlightOuterColor() instanceof MyColor &&
						((BevelBorder)b).getShadowInnerColor() instanceof MyColor && ((BevelBorder)b).getShadowOuterColor() instanceof MyColor)
				{	
					hi = newScheme.FindColor(((MyColor)((BevelBorder)b).getHighlightInnerColor()).name);
					ho = newScheme.FindColor(((MyColor)((BevelBorder)b).getHighlightOuterColor()).name);
					si = newScheme.FindColor(((MyColor)((BevelBorder)b).getShadowInnerColor()).name);
					so = newScheme.FindColor(((MyColor)((BevelBorder)b).getShadowOuterColor()).name);
				
					((JComponent)component).setBorder(new BevelBorder(((BevelBorder)b).getBevelType(),ho,hi,so,si));
				}else if(((BevelBorder)b).getHighlightInnerColor() instanceof MyColor && ((BevelBorder)b).getShadowOuterColor() instanceof MyColor)
				{
					hi = newScheme.FindColor(((MyColor)((BevelBorder)b).getHighlightInnerColor()).name);
					so = newScheme.FindColor(((MyColor)((BevelBorder)b).getShadowOuterColor()).name);
				
					((JComponent)component).setBorder(new BevelBorder(((BevelBorder)b).getBevelType(),hi,so));
				}
				
			} else if(b instanceof CompoundBorder && component.getBackground() instanceof MyColor)
			{
				Border bI = ((CompoundBorder) b).getInsideBorder();
				Border bO = ((CompoundBorder) b).getOutsideBorder();
				//SetColor((Component) bI, newScheme);
				//SetColor((Component) bO, newScheme);
			}
			
			
		}
	}
	
	public ColorScheme FindScheme(String name)
	{
		for(ColorScheme scheme : builtInSchemes)
		{
			if(scheme.name.equals(name))
				return scheme;
		}
		
		for(ColorScheme scheme : schemes)
		{
			if(scheme.name.equals(name))
				return scheme;
		}
		
		System.out.println("End of Built in and Custom Schemes.  No scheme was found.");
		System.out.println("Returning the built in scheme at pos 0");

		return builtInSchemes.get(0);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		String name = e.getActionCommand();
		ColorScheme sch = FindScheme(name);
		System.out.println(sch.name+" "+name);
		UpdateColors(window, sch);
		UpdateColors(window.getJMenuBar(), sch);
	}
}
