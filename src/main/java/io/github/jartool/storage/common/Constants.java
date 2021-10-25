package io.github.jartool.storage.common;

/**
 * Constants
 *
 * @author jartool
 */
public class Constants {

    public interface View {
        public static final String REDIRECT = "redirect:";
        public static final String VIEW_FILE_LIST = "storage/fileSystem";
    }

    public interface DateFormatter {
        public static final String DATA_YMD_H24MS = "yyyy-MM-dd HH:mm:ss";
        public static final String DATA_YMD_HH24MISS = "yyyy-mm-dd hh24:mi:ss";
        public static final String DATA_YMD_HH24MS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    }


    public interface Log {
        public static final String FILE_STORE_SUCCESS = "store file[{}] success";
        public static final String FILE_DELETE_SUCCESS = "delete file[{}] success";
    }

    public interface Rep {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String CODE = "code";
        public static final String MSG = "message";;
        public static final String SECRET = "secret";
    }

    public interface Code {
        public static final int YES = 1;
        public static final int NO = 0;

        public static final String ON = "on";
        public static final String OFF = "off";
    }

    public interface Error {
        public static final String UPLOAD_FILE = "error-upload-file";
        public static final String DELETE_FILE = "error-delete-file";
        public static final String STORAGE_INIT = "could not initialize storage";
        public static final String STORAGE_FILE_NULL = "please select file";
        public static final String STORAGE_FILE_EMPTY = "failed to store empty file[{}]";
        public static final String STORAGE_FILE_FORMAT = "cannot store file with relative path outside current directory [{}]";
        public static final String STORAGE_FILE_STORE = "failed to store file[{}]";
        public static final String STORAGE_FILE_READ_FILES = "failed to read stored files";
        public static final String STORAGE_FILE_READ_FILE = "could not read file[{}]";
    }

    public interface File {
        public static final String DIR_STORAGE = "storage";
    }

    public interface Attribute {
        public static final String UPLOAD_URL = "uploadUrl";
        public static final String ATTR_LINKERS = "linkers";
        public static final String ATTR_MESSAGE = "message";
        public static final String AUTH_ENABLE = "authEnable";
        public static final String AUTH_KEY = "authKey";
        public static final String AUTH_URL = "authUrl";
    }

    public interface Method {
        public static final String LOAD_FILE = "loadFile";
        public static final String DELETE_FILE = "deleteFile";
    }

    public interface Header {
        public static final String MULTIPART_FILE = "attachment; filename={}";
    }
}
