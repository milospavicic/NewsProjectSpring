package osa.newsproject.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<List<PostDTO>> getPosts(){
    	logger.info("GET Method, request for all posts not sorted.");
        List<Post> posts=postServiceInterface.findAll();
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
            System.out.println(post.getDate());
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }
    
    @GetMapping(value = "/orderbydate")
    public ResponseEntity<List<PostDTO>> getPostsOrderByDate(){
    	logger.info("GET Method, request for all posts sorted by date posted.");
    	System.out.println("orderbydate");
        List<Post> posts=postServiceInterface.findAllByOrderByDateDesc();
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }
    
    @GetMapping(value = "/orderbydate/{parameter}")
    public ResponseEntity<List<PostDTO>> getPostsOrderByDateAndSearch(@PathVariable("parameter") String parameter){
    	logger.info("GET Method, request for all posts sorted by date posted, and searched for \""+parameter+"\".");
    	parameter = "%"+parameter+"%";
        List<Post> posts=postServiceInterface.findAllByOrderByDateAndSearch(parameter, parameter);
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }
    
    @GetMapping(value = "/orderbypop")
    public ResponseEntity<List<PostDTO>> getPostsOrderByPopularity(){
    	logger.info("GET Method, request for all posts sorted by popularity.");
        List<Post> posts=postServiceInterface.findAllByOrderByPopularity();
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
            System.out.println(post.getDate());
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }
    @GetMapping(value = "/orderbypop/{parameter}")
    public ResponseEntity<List<PostDTO>> getPostsOrderByPopularityAndSearch(@PathVariable("parameter") String parameter){
    	logger.info("GET Method, request for all posts sorted by popularity, and searched for \""+parameter+"\".");
    	parameter = "%"+parameter+"%";
        List<Post> posts=postServiceInterface.findAllByOrderByPopularityAndSearch(parameter, parameter);
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
            System.out.println(post.getDate());
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }
    
    @GetMapping(value = "/orderbycommcount")
    public ResponseEntity<List<PostDTO>> getPostsOrderByCommentsCount(){
    	logger.info("GET Method, request for all posts sorted by comments count.");
        List<Post> posts=postServiceInterface.findAllByCommentsCount();
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
            System.out.println(post.getDate());
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }
    
    @GetMapping(value = "/orderbycommcount/{parameter}")
    public ResponseEntity<List<PostDTO>> getPostsOrderByCommentsCountAndSearch(@PathVariable("parameter") String parameter){
    	logger.info("GET Method, request for all posts sorted by comments count, and searched for \""+parameter+"\".");
    	parameter = "%"+parameter+"%";
    	System.out.println(parameter);
        List<Post> posts=postServiceInterface.findAllByCommentsCountAndSearch(parameter,parameter);
        List<PostDTO> postDTOS=new ArrayList<>();
        for (Post post:posts) {
            postDTOS.add(new PostDTO(post));
        }
        return new ResponseEntity<List<PostDTO>>(postDTOS,HttpStatus.OK);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> getPost(@PathVariable("id") Integer id){
    	logger.info("GET Method, request for post with id: "+id+".");
        Post post=postServiceInterface.findOne(id);
        if(post == null)
            return new ResponseEntity<PostDTO>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.OK);
    }

    @GetMapping(value = "tag/{id}")
    public ResponseEntity<List<TagDTO>> getTagByPost(@PathVariable("id") Integer id){
    	logger.info("GET Method, request for all tags for post with id: "+id+".");
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

    @PostMapping
    public ResponseEntity<PostDTO> saveNewPost(@RequestParam("title") String title,
    										   @RequestParam("description") String description,
    										   @RequestParam("user_id") int user_id,
    										   @RequestParam("longitude") float longitude,
    										   @RequestParam("latitude") float latitude){
    	logger.info("POST Method, new post by user with id: "+user_id+".");
    	Post post=new Post();
    	Date date = new Date();
    	System.out.println(title+" "+description+" "+user_id);
        post.setTitle(title);
        post.setDescription(description);
        post.setLikes(0);
        post.setDislikes(0);
        post.setDate(date);
        post.setLatitude(latitude);
        post.setLongitude(longitude);
        post.setUser(userServiceIterface.findOne(user_id));
		post=postServiceInterface.save(post);

        return  new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.CREATED);
    }
    @PostMapping(value="/link_tp/{postId}/{tagId}")
	public ResponseEntity<PostDTO> setTagsInPost(@PathVariable("postId") int postId,@PathVariable("tagId") int tagId){
    	logger.info("POST Method, linking tag with id: "+tagId+" with post with id: "+postId+".");
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

    @PutMapping(value = "/{id}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO,@PathVariable("id") Integer id){
    	logger.info("PUT Method, update post with id: "+id+".");
        Post post=postServiceInterface.findOne(id);
        if(post == null) {
        	logger.error("Post with id: "+id+" not found.");
            return  new ResponseEntity<PostDTO>(HttpStatus.NOT_FOUND);
        }

        post.setTitle(postDTO.getTitle());
        post.setDescription(postDTO.getDescription());
        post.setLikes(postDTO.getLikes());
        post.setDislikes(postDTO.getDislikes());
        post.setDate(postDTO.getDate());
        post.setLongitude(postDTO.getLongitude());
        post.setLatitude(postDTO.getLatitude());
        post.setUser(userServiceIterface.findOne(postDTO.getUser().getId()));

        post=postServiceInterface.save(post);
        return new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.OK);
    }

    @PostMapping(value = "/upload_photo")
    public ResponseEntity<PostDTO> updatePost(@RequestParam("id") Integer id,@RequestParam("photo") MultipartFile photo){
    	logger.info("POST Method, upload photo for post with id: "+id+".");
        Post post=postServiceInterface.findOne(id);
        if(post == null) {
        	logger.error("Post with id: "+id+" not found.");
            return  new ResponseEntity<PostDTO>(HttpStatus.NOT_FOUND);
        }

        try {
			post.setPhoto(photo.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Upload photo for post with id: "+id+" failed.");
			return  new ResponseEntity<PostDTO>(HttpStatus.BAD_REQUEST);
		}

        post=postServiceInterface.save(post);
        return new ResponseEntity<PostDTO>(new PostDTO(post),HttpStatus.OK);
    }
    
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id")Integer id){
    	logger.info("DELETE Method, delete post with id: "+id+".");
        Post post=postServiceInterface.findOne(id);
        if(post == null) {
        	logger.error("Post with id: "+id+" not found.");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        postServiceInterface.remove(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @DeleteMapping(value="/remove_tags/{postId}")
	public ResponseEntity<Void> deleteTagsFromPost(@PathVariable("postId") int postId){
    	logger.info("DELETE Method, delete all tags for post with id: "+postId+".");
        Post post=postServiceInterface.findOne(postId);
        if(post == null) {
        	logger.error("Post with id: "+postId+" not found.");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        for (Tag tag : post.getTags()) {
			tag.getPosts().remove(post);
			tagServiceInterfce.save(tag);
		}
        post.getTags().clear();
        postServiceInterface.save(post);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
