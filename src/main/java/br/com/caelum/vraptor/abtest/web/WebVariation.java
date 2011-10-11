package br.com.caelum.vraptor.abtest.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.abtest.Experiment;
import br.com.caelum.vraptor.abtest.Experiments;
import br.com.caelum.vraptor.abtest.HashCache;

public class WebVariation {

	/** Singleton cache. Nobody loves it. */
	private final static HashCache hash = new HashCache();

	public static boolean shouldView(String name, HttpServletRequest request, HttpServletResponse response) {

		Experiment experiment = experimentsFor(request).getLastExperiment();
		experiment.newVariation();
		response.addCookie(new Cookie("_ab_" + hash.getMD5For(experiment.getName()), hash.getMD5For(name)));

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

	public static String codeFor(String experiment, String variation) {
		return "<script type=\"text/javascript\">\n" + "var _abtest=['"
				+ hash.getMD5For(experiment) + "','" + experiment + "','"
				+ hash.getMD5For(variation) + "','" + variation + "'];\n"
				+ "_gaq.push(['_trackEvent', 'A/B', _abtest[1], _abtest[3]]);\n"
				+ "</script>\n";
	}

}
