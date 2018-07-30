package tpc.mc.emc.err;

/**
 * Closed exception
 * */
public class ClosedException extends DuplicateException {
	
	private static final long serialVersionUID = -5805493716237764737L;
	
	public ClosedException() {}
	public ClosedException(String str) { super(str); }
	public ClosedException(String str, Throwable e) { super(str, e); }
}
