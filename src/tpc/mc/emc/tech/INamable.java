package tpc.mc.emc.tech;

import java.util.Iterator;

/**
 * The tech that contains some information
 * */
public abstract class INamable extends ITech {
	
	/**
	 * Return a iterator, null is allowed
	 * */
	public abstract Iterator<String> infos(Language lang);
	
	public static final Language EN_US = new Language();
	public static final Language ZH_CN = new Language();
	public static final Language ZH_TW = new Language();
	public static final Language RU_RU = new Language();
	public static final Language JA_JP = new Language();
	
	private static final class Language {}
}
