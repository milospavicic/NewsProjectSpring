package osa.newsproject.service;

import java.util.List;

import osa.newsproject.entity.Comment;

public interface CommentServiceInterface {

	Comment findOne(Integer commentId);
	
	List<Comment> findAll();
	
	Comment save(Comment comment);
	
	void remove(Integer id);
	
	List<Comment> findByPost_Id(Integer postId);
	
	List<Comment> findAllByPopularity(String postId);
	
	List<Comment> findAllByOrderByDateDesc(String postId);
}
