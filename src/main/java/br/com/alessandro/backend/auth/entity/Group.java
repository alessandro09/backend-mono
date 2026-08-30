package br.com.alessandro.backend.auth.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "auth_groups")
public class Group {

	@Id
	@Column(name = "name", length = 50)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "auth_group_roles",
			joinColumns = @JoinColumn(name = "group_name"),
			inverseJoinColumns = @JoinColumn(name = "role_name"))
	private Set<Role> roles = new HashSet<>();

	protected Group() {
	}

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
