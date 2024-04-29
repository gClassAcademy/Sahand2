package analyzer.visitors;

import analyzer.AbstractVoidVisitorAdapter;
import importance.Importance;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.resolution.MethodUsage;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedFieldDeclaration;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

import Data.Point;
import Data.model.AccessModifier;
import Data.model.AccessScope;
import Data.model.Attribute;
import Data.model.AttributeAccess;
import Data.model.AttributeAccessType;
import Data.model.AttributeModifier;
import Data.model.ClassModifier;
import Data.model.Classs;
import Data.model.Inheritance;
import Data.model.InputParameter;
import Data.model.LoopStatement;
import Data.model.Method;
import Data.model.MethodInvocation;
import Data.model.MethodModifier;
import Data.model.MethodType;
import Data.repository.AttributeAccessRepository;
import Data.repository.AttributeRepository;
import Data.repository.ClassRepository;
import Data.repository.InheritanceRepository;
import Data.repository.InputParameterRepository;
import Data.repository.LoopStatementRepository;
import Data.repository.MethodInvocationRepository;
import Data.repository.MethodRepository;

@Service
public class gVisitor extends AbstractVoidVisitorAdapter<Classs> {

    //protected String className;   // inherited from parent class
	Classs current_class;
	
	@Autowired 
	ClassRepository class_Repository;
	
	@Autowired
	AttributeRepository attribute_Repository;

	@Autowired
	MethodRepository method_Repository;

	@Autowired
	InputParameterRepository inputparameter_Repository;

	@Autowired
	AttributeAccessRepository attributeaccess_Repository;

	@Autowired
	MethodInvocationRepository methodinvocation_Repository;

	@Autowired
	LoopStatementRepository loopstatement_Repository;

	@Autowired
	InheritanceRepository inheritance_Repository;

	List<Attribute> allAttributes=new ArrayList<Attribute>();
	List<Method> allMethods=new ArrayList<Method>();
	List<MethodCallExpr> allMethodCalls=new ArrayList<MethodCallExpr>();
	
	
	
