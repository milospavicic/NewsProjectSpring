package osa.newsproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.newsproject.entity.Tag;
import osa.newsproject.repository.TagRepository;

@Service
public class TagService implements TagServiceInterface{

	@Autowired
	TagRepository tagRepository;

	@Override
	public Tag findOne(Integer tagId) {
		return tagRepository.findOne(tagId);
	}

	@Override
	public Tag findByName(String name) {
		return tagRepository.findByName(name);
	}

	@Override
	public List<Tag> findAll() {
		return tagRepository.findAll();
	}

	@Override
	public Tag save(Tag tag) {
		return tagRepository.save(tag);
	}

	@Override
	public void remove(Integer id) {
		tagRepository.delete(id);
	}

	@Override
	public List<Tag> findByPosts_Id(Integer postId) {
		return tagRepository.findByPosts_Id(postId);
	}	
	
}
