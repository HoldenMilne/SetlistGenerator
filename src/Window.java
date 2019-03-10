import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

public class Window extends JFrame implements ActionListener {

	// Paths
	public static String OS = System.getProperty("os.name");
	public static String HOME_DIRECTORY;// = System.getProperty("user.home");
	public static String WORKING_DIRECTORY = "SetlistGenerator";
	public static Path themesDirectory;
	public static Path previousRunsDirectory;
	// Files
	static File AllSetlists = new File("AllSets.txt");
	static File AllSongs;
	static File importSetlist;
	public boolean init=false;
	JTextArea jta_result;
	
	// Color Scheme
	ColorScheme scheme;
	PopupWindow popup;
	static int iteration = 0;
	static ArrayList<String> setlist = new ArrayList<String>();

	private static ArrayList<String> songs = new ArrayList<String>();
	//used to hold the list of songs while songs gets changed
	static ArrayList<String> allSongs = new ArrayList<String>();
	private static ArrayList<Integer> songStartCount = new ArrayList<Integer>();
	private static int[][] songPairs;
	
	JTextArea list;
	private static String set;
	
	static JTextArea ip;
	
	static int runs = 0;
	
	int listPos = 0;
	
	private boolean used = false;
	// Constructors
	// Default Contructor (Main Menu)
	public Window()
	{
		this("Home");
	}
	
