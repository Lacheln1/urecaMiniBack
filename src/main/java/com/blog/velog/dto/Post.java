package com.blog.velog.dto;

import java.util.Date;

public class Post {
	private String title,content,username,tags,imageUrl;
	private long id; //sql에서 bigInt타입으로 선언돼서 long타입
	private int likes;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "Post [title=" + title + ", content=" + content + ", username=" + username + ", tags=" + tags
				+ ", imageUrl=" + imageUrl + ", id=" + id + ", likes=" + likes + ", createdAt=" + createdAt + "]";
	}
	public Post(String title, String content, String username, String tags, String imageUrl, long id, int likes,
			Date createdAt) {
		super();
		this.title = title;
		this.content = content;
		this.username = username;
		this.tags = tags;
		this.imageUrl = imageUrl;
		this.id = id;
		this.likes = likes;
		this.createdAt = createdAt;
	}
	private Date createdAt;
	public Post() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
