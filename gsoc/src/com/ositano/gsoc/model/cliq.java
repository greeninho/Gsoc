package com.ositano.gsoc.model;

import java.util.ArrayList;

public class cliq {
	private String title, profilePic, url;
	private String vision;
	private String story;
	private String timestamp;

	public cliq() {
	}

	public cliq(String name, String thumbnailUrl, String vision, String story,
			String timestamp, String url) {
		this.title = name;
		this.profilePic = thumbnailUrl;
		this.vision = vision;
		this.story = story;
		this.timestamp = timestamp;
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getThumbnailUrl() {
		return profilePic;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.profilePic = thumbnailUrl;
	}

	public String getVision() {
		return vision;
	}

	public void setVision(String vision) {
		this.vision = vision;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}

}
