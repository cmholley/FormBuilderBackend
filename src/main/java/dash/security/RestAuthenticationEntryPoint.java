package dash.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.ELRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

/**
 * This class handles all requests that do fail authentication. Also is
 * responsible for handling preflight requests which do not authenticate.
 * 
 * @author Tyler.swensen@gmail.com
 *
 */
@Component
public final class RestAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

	private static final RequestMatcher requestMatcher = new ELRequestMatcher(
			"hasHeader('X-Requested-With','XMLHttpRequest')");

	public RestAuthenticationEntryPoint() {
		super();
	}

	public RestAuthenticationEntryPoint(final String realmName) {
		setRealmName(realmName);
	}

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {

		if (isPreflight(request)) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else if (isRestRequest(request)) {
			response.sendError(418, "Unauthorized");
		} else {
			super.commence(request, response, authException);
		}
	}

	/**
	 * Checks if this is a X-domain pre-flight request.
	 *
	 * @param request
	 * @return
	 */
	private boolean isPreflight(final HttpServletRequest request) {
		return "OPTIONS".equals(request.getMethod());
	}

	/**
	 * Checks if it is a rest request
	 *
	 * @param request
	 * @return
	 */
	protected boolean isRestRequest(final HttpServletRequest request) {
		return requestMatcher.matches(request);
	}

}