package osa.newsproject.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import osa.newsproject.dto.PostDTO;
import osa.newsproject.dto.TagDTO;
import osa.newsproject.entity.Post;
import osa.newsproject.entity.Tag;
import osa.newsproject.service.PostServiceInterface;
import osa.newsproject.service.TagServiceInterface;
import osa.newsproject.service.UserServiceInterface;

@RestController
@RequestMapping(value="api/posts")
public class PostController {
	@Autowired
    private PostServiceInterface postServiceInterface;

    @Autowired
    private UserServiceInterface userServiceIterface;

    @Autowired
    private TagServiceInterface tagServiceInterfce;

    @GetMapping
    public ResponseEntity<List<PostDTO>> getPosts(){
        List<Post> posts=postServiceInterface.findAll();
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("id") Integer id){
        Post post=postServiceInterface.findOne(id);
        if(post == null)
            return new ResponseEntity<PostDTO>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.OK);
    }

    @GetMapping(value = "tag/{id}")
    public ResponseEntity<List<TagDTO>> getTagByPost(@PathVariable("id") Integer id){
        List<Tag> tags=tagServiceInterfce.findByPosts_Id(id);
        List<TagDTO>tagDTOS=new ArrayList<>();
        if(tags == null)
            return new ResponseEntity<List<TagDTO>>(HttpStatus.NOT_FOUND);
        else {

            for (Tag t:tags)
                tagDTOS.add(new TagDTO(t));
        }
        return new ResponseEntity<List<TagDTO>>(tagDTOS,HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<PostDTO> saveNewPost(@RequestBody PostDTO postDTO){
        Post post=new Post();
        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setLikes(postDTO.getLikes());
        post.setDislikes(postDTO.getDislikes());
        post.setDate(postDTO.getDate());
        post.setLatitude(postDTO.getLatitude());
        post.setLongitude(postDTO.getLongitude());
        post.setPhoto(postDTO.getPhoto());
        post.setUser(userServiceIterface.findOne(postDTO.getUser().getId()));

        post=postServiceInterface.save(post);
        return  new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.CREATED);
    }
    @PutMapping(value="/linkTagToPost/{postId}/{tagId}")
	public ResponseEntity<PostDTO> setTagsInPost(@PathVariable("postId") int postId,@PathVariable("tagId") int tagId){
		Post post = postServiceInterface.findOne(postId);
		Tag tag = tagServiceInterfce.findOne(tagId);
		System.out.println(post);
		System.out.println(tag);
		if(post != null && tag != null) {
			post.getTags().add(tag);
			tag.getPosts().add(post);
			post = postServiceInterface.save(post);
			tag = tagServiceInterfce.save(tag);
			return new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.OK);
		}else 
			return new ResponseEntity<PostDTO>(HttpStatus.BAD_REQUEST);

	}

    @PutMapping(value = "/{id}",consumes = "application/json")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO,@PathVariable("id") Integer id){

        Post post=postServiceInterface.findOne(id);
        if(post == null)
            return  new ResponseEntity<PostDTO>(HttpStatus.BAD_REQUEST);

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setPhoto(postDTO.getPhoto());
        post.setLikes(postDTO.getLikes());
        post.setDislikes(postDTO.getDislikes());
        post.setDate(postDTO.getDate());
        post.setLongitude(postDTO.getLongitude());
        post.setLatitude(postDTO.getLatitude());
        post.setUser(userServiceIterface.findOne(postDTO.getUser().getId()));


        post=postServiceInterface.save(post);
        return new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id")Integer id){
        Post post=postServiceInterface.findOne(id);
        if(post == null)
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        postServiceInterface.remove(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
