package osa.newsproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import osa.newsproject.entity.Post;
import osa.newsproject.repository.PostRepository;

@Service
public class PostService implements PostServiceInterface{

	@Autowired
	PostRepository postRepository;
	
	@Override
	public Post findOne(Integer postId) {
		return postRepository.findOne(postId);
	}

	@Override
	public List<Post> findAll() {
		return postRepository.findAll();
	}

	@Override
	public Post save(Post post) {
		return postRepository.save(post);
	}

	@Override
	public void remove(Integer id) {
		postRepository.delete(id);
	}
	
    @Override
    public List<Post> findByTags_Id(Integer tagId) {
        return postRepository.findByTags_Id(tagId);
    }

	@Override
	public List<Post> findAllByOrderByDateDesc() {
		return postRepository.findAllByOrderByDateDesc();
	}

	@Override
	public List<Post> findAllByOrderByDateAndSearch(String parameter, String parameter1) {
		return postRepository.findAllByOrderByDateAndSearch(parameter,parameter1);
	}

	@Override
	public List<Post> findAllByOrderByPopularity() {
		return postRepository.findAllByOrderByPopularity();
	}

	@Override
	public List<Post> findAllByOrderByPopularityAndSearch(String parameter, String parameter1) {
		return postRepository.findAllByOrderByPopularityAndSearch(parameter, parameter1);
	}
	
	@Override
	public List<Post> findAllByCommentsCount() {
		return postRepository.findAllByCommentsCount();
	}

	@Override
	public List<Post> findAllByCommentsCountAndSearch(String parameter,String parameter1) {
		return postRepository.findAllByCommentsCountAndSearch(parameter,parameter1);
	}

}
