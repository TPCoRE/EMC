package tpc.mc.emc.err;

/**
 * Duplicate exception
 * */
public class DuplicateException extends UnsupportedException {
	
	public DuplicateException() {}
	public DuplicateException(String str) { super(str); }
	public DuplicateException(String str, Throwable e) { super(str, e); }
}
