//package alessandraciccone.CorgiConnection.services;
//
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.utils.ObjectUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class CloudinaryService {
//
//    @Autowired
//    private Cloudinary cloudinary;
//
//    public String upload(MultipartFile file, String folder) throws IOException {
//        Map uploadResult = cloudinary.uploader().upload(
//                file.getBytes(),
//                ObjectUtils.asMap(
//                        "folder", folder,
//                        "use_filename", true,
//                        "unique_filename", false
//                )
//        );
//        return (String) uploadResult.get("secure_url");
//    }
//}
