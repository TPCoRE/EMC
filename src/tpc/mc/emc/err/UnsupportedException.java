package tpc.mc.emc.err;

/**
 * Unsupported exception
 * */
public class UnsupportedException extends RuntimeException {
	
	public UnsupportedException() {}
	public UnsupportedException(String str) { super(str); }
	public UnsupportedException(String str, Throwable e) { super(str, e); }
}
