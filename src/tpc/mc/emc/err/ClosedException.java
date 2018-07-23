package tpc.mc.emc.err;

/**
 * Closed exception
 * */
public class ClosedException extends RuntimeException {
	
	public ClosedException() {}
	public ClosedException(String str) { super(str); }
	public ClosedException(String str, Throwable e) { super(str, e); }
}
