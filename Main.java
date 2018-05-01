import java.io.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.Random;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Map.Entry;


class Main{

	public static void main(String[] args) {
				
		Habitat h =new HighEndHabitat();
		
		h.run(loadClass(args[0]));
		
	}
	
	public static Class loadClass(String s) {
		try{
		  return Class.forName(s); 
		}catch(Exception e){
			
		}
		return null;
	}	
}

abstract class Habitat{
	TreeMap<Double, Collection> genomes;
	
	protected abstract void run(Class lifeformClass);
	
	protected Lifeform newInstance(Collection genome, Class lifeformClass){
		try {
			Constructor ct = lifeformClass.getConstructors()[0];//lifeform has only one constructor
			Object[] argslist=new Object[]{genome};
			return(Lifeform)ct.newInstance(argslist);
		}catch(Exception e){
			System.out.println("error "+e);
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
}
