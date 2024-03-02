package analyzer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import Data.model.ProgrammingLanguage;
import Data.model.Project;
import Data.repository.ProjectRepository;

// scanning the source code is started from this class, from run method

@Service
public class AnalyzerRunner {
	
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
		analyzer.Endaction();
        
        return result;
    }


}
