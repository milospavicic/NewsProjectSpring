package osa.newsproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import osa.newsproject.entity.Comment;
import osa.newsproject.entity.Post;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	List<Comment> findByPost_Id(Integer postId);
	
	@Query(value="SELECT * FROM comments AS c WHERE c.post_id=? ORDER BY (c.likes-c.dislikes) DESC",nativeQuery=true)
	List<Comment> findAllByOrderByPopularity(String param);
	@Query(value="SELECT * FROM comments AS c WHERE c.post_id=? ORDER BY c.date DESC",nativeQuery=true)
	List<Comment> findAllByOrderByDateDesc(String param);
}
