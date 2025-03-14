package com.blog.velog.dto;

import java.util.Date;

public class Post {
	private String title,content,tags,imageUrl;
	private long authorId,id; //sql에서 bigInt타입으로 선언돼서 long타입
	private int likes;
	private Date createdAt;
	public Post() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Post(String title, String content, String tags, String imageUrl, long authorId, long id, int likes,
			Date createdAt) {
		super();
		this.title = title;
		this.content = content;
		this.tags = tags;
		this.imageUrl = imageUrl;
		this.authorId = authorId;
		this.id = id;
		this.likes = likes;
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
		return "Post [title=" + title + ", content=" + content + ", tags=" + tags + ", imageUrl=" + imageUrl
				+ ", authorId=" + authorId + ", id=" + id + ", likes=" + likes + ", createdAt=" + createdAt + "]";
	}
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
	public long getAuthorId() {
		return authorId;
	}
	public void setAuthorId(long authorId) {
		this.authorId = authorId;
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
	
	
	
}
