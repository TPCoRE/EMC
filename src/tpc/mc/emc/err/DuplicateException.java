package tpc.mc.emc.err;

/**
 * Duplicate exception
 * */
public class DuplicateException extends UnsupportedException {
	
	private static final long serialVersionUID = 5897099235280729185L;
	
	public DuplicateException() {}
	public DuplicateException(String str) { super(str); }
	public DuplicateException(String str, Throwable e) { super(str, e); }
}
