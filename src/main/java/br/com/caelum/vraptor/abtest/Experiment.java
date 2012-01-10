package br.com.caelum.vraptor.abtest;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Random;
import br.com.caelum.vraptor.abtest.Experiment;
import br.com.caelum.vraptor.abtest.Experiments;
import br.com.caelum.vraptor.abtest.HashCache;

public class Experiment {

	/** Singleton cache. Nobody loves it. */
	private final static HashCache hash = new HashCache();

	private final String name;
	private final Integer numberOfVariations;

	public Experiment(String name, Integer numberOfVariations) {
		this.name = name;
		this.numberOfVariations = numberOfVariations;
	}
	
	public String getName() {
		return name;
	}
	
	public ChosenExperiment choose(HttpServletRequest request) {
		String forcedVariation = request
				.getParameter("br.com.caelum.abtest.force_variation");

		if (forcedVariation != null) {
			return new ChosenExperiment(this, null,  Integer.parseInt(forcedVariation));
		}
		
		String variation = lookupCookie(getKey(), request);
		return randomIfNoneFound(numberOfVariations, variation);
	}
	
	public String getKey() {
		return hash.getMD5For(name);
	}

	private ChosenExperiment randomIfNoneFound(Integer numberOfVariations, String variation) {
		if (variation == null) {
			int number = new Random().nextInt(numberOfVariations) + 1;
			return new ChosenExperiment(this, null, number);
		}
		return new ChosenExperiment(this, variation, null);
	}

	private String lookupCookie(String experimentHash, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("_ab_" + experimentHash)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
}