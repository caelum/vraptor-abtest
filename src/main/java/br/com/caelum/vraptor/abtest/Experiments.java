package br.com.caelum.vraptor.abtest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Experiments {

	private final HashCache hashCache = new HashCache();
	private final Map<String, Experiment> experiments = new HashMap<String, Experiment>();

	private final HttpServletRequest request;
	private Experiment lastExperiment;

	public Experiments(HttpServletRequest request) {
		this.request = request;
	}

	public void create(String name, Integer numberOfVariations) {
		String experimentHash = hashCache.getMD5For(name);

		Integer variationNumber = null;
		String variationHash = null;

		// is there a forced variation number?
		String forcedVariationParam = request
				.getParameter("br.com.caelum.vraptor.abtest.force_variation");

		if (forcedVariationParam == null) {
			variationHash = lookupCookie(experimentHash, variationHash);
			variationNumber = randomIfNoneFound(numberOfVariations,
					variationNumber, variationHash);
		} else {
			variationNumber = Integer.parseInt(forcedVariationParam);
		}

		Experiment e = new Experiment(name, numberOfVariations, variationHash,
				variationNumber);
		experiments.put(experimentHash, e);

		lastExperiment = e;
	}

	private Integer randomIfNoneFound(Integer numberOfVariations,
			Integer variationNumber, String variationHash) {
		// generated a random variation number
		if (variationHash == null) {
			variationNumber = new Random().nextInt(numberOfVariations) + 1;
		}
		return variationNumber;
	}

	private String lookupCookie(String experimentHash, String variationHash) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("_ab_" + experimentHash)) {
					variationHash = cookie.getValue();
					break;
				}
			}
		}
		return variationHash;
	}

	public Experiment getExperimentByHash(String hash) {
		return experiments.get(hash);
	}

	public Experiment getExperimentByName(String name) {
		return experiments.get(hashCache.getMD5For(name));
	}

	public Experiment getLastExperiment() {
		return lastExperiment;
	}
}