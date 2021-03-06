package service;

import helper.serviceHelper.extensionManager.ExtensionManager;
import helper.serviceHelper.extensionManager.RegisterExtensionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * Servlet implementation class RegisterFormUploadService
 */
@WebServlet("/app/registerFormUpload")
public class RegisterFormUploadService extends HttpServlet {
	private static final long MAXIMUMFILESIZE = 1024 * 500;
	private static String root;
	private DiskFileItemFactory factory;
	private ServletFileUpload upload;
	private static final long serialVersionUID = 1L;
	private ExtensionManager em;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterFormUploadService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		setUp();
	}

	private void setUp() {
		factory = new DiskFileItemFactory();
		File repository = (File) this.getServletContext().getAttribute(
				"javax.servlet.context.tempdir");
		factory.setRepository(repository);
		upload = new ServletFileUpload(factory);
		upload.setSizeMax(MAXIMUMFILESIZE);
		root = getServletConfig().getServletContext().getRealPath("/");
		File file = new File(root + "activityRegisterForm");
		if (!file.exists())
			file.mkdir();
		File dir = new File(root + "activityRegisterTemplate");
		if (!dir.exists())
			dir.mkdir();
		em = new RegisterExtensionManager();
		em.init();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String[] parameters = request.getQueryString().split("&");
		String type = "";
		String activityID = "";
		String name = "";
		for (String parameter : parameters) {
			String[] kvp = parameter.split("=");
			if (kvp[0].equals("uploadType"))
				type = kvp[1];
			else if (kvp[0].equals("activityID"))
				activityID = kvp[1];
			else if (kvp[0].equals("memberName"))
				name = kvp[1];
		}

		List<FileItem> items = new ArrayList<FileItem>();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FileItem it = null;
		for (FileItem item : items) {
			if (item.isFormField())
				continue;
			else
				it = item;
		}

		switch (type) {
		case "REGISTERTEMPLATE":
			try {
				uploadTemplate(it, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.sendError(400);
				response.flushBuffer();
			}
			break;
		case "REGISTERFORM":
			try {
				uploadForm(it, activityID, name, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				response.sendError(400);
				response.flushBuffer();
			}
			break;
		}
	}

	private void uploadTemplate(FileItem item, HttpServletResponse response)
			throws Exception {

		String extention = "";
		if (em.containsMime(item.getContentType()))
			extention = em.getExtension(item.getContentType());
		else if (em.containsExtension(item.getName().substring(
				item.getName().indexOf("."))))
			extention = item.getName().substring(item.getName().indexOf("."));
		else
			throw new Exception("Do Not Support File Type Exception");

		String temp = "activityRegisterTemplate/" + "registerTemplate"
				+ System.currentTimeMillis() + extention;
		File uploaddedFile = new File(root + temp);
		item.write(uploaddedFile);

		response.setContentType("text/plain");
		response.setStatus(200);
		response.getWriter().write(root + temp);
		response.flushBuffer();
	}

	private void uploadForm(FileItem item, String activityID, String name,
			HttpServletResponse response) throws Exception {

		String extention = "";
		if (em.containsMime(item.getContentType()))
			extention = em.getExtension(item.getContentType());
		else if (em.containsExtension(item.getName().substring(
				item.getName().indexOf("."))))
			extention = item.getName().substring(item.getName().indexOf("."));
		else
			throw new Exception("Do Not Support File Type Exception");

		File dir = new File(root + "activityRegisterForm/" + activityID);
		if (!dir.exists()) {
			dir.mkdir();
		}

		String temp = "activityRegisterForm/" + activityID + "/"
				+ System.currentTimeMillis() + "--"
				+ java.net.URLDecoder.decode(name, "UTF-8") + extention;
		File uploaddedFile = new File(root + temp);
		item.write(uploaddedFile);

		response.setContentType("text/plain");
		response.setStatus(200);
		response.getWriter().write(root + temp);
		response.flushBuffer();
	}

}
