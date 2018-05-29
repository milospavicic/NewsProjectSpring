package osa.newsproject.dto;

import java.util.Date;

import osa.newsproject.entity.Post;

public class PostDTO {
	
	private Integer id;

	private String title;

	private String description;

	private byte[] photo;

	private Date date;

	private float longitude;

	private float latitude;

	private int likes;

	private int dislikes;
	
	private UserDTO user;
	
	public PostDTO() {}
	
	
	public PostDTO(Integer id, String title, String description, byte[] photo, Date date, float longitude,
			float latitude, int likes, int dislikes, UserDTO user) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.photo = photo;
		this.date = date;
		this.longitude = longitude;
		this.latitude = latitude;
		this.likes = likes;
		this.dislikes = dislikes;
		this.user = user;
	}


	public PostDTO(Post post) {
		this(post.getId(),post.getTitle(),post.getDescription(),post.getPhoto(),post.getDate(),post.getLongitude(),post.getLatitude(),post.getLikes(),post.getDislikes(),new UserDTO(post.getUser()));
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



	public byte[] getPhoto() {
		return photo;
	}



	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public float getLongitude() {
		return longitude;
	}



	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}



	public float getLatitude() {
		return latitude;
	}



	public void setLatitude(float latitude) {
		this.latitude = latitude;
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



	public UserDTO getUser() {
		return user;
	}



	public void setUser(UserDTO user) {
		this.user = user;
	}
	
}
