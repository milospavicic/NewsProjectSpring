package osa.newsproject.dto;

import java.io.Serializable;
import java.util.Date;

import osa.newsproject.entity.Comment;

public class CommentDTO implements Serializable{
	
	private Integer id;
    private String title;
    private String description;
    private Date date;
    private Integer likes;
    private Integer dislikes;
    private UserDTO user;
    private PostDTO post;

    public CommentDTO() {
    }

    public CommentDTO(Integer id, String title, String description, Date date, Integer likes, Integer dislikes, UserDTO user, PostDTO post) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.likes = likes;
        this.dislikes = dislikes;
        this.user = user;
        this.post = post;
    }

	public CommentDTO(Comment comment) {
        this(comment.getId(),comment.getTitle(),comment.getDescription(),comment.getDate(),comment.getLikes(),comment.getDislikes(),new UserDTO(comment.getUser()),new PostDTO(comment.getPost()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public PostDTO getPost() {
        return post;
    }

    public void setPost(PostDTO post) {
        this.post = post;
    }
}
