package com.laboratory.service.impl;

import com.laboratory.core.common.exception.LabException;
import com.laboratory.core.common.result.ResultCodeEnum;
import com.laboratory.service.FileService;
import com.laboratory.core.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author com.zyp
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private StorageService storageService;

    @Override
    public String upload(MultipartFile multipartFile) {
        try {
            byte[] bytes = multipartFile.getBytes();
            String filename = multipartFile.getOriginalFilename();
            String contentType = multipartFile.getContentType();

            return storageService.upload(bytes, filename, contentType);
        } catch (IOException e) {
            throw new LabException(ResultCodeEnum.FILE_UPLOAD_FAIL);
        }

    }

    @Override
    public void download(String filePath) {
    }

}
