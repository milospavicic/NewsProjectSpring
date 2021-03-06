package osa.newsproject.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import osa.newsproject.dto.CommentDTO;
import osa.newsproject.entity.Comment;
import osa.newsproject.service.CommentServiceInterface;
import osa.newsproject.service.PostServiceInterface;
import osa.newsproject.service.UserServiceInterface;

@RestController
@RequestMapping(value="api/comments")
public class CommentController {
	
	@Autowired
    private CommentServiceInterface commentServiceInterface;

    @Autowired
    private UserServiceInterface userServiceInterface;

    @Autowired
    private PostServiceInterface postServiceInterface;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public ResponseEntity<List<CommentDTO>>getComments(){
        logger.info("GET Method, request for all comments not sorted.");
        List<Comment> comments=commentServiceInterface.findAll();
        List<CommentDTO>commentDTOS=new ArrayList<>();
        for (Comment comment:comments) {
            commentDTOS.add(new CommentDTO(comment));
        }
        return new ResponseEntity<List<CommentDTO>>(commentDTOS,HttpStatus.OK);
    }
    @GetMapping(value = "/orderpop/{id}")
    public ResponseEntity<List<CommentDTO>>getCommentsOrderPop(@PathVariable("id")String id){
    	logger.info("GET Method, request for all comments for post with id: "+id+" sorted by popularity.");
    	System.out.println(id);
        List<Comment> comments=commentServiceInterface.findAllByPopularity(id);
        List<CommentDTO>commentDTOS=new ArrayList<>();
        for (Comment comment:comments) {
            commentDTOS.add(new CommentDTO(comment));
        }

        return new ResponseEntity<List<CommentDTO>>(commentDTOS,HttpStatus.OK);
    }
    
    @GetMapping(value = "/orderdate/{id}")
    public ResponseEntity<List<CommentDTO>>getCommentsOrderDate(@PathVariable("id")String id){
    	logger.info("GET Method, request for all comments for post with id: "+id+" sorted by date posted.");
    	System.out.println(id);
        List<Comment> comments=commentServiceInterface.findAllByOrderByDateDesc(id);
        List<CommentDTO>commentDTOS=new ArrayList<>();
        for (Comment comment:comments) {
            commentDTOS.add(new CommentDTO(comment));
        }

        return new ResponseEntity<List<CommentDTO>>(commentDTOS,HttpStatus.OK);
    }
    
    

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDTO>getComment(@PathVariable("id") Integer id){
    	logger.info("GET Method, request for one comment with id: "+id+".");
        Comment comment=commentServiceInterface.findOne(id);
        if(comment == null) {
        	logger.error("Comment with id: "+id+" not found.");
        	return new ResponseEntity<CommentDTO>(HttpStatus.NOT_FOUND);
        }
            

        return  new ResponseEntity<CommentDTO>(new CommentDTO(comment),HttpStatus.OK);
    }


    @GetMapping(value = "/post/{id}")
    public ResponseEntity<List<CommentDTO>>getCommentsByPost(@PathVariable("id")Integer id){
    	logger.info("GET Method, request for all comments for post with id: "+id+", not sorted.");
        List<Comment> comments=commentServiceInterface.findByPost_Id(id);
        List<CommentDTO>commentDTOS=new ArrayList<>();
        for (Comment comment:comments) {
            commentDTOS.add(new CommentDTO(comment));
        }

        return new ResponseEntity<List<CommentDTO>>(commentDTOS,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> addPost(@RequestParam("title") String title,
			   								  @RequestParam("description") String description,
			   								 @RequestParam("post_id") int post_id,
			   								 @RequestParam("user_id") int user_id){
    	logger.info("POST Method, new comment on post with id: "+post_id+" by user with id: "+user_id+".");
    	Date date = new Date();
        Comment comment=new Comment();
        comment.setTitle(title);
        comment.setDescription(description);
        comment.setDate(date);
        comment.setLikes(0);
        comment.setDislikes(0);
        comment.setUser(userServiceInterface.findOne(user_id));
        comment.setPost(postServiceInterface.findOne(post_id));

        comment=commentServiceInterface.save(comment);
        return new ResponseEntity<CommentDTO>(new CommentDTO(comment),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}",consumes = "application/json")
    public ResponseEntity<CommentDTO>updateComment(@PathVariable("id")Integer id,@RequestBody CommentDTO commentDTO){
    	logger.info("PUT Method, update comment with id: "+id+".");
    	Comment comment=commentServiceInterface.findOne(id);
        if(comment == null) {
        	logger.error("Comment with id: "+id+" not found.");
            return  new ResponseEntity<CommentDTO>(HttpStatus.NOT_FOUND);
        }
        comment.setTitle(commentDTO.getTitle());
        comment.setDescription(commentDTO.getDescription());
        comment.setLikes(commentDTO.getLikes());
        comment.setDislikes(commentDTO.getDislikes());

        comment=commentServiceInterface.save(comment);
        return new ResponseEntity<CommentDTO>(new CommentDTO(comment),HttpStatus.OK);

    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Integer id){
    	logger.info("DELETE Method, delete comment with id: "+id+".");
        Comment comment=commentServiceInterface.findOne(id);
        if(comment == null) {
        	logger.error("Comment with id: "+id+" not found.");
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        commentServiceInterface.remove(comment.getId());
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
