package gFrame;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;


import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class mainFrame extends JFrame implements ActionListener
{
	JMenuBar mb;    
	JMenu File;    
	JMenuItem NewProject;    

	@Autowired
	private ApplicationContext context;

	public mainFrame()
	{
		super();

		this.setForeground(Color.black);
		this.setBackground(Color.lightGray);
        this.setTitle("gVal: Java Source Code Analysis Project");        	        
        this.setPreferredSize(new Dimension(1000, 700));
		
        
		//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@    Menu bar
		NewProject = new JMenuItem("New Project");    
		NewProject.addActionListener(this);    
		
		mb=new JMenuBar();    
		File=new JMenu("File");    		
		File.add(NewProject);
		mb.add(File);    
		
		this.add(mb);    
		this.setJMenuBar(mb);          
        		
		this.pack();
		this.setLocationRelativeTo(null); // set it to center
		//this.setVisible(true); 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == NewProject)
		{
			//newProjectFrame newproject = new newProjectFrame();
			newProjectFrame newProject = context.getBean(newProjectFrame.class);
			newProject.setVisible(true);
			
			System.out.println("New Project...");
		}				
		
	}	


}
