import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

public class PopupWindow extends JFrame implements ActionListener{
	String name;
	String [] setlist;
	String [] remainder;
	JList<String> l_left;
	JList<String> l_right;
	public static int windowCount = 0;
	
	public PopupWindow(String s)
	{
		windowCount++;
		name = s;
		Container contentPane = this.getContentPane();
		Dimension dimA;
		Dimension dimB;
		Dimension dimC;
		Dimension dimD;
		switch(s)
		{
		case "Modify":
			// Set up three panels, JSelector panel on the left and right, and JPanel with one button in the middle: >
			// which inserts from the left panel, before the selected value
			
			// Dimensions
			dimA = new Dimension(260,260);
			dimB = new Dimension(60,260);
			dimC = new Dimension(60,36);
			dimD = new Dimension(100,36);
			// Array for JList
			setlist = GetSetlistAsArray();
			remainder = GetRemainder(setlist);
			
			// JLists
			// Left and right have JLists
			l_left = new JList<String>(remainder);
				l_left.setName("Left List");
				l_left.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				l_left.setPreferredSize(dimA);
			l_right = new JList<String>(setlist);
				l_right.setName("Right List");
				l_right.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				l_right.setPreferredSize(dimA);
					
			// JScrollPanes
			// Left and right both have scroll panes
			JScrollPane s_left = new JScrollPane(l_left);
				s_left.setPreferredSize(dimA);
			JScrollPane s_right = new JScrollPane(l_right);
				s_right.setPreferredSize(dimA);

			// JButtons
			JButton confirm = new JButton("Confirm");
				confirm.addActionListener(this);
			JButton cancel = new JButton("Cancel");
				cancel.addActionListener(this);
			
			JButton b_allRight = new JButton("►►");
				b_allRight.addActionListener(this);
				b_allRight.setActionCommand("brr");
				b_allRight.setPreferredSize(dimC);
			JButton b_allLeft = new JButton("◄◄");
				b_allLeft.setActionCommand("bll");
				b_allLeft.addActionListener(this);
				b_allLeft.setPreferredSize(dimC);
			JButton b_right = new JButton("►");
				b_right.setActionCommand("br");
				b_right.addActionListener(this);
				b_right.setPreferredSize(dimC);
			JButton b_left = new JButton("◄");
				b_left.setActionCommand("bl");
				b_left.addActionListener(this);
				b_left.setPreferredSize(dimC);
			JButton b_up = new JButton("▲");
				b_up.setActionCommand("bu");
				b_up.addActionListener(this);
				b_up.setPreferredSize(dimC);
			JButton b_down = new JButton("▼");
				b_down.addActionListener(this);
				b_down.setActionCommand("bd");
				b_down.setPreferredSize(dimC);
			
			// JPanels
			JPanel left = new JPanel();
				left.add(s_left);
			JPanel right = new JPanel();
				right.add(s_right);

			JPanel center = new JPanel();
				center.setPreferredSize(dimB);
				center.add(b_allRight);
				center.add(b_allLeft);
				center.add(b_right);
				center.add(b_left);
				center.add(b_up);
				center.add(b_down);
				//center.setLayout(new GridLayout(6,1));
			
			JPanel upper = new JPanel(); // Stores Left, Center and Right Panel
				//upper.setLayout(new GridLayout(1,3));
				upper.add(left);
				upper.add(center);
				upper.add(right);
			JPanel lower = new JPanel(); // Stores Confirmation Buttons
				//lower.setLayout(new GridLayout(1,2));
				lower.add(confirm, BorderLayout.WEST);
				lower.add(cancel, BorderLayout.EAST);
			contentPane.add(upper,BorderLayout.NORTH);
			contentPane.add(lower,BorderLayout.SOUTH);
			
			pack();
			this.setVisible(true);
			break;
		case "Song List":
			dimA = new Dimension(260,260); // Scroll Pane
			dimB = new Dimension(260,30); // TextArea
			dimC = new Dimension(80,34); // Buttons
			JPanel footer = new JPanel();
			JPanel west = new JPanel();
			JPanel east = new JPanel();
			JPanel body = new JPanel();
				body.add(west,BorderLayout.WEST);
				body.add(east,BorderLayout.EAST);
			
			JList<Object> list = new JList<Object>();
				list.setListData(Window.allSongs.toArray());
				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

			JScrollPane jsp = new JScrollPane(list);
				jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				jsp.setPreferredSize(dimA);	
			
			west.add(jsp);
			
			JTextArea songInput = new JTextArea();
				songInput.setPreferredSize(dimB);
			JButton addSong = new JButton("Add Song");
				addSong.addActionListener(this);
			JButton delSong = new JButton("Delete Song");
				delSong.addActionListener(this);
			east.add(songInput);
			east.add(addSong);
			east.add(delSong);
			contentPane.add(body,BorderLayout.NORTH);
			contentPane.add(footer, BorderLayout.SOUTH);
			this.pack();
			this.setVisible(true);
			break;
		}
	}

	private String[] GetRemainder(String[] setlist2) {
		ArrayList<String> arr2 = (ArrayList<String>) Window.allSongs.clone();
		for(String s : setlist)
		{
			arr2.remove(s);
		}
		
		String[] arr = new String[arr2.size()];
		for(int i =0; i < arr.length;i++)
		{
			arr[i] = arr2.get(i);
		}
		
		return arr;
	}

	private String[] GetSetlistAsArray() {
		
		String[] arr = new String[Window.setlist.size()];
		for(int i =0; i < arr.length;i++)
		{
			arr[i] = Window.setlist.get(i);
		}
			
		return arr;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String name = arg0.getActionCommand();
		System.out.println(name);
		System.out.println(this.name);
		switch(this.name) {
			case "":
				switch(name) 
				{
				case "Cancel":
					this.dispose();
					break;
				case "Confirm":
					int rsp = JOptionPane.showConfirmDialog(this, "Are you sure you would like to accept this changes?","",JOptionPane.YES_NO_OPTION);
					if(rsp == JOptionPane.YES_OPTION)
					{
						System.out.println("Different Disposal");
						this.dispose();
					}
					break;
				}
			case "Song List":
				switch(name)
				{
					case "Add Song":
						// APPEND
						break;
					case "Delete Song":
						//Remove song from list
						break;
				}
				break;
		}
	}
	
	@Override
	public void dispose()
	{
		windowCount--;
		super.dispose();
	}
}
