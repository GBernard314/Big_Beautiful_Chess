package com.bigbeautifulchess.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class RegisterForm {
	private long Id;

	@Size(min = 2, max = 20)
	private String username;
	
	@Size(min = 6)
	private String password;
	
	@Email(message = "Email should be valid")
	private String email;
	

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
