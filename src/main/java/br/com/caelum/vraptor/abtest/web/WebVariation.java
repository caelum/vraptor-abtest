package br.com.caelum.vraptor.abtest.web;

import br.com.caelum.vraptor.abtest.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.abtest.Experiment;
import br.com.caelum.vraptor.abtest.Experiments;
import br.com.caelum.vraptor.abtest.HashCache;

public class WebVariation {

	/** Singleton cache. Nobody loves it. */
	private final static HashCache hash = new HashCache();

	public static boolean shouldView(String name, HttpServletRequest request, HttpServletResponse response) {

		ChosenExperiment experiment = experimentsFor(request).getLastExperiment();
		experiment.newVariation();

		return experiment.shouldViewThisVariation(name);
	}

	private static Experiments experimentsFor(HttpServletRequest request) {
		Experiments experiments = (Experiments) request
				.getAttribute(WebExperiment.EXPERIMENTS);
		if (experiments == null)
			throw new IllegalStateException(
					"Can't declare an experiment variation without an experiment!");
		return experiments;
	}

	public static String analyticsCodeFor(String experiment, String variation) {
		return "<script type=\"text/javascript\">\n"
				+ javascriptAnalyticsCodeFor(experiment, variation)
				+ "</script>\n";
	}

	public static String javascriptAnalyticsCodeFor(String experiment, String variation) {
		return "var _abtest=['"
				+ hash.getMD5For(experiment) + "','" + experiment + "','"
				+ variation + "','" + variation + "'];\n"
				+ "_gaq.push(['_trackEvent', 'A/B', _abtest[1], _abtest[3]]);\n";
	}

}
