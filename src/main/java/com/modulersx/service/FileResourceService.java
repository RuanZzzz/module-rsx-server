package com.modulersx.service;

import com.modulersx.domain.vo.FileResourceVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileResourceService {

    FileResourceVO uploadImage(MultipartFile file);
}
