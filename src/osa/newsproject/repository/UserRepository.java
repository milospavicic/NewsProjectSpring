package osa.newsproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.newsproject.entity.User;

public interface UserRepository extends JpaRepository<User,Integer>{

	User findByUsername(String username);
	
	User findByUsernameAndPassword(String username,String password);
}
