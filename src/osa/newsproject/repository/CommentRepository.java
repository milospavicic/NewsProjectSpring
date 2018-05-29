package osa.newsproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.newsproject.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	List<Comment> findByPost_Id(Integer postId);
}
