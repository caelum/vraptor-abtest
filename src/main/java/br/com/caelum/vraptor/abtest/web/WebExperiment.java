package br.com.caelum.vraptor.abtest.web;

import javax.servlet.http.HttpServletRequest;

import br.com.caelum.vraptor.abtest.Experiments;

public class WebExperiment {

	public static void define(String name, int variations,
			HttpServletRequest request) {
		getExperiments(request).create(name, variations);
	}

	private static Experiments getExperiments(HttpServletRequest request) {
		
		Experiments experiments = (Experiments) request
				.getAttribute("br.com.caelum.abtest.Experiments");
		
		if (experiments == null) {
			experiments = new Experiments(request);
			request.setAttribute("br.com.caelum.abtest.Experiments",
					experiments);
		}
		
		return experiments;
	}

}
