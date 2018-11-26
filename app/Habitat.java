package app;

import java.util.Collection;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Optional;


public abstract class Habitat{
	
	protected abstract void start(Class lifeformClass);
	
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