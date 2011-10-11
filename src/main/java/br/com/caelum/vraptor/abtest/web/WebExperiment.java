package br.com.caelum.vraptor.abtest.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.abtest.Experiments;

public class WebExperiment {

	public static final String EXPERIMENTS = "br.com.caelum.abtest.Experiments";

	public static void define(String name, int variations,
			HttpServletRequest request, HttpServletResponse response) {
		getExperiments(request, response).create(name, variations);
	}

	private static Experiments getExperiments(HttpServletRequest request, HttpServletResponse response) {

		Experiments experiments = (Experiments) request
				.getAttribute(EXPERIMENTS);

		if (experiments == null) {
			experiments = new Experiments(request, response);
			request.setAttribute(EXPERIMENTS, experiments);
		}

		return experiments;
	}

}
