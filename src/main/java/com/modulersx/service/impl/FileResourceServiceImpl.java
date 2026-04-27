package com.modulersx.service.impl;

import com.modulersx.domain.po.FileResourcePO;
import com.modulersx.domain.vo.FileResourceVO;
import com.modulersx.exception.BizException;
import com.modulersx.repository.FileResourceMapper;
import com.modulersx.service.FileResourceService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileResourceServiceImpl implements FileResourceService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp");
    private static final DateTimeFormatter DATE_PATH_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final FileResourceMapper fileResourceMapper;
    private final Path uploadRootDir;
    private final String accessPath;

    public FileResourceServiceImpl(
            FileResourceMapper fileResourceMapper,
            @Value("${app.upload.root-dir:./uploads}") String uploadRootDir,
            @Value("${app.upload.access-path:/uploads}") String accessPath) {
        this.fileResourceMapper = fileResourceMapper;
        this.uploadRootDir = Path.of(uploadRootDir).toAbsolutePath().normalize();
        this.accessPath = normalizeAccessPath(accessPath);
    }

    @Override
    public FileResourceVO uploadImage(MultipartFile file) {
        validate(file);

        String originalName = cleanOriginalName(file);
        String extension = getExtension(originalName);
        String datePath = LocalDate.now().format(DATE_PATH_FORMATTER);
        String storedName = UUID.randomUUID() + "." + extension;

        // 按日期分目录，避免所有上传文件堆在同一个大目录里。
        Path targetDir = uploadRootDir.resolve(datePath).normalize();
        Path targetFile = targetDir.resolve(storedName).normalize();

        // 路径标准化后必须仍在上传根目录下，防止构造路径逃逸到其他目录。
        if (!targetFile.startsWith(uploadRootDir)) {
            throw new BizException(400, "invalid file path");
        }

        try {
            Files.createDirectories(targetDir);
            file.transferTo(targetFile);
        } catch (IOException ex) {
            throw new BizException(500, "failed to save file");
        }

        String relativePath = datePath + "/" + storedName;
        String url = accessPath + "/" + relativePath;

        // 数据库只保存文件元数据，图片二进制内容仍然保存在 uploads/ 目录。
        FileResourcePO resource = new FileResourcePO();
        resource.setOriginalName(originalName);
        resource.setStoredName(storedName);
        resource.setRelativePath(relativePath);
        resource.setUrl(url);
        resource.setContentType(file.getContentType());
        resource.setSize(file.getSize());
        fileResourceMapper.insert(resource);

        return toVO(resource);
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BizException(400, "file cannot be empty");
        }
        String originalName = cleanOriginalName(file);
        if (!StringUtils.hasText(originalName)) {
            throw new BizException(400, "file name cannot be blank");
        }
        String extension = getExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BizException(400, "only jpg, jpeg, png, gif, webp images are allowed");
        }
        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType) || !contentType.toLowerCase(Locale.ROOT).startsWith("image/")) {
            throw new BizException(400, "only image files are allowed");
        }
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == fileName.length() - 1) {
            throw new BizException(400, "file extension is required");
        }
        return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
    }

    private String cleanOriginalName(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFilename)) {
            throw new BizException(400, "file name cannot be blank");
        }
        return StringUtils.cleanPath(originalFilename);
    }

    private String normalizeAccessPath(String value) {
        String path = StringUtils.hasText(value) ? value.trim() : "/uploads";
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        return path;
    }

    private FileResourceVO toVO(FileResourcePO po) {
        return new FileResourceVO(po.getId(), po.getOriginalName(), po.getUrl(), po.getContentType(), po.getSize());
    }
}
