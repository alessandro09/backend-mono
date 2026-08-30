package br.com.alessandro.backend.auth.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alessandro.backend.auth.service.UserAccessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Rejeita requisições autenticadas via JWT cujo usuário foi desativado,
 * garantindo revogação imediata de acesso mesmo para tokens ainda válidos.
 */
@Component
public class DisabledUserFilter extends OncePerRequestFilter {

	private final UserAccessService userAccessService;

	public DisabledUserFilter(UserAccessService userAccessService) {
		this.userAccessService = userAccessService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof JwtAuthenticationToken jwtAuthentication && authentication.isAuthenticated()
				&& this.userAccessService.isBlocked(jwtAuthentication.getName())) {
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário desativado");
			return;
		}
		filterChain.doFilter(request, response);
	}

}
