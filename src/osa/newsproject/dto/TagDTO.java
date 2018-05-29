package osa.newsproject.dto;

import java.io.Serializable;

import osa.newsproject.entity.Tag;

public class TagDTO implements Serializable{

	private Integer id;

	private String name;

	public TagDTO() {
	}

	public TagDTO(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public TagDTO(Tag tag) {
		this(tag.getId(),tag.getName());
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

}
