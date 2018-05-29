package osa.newsproject.dto;

import java.io.Serializable;

import osa.newsproject.entity.User;

public class UserDTO implements Serializable{

    private Integer id;
    private String name;
    private String username;
    private String password;
    private byte[] photo;

    public UserDTO() {
    }

    public UserDTO(Integer id, String name, String username, String password, byte[] photo) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.photo = photo;
    }
    public UserDTO(User user) {
        this(user.getId(),user.getName(),user.getUsername(),user.getPassword(),user.getPhoto());
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
    
    
}
