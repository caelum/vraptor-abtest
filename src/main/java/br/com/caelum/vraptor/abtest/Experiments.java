package br.com.caelum.vraptor.abtest;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Experiments {

	private final HashCache hashCache = new HashCache();
	private final Map<String, ChosenExperiment> experiments = new HashMap<String, ChosenExperiment>();

	private final HttpServletRequest request;
	private ChosenExperiment lastExperiment;

	public Experiments(HttpServletRequest request) {
		this.request = request;
	}

	public void create(String name, Integer numberOfVariations) {
		String experimentHash = hashCache.getMD5For(name);
		
		Experiment ex = new Experiment(name, numberOfVariations);
		ChosenExperiment choice = ex.choose(request);
		experiments.put(ex.getKey(), choice);

		lastExperiment = choice;
	}

	public ChosenExperiment getExperimentByHash(String hash) {
		return experiments.get(hash);
	}

	public ChosenExperiment getExperimentByName(String name) {
		return experiments.get(hashCache.getMD5For(name));
	}

	public ChosenExperiment getLastExperiment() {
		return lastExperiment;
	}
}