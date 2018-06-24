package osa.newsproject.service;

import java.util.List;

import osa.newsproject.entity.Tag;

public interface TagServiceInterface {
	
	List<Tag> findByPosts_Id(Integer postId);
	
	Tag findOne(Integer tagId);
	
	Tag findByName(String name);
	
	List<Tag> findAll();
	
	Tag save(Tag tag);
	
	void remove(Integer id);
}
