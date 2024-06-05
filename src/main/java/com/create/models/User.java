package com.create.models;

import com.create.domain.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer Id;
	
	private String fullName;
	
	@Column(unique = true)
	private String email;
	
	@Column(unique = true)
	private String mobile;
	
	private String password;
	
	private String profilePicture;
	
	private UserRole role;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(Integer Id, String fullName, String email, String mobile, String password, String profilePicture,
			UserRole role) {
		super();
		this.Id = Id;
		this.fullName = fullName;
		this.email = email;
		this.mobile = mobile;
		this.password = password;
		this.profilePicture = profilePicture;
		this.role = role;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	
	@Override
	public String toString() {
		return "User [id=" + Id + ", fullName=" + fullName + ", email=" + email + ", mobile=" + mobile + ", password="
				+ password + ", profilePicture=" + profilePicture + "]";
	}

}

