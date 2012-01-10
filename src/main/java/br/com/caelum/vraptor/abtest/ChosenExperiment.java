package br.com.caelum.vraptor.abtest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.abtest.Experiment;
import br.com.caelum.vraptor.abtest.Experiments;
import br.com.caelum.vraptor.abtest.HashCache;

public class ChosenExperiment {

	private final HashCache hashCache = new HashCache();
	
	private final Experiment experiment;
	private final Integer choosenVariationNumber;
	private final String choosenVariationHash;
	
	public int variationIterator = 0;

	public ChosenExperiment(Experiment experiment, String variationHash, Integer variationNumber) {
		this.experiment = experiment;
		this.choosenVariationHash = variationHash;
		this.choosenVariationNumber = variationNumber;
	}
	
	public void newVariation() {
		variationIterator++;
	}

	public boolean shouldViewThisVariation(String variationName) {
		String thisVariationHash = hashCache.getMD5For(experiment.getName() + choosenVariationNumber);
		return thisVariationHash.equals(choosenVariationHash) || 
			(choosenVariationNumber != null && variationIterator == choosenVariationNumber);
	}
	
	public String getKeyHash() {
		return "_ab_" + experiment.getKey();
	}
	public String getValueHash() {
		return choosenVariationHash!=null ? choosenVariationHash : hashCache.getMD5For(experiment.getName() + choosenVariationNumber);
	}

}