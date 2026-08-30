package br.com.alessandro.backend.auth.config;

import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			DisabledUserFilter disabledUserFilter) {
		http.cors(withDefaults());
		http.oauth2AuthorizationServer((authorizationServer) -> {
			http.securityMatcher(authorizationServer.getEndpointsMatcher());
			authorizationServer.oidc(withDefaults());
		});
		http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated());
		http.oauth2ResourceServer((resourceServer) -> resourceServer.jwt(withDefaults()));
		http.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
				new LoginUrlAuthenticationEntryPoint("/login"), createRequestMatcher()));
		http.addFilterAfter(disabledUserFilter, BearerTokenAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	@Order(2)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, DisabledUserFilter disabledUserFilter,
			JwtAuthenticationConverter jwtAuthenticationConverter) {
		// Ignore browser/devtools probes and static assets from request cache
		HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
		requestCache.setRequestMatcher((request) -> {
			String uri = request.getRequestURI();
			return !uri.contains("/.well-known") && !uri.endsWith(".ico") && !uri.endsWith(".json")
					&& !uri.endsWith(".css") && !uri.endsWith(".js") && !uri.endsWith(".png") && !uri.endsWith(".svg");
		});

		http.cors(withDefaults());
		http.csrf((csrf) -> csrf.ignoringRequestMatchers("/api/**"));
		http.requestCache((cache) -> cache.requestCache(requestCache));
		http.authorizeHttpRequests((authorize) -> authorize
			.requestMatchers("/.well-known/**", "/favicon.ico", "/error")
			.permitAll()
			.requestMatchers("/api/admin/**")
			.hasRole("ADMIN")
			.anyRequest()
			.authenticated())
			.formLogin((formLogin) -> formLogin.loginPage("/login").permitAll())
			.logout((logout) -> logout
				.logoutRequestMatcher(PathPatternRequestMatcher.pathPattern("/logout"))
				.logoutSuccessHandler((request, response, authentication) -> {
					String redirectUri = request.getParameter("post_logout_redirect_uri");
					if (redirectUri != null && !redirectUri.isBlank()) {
						response.sendRedirect(redirectUri);
					} else {
						response.sendRedirect("/login?logout");
					}
				})
				.permitAll())
			.oauth2ResourceServer((resourceServer) -> resourceServer
				.jwt((jwt) -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)));
		http.addFilterAfter(disabledUserFilter, BearerTokenAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setExposedHeaders(List.of("Authorization", "Link", "X-Total-Count"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	private static RequestMatcher createRequestMatcher() {
		MediaTypeRequestMatcher requestMatcher = new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
		requestMatcher.setIgnoredMediaTypes(Set.of(MediaType.ALL));
		return requestMatcher;
	}

}
