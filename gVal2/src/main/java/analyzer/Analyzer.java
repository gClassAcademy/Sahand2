package analyzer;

import Data.model.Attribute;
import Data.model.AttributeAccess;
import Data.model.Classs;
import Data.model.Inheritance;
import Data.model.Method;
import Data.model.MethodInvocation;
import Data.model.Package;
import Data.model.Project;
import Data.repository.AttributeAccessRepository;
import Data.repository.AttributeRepository;
import Data.repository.ClassRepository;
import Data.repository.InheritanceRepository;
import Data.repository.MethodInvocationRepository;
import Data.repository.MethodRepository;
import Data.repository.PackageRepository;
import Data.repository.ProjectRepository;
import analyzer.visitors.gVisitor;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

@Service
public class Analyzer extends SimpleFileVisitor<Path> {

	public static int filesCount=1;
	
	@Autowired
    ProjectRepository project_repository;
   
    @Autowired
    PackageRepository package_repository;

    @Autowired
    AttributeRepository attribute_repository;

    @Autowired
    gVisitor gvisitor;
    
	@Autowired 
	ClassRepository class_Repository;
	
	@Autowired
	InheritanceRepository inheritance_Repository;

	@Autowired
	MethodInvocationRepository methodinvocation_Repository;

	@Autowired
	MethodRepository method_Repository;
	
	@Autowired
	AttributeAccessRepository attributeaccessRepository;
	
	
	
    Project project;  
    Package packagee;

    Stack<Package> stPackages = new Stack<>();
    
    CombinedTypeSolver combinedTypeSolver;
    JavaSymbolSolver symbolSolver;
    
    public static long projectId=0;
    
    
    
