package integratedTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.modelType.CommunityType;
import model.modelType.UserType;
import model.structure.Image;
import transaction.Transaction;
import transaction.DAOCreateTransaction.CreateCommunityTransaction;
import transaction.DAOCreateTransaction.RegisterCommunityOwnerTransaction;
import transaction.DAOCreateTransaction.RegisterMemberTransaction;
import utils.DateTimeUtil;
import utils.JsonUtil;
import utils.MD5;

/**
 * Application Lifecycle Listener implementation class UserSystemTestListener
 * 
 */
@WebListener
public class SystemTestListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public SystemTestListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try {
			Files.deleteIfExists(Paths.get("membersearchmap.txt"));
			Files.deleteIfExists(Paths.get("activitysearchmap.txt"));
			Files.deleteIfExists(Paths.get("communitysearchmap.txt"));
			Files.deleteIfExists(Paths.get("admirationmap.txt"));
			Files.deleteIfExists(Paths.get("rankmap.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		Transaction transaction;
		try {
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("name", "fucker");
			attributes.put("gender", "男");
			attributes.put("avatarLink", JsonUtil.toJson(new Image(
					"images/default/default-user-avartar.png")));
			attributes.put("profileImageLink", JsonUtil.toJson(new Image(
					"images/default/default-profile-background.jpg")));
			attributes.put("relationship", "");
			attributes.put("introduct", "");
			attributes.put("introduce", "");
			attributes.put("institution", "");
			attributes.put("major", "");
			attributes.put("telnum", "");
			attributes.put("email", "kingkong_webber@hotmail.com");
			attributes.put("birthday", "");
			attributes.put("campus", "");
			attributes.put("dorm", "");
			attributes.put("wechat", "");
			attributes.put("regDate", DateTimeUtil.getCurrnetDateTime());

			transaction = new RegisterMemberTransaction();
			for (int i = 2000; i > 0; i--) {
				transaction.execute("201105" + i, MD5.toMD5Code("123456"),
						attributes);
			}
			transaction.execute("2011052444", MD5.toMD5Code("123456"),
					attributes);
			transaction.execute("2011052333", MD5.toMD5Code("123456"),
					attributes);
			transaction.execute("2011052222", MD5.toMD5Code("123456"),
					attributes);
			transaction.execute("2011052666", MD5.toMD5Code("123456"),
					attributes);
			transaction.execute("2011052555", MD5.toMD5Code("123456"),
					attributes);
			transaction.execute("2011052111", MD5.toMD5Code("123456"),
					attributes);
			transaction.execute("2011052000", MD5.toMD5Code("123456"),
					attributes);

			transaction = new RegisterCommunityOwnerTransaction();
			attributes.clear();
			attributes.put("avatarLink", JsonUtil.toJson(new Image(
					"images/default/default-user-avartar.png")));
			attributes.put("profileImageLink", JsonUtil.toJson(new Image(
					"images/default/default-profile-background.jpg")));
			attributes.put("gender", "");
			attributes.put("name", "社区用户");
			attributes.put("relationship", "");
			attributes.put("introduct", "");
			attributes.put("introduce", "");
			attributes.put("telnum", "");
			attributes.put("email", "kingkong_webber@hotmail.com");
			attributes.put("institution", "");
			attributes.put("campus", "");
			attributes.put("wechat", "");
			attributes.put("Cinstitution", "");
			attributes.put("Ccontact", "");
			attributes.put("regDate", DateTimeUtil.getCurrnetDateTime());

			transaction.execute("13750046645", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750000059", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750046461", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750044737", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13631263784", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750046644", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750046643", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750046642", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750046641", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);
			transaction.execute("13750046640", MD5.toMD5Code("123456"),
					new HashMap<String, Object>(), UserType.COMMUNITYOWNER);

			transaction = new CreateCommunityTransaction();
			attributes = new HashMap<String, String>();
			attributes.put("name", "測試");
			attributes.put("introduce", "test");
			attributes.put("foundDate", DateTimeUtil.getCurrnetDateTime());
			attributes.put("communityCard", JsonUtil.toJson(new Image(
					"images/default/default-community-card.png")));
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.ENTERTAINMENT);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.ACADEMIC);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.ATHLETIC);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.OTHERS);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.STUDENTUNION);
			attributes.put("name", "test");
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.STUDENTUNION);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.STUDENTUNION);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.ACADEMIC);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.ATHLETIC);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.ENTERTAINMENT);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.OTHERS);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.OTHERS);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.OTHERS);
			transaction.execute("13750046645", attributes,
					new LinkedList<String>(), CommunityType.OTHERS);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
