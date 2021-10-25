package io.github.jartool.storage.annotation;

import io.github.jartool.storage.config.FileStorageConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableFileStorage
 *
 * @author jartool
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({FileStorageConfig.class})
@Documented
public @interface EnableFileStorage {

}
