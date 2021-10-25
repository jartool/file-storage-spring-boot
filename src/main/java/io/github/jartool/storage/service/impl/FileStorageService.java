package io.github.jartool.storage.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.system.SystemUtil;
import io.github.jartool.storage.common.Constants;
import io.github.jartool.storage.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 * FileStorageService
 *
 * @author jartool
 */
@Service
public class FileStorageService implements StorageService {

    private static final Log log = LogFactory.get();

    /**
     * rootLocation
     *
     */
    private final Path rootLocation;

    @Autowired
    public FileStorageService(@Value("${jartool.storage.file.storage-path:}") String fileStoragePath) {
        if (CharSequenceUtil.isBlank(fileStoragePath)) {
            fileStoragePath = FileUtil
                    .mkdir(new File(CharSequenceUtil.format("{}{}",
                            SystemUtil.getUserInfo().getHomeDir(),
                            Constants.File.DIR_STORAGE)))
                    .getAbsolutePath();
        }
        this.rootLocation = Paths.get(fileStoragePath);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new IllegalArgumentException(Constants.Error.STORAGE_INIT, e);
        }
    }

    /**
     * store
     *
     * @param file file
     */
    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (CharSequenceUtil.isEmpty(filename)) {
            throw new IllegalArgumentException(Constants.Error.STORAGE_FILE_NULL);
        }
        if (file.isEmpty()) {
            throw new IllegalArgumentException(CharSequenceUtil.format(Constants.Error.STORAGE_FILE_EMPTY, filename));
        }
        if (filename.contains("..")) {
            throw new IllegalArgumentException(CharSequenceUtil.format(Constants.Error.STORAGE_FILE_FORMAT, filename));
        }
        try {
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            log.info(CharSequenceUtil.format(Constants.Log.FILE_STORE_SUCCESS, filename));
        } catch (IOException e) {
            throw new IllegalArgumentException(CharSequenceUtil.format(Constants.Error.STORAGE_FILE_STORE, filename), e);
        }
    }

    /**
     * loadAll
     *
     * @return {@code Stream<Path>}
     */
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path->this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new IllegalArgumentException(Constants.Error.STORAGE_FILE_READ_FILES, e);
        }
    }


    /**
     * 负载
     *
     * @param fileName fileName
     * @return {@code Path}
     */
    @Override
    public Path load(String fileName) {
        return rootLocation.resolve(fileName);
    }

    /**
     * loadAsResource
     *
     * @param fileName fileName
     * @return {@code Resource}
     */
    @Override
    public Resource loadAsResource(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new IllegalArgumentException(CharSequenceUtil.format(Constants.Error.STORAGE_FILE_READ_FILE, fileName));
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(CharSequenceUtil.format(Constants.Error.STORAGE_FILE_READ_FILE, fileName), e);
        }
    }


    /**
     * delete file
     *
     * @param fileName fileName
     */
    @Override
    public void delete(String fileName) {
        try {
            Path file = load(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                PathUtil.del(file);
                log.info(CharSequenceUtil.format(Constants.Log.FILE_DELETE_SUCCESS, fileName));
            } else {
                throw new IllegalArgumentException(CharSequenceUtil.format(Constants.Error.STORAGE_FILE_READ_FILE, fileName));
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(CharSequenceUtil.format(Constants.Error.STORAGE_FILE_READ_FILE, fileName), e);
        }
    }


    /**
     * delete all file
     *
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
