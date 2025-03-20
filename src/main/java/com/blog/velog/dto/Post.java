package com.blog.velog.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Post {
	@NotBlank(message = "title는 빈칸이 될 수 없습니다.")
	@NotNull(message="title : null")
	@Size(max=255, message="title은 255자를 넘을 수 없습니다.")
	private String title;
	
	@NotBlank(message = "content는 빈칸이 될 수 없습니다.")
	@NotNull(message="content : null")
	private String content;
	
	@NotBlank(message = "username는 빈칸이 될 수 없습니다.")
	@NotNull(message="username : null")
	@Size(max=50, message="username은 50자를 넘을 수 없습니다.")
	private String username;
	
	@NotBlank(message = "tags는 빈칸이 될 수 없습니다.")
	@NotNull(message="tags : null")
	@Size(max=255, message="tags은 255자를 넘을 수 없습니다.")
	private String tags;
	
	
	private String imageUrl;
	private String profileImage;
	private long id; //sql에서 bigInt타입으로 선언돼서 long타입
	private int likes;
	private Date createdAt;
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
	public String getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
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
				+ ", imageUrl=" + imageUrl + ", profileImage=" + profileImage + ", id=" + id + ", likes=" + likes
				+ ", createdAt=" + createdAt + "]";
	}
	public Post(String title, String content, String username, String tags, String imageUrl, String profileImage,
			long id, int likes, Date createdAt) {
		super();
		this.title = title;
		this.content = content;
		this.username = username;
		this.tags = tags;
		this.imageUrl = imageUrl;
		this.profileImage = profileImage;
		this.id = id;
		this.likes = likes;
		this.createdAt = createdAt;
	}
	public Post() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}
