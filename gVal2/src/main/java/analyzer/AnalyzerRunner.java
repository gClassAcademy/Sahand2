package analyzer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import Data.model.ProgrammingLanguage;
import Data.model.Project;
import Data.repository.ProjectRepository;

// scanning the source code is started from this class, from run method

@Service
public class AnalyzerRunner {
	
	public static Date startTime; 
	
	@Autowired
	Analyzer analyzer;
	
    public String run(String path) throws IOException 
    {
    	return run(Paths.get(path));
    }


    private String run(Path p) throws IOException 
    {
    	String result="";
        
    	String path=p.toString();
		String projectName = path.substring(path.lastIndexOf('\\')+1, path.length());
		System.out.println("Project Name is : " + projectName);
		
		System.out.println("\nStart Time: " + new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));
		startTime = Calendar.getInstance().getTime();
		
		
		// define new project object for the start
		Project newProject = new Project();
		newProject.setFullpath(path);
		newProject.setName(projectName);
		newProject.setCdate(Instant.now().getNano());
		newProject.setDdate(null);
		newProject.setGdesc(projectName);
		newProject.setGorder(1);
		newProject.setProgramminglanguageid(ProgrammingLanguage.Java);
		
		// to define project directories into JavaParser
		analyzer.ResolveDirs(p);
		
		// start the project scanner
		analyzer.start(newProject);		
		Files.walkFileTree(p, analyzer);
		
		// after full scanning, to find target of all method calls and target of all inheritances:
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

		analyzer.Endaction1();
		analyzer.Endaction2();
		analyzer.Endaction3();
        
		System.out.println("Start Time: " + new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(startTime));
		System.out.println("End Time: " + new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

        return result;
    }


}
