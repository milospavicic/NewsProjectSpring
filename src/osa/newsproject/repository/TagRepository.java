package osa.newsproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import osa.newsproject.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Integer>{
	List<Tag> findByPosts_Id(Integer postId);

	Tag findByName(String name);
//	@Query(value="SELECT * FROM tags AS t WHERE t.name=?",nativeQuery=true)
//	Tag findOne(String name);
}
