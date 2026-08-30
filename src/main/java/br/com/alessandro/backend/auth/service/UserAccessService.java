package br.com.alessandro.backend.auth.service;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import br.com.alessandro.backend.auth.repository.UserRepository;

/**
 * Blocklist de usuários desativados em Redis. Garante que JWTs já emitidos
 * (válidos até o fim do TTL) parem de ser aceitos imediatamente após a
 * desativação do usuário.
 */
@Service
public class UserAccessService implements ApplicationRunner {

	private static final String DISABLED_USERS_KEY = "auth:disabled-users";

	private final StringRedisTemplate redisTemplate;

	private final UserRepository userRepository;

	public UserAccessService(StringRedisTemplate redisTemplate, UserRepository userRepository) {
		this.redisTemplate = redisTemplate;
		this.userRepository = userRepository;
	}

	public void block(String username) {
		this.redisTemplate.opsForSet().add(DISABLED_USERS_KEY, username);
	}

	public void unblock(String username) {
		this.redisTemplate.opsForSet().remove(DISABLED_USERS_KEY, username);
	}

	public boolean isBlocked(String username) {
		return Boolean.TRUE.equals(this.redisTemplate.opsForSet().isMember(DISABLED_USERS_KEY, username));
	}

	@Override
	public void run(ApplicationArguments args) {
		// sincroniza a blocklist com o banco ao subir a aplicação
		this.redisTemplate.delete(DISABLED_USERS_KEY);
		this.userRepository.findAllByEnabledFalse().forEach((user) -> block(user.getUsername()));
	}

}
