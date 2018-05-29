package osa.newsproject.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "posts")
public class Post  implements Serializable{

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "post_id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "title", unique = false, nullable = false)
	private String title;

	@Column(name = "description", unique = false, nullable = false)
	private String description;

	@Lob
	@Basic(fetch = LAZY)
	@Column(name = "photo", unique = false, nullable = true)
	private byte[] photo;

	@Temporal(TIMESTAMP)
	@Column(name = "date", unique = false, nullable = false)
	private Date date;

	@Column(name = "longitude", unique = false, nullable = false)
	private float longitude;

	@Column(name = "latitude", unique = false, nullable = false)
	private float latitude;

	@Column(name = "likes", unique = false, nullable = false)
	private int likes;

	@Column(name = "dislikes", unique = false, nullable = false)
	private int dislikes;
	
	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
	private User user;
	
	@ManyToMany(cascade = { CascadeType.PERSIST,CascadeType.MERGE }, fetch = LAZY)
	@JoinTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "tag_id"))
	private Set<Tag> tags = new HashSet<Tag>();

	@OneToMany(cascade = { CascadeType.ALL }, fetch = LAZY, mappedBy = "post")
	private Set<Comment> comments= new HashSet<Comment>();
	
	public Post() {}

	public Post(Integer id, String title, String description, byte[] photo, Date date, float longitude, float latitude,
			int likes, int dislikes, User user, Set<Tag> tags, Set<Comment> comments) {
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
		this.tags = tags;
		this.comments = comments;
	}
	
    public void add(Comment c){
        if(c.getPost()!= null)
            c.getPost().getComments().remove(c);
        c.setPost(this);
        getComments().add(c);
    }

    public void remove(Comment c){
        c.setPost(null);
        getComments().remove(c);
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", title=" + title + "]";
	}
	
}