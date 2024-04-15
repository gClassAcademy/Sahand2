package importance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

import Data.Point;
import Data.model.AccessModifier;
import Data.model.Attribute;
import Data.model.AttributeModifier;
import Data.model.CollectionType;
import Data.model.DataType;
import Data.model.InputParameter;
import Data.model.Method;
import Data.model.MethodModifier;
import Data.repository.AttributeRepository;
import Data.repository.MethodRepository;

public class Importance {

	
	
	public static Point calcAttrSeenPerMethod(MethodDeclaration md)
	{
		
        List<String> fields = new ArrayList<String>();
        List<String> Writefields = new ArrayList<String>();
        md.walk(            		
        		node -> {            		
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
            		        	Writefields.add(((FieldAccessExpr)node1).getNameAsString());
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
                		        			Writefields.add(((NameExpr)node2).getNameAsString());
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
                	            	Writefields.add(((NameExpr)node1).getNameAsString());
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
            		        			Writefields.add(((NameExpr)node2).getNameAsString());
            		        	}
                	        }
                	        catch(Exception e)
                	        {
                	        } 

            			}
        			}
        			
        			
        			
        			
        			if(node instanceof FieldAccessExpr) {
        		        if(((FieldAccessExpr)node).getScope().isThisExpr())
        		            fields.add(((FieldAccessExpr)node).getNameAsString());
        			}
        			else if(node instanceof NameExpr)
        		        try
            	        {
        		        	NameExpr ne = (NameExpr)node;
            	            ResolvedValueDeclaration decl = ne.resolve();
            	            
            	            if(decl.isField())
            	                fields.add(ne.getNameAsString());
            	        }
            	        catch(Exception e)
            	        {
            	        }
        		});
        
        
        
        int allReades = fields.size() - Writefields.size();
        if (allReades<0) allReades=0; 
        return new Point (allReades, Writefields.size());
	}

	
	
    public static int calculateCognitiveComplexity(MethodDeclaration methodDeclaration) {
        int cognitiveComplexity = 0;

        // Count IfStmt occurrences
        cognitiveComplexity += methodDeclaration.findAll(IfStmt.class).size();

        // Count ForStmt occurrences
        cognitiveComplexity += methodDeclaration.findAll(ForStmt.class).size();

        // Count WhileStmt occurrences
        cognitiveComplexity += methodDeclaration.findAll(WhileStmt.class).size();

        // Count DoStmt occurrences
        cognitiveComplexity += methodDeclaration.findAll(DoStmt.class).size();

        // Count BinaryExpr occurrences
        //cognitiveComplexity += methodDeclaration.findAll(BinaryExpr.class).size();

        // Add more findAll calls to count other control flow structures as needed
        //ConditionalExpr
        //cognitiveComplexity += methodDeclaration.findAll(ConditionalExpr.class).size();

        return cognitiveComplexity;
    }

	public static float calcMethodImp1(Method method , Set<InputParameter> sss)
	{
		float sum=0;
		
		// calculate Access Modifier
		if ((method.getAccessmodifier()) == AccessModifier.Public)
			sum += 0.9041 * 1;
		else if ((method.getAccessmodifier()) == AccessModifier.Private)
			sum += 0.9041 * 0.01;
		else if ((method.getAccessmodifier()) == AccessModifier.Protected)
			sum += 0.9041 * 0.01;
		
		
		
		// calculate Non-Access Modifier
		if ((method.getMethodmodifier()) == MethodModifier.Final)
			sum += 0.9419 * 0.01;
		if ((method.getMethodmodifier()) == MethodModifier.Abstract)
			sum += 0.9419 * 0.01;
		if ((method.getMethodmodifier()) == MethodModifier.Default)
			sum += 0.9419 * 0.4;
		if (method.isIsstatic())
			sum += 0.9419 * 1;
		if (method.isIssynchronized())
			sum += 0.9419 * 1;
				
		
		// calculate RETURN Data Type Complexity
		float sum1=0;
		if (((method.getReturndatatype()) == DataType.Int)
				 || (method.getReturndatatype()) == DataType.String
				 || (method.getReturndatatype()) == DataType.Byte				
				 || (method.getReturndatatype()) == DataType.Short				
				 || (method.getReturndatatype()) == DataType.Long			
				 || (method.getReturndatatype()) == DataType.Float				
				 || (method.getReturndatatype()) == DataType.Double			
				 || (method.getReturndatatype()) == DataType.Boolean				
				 || (method.getReturndatatype()) == DataType.Char				
				 || (method.getReturndatatype()) == DataType.Void				
			)
			if ((method.getReturncollectiontype()) == CollectionType.SingleVariable)
					sum1 += 0.1216 * 0.01;
		
		if (((method.getReturncollectiontype()) == CollectionType.ArrayD1) || ((method.getReturncollectiontype()) == CollectionType.List_Stack_Queue))
			sum1 += 0.1216 * 0.3;
		if (((method.getReturncollectiontype()) == CollectionType.ArrayD2) || ((method.getReturncollectiontype()) == CollectionType.ArrayD3))
			sum1 += 0.1216 * 0.6;
		if (((method.getReturncollectiontype()) == CollectionType.Map) || ((method.getReturncollectiontype()) == CollectionType.Tree))
			sum1 += 0.1216 * 0.8;
		
		// other class return
		if (sum1 == 0)
			sum1 += 0.1216 *1.0;
		
		
		
		// calculate inputs of method Data Type Complexity
		float sum2=0;
		
		//if (method.getInputparameters() != null)
		//for (InputParameter param : method.getInputparameters())
		for (InputParameter param : sss)
		{
			float sumtep=0;
			if (((param.getDatatype()) == DataType.Int)
					 || (param.getDatatype()) == DataType.String
					 || (param.getDatatype()) == DataType.Byte				
					 || (param.getDatatype()) == DataType.Short				
					 || (param.getDatatype()) == DataType.Long			
					 || (param.getDatatype()) == DataType.Float				
					 || (param.getDatatype()) == DataType.Double			
					 || (param.getDatatype()) == DataType.Boolean				
					 || (param.getDatatype()) == DataType.Char				
					 || (param.getDatatype()) == DataType.Void				
				)
				if ((param.getCollectiontype()) == CollectionType.SingleVariable)
					sumtep += 0.1216 * 0.01;
			
			if (((param.getCollectiontype()) == CollectionType.ArrayD1) || ((param.getCollectiontype()) == CollectionType.List_Stack_Queue))
				sumtep += 0.1216 * 0.3;
			if (((param.getCollectiontype()) == CollectionType.ArrayD2) || ((param.getCollectiontype()) == CollectionType.ArrayD3))
				sumtep += 0.1216 * 0.6;
			if (((param.getCollectiontype()) == CollectionType.Map) || ((param.getCollectiontype()) == CollectionType.Tree))
				sumtep += 0.1216 * 0.8;
			
			//other class
			if (sumtep == 0)
				sumtep += 0.1216 * 1.0;
			
			sum2 += sumtep;
		}
		
		sum += (sum1 + sum2);
		
		return sum;
	}

	
	
	
	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////// Attributes
	@Transactional
	public static void calcAttrImp(ClassOrInterfaceDeclaration declaration, List<Attribute> allAttributes, AttributeRepository attribute_Repository)
	{
		if (allAttributes.size()==0)
			return;
	
		//calcuate part 1
		//for (int x=0 ; x<allAttributes.size();x++)
		//	allAttributes.get(x).setImportance(calcAttrImp1(allAttributes.get(x)));
		
		int attrSeenInMethod[] = new int[allAttributes.size()]; 
		
		int allseeCount_process[]   =new int[allAttributes.size()];
		int allWriteCount_process[] =new int[allAttributes.size()];
		
		int allseeCount[]   =new int[allAttributes.size()];
		int allWriteCount[] =new int[allAttributes.size()];
		
		int methodCount=declaration.getMethods().size();
        //System.out.println("Class: " + declaration.getNameAsString());
        for(MethodDeclaration md : declaration.getMethods())
        {
            List<String> fields = new ArrayList<String>();
            List<String> Writefields = new ArrayList<String>();
            md.walk(            		
            		node -> {            		
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
                		        	Writefields.add(((FieldAccessExpr)node1).getNameAsString());
                		        else 
                		        {	
                		        	//System.out.println("###" + ((FieldAccessExpr)node1).getChildNodes().get(0).toString());
                    		        try
    	                	        {
                    		        	Node nndd=((FieldAccessExpr)node1).getChildNodes().get(0);
                    		        	//NameExpr node2 = (NameExpr)((FieldAccessExpr)node1).getChildNodes().get(0);
                    		        	if (nndd instanceof NameExpr)
                    		        	{
                    		        		NameExpr node2 = (NameExpr)nndd;
                    		        	
                    		        		ResolvedValueDeclaration decl = ((NameExpr)node2).resolve();    	                	            
                    		        		if(decl.isField())
                    		        			Writefields.add(((NameExpr)node2).getNameAsString());
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
	                	            	Writefields.add(((NameExpr)node1).getNameAsString());
	                	        }
	                	        catch(Exception e)
	                	        {
	                	        } 
                			else if (node1 instanceof ArrayAccessExpr)
                			{
                				//System.out.println("arr: " + ((ArrayAccessExpr) node1).getName().toString());
                		        try
	                	        {
                    				NameExpr node2 = (NameExpr) ((ArrayAccessExpr) node1).getName();
	                	            ResolvedValueDeclaration decl = ((NameExpr)node2).resolve();
	                	            
	                	            if(decl.isField())
	                	            	Writefields.add(((NameExpr)node2).getNameAsString());
	                	        }
	                	        catch(Exception e)
	                	        {
	                	        } 

                			}
            			}
            			
            			
            			
            			
            			if(node instanceof FieldAccessExpr) {
            		        if(((FieldAccessExpr)node).getScope().isThisExpr())
            		            fields.add(((FieldAccessExpr)node).getNameAsString());
            			}
            			else if(node instanceof NameExpr)
            		        try
                	        {
                	            ResolvedValueDeclaration decl = ((NameExpr)node).resolve();
                	            
                	            if(decl.isField())
                	                fields.add(((NameExpr)node).getNameAsString());
                	        }
                	        catch(Exception e)
                	        {
                	        }
            		});
            
            
            Set<String> fset = new HashSet<String> ();
            for(String f : fields)
            	fset.add(f);
            
            for(String f : fset)
            	for (int x=0 ; x<allAttributes.size();x++)
            		if (allAttributes.get(x).getName().equals(f))
            		{
            			attrSeenInMethod[x]++;
            			break;
            		}
            
            
            // for all fields read and write access
            for(String f : fields)
            	for (int x=0 ; x<allAttributes.size();x++)
            	{	
            		if (allAttributes.get(x).getName().equals(f))
            			allseeCount[x]++;            	
            		if (allAttributes.get(x).getName().equals(f) && (md.findAll(WhileStmt.class).size() + md.findAll(ForStmt.class).size() >=1))
            			allseeCount_process[x]++;            	
        
            	}

            // for all fields write access
            for(String f : Writefields)
            	for (int x=0 ; x<allAttributes.size();x++)
            	{
            		if (allAttributes.get(x).getName().equals(f))            	
            			allWriteCount[x]++;            	
            		if (allAttributes.get(x).getName().equals(f) && (md.findAll(WhileStmt.class).size() + md.findAll(ForStmt.class).size() >=1))
            			allWriteCount_process[x]++;            	        
            	}
        } // end of for
        
        
        //change allseeCount to allReadCount
        for (int x=0; x<allseeCount.length;x++)
        {	
        	allseeCount[x]-=allWriteCount[x];
        	allseeCount_process[x]-=allWriteCount_process[x];
        }

        int MaxR=allseeCount[0], MaxW=allWriteCount[0];
        
        int MaxRp=allseeCount_process[0], MaxWp=allWriteCount_process[0];
        for (int x=1; x<allWriteCount.length;x++)
        {
        	//System.out.println("w: " + allAttributes.get(x).getName() + ": " + allWriteCount[x]);
        	if (allseeCount[x]>MaxR)
        		MaxR=allseeCount[x];
        	if (allWriteCount[x]>MaxW)
        		MaxW=allWriteCount[x];        	
        }
        
        if (MaxR==0) MaxR=1;
        if (MaxW==0) MaxW=1;
        if (MaxRp==0) MaxRp=1;
        if (MaxWp==0) MaxWp=1;
        
        for (int x=0; x<allAttributes.size();x++)
        {
        	float imp1 = 0;
        	
        	//imp1= (float) ((allseeCount[x] * 1.0 / MaxR) + (4 * (allWriteCount[x] * 1.0 / MaxW)))/5;
        	
        	if (MaxR>0)
        		imp1 += (float) ((allseeCount[x] * 1.0 / MaxR)/5); 
        	if (MaxW>0)
        		imp1 += (float) ((allWriteCount[x] * 1.0 / MaxW) * 4 /5.0); 
        	
        	if (MaxRp>0)
        		imp1 += (float) ((allseeCount_process[x] * 1.0 / MaxRp)/5.0); 
        	if (MaxWp>0)
        		imp1 += (float) ((allWriteCount_process[x] * 1.0 / MaxWp) * 4 /5.0); 

        	//attr seen in method percent
        	if (methodCount>0)
        		imp1 += attrSeenInMethod[x] * 1.0 / methodCount;
        	
        	float imp2= calcAttrImp2(allAttributes.get(x));
        	allAttributes.get(x).setImportance(imp1 * imp2);
        	attribute_Repository.save(allAttributes.get(x));
        }  
        attribute_Repository.flush();

	}
	


	
	public static float calcAttrImp2(Attribute attribute)
	{
		float sum=0;
		
		// calculate Access Modifier
		if ((attribute.getAccessmodifier()) == AccessModifier.Public)
			sum += 0.9041 * 1;
		else if ((attribute.getAccessmodifier()) == AccessModifier.Private)
			sum += 0.9041 * 0.01;
		else if ((attribute.getAccessmodifier()) == AccessModifier.Protected)
			sum += 0.9041 * 0.01;
		
		
		
		// calculate Non-Access Modifier
		if ((attribute.getAttributemodifier()) == AttributeModifier.Final)
			sum += 0.9419 * 0.01;
		else if ((attribute.getAttributemodifier()) == AttributeModifier.Volitile)
			sum += 0.9419 * 1;
		else if ((attribute.getAttributemodifier()) == AttributeModifier.Default)
			sum += 0.9419 * 0.4;
		if (attribute.isIsstatic())
			sum += 0.9419 * 1;
		if (attribute.isIstransient())
			sum += 0.9419 * 0.5;
				
		
		// calculate Data Type Complexity
				
		if (((attribute.getDatatype()) == DataType.Int)
				 || (attribute.getDatatype()) == DataType.String
				 || (attribute.getDatatype()) == DataType.Byte				
				 || (attribute.getDatatype()) == DataType.Short				
				 || (attribute.getDatatype()) == DataType.Long			
				 || (attribute.getDatatype()) == DataType.Float				
				 || (attribute.getDatatype()) == DataType.Double			
				 || (attribute.getDatatype()) == DataType.Boolean				
				 || (attribute.getDatatype()) == DataType.Char				
				 || (attribute.getDatatype()) == DataType.Void				
			)
			if ((attribute.getCollectiontype()) == CollectionType.SingleVariable)
					return (float)(sum + 0.8235 * 0.01);
		
		if (((attribute.getCollectiontype()) == CollectionType.ArrayD1) || ((attribute.getCollectiontype()) == CollectionType.List_Stack_Queue))
			return (float)(sum + 0.8235 * 0.3);
		if (((attribute.getCollectiontype()) == CollectionType.ArrayD2) || ((attribute.getCollectiontype()) == CollectionType.ArrayD3))
			return (float)(sum + 0.8235 * 0.6);
		if (((attribute.getCollectiontype()) == CollectionType.Map) || ((attribute.getCollectiontype()) == CollectionType.Tree))
			return (float)(sum + 0.8235 * 0.8);
			
		// type is other class 
		return (float)(sum + 0.8235 * 1);
	}
	


} // end of class
