package fr.couderc.thomas.mystore.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.SingletonManager;

import fr.couderc.thomas.mystore.ws.cloudinary.models.Photo;
import fr.couderc.thomas.mystore.ws.cloudinary.models.PhotoUpload;
import fr.couderc.thomas.mystore.ws.cloudinary.repositories.PhotoRepository;

import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/file")
public class UploadFileService {

	public UploadFileService() {
//		Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
//				"cloud_name", "mystore",
//				"api_key", "173114377761594",
//				"api_secret", "HlzupGiXkgXH9uzmFda9M2_iydU"));

//		Cloudinary cloudinary = Singleton.getCloudinary();
//		SingletonManager manager = new SingletonManager();
//		manager.setCloudinary(cloudinary);
//		manager.init();
	}

	@Autowired
	private PhotoRepository photoRepository;

	@POST
	@Path("/upload")
	@Consumes("multipart/form-data")
	public Response uploadFile(@MultipartForm FileUploadForm form) {

		try {
			// Upload photo
//			Cloudinary cloudinary = new Cloudinary(Cloudinary.asMap(
//					"cloud_name", "mystore",
//					"api_key", "173114377761594",
//					"api_secret", "HlzupGiXkgXH9uzmFda9M2_iydU"));
			Map options = new HashMap<String,String>();
			Map<?, ?> uploadResult = Singleton.getCloudinary().uploader().upload(form.getData(), options);//,
//					Cloudinary.asMap(
//							"cloud_name", "mystore",
//							"api_key", "173114377761594",
//							"api_secret", "HlzupGiXkgXH9uzmFda9M2_iydU"));
//					Cloudinary.asMap("resource_type", "auto"));
			PhotoUpload photoUpload = new PhotoUpload();
			photoUpload.setPublicId((String) uploadResult.get("public_id"));
			photoUpload.setVersion(new Long((Integer) uploadResult.get("version")));
			photoUpload.setSignature((String) uploadResult.get("signature"));
			photoUpload.setFormat((String) uploadResult.get("format"));
			photoUpload.setResourceType((String) uploadResult.get("resource_type"));

			// Add photo to repo
			Photo photo = new Photo();
			photo.setTitle(photoUpload.getPublicId()); // ATTENTION J'AI MODIFIE ICI
			photo.setUpload(photoUpload);
			photoRepository.save(photo);

			System.out.println("Done");

		} catch (IOException e) {
			System.out.println("error");
			e.printStackTrace();
		}

		return Response.status(200).entity("upload 2 is called").build();

	}

	// save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {

		File file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileOutputStream fop = new FileOutputStream(file);

		fop.write(content);
		fop.flush();
		fop.close();

	}
}