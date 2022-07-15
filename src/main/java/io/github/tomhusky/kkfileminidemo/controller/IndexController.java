package io.github.tomhusky.kkfileminidemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 页面
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2022/7/8 14:33
 */
@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index() {
        return "redirect:index.html";
    }
}
