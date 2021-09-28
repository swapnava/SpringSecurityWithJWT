package com.swapnava.springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swapnava.springsecurity.model.Profile;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Integer>{
	Profile findByUsername(String username);
}
