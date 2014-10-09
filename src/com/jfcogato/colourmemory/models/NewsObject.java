package com.jfcogato.colourmemory.models;

import java.io.Serializable;

public class NewsObject implements Serializable {

	private static final long serialVersionUID = -7060210544600464481L;

	//The values are allway init, to store data on databases that now only takes not nulls values
	private String title = "";
	private String description = "";
	private String link = "";
	private String content = "";
	private String picture = "";

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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

}
