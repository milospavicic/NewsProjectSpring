package osa.newsproject.service;

import java.util.List;

import osa.newsproject.entity.Post;

public interface PostServiceInterface{
	Post findOne(Integer postId);
	
	List<Post> findAll();
	
	Post save(Post post);
	
	void remove(Integer id);
	
	List<Post> findByTags_Id(Integer tagId);
}
