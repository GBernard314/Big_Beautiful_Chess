package com.bigbeautifulchess.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="users")
public class User implements UserDetails {
	private static final long serialVersionUID = -2963008589618789228L;
	
	public User(String name) {
		this.username = name;
	}
	
	public User() {
		
	}
	
	@Id @Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	@SequenceGenerator(name = "seqUser", sequenceName = "seq_user")
	private Long id;

	@Column(length = 100)
	private String username;
	
	@Column(length = 100)
	private String password;
	
	@Column(length = 100)
	private String email;
	
	@Column(length = 100)
	private String friends_list_id; 
	
	@ManyToMany(fetch=FetchType.EAGER)
	private Collection<Authority> authorities;
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	public String getFriends_list_id() {
		return friends_list_id;
	}

	public void setFriends_list_id(String friends_list_id) {
		this.friends_list_id = friends_list_id;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}

	public Collection<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public String[] splitFriendsList() {
		String s = this.friends_list_id;
		String[] result = s.split(";");
		for (String a : result) 
            System.out.println(a); 
		return result;
	}
	
}
