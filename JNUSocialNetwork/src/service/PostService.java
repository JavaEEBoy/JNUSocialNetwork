package service;

import helper.securityHelper.ContentEncoder;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import model.ServerSentEvent;
import model.modelType.PostType;
import system.ServerSentEventBroadcaster;
import transaction.Transaction;
import transaction.DAOFetchTransaction.FetchInterestedPostsTransaction;
import transaction.DAOFetchTransaction.FetchMembersTransaction;
import transaction.DAOFetchTransaction.FetchPostTransaction;
import transaction.DAOFetchTransaction.FetchPostsByIDsTransaction;
import transaction.DAOFetchTransaction.FetchPostsTransaction;
import transaction.DAOUpdateTransaction.AddPostImagesTransaction;
import transaction.DAOUpdateTransaction.RemovePostImageLinksTransaction;
import transaction.DAOUpdateTransaction.UpdatePostAttributeTransaction;
import transaction.SSETransaction.SSECancelCollectPostTransaction;
import transaction.SSETransaction.SSECancelLikePostTransaction;
import transaction.SSETransaction.SSECollectPostTransaction;
import transaction.SSETransaction.SSECreatePostInCommunityTransaction;
import transaction.SSETransaction.SSECreatePostTransaction;
import transaction.SSETransaction.SSEDeletePostFromCommunityTransaction;
import transaction.SSETransaction.SSEDeletePostTransaction;
import transaction.SSETransaction.SSEJoinActivityTransaction;
import transaction.SSETransaction.SSELeaveActivityTransaction;
import transaction.SSETransaction.SSELikePostTransaction;

