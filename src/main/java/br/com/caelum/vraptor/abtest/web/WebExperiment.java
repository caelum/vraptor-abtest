package br.com.caelum.vraptor.abtest.web;

import br.com.caelum.vraptor.abtest.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.abtest.Experiments;

public class WebExperiment {

	public static final String EXPERIMENTS = "br.com.caelum.abtest.Experiments";

	public static void define(String name, int variations,
			HttpServletRequest request) {
		getExperiments(request).create(name, variations);
	}

	private static Experiments getExperiments(HttpServletRequest request) {

		Experiments experiments = (Experiments) request
				.getAttribute(EXPERIMENTS);

		if (experiments == null) {
			experiments = new Experiments(request);
			request.setAttribute(EXPERIMENTS, experiments);
		}

		return experiments;
	}

}