	// When JavaParser Visits a Class or Interface
    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Classs classs) {

    	if (declaration.isInterface())
    		return;

    	allAttributes=new ArrayList<Attribute>();
    	allMethods=new ArrayList<Method>();
    	
    	
    	//checks for inner/nested class
    	if (!declaration.getNameAsString().equals(className))
    	{
    		//inner or nested class
            Classs innerclasss=new Classs();
	        
        	try {
                innerclasss.setFullpath(declaration.getFullyQualifiedName().get());
        	} catch (Exception e) {

        	}     

            
            innerclasss.setPackagee(null);
            //innerclasss.setPackagee(classs.getPackagee());  // no need to set package for inner/nested classes
            innerclasss.setParentClass(classs);
        	innerclasss.setName(declaration.getNameAsString());
        	innerclasss.setGdesc(declaration.getNameAsString());
	    	innerclasss.setCdate(Instant.now().getNano());
	    	    	
	    	//set class modifier
	    	innerclasss.setClassmodifier(ClassModifier.Default);
	    	if (declaration.isAbstract())
	    		innerclasss.setClassmodifier(ClassModifier.Abstract);
	    	else if (declaration.isFinal())
	    		innerclasss.setClassmodifier(ClassModifier.Final);
	    	
	    	
	    	//set access modifier
	    	innerclasss.setAccessmodifier(AccessModifier.Default);
	    	if (declaration.isPrivate())
	    		innerclasss.setAccessmodifier(AccessModifier.Private);
	    	else if (declaration.isProtected())
	    		innerclasss.setAccessmodifier(AccessModifier.Protected);
	    	else if (declaration.isPublic())
	    		innerclasss.setAccessmodifier(AccessModifier.Public);
	    	
	    	
	    	//innerclasss.setBody(declaration.toString());      	
	    	innerclasss.setLoc(-declaration.getRange().map(range -> range.begin.line - range.end.line).orElse(0));
	    	
	    	//SAVE Class into DBMS
	    	class_Repository.save(innerclasss);	    
	    	
	    	current_class=innerclasss;        	
    	}
    	else
    	{
    		//normal class into packages
	    	classs.setName(className);
	    	classs.setCdate(Instant.now().getNano());
	    	classs.setFullpath(declaration.getFullyQualifiedName().get());
	    	
	    	//set class modifier
	    	classs.setClassmodifier(ClassModifier.Default);
	    	if (declaration.isAbstract())
	    		classs.setClassmodifier(ClassModifier.Abstract);
	    	else if (declaration.isFinal())
	    		classs.setClassmodifier(ClassModifier.Final);
	    	
	    	
	    	//set access modifier
	    	classs.setAccessmodifier(AccessModifier.Default);
	    	if (declaration.isPrivate())
	    		classs.setAccessmodifier(AccessModifier.Private);
	    	else if (declaration.isProtected())
	    		classs.setAccessmodifier(AccessModifier.Protected);
	    	else if (declaration.isPublic())
	    		classs.setAccessmodifier(AccessModifier.Public);
	    	
	    	//classs.setBody(declaration.toString());      	
	    	classs.setLoc(-declaration.getRange().map(range -> range.begin.line - range.end.line).orElse(0));
	    	
	    	
	    	//SAVE Class into DBMS
	    	class_Repository.save(classs);
	    	
	    	current_class=classs;
    	}    	    	
        //System.out.println("ClassOrInterfaceDeclaration - Class Name: " + current_class.getName());


        
        
        
        
        //Find all Inherited Classes (1 class in java)
        // class_source : is current_class
        //System.out.println("iiiiiiiiiiiiiiiiiiiiiii \n"+declaration.getExtendedTypes());
        for (ClassOrInterfaceType cc : declaration.getExtendedTypes())
        {
        	Inheritance inherit=new Inheritance();
        	inherit.setCdate(Instant.now().getNano());  
        	inherit.setClass_source(current_class);
        	
        	inherit.setTarget_classname(cc.getName().toString());
        	inherit.setTarget_elementtype(cc.getElementType().toString());
        	//target full path
        	try {
            	inherit.setTarget_fullpath(cc.resolve().asReferenceType().getQualifiedName());
        	} catch (Exception e) {

        	}     
        	
        	
	    	//SAVE Class into DBMS
	    	inheritance_Repository.save(inherit);
        }
        
        
        
                  
        
        
        
    	//Find all internal fields of class
    	List<FieldDeclaration> allInternalFields = declaration.getFields();    	
		for (FieldDeclaration fdec : allInternalFields) 
		{			
			int i=0;
	    	for (VariableDeclarator variable : fdec.getVariables()) 
	    	{
	   	        Attribute attribute=new Attribute(); 
	   	        attribute.setImportance(0);
	   	    	attribute.setClasss(current_class); // or current_class
	   	    	attribute.setName(variable.getName().asString());
	   	    	attribute.setGdesc(variable.getNameAsString());
	   	    	attribute.setCdate(Instant.now().getNano());   	    	
	   	    	//attribute.setBody(fdec.toString());
	   	    	attribute.setGorder(++i);
	   	    	attribute.setLnic(variable.getBegin().get().line);
	   	    	attribute.setFullpath(current_class.getFullpath() + "." + variable.getName().asString());
		    	attribute.setIsinherited(false);

	   	    	attribute.setDatatype(Utils.getDataType(variable.getType().toString()));
	   	    	attribute.setCollectiontype(Utils.getCollectionType(variable.getType().toString()));	   	   

		    	//set access modifier
	   	    	attribute.setAccessmodifier(AccessModifier.Default);
		    	if (fdec.isPrivate()) attribute.setAccessmodifier(AccessModifier.Private);	
		    	else if (fdec.isProtected()) attribute.setAccessmodifier(AccessModifier.Protected);
		    	else if (fdec.isPublic()) attribute.setAccessmodifier(AccessModifier.Public);

		    	//set Attribute modifier
		    	attribute.setAttributemodifier(AttributeModifier.Default);
		    	if (fdec.isFinal()) attribute.setAttributemodifier(AttributeModifier.Final);
		    	else if (fdec.isVolatile()) attribute.setAttributemodifier(AttributeModifier.Volitile);
		    	
		    	
		    	// static and transient
		    	if (fdec.isStatic()) attribute.setIsstatic(true);
		    	if (fdec.isTransient())	attribute.setIstransient(true);
		    	
		    	
	    		//attribute.setImportance(Importance.calcAttrImp1(attribute));
		    	//System.out.println(attribute.getFullpath() + ": " + attribute.getImportance() );

		    	//SAVE Attribute into DBMS
		    	attribute_Repository.save(attribute);	
		    	
		    	allAttributes.add(attribute);
		    	
	    	}// end of VariableDeclarator
		} //FieldDeclaration fdec
    	
		
		
    	
		
		
		
		//Find all inherited attributes (except PRIVATE attributes)
    	List<ResolvedFieldDeclaration> allFields = null;    	
    	try {
    		allFields = declaration.resolve().getVisibleFields();

	    	int gOrder=0;
	    	for (ResolvedFieldDeclaration variable : allFields) 
			{
				boolean find=false;
				for(FieldDeclaration fdec: allInternalFields)
					for (VariableDeclarator vdec : fdec.getVariables())
						if (variable.getName().equals(vdec.getName().asString()))
						{	
							find=true;  break;
						}
				if (!find)
				{
					//find new inherited field and should be saved
		   	        Attribute attribute=new Attribute();  
		   	        attribute.setImportance(0);
		   	    	attribute.setClasss(current_class); // or current_class
		   	    	attribute.setName(variable.getName());
		   	    	attribute.setGdesc(variable.getName());
		   	    	attribute.setCdate(Instant.now().getNano());   	    	
		   	    	//attribute.setBody(variable.toAst().get().toString());
		   	    	attribute.setGorder(++gOrder);
		   	    	attribute.setLnic(0);
		   	    	attribute.setFullpath(current_class.getFullpath() + "." + variable.getName());
			    	attribute.setIsinherited(true);
	
		   	    	attribute.setDatatype(Utils.getDataType(variable.getType().toString()));
		   	    	attribute.setCollectiontype(Utils.getCollectionType(variable.getType().toString()));	   	   

		   	    	//set access modifier
		   	    	attribute.setAccessmodifier(AccessModifier.Public);
			    	if (variable.accessSpecifier().name().equals("PROTECTED"))	attribute.setAccessmodifier(AccessModifier.Protected);
	
			    	//set Attribute modifier
			    	attribute.setAttributemodifier(AttributeModifier.Default);
			    	if (variable.isVolatile()) 	attribute.setAttributemodifier(AttributeModifier.Volitile);
	
			    	// static and transient
			    	if (variable.isStatic()) attribute.setIsstatic(true);
		    		attribute.setIstransient(false);
	
			    	
			    	//SAVE Attribute into DBMS
			    	attribute_Repository.save(attribute);		    	
			    	
			    	allAttributes.add(attribute);

				}// end of if (!find)
			}// end of for (ResolvedFieldDeclaration variable : allFields)
    	}
    	catch (Exception e) {
			//System.out.println("can not read external classes of inheritance (Attributes)");
		}
 
    	
    	
    	// #$#$#$#$#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
    	Importance.calcAttrImp(declaration, allAttributes, attribute_Repository);
    	
    	
    	
    	

    	// finding all internal methods
    	long gOrder=0;
    	List<MethodDeclaration> allmethods = declaration.getMethods();
    	
    	for (MethodDeclaration m : allmethods)
    	{
    		//System.out.println("IIIIIIIInternal Method : " +  m.getDeclarationAsString());
    		Method method =new Method(); 
    		
    		method.setCognitivecomplexity(Importance.calculateCognitiveComplexity(m));
    		method.setImportance(0);
    		method.setClasss(current_class);
    		method.setFullpath(current_class.getFullpath() + "." + m.getSignature());
    		method.setName(m.getName().asString());
    		method.setGdesc(m.getName().asString());
    		method.setCdate(Instant.now().getNano()); 
    		//method.setBody(m.toString());
    		method.setLnic(m.getBegin().get().line);
    		method.setLoc(-m.getRange().map(range -> range.begin.line - range.end.line).orElse(0));
    		method.setDecleration(m.getDeclarationAsString());
    		method.setIsinherited(false);
    		if (m.isStatic())  method.setIsstatic(true); else method.setIsstatic(false);
    		if (m.isSynchronized())  method.setIssynchronized(true); else method.setIssynchronized(false);
    		
    		if (method.getName().equals(current_class.getName()))
    			method.setMethodtype(MethodType.Constructor);
    		else 
    			method.setMethodtype(MethodType.Default);
    		
			method.setAccessmodifier(AccessModifier.Default);
			if (m.isPrivate())	method.setAccessmodifier(AccessModifier.Private);
			else if (m.isProtected())  method.setAccessmodifier(AccessModifier.Protected);
			else if (m.isPublic())  method.setAccessmodifier(AccessModifier.Public);
			
			method.setMethodmodifier(MethodModifier.Default);
			if (m.isAbstract())	method.setMethodmodifier(MethodModifier.Abstract);
			else if (m.isFinal()) method.setMethodmodifier(MethodModifier.Final);
    					
			method.setReturndatatype(Utils.getDataType(m.getType().toString()));
			method.setReturncollectiontype(Utils.getCollectionType(m.getType().toString()));

	    	//SAVE Method into DBMS
	    	method_Repository.save(method);	    	

			
	    	//saving input parameters
	    	Set<InputParameter> sss=new HashSet<>();
	    	int ipgOrder=0;
	    	for(Parameter p : m.getParameters())
	    	{
	    		InputParameter ip = new InputParameter();
	    		ip.setMethod(method);
	    		ip.setName(p.getNameAsString());
	    		ip.setCdate(Instant.now().getNano()); 
	    		//ip.setBody(p.toString());
	    		ip.setGorder(++ipgOrder);
	    		ip.setDatatype(Utils.getDataType(p.getTypeAsString()));
	    		ip.setCollectiontype(Utils.getCollectionType(p.getTypeAsString()));
	    			    		
	    		sss.add(ip);
		    	//SAVE InputParameter into DBMS
	    		inputparameter_Repository.save(ip);	    		
	    	}
	    	
    		method.setImp1(Importance.calcMethodImp1(method,sss));
    		
    		Point p=Importance.calcAttrSeenPerMethod(m);
    		method.setAttrseenr(p.x);
    		method.setAttrseenw(p.y);
	    	//SAVE Method into DBMS
	    	method_Repository.save(method);	    	
	    	allMethods.add(method);
	    	
    	}// end of internal methods
    	    	
    	
    	// finding inherited methods
    	Set<MethodUsage> allinheritedmethods = null;    	
    	try {
    		allinheritedmethods = declaration.resolve().getAllMethods();

    		for (MethodUsage mi : allinheritedmethods)
        	{
        		if (!mi.getQualifiedSignature().startsWith("java.") && !mi.getQualifiedSignature().startsWith("javax.")
        				&& !mi.getQualifiedSignature().startsWith(current_class.getName())
        				&& mi.getQualifiedSignature().indexOf("." + current_class.getName() + ".")==-1)
        		{        			
        			
            		//System.out.println("EEEEEEExternal Method : " +  mi.getQualifiedSignature());
            		Method method =new Method(); 
            		method.setCognitivecomplexity(Importance.calculateCognitiveComplexity(mi.getDeclaration().toAst().get()));
            		method.setImportance(0);
            		
            		method.setClasss(current_class);
            		method.setFullpath(mi.getQualifiedSignature());
            		method.setName(mi.getName());
            		method.setGdesc(mi.getQualifiedSignature());
            		method.setCdate(Instant.now().getNano()); 
            		//method.setBody(mi.getDeclaration().toAst().get().toString());
            		method.setLnic(0);
            		method.setLoc(0);
            		method.setAccessmodifier(AccessModifier.Public);
            		if (mi.getDeclaration().accessSpecifier().asString().equals("PROTECTED"))
            			method.setAccessmodifier(AccessModifier.Protected);
        			method.setMethodmodifier(MethodModifier.Default);
        			if (mi.getDeclaration().isAbstract())	
        				method.setMethodmodifier(MethodModifier.Abstract);
            		if (mi.getDeclaration().isStatic())  
            			method.setIsstatic(true); 
            		else 
            			method.setIsstatic(false);
        			method.setReturndatatype(Utils.getDataType(mi.getDeclaration().getReturnType().toString()));
        			method.setReturncollectiontype(Utils.getCollectionType(mi.getDeclaration().getReturnType().toString()));
            		method.setIsinherited(true);
            		method.setDecleration(mi.getQualifiedSignature());

    		    	//SAVE Method into DBMS
        	    	method_Repository.save(method);	    	
           		
            		//saving input parameters
        	    	Set<InputParameter> sss=new HashSet<>();

            		for (int x=0;x<mi.getDeclaration().getNumberOfParams();x++)
            		{
        	    		InputParameter ip = new InputParameter();
        	    		ip.setMethod(method);
        	    		ip.setName(mi.getDeclaration().getParam(x).getName());
        	    		ip.setCdate(Instant.now().getNano()); 
        	    		ip.setGorder(x+1);
        	    		ip.setDatatype(Utils.getDataType(mi.getDeclaration().getParam(x).getType().toString()));
        	    		ip.setCollectiontype(Utils.getCollectionType(mi.getDeclaration().getParam(x).getType().toString()));
        	    		//ip.setBody(mi.getDeclaration().getParam(x).toString());
        	    		
        	    		
        	    		sss.add(ip);
        		    	//SAVE InputParameter into DBMS
        	    		inputparameter_Repository.save(ip);	    		
        	    	}
            		
            		method.setImp1(Importance.calcMethodImp1(method,sss));            		            	
    		    	//SAVE Method into DBMS
        	    	method_Repository.save(method);	    	
        			allMethods.add(method);

        		}
        	}
    	}  	
    	catch (Exception e) {
			//System.out.println("can not read external classes of inheritance (Methods)");
		}  // end of finding inherited methods    

    	// #$#$#$#$#$##$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$#$
    	//Importance.calcMethodImp(declaration, allMethods, method_Repository);
    	
    	//System.out.println("ClassOrInterfaceDeclaration declaration");
    	
    	
        super.visit(declaration, current_class);
    }
    // end of visit(ClassOrInterfaceDeclaration declaration, Classs classs)
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public void visit(MethodDeclaration declaration, Classs classs) 
    {
        //System.out.println("MMMMMMMMethodDeclaration - Class Name : " + classs.getName() + " ,  Method Name: " + declaration.getSignature().toString());

    	
        //for (FieldAccessExpr fieldAccess : declaration.findAll(FieldAccessExpr.class)) {
        //        System.out.println("Read: " + fieldAccess.getName());
    	//}
    	
    	
    	
        // finding method Object
        Method current_Method=null;
        for (Method method : allMethods)
        	if (method.getFullpath().equals(current_class.getFullpath() + "." + declaration.getSignature())) {
        		current_Method=method;
        		break;
        	}   
        final Method cccc=current_Method;
        if (current_Method != null)
        {
        	//cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc
            //List<String> allfields = new ArrayList<String>();
            //List<String> Writefields = new ArrayList<String>();
            declaration.walk(            		
        		node -> { 
    				//System.out.println("---------------------------  Method: " + declaration.getNameAsString());
    				
        			if (node instanceof AssignExpr || node instanceof UnaryExpr)
        			{	
        				//System.out.println("---------------------------  AssignExpr: " + ((AssignExpr) node).toString());
        				Expression node1 = null;
        				if (node instanceof AssignExpr)
        					node1 = ((AssignExpr) node).getTarget();
        				else if (node instanceof UnaryExpr)
        					node1 = ((UnaryExpr) node).getExpression();
        				
        				if(node1 instanceof FieldAccessExpr) {
            		        if(((FieldAccessExpr)node1).getScope().isThisExpr())               		        	
            		        	saveAA(((FieldAccessExpr)node1).getNameAsString(), cccc, AttributeAccessType.Write, ((FieldAccessExpr)node1).getRange().get());
            		        else 
            		        {	
                		        try
	                	        {
                		        	Node nndd=((FieldAccessExpr)node1).getChildNodes().get(0);
                		        	//NameExpr node2 = (NameExpr)((FieldAccessExpr)node1).getChildNodes().get(0);
                		        	if (nndd instanceof NameExpr)
                		        	{
                		        		NameExpr node2 = (NameExpr)nndd;

                		        		ResolvedValueDeclaration decl = ((NameExpr)node2).resolve();
                		        		if(decl.isField())
                		        			saveAA(((NameExpr)node2).getNameAsString(), cccc, AttributeAccessType.Write, ((NameExpr)node2).getRange().get());
                		        	}
	                	        }
	                	        catch(Exception e)
	                	        {
	                	        } 

            		        }
            			}
            			else if(node1 instanceof NameExpr)
            		        try
                	        {
                	            ResolvedValueDeclaration decl = ((NameExpr)node1).resolve();
                	            if(decl.isField())
                		        	saveAA(((NameExpr)node1).getNameAsString(), cccc, AttributeAccessType.Write, ((NameExpr)node1).getRange().get());
                	        }
                	        catch(Exception e)
                	        {
                	        } 
            			else if (node1 instanceof ArrayAccessExpr)
            			{
            		        try
                	        {
                				Node nndd = ((ArrayAccessExpr) node1).getName();
                	            if (nndd instanceof NameExpr)
                				{
                	            	NameExpr node2 = (NameExpr) nndd;
                	            	ResolvedValueDeclaration decl = ((NameExpr)node2).resolve();
                				
                	            	if(decl.isField())
                	            		saveAA(((NameExpr)node2).getNameAsString(), cccc, AttributeAccessType.Write, ((NameExpr)node2).getRange().get());
                				}
                	        }
                	        catch(Exception e)
                	        {
                	        } 

            			}
        			}
        			
        			if(node instanceof FieldAccessExpr) {
        		        if(((FieldAccessExpr)node).getScope().isThisExpr())
        		        	saveAA(((FieldAccessExpr)node).getNameAsString(), cccc, AttributeAccessType.Read,((FieldAccessExpr)node).getRange().get());
        			}
        			else if(node instanceof NameExpr)
        		        try
            	        {
            	            ResolvedValueDeclaration decl = ((NameExpr)node).resolve();
            	            if(decl.isField())
            		        	saveAA(((NameExpr)node).getNameAsString(), cccc, AttributeAccessType.Read,((NameExpr)node).getRange().get());
            	        }
            	        catch(Exception e)
            	        {
            	        }
            		}); // end of if walk
                        
        	//cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc

        	        
	        // finding all method calls
	        allMethodCalls = declaration.findAll(MethodCallExpr.class);
	        for (MethodCallExpr methodcall : allMethodCalls)
	        {
	        	MethodInvocation mcall=new MethodInvocation();
	        	mcall.setMethod_source(current_Method);
	        	mcall.setCdate(Instant.now().getNano());
	        	mcall.setLnic(methodcall.getBegin().get().line);
	        	mcall.setName(methodcall.getNameAsString());
	        	//mcall.setAccessscope(AccessScope.projectExternal);
	        	
	        	if (methodcall.getScope().isPresent())
	        		mcall.setZscope(methodcall.getScope().get().toString());
	
	        	
	        	try {
	        		mcall.setZreturntype(methodcall.calculateResolvedType().describe());
	        		mcall.setZparentnode(methodcall.getParentNode().get().toString());
	        	}
	        	catch (Exception e) {
				}
	        	
	        	try {
	            	mcall.setZsignature(methodcall.resolve().getSignature());        		
	            	mcall.setZqualifiedsignature(methodcall.resolve().getQualifiedSignature());
	            	mcall.setZqualifiedname(methodcall.resolve().getQualifiedName());
	            	mcall.setZzqualifiedname(methodcall.resolve().declaringType().getQualifiedName());
	            	mcall.setZzcontainertype(methodcall.resolve().declaringType().containerType().toString());
	        	}
	        	catch (Exception e) {
				}
	        
	        
	        	//SAVE MethodInvocation into DBMS
	        	methodinvocation_Repository.save(mcall);

	        }
        


        
	        
	        // finding Loops into method
	        List<ForStmt> allFors= declaration.findAll(ForStmt.class);
	        List<WhileStmt> allWhiles= declaration.findAll(WhileStmt.class);
	        List<DoStmt> alldos= declaration.findAll(DoStmt.class);
	
	        for (ForStmt loop : allFors)
	        {
	        	LoopStatement loopst=new LoopStatement();
	        	loopst.setMethod(current_Method);
	        	loopst.setCdate(Instant.now().getNano());
	        	//loopst.setBody(loop.toString());
	        	loopst.setLooptype(0);
	        	loopst.setLoc(-loop.getRange().map(range -> range.begin.line - range.end.line).orElse(0));
	
	        	
	        	//SAVE Loops into DBMS
	        	loopstatement_Repository.save(loopst);	        	
	        }        
	        for (WhileStmt loop : allWhiles)
	        {
	        	LoopStatement loopst=new LoopStatement();
	        	loopst.setMethod(current_Method);
	        	loopst.setCdate(Instant.now().getNano());
	        	//loopst.setBody(loop.toString());
	        	loopst.setLooptype(1);
	        	loopst.setLoc(-loop.getRange().map(range -> range.begin.line - range.end.line).orElse(0));
	
	        	
	        	//SAVE Loops into DBMS
	        	loopstatement_Repository.save(loopst);	        	
	        }        
	        for (DoStmt loop : alldos)
	        {
	        	LoopStatement loopst=new LoopStatement();
	        	loopst.setMethod(current_Method);
	        	loopst.setCdate(Instant.now().getNano());
	        	//loopst.setBody(loop.toString());
	        	loopst.setLooptype(2);
	        	loopst.setLoc(-loop.getRange().map(range -> range.begin.line - range.end.line).orElse(0));
	
	        	
	        	//SAVE Loops into DBMS
	        	loopstatement_Repository.save(loopst);	        	
	        }    
	        
	        
	        
	        
	        
        }// end of if (method !=null)
        
    	//System.out.println("MethodDeclaration declaration");

        super.visit(declaration, classs);
    }
    // end of visit(MethodDeclaration declaration, Classs classs) 
    
    
	private void saveAA(String attrName, Method cccc, AttributeAccessType aat, Range rr)
    {
    	// create and save AttributeAccess
    	AttributeAccess aa=new AttributeAccess();
    	aa.setFullpath(cccc.getClasss().getFullpath() + "." + (attrName));
    	aa.setMethod(cccc);
    	aa.setFieldname(attrName);
		aa.setCdate(Instant.now().getNano());
		aa.setAttributeaccesstype(aat);
		aa.setLine(rr.begin.line);
		aa.setCol(rr.begin.column);
    	for (Attribute a : allAttributes)
    		if (a.getFullpath().equals(aa.getFullpath()))
    		{
    			aa.setAttribute(a);
    			aa.setImportance(a.getImportance()*cccc.getImportance());
    			break;
    		}
    	attributeaccess_Repository.save(aa);
    }
    
    
    

}
