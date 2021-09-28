package com.swapnava.springsecurity.service;

import java.util.List;

import com.swapnava.springsecurity.model.Profile;
import com.swapnava.springsecurity.model.Role;

public interface ProfileService {
	Profile saveProfile(Profile profile);
	Role saveRole(Role role);
	void addRoleToProfile(String username,String roleName);
	Profile getProfile(String username);
	List<Profile> getProfiles();
}
