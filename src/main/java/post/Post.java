package post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import comment.Comment;
import like.Like;
import like.Likeable;
import user.User;

public class Post implements Likeable, Comparable<Post>{
	private int id;
	private String text;
	private String pictureUrl;
	private LocalDateTime time;
	private int userId;
	private List<Comment> comments;
	private Set<Like> likes;
	
	public Post(int id, int userId) {
		this.id = id;
		this.text = "";
		this.pictureUrl = "";
		this.time = LocalDateTime.now();
		this.userId = userId;
		this.comments = new ArrayList<>();
		this.likes = new HashSet<>();
	}
	
	public int getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		if(text != null & text.length() > 0) {
			this.text = text;
		}		
	}
	
	public String getPictureUrl() {
		return pictureUrl;
	}
	
	public void setPictureUrl(String pictureUrl) {
		if(pictureUrl != null & pictureUrl.length() > 0) {
			this.pictureUrl = pictureUrl;
		}		
	}
	
	public LocalDateTime getTime() {
		return time;
	}
	
	public void setTime(LocalDateTime time) {
		if(time != null) {
			this.time = time;
		}		
	}

	public int getUserId() {
		return userId;
	}
	
	public List<Comment> getComments() {
		return Collections.unmodifiableList(comments);
	}
	
	public void addComment(Comment comment) {
		if(comment != null) {
			this.comments.add(comment);
		}
	}
	
	public void removeComment(Comment comment) {
		if(comment != null) {
			for(Comment c: comments) {
				if(c.equals(comment)) {
					this.comments.remove(c);
					break;
				}
			}			
		}
	}
	
	public Set<Like> getLikes() {
		return Collections.unmodifiableSet(likes);
	}
	
	public void addLike(Like like) {
		if(like != null) {
			this.likes.add(like);
		}
	}
	
	public void removeLike(Like like) {
		if(like != null) {
			for(Iterator<Like> it = likes.iterator(); it.hasNext();) {
				if(it.equals(like)) {
					this.likes.remove(like);
					break;
				}
			}			
		}
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", text=" + text + ", pictureUrl=" + pictureUrl + ", time=" + time + ", userId="
				+ userId + ", comments=" + comments + ", likes=" + likes + "]";
	}

	@Override
	public int compareTo(Post otherPost) {
		return otherPost.getTime().compareTo(this.getTime());
	}
	
}
