package io.github.tomhusky.kkfileminidemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 自动打开浏览器
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2022/7/8 14:41
 */
@Slf4j
@Component
public class OpenBrowser implements CommandLineRunner {

    @Override
    public void run(String... args) {
        try {
            Runtime.getRuntime().exec("cmd   /c   start   http://127.0.0.1:8081/officePreview/index.html");
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}