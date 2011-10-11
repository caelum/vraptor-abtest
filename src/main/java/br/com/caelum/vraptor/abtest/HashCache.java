package br.com.caelum.vraptor.abtest;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Caches all MD5 calculations for Experiment names and Variation names.
 *  
 * @author SŽrgio Lopes
 */
public class HashCache {

	private final Map<String, String> cache = new ConcurrentHashMap<String, String>();

	public String getMD5For(String value) {
		String md5 = cache.get(value);
		if (md5 == null) {
			md5 = calculateMD5(value);
			cache.put(value, md5);
		}
		return md5;
	}

	private String calculateMD5(String value) {
		return DigestUtils.md5Hex(value);
	}
}
