package tpc.mc.emc.tech.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import sun.misc.Unsafe;
import tpc.mc.emc.tech.ITech;

/**
 * Internal IO
 * */
final class IO {
	
	private static final Unsafe UNSAFE = unsafe0();
	
	private static final String MESSAGE0 = "CAN'T GET SUN.MISC.UNSAFE!";
	private static final String MESSAGE1 = null;
	
	/**
	 * Get the unsafe instance
	 * */
	private static final Unsafe unsafe0() {
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			
			return (Unsafe) f.get(null);
		} catch(Throwable e) {
			throw new InternalError(MESSAGE0, e);
		}
	}
	
	/**
	 * Calc the hashcode of the class name
	 * */
	private static final int hash0(Class<?>[] parms) {
		int l = parms.length;
		
		//quick judege
		if(l == 0) return 0;
		if(l == 1) return 527 + parms[0].getName().hashCode();
		Arrays.sort(parms, (a, b) -> a.getName().hashCode() - b.getName().hashCode());
		
		//init val
		int result = 17;
		
		//calc
		for(int i = 0; i < l; ++i) {
			result = 31 * result + parms[i].getName().hashCode();
		}
		
		return result;
	}
	
	/**
	 * Calc the internal hashcode of the class
	 * */
	private static final long hash(Class<?> klass) {
		long result = 17;
		int i, l;
		
		//record class misc info
		result = 31 * result + klass.getName().hashCode();
		result = 31 * result + (Modifier.isAbstract(klass.getModifiers()) ? 1 : 0);
		
		//record fields
		Field[] fs = klass.getDeclaredFields();
		if((l = fs.length) > 0) {
			if(l > 1) Arrays.sort(fs, (a, b) -> a.getName().hashCode() - b.getName().hashCode());
			
			//calc entry
			for(i = 0; i < l; ++i) {
				Field f = fs[i];
				
				result = 31 * result + f.getName().hashCode();
				result = 31 * result + f.getType().getName().hashCode();
				result = 31 * result + (Modifier.isFinal(f.getModifiers()) ? (Modifier.isTransient(f.getModifiers()) ? 3 : 1) : 0);
			}
		}
		
		//recored methods
		Method[] ms = klass.getDeclaredMethods();
		if((l = ms.length) > 0) {
			if(l > 1) Arrays.sort(ms, (a, b) -> a.getName().hashCode() - b.getName().hashCode());
			
			//calc entry
			for(i = 0; i < l; ++i) {
				Method m = ms[i];
				
				result = 31 * result + m.getName().hashCode();
				result = 31 * result + hash0(m.getParameterTypes());
			}
		}
		
		//return
		return result;
	}
	
	/**
	 * Write into
	 * */
	static final void write(ITech tech, DataOutput out) throws IOException {
		//TODO
	}
	
	/**
	 * Read from
	 * */
	static final ITech read(DataInput input) throws Throwable {
		return null; //TODO
	}
}
