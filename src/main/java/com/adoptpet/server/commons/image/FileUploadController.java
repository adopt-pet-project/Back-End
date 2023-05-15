package com.adoptpet.server.commons.image;

//import com.adoptpet.server.commons.image.service.AwsS3Service;
import com.adoptpet.server.commons.image.dto.ImageDto;
import com.adoptpet.server.commons.image.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final AwsS3Service awsS3Service;

    @PostMapping(value = "",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ImageDto> uploadImage(
            @RequestPart(name = "image") MultipartFile image,
            @RequestPart(name = "path") String path){

        ImageDto infoDto = awsS3Service.upload(image, path);

        return ResponseEntity.ok(infoDto);

    }

}
