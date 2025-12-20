package com.gtech.food_api.infra.service.storage;

import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.gtech.food_api.domain.service.PhotoStorageService;

@Service
public class S3StorageService implements PhotoStorageService {
    
    @Override
    public void storeFile(NewPhoto newPhoto) {
    }

    @Override
    public void removeFile(String fileName) {
    }

    @Override
    public InputStream recoverFile(String fileName) {
        return null;
    }
}
