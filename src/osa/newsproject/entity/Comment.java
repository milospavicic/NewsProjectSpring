package osa.newsproject.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;

@Entity
@Table(name = "comments")
public class Comment implements Serializable{

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "comment_id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "title", unique = false, nullable = false, length = 100)
	private String title;

	@Column(name = "description", unique = false, nullable = false, length = 1000)
	private String description;

	@Temporal(TIMESTAMP)
	@Column(name = "date", unique = false, nullable = false)
	private Date date;

	@Column(name = "likes", unique = false, nullable = false)
	private int likes;

	@Column(name = "dislikes", unique = false, nullable = false)
	private int dislikes;

	@ManyToOne
	@JoinColumn(name = "post_id", referencedColumnName = "post_id", nullable = false)
	private Post post;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;

	public Comment() {
	}

	public Comment(Integer id, String title, String description, Date date, int likes, int dislikes, Post post,
			User user) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.date = date;
		this.likes = likes;
		this.dislikes = dislikes;
		this.post = post;
		this.user = user;
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

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}



}
