package br.com.alessandro.auth.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;

/**
 * Builds the standalone Jackson {@link ObjectMapper} used to serialize/deserialize the
 * JSON payloads (client settings, token settings, authorization attributes/metadata)
 * persisted in the JPA-backed OAuth2 stores.
 *
 * <p>This is intentionally <b>not</b> exposed as a Spring {@code @Bean}: Spring Boot's
 * Jackson auto-configuration only creates its own general-purpose {@code ObjectMapper}
 * bean when none exists yet ({@code @ConditionalOnMissingBean(ObjectMapper.class)}), so
 * registering this specialized mapper as a bean would silently replace the one used by
 * Spring MVC for regular REST responses. Instead, each JPA datasource that needs it
 * builds its own private instance via {@link #create()}, mirroring the approach used by
 * Spring Authorization Server's own {@code JdbcRegisteredClientRepository} and
 * {@code JdbcOAuth2AuthorizationService} row mappers.
 */
public final class AuthorizationServerObjectMapperFactory {

    private AuthorizationServerObjectMapperFactory() {
    }

    public static ObjectMapper create() {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = AuthorizationServerObjectMapperFactory.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        return objectMapper;
    }
}
