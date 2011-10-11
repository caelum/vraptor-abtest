package br.com.caelum.vraptor.abtest;

public class Experiment {

	private final HashCache hashCache = new HashCache();
	
	private final String name;
	private final Integer numberOfVariations;
	
	private final Integer choosenVariationNumber;
	private final String choosenVariationHash;
	
	public int variationIterator = 0;

	public Experiment(String name, Integer numberOfVariations, String variationHash, Integer variationNumber) {
		this.name = name;
		this.numberOfVariations = numberOfVariations;
		this.choosenVariationHash = variationHash;
		this.choosenVariationNumber = variationNumber;
	}
	
	public void newVariation() {
		variationIterator++;
		if (variationIterator > numberOfVariations)
			throw new IllegalStateException("There are more variations than the number alowed in experiment declaration");
	}

	public boolean shouldViewThisVariation(String variationName) {
		String thisVariationHash = hashCache.getMD5For(variationName);
		return thisVariationHash.equals(choosenVariationHash) || 
			(choosenVariationNumber != null && variationIterator == choosenVariationNumber);
	}
	
	public String getName() {
		return name;
	}
}