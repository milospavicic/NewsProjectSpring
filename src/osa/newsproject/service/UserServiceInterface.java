package osa.newsproject.service;

import java.util.List;

import osa.newsproject.entity.User;


public interface UserServiceInterface {
	
	User findByUsernameAndPassword(String username,String password);
	
	User findByUsername(String username);

	User findOne(Integer userId);
	
	List<User> findAll();
	
	User save(User user);
	
	void remove(Integer id);
}
