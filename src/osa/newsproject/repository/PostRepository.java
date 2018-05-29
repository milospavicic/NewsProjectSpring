package osa.newsproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.newsproject.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	List<Post> findByTags_Id(Integer tagId);
}
