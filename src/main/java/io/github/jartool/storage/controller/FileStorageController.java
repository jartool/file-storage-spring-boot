package io.github.jartool.storage.controller;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.github.jartool.storage.common.Constants;
import io.github.jartool.storage.entity.AuthEntity;
import io.github.jartool.storage.entity.Linker;
import io.github.jartool.storage.service.StorageService;
import io.github.jartool.storage.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * FileStorageController
 *
 * @author jartool
 */
@Controller
public class FileStorageController {

    private static final Log log = LogFactory.get();

    private final StorageService storageService;
    @Autowired
    public FileStorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @Value("${jartool.storage.file.view:/storage}")
    private String fileSystemView;
    @Value("${jartool.storage.file.upload-url:/storage/file/upload}")
    private String fileUploadUrl;
    @Value("${jartool.storage.file.download-url:/storage/file/download}")
    private String fileDownloadUrl;
    @Value("${jartool.storage.file.delete-url:/storage/file/delete}")
    private String fileDeleteUrl;


    @Value("${jartool.storage.auth.enable:true}")
    private boolean authEnable;
    @Value("${jartool.storage.auth.key:auth}")
    private String authKey;
    @Value("${jartool.storage.auth.url:/storageAuth}")
    private String authUrl;
    @Value("${jartool.storage.auth.username:admin}")
    private String username;
    @Value("${jartool.storage.auth.password:admin}")
    private String password;


    /**
     * File System
     *
     * @param model model
     * @param request request
     * @param redirectAttributes redirectAttributes
     * @return view
     */
    @GetMapping("${jartool.storage.file.view:/storage}")
    public String fileSystem(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String address = request.getRequestURL().toString().replace(fileSystemView, "");
        List<Linker> linkers = storageService.loadAll().map(
                path -> new Linker(String.valueOf(path.getFileName()),
                        CharSequenceUtil.format("{}{}{}{}{}", address, "/", fileDownloadUrl, "/", String.valueOf(path.getFileName())),
                        CharSequenceUtil.format("{}{}{}{}{}", address, "/", fileDeleteUrl, "/", String.valueOf(path.getFileName())))
        ).collect(Collectors.toList());

        if (!model.containsAttribute(Constants.Rep.MSG)) {
            model.addAttribute(Constants.Rep.MSG, " ");
        }
        model.addAttribute(Constants.Attribute.UPLOAD_URL, fileUploadUrl);
        model.addAttribute(Constants.Attribute.ATTR_LINKERS, linkers);
        model.addAttribute(Constants.Attribute.AUTH_ENABLE, authEnable);
        model.addAttribute(Constants.Attribute.AUTH_KEY, authKey);
        model.addAttribute(Constants.Attribute.AUTH_URL, authUrl);
        return Constants.View.VIEW_FILE_LIST;
    }

    /**
     * loadFile
     *
     * @param fileName fileName
     * @return ResponseEntity Resource
     * @throws UnsupportedEncodingException
     */
    @GetMapping("${jartool.storage.file.download-url:/storage/file/download}/{fileName:.+}")
    @ResponseBody
    public ResponseEntity<Resource> loadFile(@PathVariable String fileName) throws UnsupportedEncodingException {
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                    CharSequenceUtil.format(Constants.Header.MULTIPART_FILE,
                            new String(fileName.getBytes(StandardCharsets.UTF_8),StandardCharsets.ISO_8859_1)))
                .body(storageService.loadAsResource(fileName));
    }

    /**
     * upload
     *
     * @param file file
     * @param redirectAttributes redirectAttributes
     * @return msg
     */
    @PostMapping("${jartool.storage.file.upload-url:/storage/file/upload}")
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        String msg = Constants.Rep.SUCCESS;
        try {
            storageService.store(file);
        } catch (Exception e) {
            log.error(Constants.Error.UPLOAD_FILE, e);
            msg = e.getMessage();
        }
        redirectAttributes.addFlashAttribute(Constants.Attribute.ATTR_MESSAGE, msg);
        return CharSequenceUtil.format("{}{}", Constants.View.REDIRECT, fileSystemView);
    }

    /**
     * deleteFile
     *
     * @param fileName fileName
     * @return msg
     */
    @GetMapping("${jartool.storage.file.delete-url:/storage/file/delete}/{fileName:.+}")
    public String deleteFile(@PathVariable String fileName, RedirectAttributes redirectAttributes) {
        String msg = Constants.Rep.SUCCESS;
        try {
            storageService.delete(fileName);
        } catch (Exception e) {
            log.error(Constants.Error.DELETE_FILE, e);
            msg = Constants.Rep.ERROR;
        }
        redirectAttributes.addFlashAttribute(Constants.Attribute.ATTR_MESSAGE, msg);
        return CharSequenceUtil.format("{}{}", Constants.View.REDIRECT, fileSystemView);
    }

    @PostMapping("${jartool.storage.auth.url:/storageAuth}")
    @ResponseBody
    public JSONObject auth(@RequestBody AuthEntity authEntity) {
        JSONObject json = new JSONObject();
        json.set(Constants.Rep.CODE, -1);
        String secretKey = SecurityUtil.encrypt(username + password);
        if (CharSequenceUtil.isNotBlank(authEntity.getKey()) && authEntity.getKey().equals(secretKey)) {
            json.set(Constants.Rep.CODE, 0);
        } else if (username.equals(authEntity.getUsername()) && password.equals(authEntity.getPassword())){
            json.set(Constants.Rep.CODE, 0);
            json.set(Constants.Rep.SECRET, secretKey);
        }
        return json;
    }
}
