package redpanda.lu.Service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor //final,@NotNull붙은 필드에 생성자주입.
@Component
@PropertySource("classpath:application-amazons3.properties")
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    //properties에서 bucket 값 읽어옴.
    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {

        //CONVERT multipartFile to FILE
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile, dirName);
    }

    // it goes to putS3 method
    private String upload(File uploadFile, String dirName) {

        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName);

        //remove the file on the working directory
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {

      // putObject == ADD an object to a BUCKET
      // CannedAccessControlList == make an object PUBLIC
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // CONVERT multipartFile to FILE
    // and SAVE it on LOCAL
    private Optional<File> convert(MultipartFile file) throws IOException {

        //new File(파일경로), 여기에 user.dir로 working directory address + fileName
        //파일객체 생성
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());

        //CREATE A NEW FILE on the ABOVE address
        if (convertFile.createNewFile()) {

            // FileOutputStream == output stream for WRITING DATA to a FILE
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {

                fos.write(file.getBytes());
            }
            //Optional.of == GET AN INSTANCE of this Optional class
            return Optional.of(convertFile);
        }
        // get an EMPTY INSTANCE of this Optional class
        return Optional.empty();
    }
}