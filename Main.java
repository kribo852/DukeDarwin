import java.io.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.Random;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.ArrayList; 
import app.Lifeform;
import app.Habitat;

class Main{

	public static void main(String[] args) {
						
		Class c = loadClass(args[0]);
		
		Lifeform l = newInstance(c);
		
		l.start();
		
	}
	
	protected static Class loadClass(String s) {
		try{
		  return Class.forName(s); 
		}catch(Exception e){
			
		}
		return null;
	}
	
	protected static Lifeform newInstance(Class lifeformClass){
		try {
			Constructor ct = lifeformClass.getConstructors()[0];//lifeform has only one constructor
			Object[] argslist=new Object[]{null};
			return(Lifeform)ct.newInstance(argslist);
		}catch(Exception e){
			System.out.println("error "+e);
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
		
}
