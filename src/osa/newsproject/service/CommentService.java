package osa.newsproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.newsproject.entity.Comment;
import osa.newsproject.repository.CommentRepository;

@Service
public class CommentService implements CommentServiceInterface{

	@Autowired
	CommentRepository commentRepository;
	
	@Override
	public Comment findOne(Integer commentId) {
		return commentRepository.findOne(commentId);
	}

	@Override
	public List<Comment> findAll() {
		return commentRepository.findAll();
	}

	@Override
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}

	@Override
	public void remove(Integer id) {
		commentRepository.delete(id);
	}

	@Override
	public List<Comment> findByPost_Id(Integer postId) {
		return commentRepository.findByPost_Id(postId);
	}

	@Override
	public List<Comment> findAllByPopularity(String postId) {
		return commentRepository.findAllByOrderByPopularity(postId);
	}

	@Override
	public List<Comment> findAllByOrderByDateDesc(String postId) {
		return commentRepository.findAllByOrderByDateDesc(postId);
	}

	
	
}
