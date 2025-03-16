package com.blog.velog.dto;

public class Salt {
	 private Long id;
	    private String email;
	    private String salt;

	    public Salt() {}

	    public Salt(String email, String salt) {
	        this.email = email;
	        this.salt = salt;
	    }

	    public Long getId() { return id; }
	    public void setId(Long id) { this.id = id; }

	    public String getEmail() { return email; }
	    public void setEmail(String email) { this.email = email; }

	    public String getSalt() { return salt; }
	    public void setSalt(String salt) { this.salt = salt; }
}
