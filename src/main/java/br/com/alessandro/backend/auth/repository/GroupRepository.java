package br.com.alessandro.backend.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alessandro.backend.auth.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
}
