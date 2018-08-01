package tpc.mc.emc.tech;

/**
 * The tech that contains some information
 * */
public abstract class INamable extends ITech {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor, See {@link #ITech(byte[])}
	 * */
	public INamable(byte[] attributes) {
		super(attributes);
	}
	
	/**
	 * Return a iterable
	 * */
	public abstract Iterable<String> infos(Language lang);
	
	public static final Language EN_US = new Language();
	public static final Language ZH_CN = new Language();
	public static final Language ZH_TW = new Language();
	public static final Language RU_RU = new Language();
	public static final Language JA_JP = new Language();
	
	private static final class Language {}
}
