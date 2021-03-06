package service;

import helper.serviceHelper.extensionManager.ExtensionManager;
import helper.serviceHelper.extensionManager.MultiMediaExtensionManager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.structure.Image;
import model.structure.ImageTuple;
import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import utils.JsonUtil;

import com.google.gson.reflect.TypeToken;

@WebServlet("/app/fileUploader")
public class ImageUploadService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final long MAXIMUMFILESIZE = 1024 * 1024 * 10 * 3;
	private final static Type TYPE = new TypeToken<Map<String, Integer>>() {
	}.getType();
	private static String root;
	private DiskFileItemFactory factory;
	private ServletFileUpload upload;
	private ExtensionManager em;

	public ImageUploadService() {
		super();
	}

	public void init() {
		setUp();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				throw new FileUploadBase.InvalidContentTypeException();
			}

			List<String> links = process(request);
			response.setContentType("application/json");
			response.setStatus(200);
			System.out.println("about to write back:" + JsonUtil.toJson(links));
			response.getWriter().write(JsonUtil.toJson(links));
			response.flushBuffer();

		} catch (FileUploadBase.InvalidContentTypeException icte) {
			icte.printStackTrace();
			response.sendError(406);
		} catch (FileUploadBase.FileSizeLimitExceededException fsle) {
			fsle.printStackTrace();
			response.sendError(413);
		} catch (FileUploadException fue) {
			fue.printStackTrace();
			response.sendError(400);
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(500);
		}
	}

	private void setUp() {
		factory = new DiskFileItemFactory();
		File repository = (File) this.getServletContext().getAttribute(
				"javax.servlet.context.tempdir");
		factory.setRepository(repository);
		upload = new ServletFileUpload(factory);
		upload.setSizeMax(MAXIMUMFILESIZE);
		root = getServletConfig().getServletContext().getRealPath("/")
				+ "pages/";
		em = new MultiMediaExtensionManager();
		em.init();
	}

	private List<String> process(HttpServletRequest request)
			throws FileUploadException,
			FileUploadBase.FileSizeLimitExceededException, Exception {
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> iter = items.iterator();
		List<String> links = new ArrayList<String>();

		Map<String, Integer> cropData = null;
		while (iter.hasNext()) {
			FileItem item = iter.next();

			System.out.println(item.isFormField() + " " + item.getFieldName());

			if (item.isFormField() && item.getFieldName().equals("crop_data")) {
				cropData = JsonUtil.fromJson(item.getString(), TYPE);
				continue;
			} else if (item.isFormField())
				continue;
			else if (!item.isFormField()
					&& (item.getName() == null || item.getName().equals("")))
				continue;

			String extention = "";
			if (em.containsMime(item.getContentType()))
				extention = em.getExtension(item.getContentType());
			else if (em.containsExtension(item.getName().substring(
					item.getName().lastIndexOf(".")))) {
				extention = item.getName().substring(
						item.getName().indexOf("."));
			} else
				continue;

			File dir = new File(root + extention.substring(1));
			if (!dir.exists()) {
				dir.mkdir();
			}
			String imageName = extention.substring(1) + "/--"
					+ System.currentTimeMillis();
			ImageTuple imageTuple = new ImageTuple();

			String orignDir = imageName + "--original" + extention;
			File uploaddedFile = new File(root + orignDir);
			item.write(uploaddedFile);

			String thumbDir = imageName + "--thumbnail" + extention;
			File thumbnail = new File(root + thumbDir);

			double size = (double) ((uploaddedFile.length() * 1.0) / (1024 * 1024));
			if (size > 0 && size < 0.1)
				Thumbnails.of(uploaddedFile).scale(1f).toFile(thumbnail);
			else if (size >= 0.1 && size < 0.15)
				Thumbnails.of(uploaddedFile).scale(0.95f).toFile(thumbnail);
			else if (size >= 0.15 && size < 0.2)
				Thumbnails.of(uploaddedFile).scale(0.9f).toFile(thumbnail);
			else if (size >= 0.2 && size < 0.3)
				Thumbnails.of(uploaddedFile).scale(0.88f).toFile(thumbnail);
			else if (size >= 0.3 && size < 0.5)
				Thumbnails.of(uploaddedFile).scale(0.85f).toFile(thumbnail);
			else if (size >= 0.5 && size < 0.7)
				Thumbnails.of(uploaddedFile).scale(0.7f).toFile(thumbnail);
			else if (size >= 0.7 && size < 0.9)
				Thumbnails.of(uploaddedFile).scale(0.68f).toFile(thumbnail);
			else if (size >= 0.9 && size < 1)
				Thumbnails.of(uploaddedFile).scale(0.67f).toFile(thumbnail);
			else if (size >= 1 && size < 1.1)
				Thumbnails.of(uploaddedFile).scale(0.66f).toFile(thumbnail);
			else if (size >= 1.1 && size < 1.3)
				Thumbnails.of(uploaddedFile).scale(0.65f).toFile(thumbnail);
			else if (size > 1.3 && size <= 1.5)
				Thumbnails.of(uploaddedFile).scale(0.57f).toFile(thumbnail);
			else if (size > 1.5 && size <= 1.7)
				Thumbnails.of(uploaddedFile).scale(0.54f).toFile(thumbnail);
			else if (size > 1.7 && size <= 1.9)
				Thumbnails.of(uploaddedFile).scale(0.50f).toFile(thumbnail);
			else if (size > 2.1 && size <= 2.3)
				Thumbnails.of(uploaddedFile).scale(0.40f).toFile(thumbnail);
			else if (size > 2.3 && size <= 2.5)
				Thumbnails.of(uploaddedFile).scale(0.35f).toFile(thumbnail);
			else if (size > 2.5 && size <= 2.7)
				Thumbnails.of(uploaddedFile).scale(0.3f).toFile(thumbnail);
			else if (size > 2.7 && size <= 2.9)
				Thumbnails.of(uploaddedFile).scale(0.25f).toFile(thumbnail);
			else if (size >= 3.0)
				Thumbnails.of(uploaddedFile).scale(0.2f).toFile(thumbnail);

			BufferedImage bi = ImageIO.read(thumbnail);
			Image image = new Image(thumbDir, bi.getHeight(), bi.getWidth());
			imageTuple.setThumbnail(JsonUtil.toJson(image));

			bi = ImageIO.read(uploaddedFile);
			image = new Image(orignDir, bi.getHeight(), bi.getWidth());
			imageTuple.setOriginalImaage(JsonUtil.toJson(image));

			BufferedImage ci;
			Image croppedImage;

			if (cropData != null) {
				bi = bi.getSubimage(cropData.get("x"), cropData.get("y"),
						cropData.get("width"), cropData.get("height"));

				String croppedImageDir = imageName + "--cropped" + extention;
				File croppedFile = new File(root + croppedImageDir);
				ImageIO.write(bi, extention.substring(1), croppedFile);
				ci = ImageIO.read(croppedFile);
				croppedImage = new Image(croppedImageDir, ci.getHeight(),
						ci.getWidth());
				imageTuple.setCroppedImaage(JsonUtil.toJson(croppedImage));
				cropData = null;
			}

			links.add(JsonUtil.toJson(imageTuple.toTuple()));

		}

		return links;
	}
}
