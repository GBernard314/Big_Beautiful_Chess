package com.bigbeautifulchess.service;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bigbeautifulchess.domain.Authority;
import com.bigbeautifulchess.repository.UserRepository;

@Service
public class DbUserDetailsService implements UserDetailsService {
	@Autowired
	private UserRepository users;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user = users.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		
		return user;
	}
}
