package system;

import helper.persistenceHelper.EntityManagerFactoryUtil;
import helper.serviceHelper.NumberManager;
import helper.serviceHelper.OnlineUserIDArray;
import helper.serviceHelper.searchHelper.ActivitySearchMap;
import helper.serviceHelper.searchHelper.AdmirationMap;
import helper.serviceHelper.searchHelper.CommunitySearchMap;
import helper.serviceHelper.searchHelper.DesertFileLinkMap;
import helper.serviceHelper.searchHelper.MemberSearchMap;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import transaction.Transaction;
import transaction.DAOCreateTransaction.RegisterGodTransaction;
import transaction.DAOFetchTransaction.GetNumTransaction;
import utils.ExecutorUtil;
import utils.Logger;
import utils.MD5;
import utils.RootPathHelper;

/**
 * Application Lifecycle Listener implementation class Boostrap
 * 
 */
// @WebListener
public class Initialtor implements ServletContextListener {
	private static final int ACTIVITYREMINDTIME = 5 * 60;
	private static final int DELETEFILETIME = 60 * 60;

	// private static final int DELETEMODELTIME = 60 * 60;

	/**
	 * Default constructor.
	 */
	public Initialtor() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		ThreadPoolExecutor executor = (ThreadPoolExecutor) servletContextEvent
				.getServletContext().getAttribute("executor");
		executor.shutdown();

		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) servletContextEvent
				.getServletContext()
				.getAttribute("scheduledThreadPoolExecutor");
		scheduledThreadPoolExecutor.shutdownNow();

		EntityManagerFactoryUtil.getInstance().closeEntityManagerFactory();

		ExecutorUtil.destory();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// TODO Auto-generated method stub
		Executor executor = Executors.newCachedThreadPool();
		servletContextEvent.getServletContext().setAttribute("executor",
				executor);

		ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors
				.newScheduledThreadPool(5);
		scheduledThreadPoolExecutor.scheduleAtFixedRate(new SmsRemindTask(),
				ACTIVITYREMINDTIME, ACTIVITYREMINDTIME, TimeUnit.SECONDS);

		scheduledThreadPoolExecutor.scheduleAtFixedRate(new DeleteFileTask(),
				DELETEFILETIME, DELETEFILETIME, TimeUnit.SECONDS);

		/*
		 * scheduledThreadPoolExecutor.scheduleAtFixedRate( new
		 * DeleteUnavailableTask(), DELETEMODELTIME, DELETEMODELTIME,
		 * TimeUnit.SECONDS);
		 */

		servletContextEvent.getServletContext().setAttribute(
				"scheduledThreadPoolExecutor", scheduledThreadPoolExecutor);

		Logger.initialize();

		Transaction transaction;
		try {
			transaction = new RegisterGodTransaction();
			transaction.execute("Admin", MD5.toMD5Code("123456"));
			//for the first time update
			//transaction  = new ModelUpdateInitiationTransaction();
			//transaction.execute();

			MemberSearchMap.initializeEnvironment();
			ActivitySearchMap.initializeEnvironment();
			CommunitySearchMap.initializeEnvironment();
			DesertFileLinkMap.initializeEnvironment();
			OnlineUserIDArray.initializeEnvironment();
			AdmirationMap.initializeEnvironment();
			RootPathHelper.setRootPath(servletContextEvent.getServletContext()
					.getRealPath("/"));

			transaction = new GetNumTransaction();
			long mnum = (long) transaction.execute("Member.size");
			long cnum = (long) transaction.execute("Community.size");
			NumberManager.initiate(mnum, cnum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
