package com.csetutorials.expensecircle.filters;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import org.springframework.stereotype.Component;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class InlineUrlRewriteFilter extends UrlRewriteFilter {

	@Override
	protected void loadUrlRewriter(FilterConfig filterConfig) throws ServletException {
		String xmlConfig =
			"""
				<urlrewrite>
					<rule>
						<from>^/groups.*</from>
						<to>/index.html</to>
					</rule>
				</urlrewrite>
				""";

		try (InputStream inputStream = new ByteArrayInputStream(xmlConfig.getBytes(StandardCharsets.UTF_8))) {
			Conf conf = new Conf(filterConfig.getServletContext(), inputStream, "inline-conf.xml", "");
			checkConf(conf);
		} catch (Exception e) {
			throw new ServletException("Failed to load inline URL rewrite rules", e);
		}
	}
}