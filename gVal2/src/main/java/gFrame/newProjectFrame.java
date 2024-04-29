package gFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import analyzer.AnalyzerRunner;

@Component
public class newProjectFrame extends JFrame implements ActionListener
{
	public String projectFolder=null;
	public JPanel treepanel;
	public JPanel printpanel;
	public JPanel northpanel;
	public JButton Browse;
	public JTextArea display;
	
	@Autowired
	AnalyzerRunner analyzerrunner;
	
	public newProjectFrame()
	{
		super();
		
		this.getContentPane().setBackground(Color.pink);
        this.setTitle("gVal: New Project");        	        
        this.setPreferredSize(new Dimension(1020, 740));

        
        // tree panel
        treepanel=new JPanel();  
        treepanel.setBounds(0,40,250,600);    
        treepanel.setBackground(Color.white);  
        this.add(treepanel);  

        // print panel
        printpanel=new JPanel();  
        printpanel.setBounds(250,40,750,600);    
        printpanel.setBackground(Color.lightGray);  
        this.add(printpanel);  
        
        
        printpanel.setBorder (new TitledBorder (new EtchedBorder (), "Project Analysis"));
        display = new JTextArea (35, 62);
        display.setEditable (false); // set textArea non-editable
        JScrollPane scroll = new JScrollPane (display);
        scroll.setVerticalScrollBarPolicy (ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //Add Textarea in to middle panel
        printpanel.add ( scroll );


        // North panel
        northpanel=new JPanel();  
        northpanel.setBounds(0,0,1000,40);    
        northpanel.setBackground(Color.lightGray);  
        this.add(northpanel);  

		JLabel L = new JLabel("Please Select Project Folder: ");
		Browse = new JButton("Browse...");
		Browse.addActionListener(this);

		northpanel.add(L);
        northpanel.add(Browse);      
        
		this.pack();
		this.setLocationRelativeTo(null); // set it to center
		//this.setVisible(true); 

	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == Browse)
		{
			JFileChooser f = new JFileChooser();
			f.setDialogTitle("Select Project Folder...");
			f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			f.showSaveDialog(null);
			projectFolder = f.getSelectedFile().toString();
			System.out.println("Selected Project Folder is: " + projectFolder);
			Browse.setEnabled(false);
		
	        try {
	        	
	        	display.setText(analyzerrunner.run(projectFolder));
	        	
	        	JOptionPane.showMessageDialog(null, "Selected Project converted into Sahand Model and saved into Sahand Database Model.");
	        	
	        	
	        	
	        } catch (Exception e1) {

	        	e1.printStackTrace();
	        	System.out.println("Invalid Project Directory...");
	        }
			
			
	        treepanel.setLayout(new BoxLayout(treepanel, BoxLayout.X_AXIS));
			treepanel.add(new FileTree(new File(projectFolder)));
			treepanel.revalidate();
			treepanel.repaint();
		}
		
	}
	
	
	


}
