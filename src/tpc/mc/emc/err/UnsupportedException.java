package tpc.mc.emc.err;

/**
 * Unsupported exception
 * */
public class UnsupportedException extends RuntimeException {
	
	private static final long serialVersionUID = -8905180658824008633L;
	
	public UnsupportedException() {}
	public UnsupportedException(String str) { super(str); }
	public UnsupportedException(String str, Throwable e) { super(str, e); }
}
