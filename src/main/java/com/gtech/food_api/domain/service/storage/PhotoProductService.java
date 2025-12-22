package com.gtech.food_api.domain.service.storage;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtech.food_api.domain.model.PhotoProduct;
import com.gtech.food_api.domain.repository.ProductRepository;
import com.gtech.food_api.domain.service.exceptions.PhotoProductNotFoundException;
import com.gtech.food_api.domain.service.storage.PhotoStorageService.NewPhoto;

@Service
public class PhotoProductService {

    @Autowired
    private ProductRepository productRepository;

    // usamos uma interface para abstrair a implementação do storage, assim podemos usar diferentes implementações de storage, como local, aws, etc. Assim diminuimos o acoplamento da aplicação.
    @Autowired
    private PhotoStorageService photoStorageService;

    @Transactional
    public PhotoProduct savePhoto(PhotoProduct photo, InputStream inputStream) {
        Long productId = photo.getProductId();
        Long restaurantId = photo.getRestaurantId();
        String newFileName = photoStorageService.generateNewFileName(photo.getFileName());
        String existingFileName = null;

        Optional<PhotoProduct> existingPhoto = productRepository.findPhotoById(productId, restaurantId);

        if (existingPhoto.isPresent()) {
            // se existe uma foto existente, pega o nome da foto existente
            existingFileName = existingPhoto.get().getFileName();
            // exclui a foto existente
            productRepository.deletePhoto(existingPhoto.get());
        }

        // salvamos o arquivo com nome unico gerado pelo sistema
        photo.setFileName(newFileName);
        // garantimos que a foto foi salva no banco de dados antes de armazenar a foto no storage
        photo = productRepository.savePhoto(photo);
        productRepository.flush();

        NewPhoto newPhoto = NewPhoto.builder()
            .fileName(photo.getFileName())
            .contentType(photo.getContentType())
            .inputStream(inputStream)
            .contentLength(photo.getSize())
            .build();

        // caso de erro, a foto nao é armazenada no storage, e o banco de dados nao é salvo e da um rollback automaticamente pelo spring data jpa
        photoStorageService.replaceFile(existingFileName, newPhoto);

        return photo;
    }  

    @Transactional
    public void delete(Long restaurantId, Long productId){
        PhotoProduct photoProduct = findOrFail(restaurantId, productId);

        productRepository.deletePhoto(photoProduct);
        productRepository.flush();
        
        photoStorageService.removeFile(photoProduct.getFileName());
    }

    @Transactional(readOnly = true)
    public PhotoProduct findOrFail(Long restaurantId, Long productId) {
        return productRepository.findPhotoById(productId, restaurantId).orElseThrow(()
                -> new PhotoProductNotFoundException(restaurantId, productId));
    }
}
