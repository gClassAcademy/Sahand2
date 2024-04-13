package analyzer.visitors;

import Data.model.AccessModifier;
import Data.model.CollectionType;
import Data.model.DataType;

public class Utils 
{
	

	public static DataType getDataType (String datatype)
	{
    	if (datatype.indexOf("String")>=0)
    		return DataType.String;
    	else if (datatype.indexOf("int")>=0)
    		return DataType.Int;
    	else if (datatype.indexOf("byte")>=0)
    		return DataType.Byte;
    	else if (datatype.indexOf("short")>=0)
    		return DataType.Short;
    	else if (datatype.indexOf("long")>=0)
    		return DataType.Long;
    	else if (datatype.indexOf("float")>=0)
    		return DataType.Float;
    	else if (datatype.indexOf("double")>=0)
    		return DataType.Double;
    	else if (datatype.indexOf("boolean")>=0)
    		return DataType.Boolean;
    	else if (datatype.indexOf("char")>=0)
    		return DataType.Char;
    	else if (datatype.indexOf("Void")>=0)
    		return DataType.Void;
    	else if (datatype.indexOf("void")>=0)
    		return DataType.Void;

    	return DataType.ExternalClass;
	}
	
	public static CollectionType getCollectionType(String collectiontype)
	{
    	if (collectiontype.indexOf("[][][]")>=0)
	    	return CollectionType.ArrayD3;
	    else if (collectiontype.indexOf("[][]")>=0)
	    	return CollectionType.ArrayD2;
	    else if (collectiontype.indexOf("[]")>=0)
	    	return CollectionType.ArrayD1;
	    
    	
    	if (collectiontype.indexOf("HashMap")>=0)
    		return CollectionType.Map;
    	else if (collectiontype.indexOf("TreeMap")>=0)
    		return CollectionType.Map;
    	else if (collectiontype.indexOf("HashSet")>=0)
    		return CollectionType.Tree;
    	else if (collectiontype.indexOf("TreeSet")>=0)
    		return CollectionType.Tree;

    	else if (collectiontype.indexOf("LinkedList")>=0)
    		return CollectionType.List_Stack_Queue;
    	else if (collectiontype.indexOf("Stack")>=0)
    		return CollectionType.List_Stack_Queue;
    	else if (collectiontype.indexOf("Queue")>=0)
    		return CollectionType.List_Stack_Queue;
    	else if (collectiontype.indexOf("ArrayList")>=0)
    		return CollectionType.List_Stack_Queue;

    	
		return CollectionType.SingleVariable;

	}
	
}





