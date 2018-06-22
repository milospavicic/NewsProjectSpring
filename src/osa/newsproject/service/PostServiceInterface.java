package osa.newsproject.service;

import java.util.List;

import osa.newsproject.entity.Post;

public interface PostServiceInterface{
	Post findOne(Integer postId);
	
	List<Post> findAll();
	
	Post save(Post post);
	
	void remove(Integer id);
	
	List<Post> findByTags_Id(Integer tagId);
	
	List<Post> findAllByOrderByDateDesc();
	
	List<Post> findAllByOrderByDateAndSearch(String parameter,String parameter1);
	
	List<Post> findAllByOrderByPopularity();
	
	List<Post> findAllByOrderByPopularityAndSearch(String parameter,String parameter1);
	
	List<Post> findAllByCommentsCount();
	
	List<Post> findAllByCommentsCountAndSearch(String parameter,String parameter1);
}
