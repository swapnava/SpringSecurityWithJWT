package com.swapnava.springsecurity.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swapnava.springsecurity.model.Profile;
import com.swapnava.springsecurity.model.Role;
import com.swapnava.springsecurity.repository.ProfileRepo;
import com.swapnava.springsecurity.repository.RoleRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProfileServiceImpl implements ProfileService, UserDetailsService{
	private final ProfileRepo profileRepo;
	private final RoleRepo roleRepo;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Profile profile=profileRepo.findByUsername(username);
		if(profile == null) {
			log.error("User Not found");
			throw new UsernameNotFoundException("No user with given username present");
		}
		else {
			log.info("User Found");
		}
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		profile.getRole().forEach(
				role -> authorities.add(new SimpleGrantedAuthority(role.getName()))
				);
		return new org.springframework.security.core.userdetails.User(profile.getName(),profile.getPassword(),authorities);
	}
	
	@Override
	public Profile saveProfile(Profile profile) {
		log.info("Profile saved {}" + profile);
		profile.setPassword(passwordEncoder.encode(profile.getPassword()));
		return profileRepo.save(profile);
	}

	@Override
	public Role saveRole(Role role) {
		return roleRepo.save(role);
	}

	@Override
	public void addRoleToProfile(String username, String roleName) {
		Profile profile = profileRepo.findByUsername(username);
		Role r = roleRepo.findByName(roleName);
		profile.getRole().add(r);
	}

	@Override
	public Profile getProfile(String username) {
		return profileRepo.findByUsername(username);
	}

	@Override
	public List<Profile> getProfiles() {
		return profileRepo.findAll();
	}

}