	// String Set Constructor
	public Window(String name)
	{
		super(name);
		if(!init)
		{
			try {
				Initialize();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Get the Content Pane to build on
		Container contentPane = this.getContentPane();
		
		// Dimensions
		Dimension button_dim_1 = new Dimension(100,40);
		Dimension textarea_dim_1 = new Dimension(300,320);
		Dimension textarea_dim_2 = new Dimension(60,22);
		
		switch(name)
		{
			case "Home":
				
				// Top Level Panels
				JPanel top = new JPanel(); // Generate button mostly
					top.setBackground(scheme.c_left);
				JPanel bottom = new JPanel(); // Results from generation
					top.setBackground(scheme.c_right);	
				
				// JButtons
				JButton jb_gen = new JButton("Generate");
					jb_gen.setPreferredSize(button_dim_1);
					jb_gen.addActionListener(this);
					jb_gen.setBackground(scheme.c_accent1);
					scheme.AddColor("S",1);
				// JTextAreas
				jta_result = new JTextArea("");
					//jta_result.setPreferredSize(textarea_dim_1);
					jta_result.setEditable(false);
					jta_result.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					
				JScrollPane scrollPane = new JScrollPane(jta_result);
					scrollPane.setPreferredSize(textarea_dim_1);
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				
				ip = new JTextArea("test");
					ip.setPreferredSize(textarea_dim_2);
					BevelBorder bb_ip = new BevelBorder(BevelBorder.LOWERED,scheme.FindColor(1), scheme.FindColor(2));
					ip.setBorder(bb_ip);
				// JMenuBar
				JMenuBar bar = new JMenuBar();
			
				// JMenuItems & such
				JMenu file = new JMenu("File");
				JMenuItem themes = new JMenu("Themes");
					themes.addActionListener(this);
					
				JMenuItem newSet = new JMenuItem("Build Setlist");
					newSet.addActionListener(this);
					newSet.setBackground(scheme.FindColor(ColorScheme.ACCENT1));
				JMenuItem modify = new JMenuItem("Modify");
					modify.addActionListener(this);
				
				JMenuItem use = new JMenuItem("Use");
					use.addActionListener(this);
					
				JMenuItem sList = new JMenuItem("Song List");
					sList.addActionListener(this);
				
				JMenuItem exit = new JMenuItem("Exit");
					exit.addActionListener(this);	
					
				for(ColorScheme c : ColorScheme.builtInSchemes)
				{
					JMenuItem item = new JMenuItem(c.name);
					item.addActionListener(scheme);
					themes.add(item);
					
				}
				file.add(newSet);
				file.add(modify);
				file.add(sList);
				file.add(use);
				file.add(themes);
				file.add(new JSeparator(JSeparator.HORIZONTAL));
				file.add(exit);
				
				bar.add(file);

				top.add(new JLabel("Set Size: "));
				top.add(ip);
				top.add(jb_gen);
				bottom.add(scrollPane);
				
				// Add the panels
				contentPane.add(top, BorderLayout.NORTH);
				contentPane.add(bottom, BorderLayout.SOUTH);
				
				// Set the menu bar
				setJMenuBar(bar);
				
				// Close Operation
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				break;
		}
		pack();
		this.setVisible(true);
	}
	
	private void Initialize() throws IOException {
		// TODO Auto-generated method stub
		JOptionPane.showMessageDialog(null,"A file or directory is missing and must be rebuild");
		if(!new File(WORKING_DIRECTORY).exists())
		{
			new File(WORKING_DIRECTORY).mkdirs();
		}
		
		if(AllSongs == null)
		{
			AllSongs = new File(WORKING_DIRECTORY+ File.separator+"songs.txt"); //Set up a fileChoose
			
		}
		
		if(!AllSongs.exists())
		{
			AllSongs.createNewFile();
		}
		
		if(AllSetlists == null)
		{
			AllSetlists = new File(WORKING_DIRECTORY+ File.separator+"setlists.txt");
		}
		
		if(!AllSetlists.exists())
		{
			AllSetlists.createNewFile();
		}
		
		ColorScheme.LoadDefaultSchemes();
		scheme = ColorScheme.SchemeFactory("Geometrix");
		
		for(int i = 0; i <10; i++)
			allSongs.add("Hello "+i);
		init = true;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		String name = arg0.getActionCommand();
		
		switch(name)
		{
			case "Generate":
				if(!(ip.getText().equals("")) && !(ip.getText().equals("Enter A Number")))
					Generate();
				else
					JOptionPane.showMessageDialog(null, "Please Select how many songs to choose");
			
				break;
			case "Build Setlist":
				/*
				while(songs.size()>0)
					songs.remove(0);
				while(setlist.size()>0)
					setlist.remove(0);
				while(allSongs.size()>0)
					allSongs.remove(0);
				Generate();
				dispose();
				*/
				
				break;
			case "Song List":
				System.out.println(name);
				new PopupWindow("Song List");
				break;
			case "Use":
				WriteToFile();
				break;
			case "Exit":
				if(!used && !jta_result.getText().equals(""))
				{
					int jo = JOptionPane.showConfirmDialog(null, "The Setlist hasn't been used yet.  Would you like to use this setlist?");
					if(jo == JOptionPane.YES_OPTION)
					{
						WriteToFile();
						dispose();
					}else if (jo == JOptionPane.NO_OPTION)
					{
						dispose();
					}
				}else
				{
					dispose();
				}
				break;
			case "Modify":
				popup = new PopupWindow("Modify");
				
				break;
			
		}

	}
	
	private void Generate() {
		// TODO Auto-generated method stub
		while(songs.size()>0)
			songs.remove(0);
		while(setlist.size()>0)
			setlist.remove(0);
		while(allSongs.size()>0)
			allSongs.remove(0);
		
		iteration = Integer.valueOf(ip.getText());
		System.out.println("Setlist # " + (++runs));
		Load();
		for(String s:songs)
			allSongs.add(s);
		
		CompileFrequencies("starter");
		CompileFrequencies("body");
		
		Selector("starter");
		Selector("body");
		
		//WriteToFile();
		BuildText();
	}
	
	private void BuildText() {
		
		set = "";
		for(int l = 0; l<setlist.size(); l++)
			set += (l+1)+ ": " + setlist.get(l) + "\n";
		
		jta_result.setText(set);
		
	}
	
	// Load from a file, all the songs a band has
	private static void Load() {
		Scanner sc = null;
		File file = AllSongs;
		
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(sc.hasNextLine())
		{
			songs.add(sc.nextLine());
		}
		sc.close();
		
		for(@SuppressWarnings("unused") String s:songs)
		{
			songStartCount.add(1);
		}
		
		songPairs = new int[songs.size()][songs.size()];
		
		for(int x = 0; x < songs.size(); x++)
		{
			for(int y = 0; y < songs.size(); y++)
				songPairs[x][y]=1;
		}
	}
	
	private static void WriteToFile() { //for some reason has an extra "//set" on write in setlists.txt
		BufferedWriter bw = null;
		try { //write to the temp file
			bw = new BufferedWriter(new FileWriter("res" +File.separator+"tempSet.txt",true));
			String temp = "//set\n";
			
			for(String s:setlist)
			{
				temp+=(s+"\n");
			}
			temp += "\n";
			//TODO: Figure out why it won't write...
			//System.out.println(temp);
			bw.append(temp);
			System.out.println(temp);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally
		{
			if(bw != null)
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		//write to the setlist file
		BufferedWriter bWriter = null;
		try { //write to the temp file
			bWriter = new BufferedWriter(new FileWriter(AllSetlists,true));
			String temp = "//set\n";
			
			for(String s:setlist)
			{
				temp+=(s+"\n");
			}
			temp += "\n";
			//TODO: Figure out why it won't write...
			//System.out.println(temp);
			bWriter.write(temp);
			System.out.println(temp);
			bWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 finally {
			if(bWriter != null)
				try {
					bWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	@SuppressWarnings("unused")
	private static void SudoGen() {
		int fin = songs.size();
		String text = "";
		for(int x = 0; x<fin;x++)
		{
			int r = (int)(Math.random()*songs.size());
			setlist.add(songs.get(r));
			text += songs.get(r)+"\n";
			songs.remove(r);
		}
		BufferedWriter bw = null;
		Scanner sc = null;
		try {
			File fq = new File("res" + File.separator+ "setlists.txt");
			sc = new Scanner(fq);
			String text2 = "";
			while(sc.hasNextLine())
					text2+=sc.nextLine() + "\n";
			
			bw = new BufferedWriter(new FileWriter(fq));
			bw.write(text2 + "//set\n" + text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			if(bw!=null)
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(sc!=null)
				sc.close();
		}
		Load();
	
	}
	
	private static void CompileFrequencies(String string) {
		switch(string)
		{
		case "starter":
		{
			Scanner setScan = null;
			try {
				setScan = new Scanner(AllSetlists);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int index = 0;
			while(setScan.hasNextLine())
			{
				if(setScan.nextLine().indexOf("//")>=0)
				{
					String text = setScan.nextLine();
					for(int x = 0; x<songs.size();x++)
					{
						if(songs.get(x).equals(text))
						{
							index = x;
							break;
						}
					}
					songStartCount.set(index, songStartCount.get(index)+1);
				}
			}
			}
			break;
		case "body":
			Scanner sc;
			try {
				sc = new Scanner(AllSetlists);
				String text1 = "";
				String text2 = "";
				if(sc.hasNextLine())
					text2 = sc.nextLine();
				
				while(sc.hasNextLine())
				{
					int val1 = -1;
					int val2 = -1;
					
					text1 = text2;
					text2 = sc.nextLine();
					
					if(text1.indexOf("//")>=0 || text2.indexOf("//")>=0)
						continue;
					else //get value of in songs that matches text1 and text2
					{
						for(int x = 0; x < songs.size();x++)
						{
							if(songs.get(x).equals(text1))
									val1 = x;
							if(songs.get(x).equals(text2))
									val2 = x;
							
							if(val1!=-1 && val2!=-1)
								{
									songPairs[val1][val2] += 1;
									break;
								}
							else
								if(x == songs.size())
									{
										System.out.println("CRITICAL ERROR");
										Thread.currentThread().interrupt();
									}
						}
						
					}
					
				}
				
					for(int x = 0; x<songs.size();x++)
					{
						for(int y = 0; y<songs.size(); y++)
							if(songPairs[x][y]>1);
								//System.out.println(songs.get(x) + " " + songs.get(y) + " " + songPairs[x][y]);
					}
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		}
		
	}
	
	private static void Selector(String string) {
		switch(string)
		{
			case "starter":
				int sumOfFreq = 0;
				for(Integer i:songStartCount)
				{
					sumOfFreq+=i+1;
				}
				int rand = (int)(0.98*sumOfFreq);//Math.random()
				int sum = 0;
				int holder = 0;
				int bottom = 0;
				System.out.println(songs.size());
				System.out.println(sumOfFreq);
				//System.out.println("SumOfFreq: " +sumOfFreq);
				for(int x = 0; x < songs.size(); x++)
				{
					if(x == 0)
						bottom = 0;
					else
						bottom += songStartCount.get(x-1)+1;
					//System.out.println("Sum: " +sum);
					int top = songStartCount.get(x);
					sum += top+1;
					
					if(rand >= bottom && rand < sum)
					{
						//System.out.println("Song 1 is: " + songs.get(x));
						setlist.add(0, songs.get(x));
						System.out.println("SOng: " + songs.get(x));
						break;
					}
					holder = x;
				}	
				//make sure somethings there
				if(setlist.size() == 0)
				{
					setlist.add(0,songs.get(holder));
					
				}
			
			break;
			/*
			 * TODO: Build the selector for the body.  
			 *  1. Look at the initial song.  Sum the amount of hits for that song from
			 *  	songPairs[][] using songPairs[initial song][all other songs]
			 *  2. Generate a number then pair the weighting using 
			 *  	songPairs[initial song][song] where "song" will be the song chosen
			 *  	if the random value falls between [prev_weight] and 
			 *  	[prev_weight + songs_weight] (As above: top and bottom for loop) then
			 *  	remove that song from the list (including first song) so it isn't used
			 *  	twice
			 *  3. Loop this N number of times using the newly generated song where
			 *  	"initial song" and N is an input of the number of songs (if 
			 *  	N> songs.size() set the size to songs.size()
			 *  4. Display set list
			 *  5. Get input for whether this set list is wanting to be used, or if some
			 *  	changes need to be made (swap/add/remove)
			 *  6. if changes need to be made, open a loop function to make changes, then
			 *  	return to 5. for confirmation.  If user doesn't want to use setlist
			 *  	generate a new one, if user accepts the list, append setlist to
			 *  	setlist.txt to make changes to the data when read in.
			 */
			case "body":
				System.out.println("Song 1 " +"is: " + setlist.get(0));
				
				String song = setlist.get(0);
				int temp = -1;
				if(iteration > songs.size())
					iteration = songs.size();
				while(setlist.size()<iteration)
				{
					for(int x = 0; x<allSongs.size(); x++)//find the song
					{
						if(allSongs.get(x).equals(song))
						{
							temp = x;
							break;
						}	
						else
							temp = -1;
					}
				
					if(songs.size() == allSongs.size())
						for(int y = 0; y<songs.size();y++)
							if(songs.get(y).equals(setlist.get(0)))
								songs.remove(y);
					if(temp != -1)
						song = allSongs.get(temp);
					else
						System.out.println("ERROR");
					//sum:
					int weightSum = 0;
					for(int y = 0; y<allSongs.size();y++)
					{
						weightSum+=songPairs[temp][y];
					}
					//System.out.println(weightSum);
					int random = (int)(Math.random()*weightSum);
					int sum2 = 0;
					
					for(int y = 0; y<songs.size();y++)
					{
						int top = songPairs[temp][y];
						sum2 += top+1;
						int bottom2;
						if(y == 0)
							bottom2 = 0;
						else
							bottom2 = songPairs[temp][y-1];
					
						if(random >= bottom2 && random < sum2)
						{
							System.out.println("Song " +(setlist.size()+1)+" is: " + songs.get(y));
							setlist.add(songs.get(y));
							songs.remove(y);
							break;
						}
					}
				}	
				
		}
	}
	
	@Override 
	public void dispose() {
		System.exit(0);
	}

}
