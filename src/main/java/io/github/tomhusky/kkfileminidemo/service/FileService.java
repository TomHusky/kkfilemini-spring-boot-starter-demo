package io.github.tomhusky.kkfileminidemo.service;

import io.github.tomhusky.kkfileminidemo.vo.FileInfoRespVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 文件服务接口
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2022/3/31 15:27
 */
public interface FileService {

    /**
     * 上次文件到sftp
     *
     * @param file 文件
     * @return com.chinaunicom.deliver.api.model.vo.SysFileInfoVo
     */
    FileInfoRespVo uploadFile(MultipartFile file);

    /**
     * 文件预览
     *
     * @param fileNo   文件编号
     * @param response 响应对象
     */
    void viewFile(String fileNo, HttpServletResponse response);

    List<FileInfoRespVo> queryFileList();

    void removeFileInfo(FileInfoRespVo fileInfoRespVo);
}
