package osa.newsproject.entity;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User implements Serializable{
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "user_id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "name", unique = false, nullable = false)
	private String name;

	@Lob
	@Basic(fetch = LAZY)
	@Column(name = "photo" , unique = false, nullable = true)
	private byte[] photo;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "pasword", unique = false, nullable = false)
	private String password;

	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "user")
	private Set<Post> posts=new HashSet<Post>();

	@OneToMany(cascade = { ALL }, fetch = LAZY, mappedBy = "user")
	private Set<Comment> comments=new HashSet<Comment>();

	public User() {
	}

	public User(Integer id, String name, byte[] photo, String username, String password, Set<Post> posts,
			Set<Comment> comments) {
		super();
		this.id = id;
		this.name = name;
		this.photo = photo;
		this.username = username;
		this.password = password;
		this.posts = posts;
		this.comments = comments;
	}

    public void add(Post p){
        if(p.getUser()!=null)
            p.getUser().getPosts().remove(p);
        p.setUser(this);
        getPosts().add(p);
    }

    public  void remove(Post p){
        p.setUser(null);
        getPosts().remove(p);
    }

    public void add(Comment c){
        if(c.getUser() != null)
            c.getUser().getComments().remove(c);
        c.setUser(this);
        getComments().add(c);
    }

    public void remove(Comment c){
        c.setUser(null);
        getComments().remove(c);
    }
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

	
}
