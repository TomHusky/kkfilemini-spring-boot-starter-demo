package io.github.tomhusky.kkfileminidemo.controller;

import io.github.tomhusky.kkfileminidemo.base.R;
import io.github.tomhusky.kkfileminidemo.service.FileService;
import io.github.tomhusky.kkfileminidemo.vo.FileInfoRespVo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 文件操作接口
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2022/3/31 14:22
 */
@CrossOrigin(origins = "*")
@RestController
public class FileController {

    @Resource
    private FileService fileService;

    /**
     * 文件上传
     */
    @PostMapping("uploadFile")
    public R<FileInfoRespVo> uploadFile(@RequestParam("file") MultipartFile file) {
        FileInfoRespVo sysFileInfo = fileService.uploadFile(file);
        return R.ok(sysFileInfo);
    }

    /**
     * 文件预览
     */
    @GetMapping("viewFile")
    public void viewFile(@RequestParam String fileNo,
                         HttpServletResponse response) {
        fileService.viewFile(fileNo, response);
    }


    /**
     * 上传文件查询
     */
    @GetMapping("queryFileList")
    public R<List<FileInfoRespVo>> queryFileList() {
        return R.ok(fileService.queryFileList());
    }
}
