package tpc.mc.emc.tech;

import java.io.Serializable;
import java.util.Locale;

import tpc.mc.emc.Stepable;
import tpc.mc.emc.platform.standard.IOption;

/**
 * The attribute of {@link Technique}
 * */
public interface IAttribute extends Serializable {
	
	/**
	 * Produce a {@link Stepable}
	 * */
	Stepable tack(IOption opt);
	
	/**
	 * Get the tech's information
	 * */
	Iterable<String> info(Locale loc);
}
