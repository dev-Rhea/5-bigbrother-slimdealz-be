package bigbrother.slimdealz.controller.product;

import bigbrother.slimdealz.exception.CustomException;
import bigbrother.slimdealz.service.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/v1/image")
public class ImageController {

    private final S3Service s3Service;

    @GetMapping
    public ResponseEntity<String> getProductImageUrl(@RequestParam("productName") String productName) {
        try {
            String imageUrl = s3Service.getProductImageUrl(productName);

            return ResponseEntity.ok(imageUrl);
        }
        catch (CustomException e) {
            log.error(e.getDetailMessage());
            throw e;
        }
    }
}