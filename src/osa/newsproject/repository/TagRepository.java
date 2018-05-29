package osa.newsproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import osa.newsproject.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer>{
	List<Tag> findByPosts_Id(Integer postId);
}
