package model;

import helper.serviceHelper.searchHelper.DesertFileLinkMap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import utils.DateTimeUtil;
import utils.RootPathHelper;
import model.factory.AttributesFactory;
import model.modelType.PostType;

@Entity
@Access(AccessType.FIELD)
@NamedQueries(value = {
		@NamedQuery(name = "Post.fetchPostsByCommunity", query = "SELECT p FROM Community c JOIN c.posts p WHERE c.ID = ?1 AND p.postType = model.modelType.PostType.NORMAL ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchActivitiesByCommunity", query = "SELECT p FROM Community c JOIN c.posts p WHERE c.ID = ?1 AND p.postType = model.modelType.PostType.ACTIVITY ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchAllActivities", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.postType = model.modelType.PostType.ACTIVITY ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchHeatActivities", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.postType = model.modelType.PostType.ACTIVITY ORDER BY SIZE(p.participants) DESC"),
		@NamedQuery(name = "Post.fetchActivitiesByOwner", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.postType = model.modelType.PostType.ACTIVITY AND p.owner.ID = ?1 ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchJoinedActivities", query = "SELECT p FROM Post p JOIN p.participants m WHERE p.available = 1 AND p.postType = model.modelType.PostType.ACTIVITY AND (SELECT me FROM Member me WHERE me.ID = ?1) IN m ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchActivitiesByTag", query = "SELECT p FROM Post p JOIN p.activityTypeTags t WHERE p.available = 1 AND p.postType = model.modelType.PostType.ACTIVITY AND (SELECT tag FROM Tag tag WHERE tag.ID = ?1) IN t ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchByFolloweeOrOwner", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.owner.ID = ?1 OR p.owner IN(SELECT f FROM Member m JOIN m.followees f WHERE m.ID = ?1) ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchByFollowee", query = "SELECT p FROM Post p "
				+ "WHERE p.available = 1 AND p.owner IN(SELECT f FROM Member m JOIN m.followees f WHERE "
				+ "m.ID = ?1 ) ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchByJoinedCommunity", query = "SELECT p FROM Community c JOIN c.posts p WHERE c IN "
				+ "(SELECT cs FROM Member m JOIN m.joinedCommunities cs WHERE m.ID = ?1) ORDER BY p.ID"),
		@NamedQuery(name = "Post.fetchByOwner", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.owner.ID = ?1 ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchByOwnerSize", query = "SELECT COUNT(p) FROM Post p WHERE p.available = 1 AND p.owner.ID = ?1"),
		@NamedQuery(name = "Post.fetchByTypeASC", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.postType = ?1 ORDER BY p.ID"),
		@NamedQuery(name = "Post.fetchByTypeDESC", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.postType = ?1 ORDER BY p.ID DESC"),
		@NamedQuery(name = "Post.fetchByID", query = "SELECT p FROM Post p WHERE p.available = 1 AND p.ID = ?1"),
		@NamedQuery(name = "Post.fetchHeat", query = "SELECT p FROM Post p WHERE p.available = 1 ORDER BY SIZE(p.comments) DESC, p.ID DESC"),
		@NamedQuery(name = "Post.fetchParticipants", query = "SELECT pa FROM Post p JOIN p.participants pa WHERE p .available = 1 AND p.ID = ?1"),
		@NamedQuery(name = "Post.fetchUnavailableIDs", query = "SELECT p.ID FROM Post p WHERE p.available = 0"),
		@NamedQuery(name = "Post.deleteUnavailable", query = "DELETE FROM Post p WHERE p.available = 0") })
public class Post extends AttributeModel {
	@Id
	private Long ID;
	@Enumerated(EnumType.STRING)
	private PostType postType;
	private String publishDate;
	@Lob
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> imageLinks;
	@Lob
	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, String> attributes;

	@ManyToOne
	private Member owner;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "POST_LIKER", joinColumns = @JoinColumn(name = "POST_ID"), inverseJoinColumns = @JoinColumn(name = "MEMBER_ID", unique = false, nullable = false))
	private Set<Member> likers;
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "collectedPosts")
	private Set<Member> collectors;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "POST_PARTICIPANT", joinColumns = @JoinColumn(name = "POST_ID"), inverseJoinColumns = @JoinColumn(name = "MEMBER_ID", unique = false, nullable = false))
	private Set<Member> participants;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private Set<Comment> comments;
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private Set<Tag> activityTypeTags;

	public Post() {
	}

	@SuppressWarnings("unchecked")
	public void init(Object... initParams) {
		this.ID = System.currentTimeMillis();
		this.available = true;
		this.postType = (PostType) initParams[0];
		this.publishDate = DateTimeUtil.getCurrnetDateTime();
		this.attributes = AttributesFactory.getInstance().create(Post.class,
				postType, initParams[1]);
		imageLinks = new LinkedHashSet<String>();
		imageLinks.addAll((Collection<String>) initParams[2]);
		likers = new LinkedHashSet<Member>();
		collectors = new LinkedHashSet<Member>();
		participants = new LinkedHashSet<Member>();
		comments = new LinkedHashSet<Comment>();
		activityTypeTags = new LinkedHashSet<Tag>();
	}

	public Long getID() {
		return ID;
	}

	public boolean isAvailable() {
		return available;
	}

	public void delete() {
		this.available = false;
	}

	public void addImageLink(String imageLink) {
		this.imageLinks.add(imageLink);
	}

	public void addImageLinks(Set<String> links) {
		this.imageLinks.addAll(links);
	}

	public void removeImageLink(String imageLink) {
		try {
			DesertFileLinkMap.deserialize();
			DesertFileLinkMap.addLink(imageLink);
			DesertFileLinkMap.serialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> temp = new LinkedHashSet<String>();
		for (String postImageLink : this.imageLinks) {
			if (!postImageLink.contains(imageLink))
				temp.add(postImageLink);
		}
		this.imageLinks = temp;
	}

	public void removeImageLinks(Set<String> imageLinks) {
		try {
			DesertFileLinkMap.deserialize();
			DesertFileLinkMap.addLinks(imageLinks);
			DesertFileLinkMap.serialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Set<String> temp = new LinkedHashSet<String>();
		for (String postImageLink : this.imageLinks) {
			for (String imageLink : imageLinks) {
				if (!postImageLink.contains(imageLink))
					temp.add(postImageLink);
			}
		}
		this.imageLinks = temp;
	}

	public Set<String> getImageLinks() {
		return imageLinks;
	}

	public void clearImageLinks() {
		try {
			DesertFileLinkMap.deserialize();
			DesertFileLinkMap.addLinks(this.imageLinks);
			DesertFileLinkMap.serialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.imageLinks.clear();
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public String getAttribute(String name) {
		return this.attributes.get(name);
	}

	public void setAttribute(String name, String value) {
		this.attributes.put(name, value);
	}

	public void removeAttribute(String name) {
		this.attributes.remove(name);
	}

	public void clearAttributes() {
		try {
			DesertFileLinkMap.deserialize();
			DesertFileLinkMap
					.addLink(this.getAttribute("registerTemplateAddr"));
			DesertFileLinkMap.addLink(RootPathHelper.getRootPath()
					+ "activityRegisterForm/" + this.ID);
			DesertFileLinkMap.serialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.attributes.clear();
	}

	@Override
	public void updateAttributes(Map<String, String> attributes) {
		// TODO Auto-generated method stub
		this.attributes.putAll(attributes);
	}

	public PostType getPostType() {
		return postType;
	}

	public void setPostType(PostType postType) {
		this.postType = postType;
	}

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}

	public Member getOwner() {
		return owner;
	}

	public void setOwner(Member owner) {
		this.owner = owner;
	}

	public Set<Member> getLikers() {
		return likers;
	}

	public void addLiker(Member member) {
		this.likers.add(member);
	}

	public void removeLiker(Member member) {
		this.likers.remove(member);
	}

	public void clearLikers() {
		this.likers.clear();
	}

	public Set<Member> getCollectors() {
		return collectors;
	}

	public void addCollector(Member member) {
		this.collectors.add(member);
	}

	public void removeCollector(Member member) {
		this.collectors.remove(member);
	}

	public void clearCollectors() {
		this.collectors.clear();
	}

	public Set<Member> getParticipants() {
		return participants;
	}

	public void addParticipant(Member member) {
		this.participants.add(member);
	}

	public void removeParticipant(Member member) {
		this.participants.remove(member);
	}

	public void clearParticipants() {
		this.participants.clear();
	}

	public void addComment(Comment comment) {
		this.comments.add(comment);
	}

	public void removeComment(Comment comment) {
		this.comments.remove(comment);
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void clearComments() {
		this.comments.clear();
	}

	public void addActivityTypeTag(Tag tag) {
		if (this.activityTypeTags.size() >= 5)
			return;
		this.activityTypeTags.add(tag);
		tag.activityTypeUsedBy(this);
	}

	public void removeActivityTypeTag(Tag tag) {
		this.activityTypeTags.remove(tag);
		tag.activityTypeRemovedBy(this);
	}

	public void clearActivityTypeTags() {
		for (Tag tag : this.activityTypeTags)
			tag.activityTypeRemovedBy(this);
		this.activityTypeTags.clear();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Post) {
			Post other = (Post) o;
			if (other.getID() != null && this.ID != null) {
				return other.getID().equals(this.ID);
			} else if (other.getID() == null && this.ID == null) {
				return super.equals(other);
			} else {
				return false;
			}
		}

		return false;
	}

	@Override
	public int hashCode() {
		if (this.ID == null) {
			return super.hashCode();
		} else {
			return this.ID.hashCode();
		}
	}

	@Override
	public String toString() {
		return toRepresentation().toString();

	}

	@Override
	public Map<String, Object> toRepresentation() {
		// TODO Auto-generated method stub
		Map<String, Object> representation = new HashMap<String, Object>();
		representation.put("ID", this.ID);
		representation.put("available", this.available);
		representation.put("postType", this.postType);
		representation.put("publishDate", this.publishDate);
		representation.put("imageLinks", this.imageLinks);
		representation.put("attributes", this.attributes);

		if (this.owner != null) {
			Map<String, Object> owner = this.owner.toRepresentation();
			owner.remove("followeeIDs");
			owner.remove("followerIDs");
			representation.put("owner", owner);
		}

		List<String> likerIDs = new ArrayList<String>();
		for (Member liker : this.likers) {
			likerIDs.add(liker.getID());
		}
		List<String> collectorIDs = new ArrayList<String>();
		for (Member collector : this.collectors) {
			collectorIDs.add(collector.getID());
		}
		List<String> participantIDs = new ArrayList<String>();
		for (Member participant : this.participants) {
			participantIDs.add(participant.getID());
		}

		representation.put("likerIDs", likerIDs);
		representation.put("collectorIDs", collectorIDs);
		representation.put("participantIDs", participantIDs);

		return representation;
	}

}