    // finding all directories of project and define to JavaParser
    public void ResolveDirs(Path startDir) throws IOException
    {
        // Set up a minimal type solver that only looks at the classes used to run this sample.
        combinedTypeSolver = new CombinedTypeSolver();
        combinedTypeSolver.add(new ReflectionTypeSolver());
        
    	Files.walk(startDir).filter(entry -> !entry.equals(startDir))
        .filter(Files::isDirectory).forEach(subdirectory ->
        {
        	// do whatever you want with the subdirectories
        	System.out.println(subdirectory.toFile().toString());
            combinedTypeSolver.add(new JavaParserTypeSolver(subdirectory.toFile().toString()));
        });
    	
        // Configure JavaParser to use type resolution
        symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);
    }    
    

    
    public void start(Project project) {
    	
    	//defining first default package for project with same as project name without parent
        this.project = project;        
    	packagee = new Package();
    	packagee.setProject(project);
    	packagee.setFullname(project.getName());    	
    	packagee.setName(project.getName());
    	packagee.setCdate(Instant.now().getNano());
    	packagee.setGdesc("first package");
    	packagee.setFullpath(project.getName());
    	
    	
    	//SAVE Project and Package into DBMS
    	project_repository.save(this.project);
    	package_repository.save(packagee);

    	
    	projectId = project.getId();

    	
    	//stPackages is a stack to hold previous package in traveling package tree    	
    	stPackages.clear();
    	stPackages.push(packagee);
    }

    
    
    
    
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            throws IOException {      
    	
    	//System.out.println("Enter new Directory: " + dir);
    	packagee=new Package();
    	packagee.setProject(this.project); // no : set project as project parent just for first package (root), Others are null
    	packagee.setFullname(dir.toString().substring(dir.toString().indexOf('\\')+1,dir.toString().length()));    	
    	packagee.setName(dir.toString().substring(dir.toString().lastIndexOf('\\')+1,dir.toString().length()));
    	packagee.setCdate(Instant.now().getNano());
    	packagee.setGdesc(dir.toString());
    	packagee.setFullpath(dir.toString());
    	
   		packagee.setParentPackage(stPackages.peek());
    	//packagee.setRealpath(packagee.getParentPackage().getRealpath() + "." + packagee.getName());

   		
    	//SAVE Package into DBMS
    	package_repository.save(packagee);
    	
    	stPackages.push(packagee);
    	
        return FileVisitResult.CONTINUE;
    }
    
    
    
    
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc){
    	
    	//System.out.println("Exit Directory: " + dir);
        stPackages.pop();
        
    	return FileVisitResult.CONTINUE;
    }

    
    
    
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
    	
        if (isNotJava(file))
            return FileVisitResult.CONTINUE;

        System.out.println("(" + filesCount++ + ")---  visitFile : " + file.toRealPath());
        
        Classs classs=new Classs();
        classs.setPackagee(stPackages.peek());
        classs.setGdesc(file.toRealPath().toString());
        
        try {
       	CompilationUnit unit = StaticJavaParser.parse(file.toFile());
       	gvisitor.visit(unit, classs);
        }
        catch (Exception e) {
		}
        return FileVisitResult.CONTINUE;
    }

    
    
    @Transactional
    public void Endaction1()
    {
    	System.out.println("\nFinal Processing Started   .......................\n");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

		
    	// find Inheritance target class id
    	List<Inheritance> alllist = inheritance_Repository.findByProject_ID(projectId);    	
    	List<Classs> allclass = class_Repository.findByProject_ID(projectId);
    	List<Attribute> allattrs = attribute_repository.findByProject_ID(projectId);      
    	
    	
    	System.out.println("\nStart Action (1/10) ....................... Inherited Class Target Finding");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));
    	for (Inheritance inherit : alllist)
    		for (Classs classs : allclass)
    			if (classs.getFullpath() !=null && classs.getFullpath().equals(inherit.getTarget_fullpath()))
    		   	{
    				//System.out.println(inherit.getTarget_fullpath());
    				inherit.setClass_target(classs);
    				inheritance_Repository.save(inherit);
    				break;
    		   	}

    	
    	
    	// find method_invocation target method ID
    	System.out.println("\nStart Action  (2/10) ....................... Finding method_invocation target method IDs");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

		List<MethodInvocation> allinvocations = methodinvocation_Repository.findByProject_ID(projectId);    	
    	List<Method> allmethods = method_Repository.findByProject_ID(projectId);
    	    	
    	for (MethodInvocation invoc : allinvocations)
    		for (Method method : allmethods)
    		{
    			if (invoc.getZqualifiedsignature().equals(method.getFullpath()))
    		   	{
    				//System.out.println(invoc.getZqualifiedsignature());
    				invoc.setMethod_target(method);
    				methodinvocation_Repository.save(invoc);
    				break;
    		   	}
    			if (getwithoutPath(invoc.getZqualifiedsignature()).equals(method.getFullpath()))
    			{
    				//System.out.println(invoc.getZqualifiedsignature());
    				invoc.setMethod_target(method);
    				methodinvocation_Repository.save(invoc);
    				break;
    			}
    		}
    	methodinvocation_Repository.flush();
    	
    }// End of EndAction1
    
    
    
    
    @Transactional (propagation = Propagation.REQUIRES_NEW)
    public void Endaction2()
    {
    	System.out.println("\nStart Action  (3/10)  ....................... Finding Method Call In/Out , Fan Out");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

		method_Repository.SetAll_Call_In();
		method_Repository.SetAll_Call_Out();
		method_Repository.SetAll_Fan_Out();
    	
    }// End of EndAction

    @Transactional
    public void Endaction3()
    {
    	System.out.println("\nStart Action  (4/10) ....................... Finding Max LOC, Complexity, Attrseens");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

    	int MaxComplexity = method_Repository.findMaxComplexity();
    	int MaxLoC = method_Repository.findMaxLoC();
    	int MaxAttrseenR = method_Repository.findMaxAttrSeenR();
    	int MaxAttrseenW = method_Repository.findMaxAttrSeenW();

    	if (MaxAttrseenR==0) MaxAttrseenR=1;
    	if (MaxAttrseenW==0) MaxAttrseenW=1;
    	if (MaxComplexity==0) MaxComplexity=1;
    	if (MaxLoC==0) MaxLoC=1;
    	


    	System.out.println("\nStart Action  (5/10) ....................... Finding Max MaxCall In and Out and Fan Out");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));
    	
    	long MaxCall_In=1;
    	long MaxCall_Out=1;
    	long MaxFan_Out= 1;

    	MaxFan_Out = methodinvocation_Repository.Max_Fan_Out();
    	if (MaxFan_Out<1) MaxFan_Out=1;
    	
    	MaxCall_Out = methodinvocation_Repository.Max_Call_Out();
    	if (MaxCall_Out<1) MaxCall_Out=1;
    	
    	MaxCall_In = methodinvocation_Repository.Max_Call_In();
    	if (MaxCall_In<1) MaxCall_In=1;    	
    	

    	
    	System.out.println("\nStart Action  (6/10)  ....................... Finding Method Importances");
		System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));
		
    	List<Method> allmethods = method_Repository.findByProject_ID(projectId);
		double importance = 0.0;
    	for (Method method : allmethods)
		{
    		importance = method.getImp1();
    		
    		double imp2 = 0.0;
    		imp2 += 0.2719 * (method.getLoc() * 1.0 / MaxLoC);
    		imp2 += 1.0 * (method.getCognitivecomplexity() * 1.0 / MaxComplexity);
    		
    		double seen = 0.0;
    		seen =((1.0 * method.getAttrseenr()/MaxAttrseenR) + (4.0 * method.getAttrseenw() / MaxAttrseenW)) / 5.0;
    		imp2 += 0.9041 * seen;
    		
    		imp2 += (0.9041 * (1.0 * method.getCallout() / MaxCall_Out));
    		
    		imp2 += (0.9041 * (1.0 * method.getCallin() / MaxCall_In));

    		imp2 += (0.7119 * (1.0 * method.getFanout() / MaxFan_Out));
    		
    		method.setImp2((float) imp2);
    		importance *= imp2;
    		float x= (float) importance;
    		method.setImportance(x);
    		
    		method_Repository.save(method);    		
		}    	    	
    	method_Repository.flush();
    	

    	
    	System.out.println("\nStart Action  (7/10)  ....................... set final importance for all attributes");
    	System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

    	float MaxImp1 = attribute_repository.FindMaxImp1();
    	attribute_repository.SetImportance(MaxImp1);
    	attribute_repository.flush();
    	

    	
    	
    	System.out.println("\nStart Action  (8/10)  ....................... set importance for all Method_Invocations, BUT internals");
    	System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));

    	List<MethodInvocation> allinvocations = methodinvocation_Repository.findByProject_ID(projectId);    	
    	for (MethodInvocation invoc : allinvocations)
    		if (invoc.getMethod_target() !=null)
    			invoc.setImportance(invoc.getMethod_source().getImportance()*invoc.getMethod_target().getImportance());    	
    	methodinvocation_Repository.flush();
    	
    	
    	

    	
    	System.out.println("\nStart Action  (9/10)  ....................... set importance for all Attribute_Access");
    	System.out.println(new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss").format(Calendar.getInstance().getTime()));
    	
    	attributeaccessRepository.SetAllImportance();
    	

    	
    	System.out.println("\nEnd Actions (10/10) ....................... ENDENDENDENDENDENDENDENDENDENDENDENDENDENDEND\n");
    }// End of EndAction

    
    
    
    private String getwithoutPath(String input)
    {
		String s1=input.substring(0, input.indexOf('('));
		String s2=input.substring(input.indexOf('(')+1,input.indexOf(')'));
		
		String finals="";
		String ss[]=s2.split(",");
		for (int x=0;x<ss.length;x++)
		{	
			if (finals.length()>0)
				finals+=", ";
			
			ss[x]=ss[x].trim();
			//System.out.println(ss[x] + ss[x].lastIndexOf('.'));
			
			if (ss[x].indexOf('.')<0)
				finals+=ss[x];
			else
				finals+=ss[x].substring(ss[x].lastIndexOf('.')+1, ss[x].length());
		}
		finals=s1 + "(" + finals + ")";
		
		//System.out.println(finals);
		return finals;
    }
    
    
    private String getClassName(Path file) {
        String filename = file.getFileName().toString();
        if (filename.indexOf(".") > 0) {
            filename = filename.substring(0, filename.lastIndexOf("."));
        }
        return filename;
    }

    private boolean isNotJava(Path file) {
        return !file.toString().endsWith("java");
    }

}
