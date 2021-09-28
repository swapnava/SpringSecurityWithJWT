package com.swapnava.springsecurity.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int profileId;
	private String name;
	private String username;
	private String password;
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Role> role = new ArrayList<>();
}
