package br.com.alessandro.backend.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.auth.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