@Path("/post")
public class PostService {
	private Transaction transaction;
	private ServerSentEvent sse;
	private ServerSentEventBroadcaster broadcaster = ServerSentEventBroadcaster
			.getInstance();
	private ContentEncoder contentEncoder = new ContentEncoder();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("add/{ID : \\d+}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPost(@PathParam("ID") String ID, Map post)
			throws Exception {
		transaction = new SSECreatePostTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, PostType.NORMAL,
					contentEncoder.encodeMapContent((Map<String, String>) post
							.get("attributes")), post.get("imageLinks"),
					new ArrayList<String>());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Path("addToCommunity/{ID : \\d+}/{communityID : \\d+}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addPostToCommuniy(@PathParam("ID") String ID,
			@PathParam("communityID") Long communityID,
			@PathParam("userType") String userType, Map post) throws Exception {
		transaction = new SSECreatePostInCommunityTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, communityID,
					PostType.valueOf((String) post.get("postType")),
					contentEncoder.encodeMapContent((Map<String, String>) post
							.get("attributes")), post.get("imageLinks"), post
							.get("activityTypeTags"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@Path("delete/{postID : \\d+}")
	@PUT
	public Response deletePost(@PathParam("postID") Long postID)
			throws Exception {
		transaction = new SSEDeletePostTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@Path("delete/{communityID : \\d+}/{postID : \\d+}")
	@PUT
	public Response deletePostFromCommunity(@PathParam("postID") Long postID,
			@PathParam("communityID") Long communityID) throws Exception {
		transaction = new SSEDeletePostFromCommunityTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(communityID, postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetch/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetch(@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> results;
		try {
			results = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchHeat", null, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(results) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchByCommunity/{communityID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchPostsByCommunity(
			@PathParam("communityID") Long communityID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> results;
		try {
			results = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchPostsByCommunity", communityID, startIndex,
					pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(results) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchByOwner/{ID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response fetchPostsByOwner(@PathParam("ID") String ID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> results;
		try {
			results = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchByOwner", ID, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(results) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchByFollowee/{ID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response fetchPostsByFollowee(@PathParam("ID") String ID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> results;
		try {
			results = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchByFollowee", ID, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(results) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchByFolloweeOrOwner/{ID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response fetchPostsByFolloweeOrOwner(@PathParam("ID") String ID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> results;
		try {
			results = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchByFolloweeOrOwner", ID, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(results) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchActivities/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	public Response fetchActivities(@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> activities;
		try {
			activities = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchByTypeDESC", PostType.ACTIVITY, startIndex,
					pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(activities) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchActivitiesByCommunity/{communityID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	public Response fetchActivitiesByCommunity(
			@PathParam("communityID") Long communityID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> activities;
		try {
			activities = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchActivitiesByCommunity", communityID, startIndex,
					pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(activities) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchAllActivities/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	public Response fetchAllActivities(@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> activities;
		try {
			activities = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchAllActivities", null, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(activities) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchHeatActivities/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	public Response fetchHeatActivities(
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> activities;
		try {
			activities = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchHeatActivities", null, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(activities) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchJoinedActivities/{ID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	public Response fetchJoinedActivities(@PathParam("ID") String ID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> activities;
		try {
			activities = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchJoinedActivities", ID, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(activities) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchActivitiesByOwner/{ID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	public Response fetchActivitiesByOwner(@PathParam("ID") String ID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> activities;
		try {
			activities = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchActivitiesByOwner", ID, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(activities) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchActivitiesByType/{tagID}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	public Response fetchActivitiesByType(@PathParam("tagID") String tagID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchPostsTransaction();
		List<Map<String, Object>> activities;
		try {
			activities = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchActivitiesByTag", tagID, startIndex, pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(activities) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchByID/{postID : \\d+}")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response fetchPostByID(@PathParam("postID") Long postID)
			throws Exception {
		transaction = new FetchPostTransaction();
		Map<String, Object> results;
		try {
			results = (Map<String, Object>) transaction.execute(postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return Response.ok(new GenericEntity<Map<String, Object>>(results) {
		}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchByPostIDs")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchPostsByIDs(@QueryParam("postIDs") List<Long> postIDs)
			throws Exception {
		transaction = new FetchPostsByIDsTransaction();
		List<Map<String, Object>> results;
		try {
			results = (List<Map<String, Object>>) transaction.execute(postIDs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(results) {
				}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchInterested/{ID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchInterestedPosts(@PathParam("ID") String ID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchInterestedPostsTransaction();
		List<Map<String, Object>> posts;
		try {
			posts = (List<Map<String, Object>>) transaction.execute(ID, startIndex,
					pageSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(new GenericEntity<List<Map<String, Object>>>(posts) {
		}).build();
	}

	@Path("like/{ID : \\d+}/{postID : \\d+}")
	@PUT
	public Response likePost(@PathParam("ID") String ID,
			@PathParam("postID") Long postID) throws Exception {
		transaction = new SSELikePostTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@Path("cancelLike/{ID : \\d+}/{postID : \\d+}")
	@PUT
	public Response cancelLike(@PathParam("ID") String ID,
			@PathParam("postID") Long postID) throws Exception {
		transaction = new SSECancelLikePostTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@Path("collect/{ID : \\d+}/{postID : \\d+}")
	@PUT
	public Response collectPost(@PathParam("ID") String ID,
			@PathParam("postID") Long postID) throws Exception {
		transaction = new SSECollectPostTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@Path("cancelCollect/{ID : \\d+}/{postID : \\d+}")
	@PUT
	public Response cancelCollect(@PathParam("ID") String ID,
			@PathParam("postID") Long postID) throws Exception {
		transaction = new SSECancelCollectPostTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@Path("joinActivity/{ID : \\d+}/{postID : \\d+}")
	@PUT
	public Response joinActivity(@PathParam("ID") String ID,
			@PathParam("postID") Long postID) throws Exception {
		transaction = new SSEJoinActivityTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@Path("leaveActivity/{ID : \\d+}/{postID : \\d+}")
	@PUT
	public Response leaveActivity(@PathParam("ID") String ID,
			@PathParam("postID") Long postID) throws Exception {
		transaction = new SSELeaveActivityTransaction();
		try {
			sse = (ServerSentEvent) transaction.execute(ID, postID);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		broadcaster.broadcast(sse);

		return Response.ok().build();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Path("updateAttributes/{postID : \\d+}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateAttributes(@PathParam("postID") Long postID,
			Map attributes) throws Exception {
		transaction = new UpdatePostAttributeTransaction();
		Map<String, Object> result;
		try {
			result = (Map<String, Object>) transaction.execute(postID,
					attributes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok(new GenericEntity<Map<String, Object>>(result) {
		}).build();
	}

	@SuppressWarnings("unchecked")
	@Path("addImages/{postID : \\d+}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response addImages(@PathParam("postID") Long postID,
			@QueryParam("imageLinks") List<String> imageLinks) throws Exception {
		transaction = new AddPostImagesTransaction();
		Set<String> links = new LinkedHashSet<String>(imageLinks);
		Map<String, Object> result;
		try {
			result = (Map<String, Object>) transaction.execute(postID, links);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		return Response.ok(new GenericEntity<Map<String, Object>>(result) {
		}).build();
	}

	@Path("removeImages/{postID : \\d+}")
	@PUT
	public Response removeImages(@PathParam("postID") Long postID,
			@QueryParam("imageLinks") List<String> imageLinks) throws Exception {
		transaction = new RemovePostImageLinksTransaction();
		Set<String> links = new LinkedHashSet<String>(imageLinks);
		try {
			transaction.execute(postID, links);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return Response.ok().build();
	}

	@SuppressWarnings("unchecked")
	@Path("fetchParticipants/{postID : \\d+}/{startIndex : \\d{1,}}/{pageSize : \\d{1,}}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response fetchParticipants(@PathParam("postID") Long postID,
			@PathParam("startIndex") int startIndex,
			@PathParam("pageSize") int pageSize) throws Exception {
		transaction = new FetchMembersTransaction();
		List<Map<String, Object>> members = new ArrayList<>();
		try {
			members = (List<Map<String, Object>>) transaction.execute(
					"Post.fetchParticipants", postID, null, startIndex,
					pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return Response.ok(
				new GenericEntity<List<Map<String, Object>>>(members) {
				}).build();
	}
}
