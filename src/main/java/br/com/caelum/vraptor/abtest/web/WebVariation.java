package br.com.caelum.vraptor.abtest.web;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.abtest.Experiment;
import br.com.caelum.vraptor.abtest.Experiments;
import br.com.caelum.vraptor.abtest.HashCache;

public class WebVariation {

	/** Singleton cache. Nobody loves it. */
	private final static HashCache hash = new HashCache();

	public static boolean shouldView(String name, HttpServletRequest request) {

		Experiment experiment = experimentsFor(request).getLastExperiment();
		experiment.newVariation();

		return experiment.shouldViewThisVariation(name);
	}

	private static Experiments experimentsFor(HttpServletRequest request) {
		Experiments experiments = (Experiments) request
				.getAttribute("br.com.caelum.abtest.Experiments");
		if (experiments == null)
			throw new IllegalStateException(
					"Can't declare an experiment variation without an experiment!");
		return experiments;
	}
	
	public static String codeFor(String experiment, String variation) {
		return "<script type=\"text/javascript\">\n" +
	  	  "var _abtest=['" + hash.getMD5For(experiment) + "','" + experiment+"','" +hash.getMD5For(variation)+"','"+variation+"'];\n" +
	  	  "gaq.push(['_trackEvent', 'A/B', _abtest[1], _abtest[3]]);\n" +
		  "</script>\n";
	}

}
