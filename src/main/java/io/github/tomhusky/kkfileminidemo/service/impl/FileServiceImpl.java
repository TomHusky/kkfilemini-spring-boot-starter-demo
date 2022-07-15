package io.github.tomhusky.kkfileminidemo.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tomhusky.kkfileminidemo.base.R;
import io.github.tomhusky.kkfileminidemo.service.FileService;
import io.github.tomhusky.kkfileminidemo.vo.FileCacheVo;
import io.github.tomhusky.kkfileminidemo.vo.FileInfoRespVo;
import io.github.tomhusky.kkfilemini.KKFileViewComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * <p>
 * 文件服务实现类
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2022/3/31 15:28
 */
@Service
public class FileServiceImpl implements FileService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private KKFileViewComponent fileViewComponent;

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<String, FileCacheVo> tempFilePathCache = new ConcurrentHashMap<>();

    private static final Set<String> ALLOW_FILE_TYPE = CollUtil.set(false, ".doc", ".docx", ".pdf", ".xlsx", ".xls", ".pptx", ".ppt");

    private static final List<FileInfoRespVo> FILE_SET = new CopyOnWriteArrayList<>();

    @Override
    public FileInfoRespVo uploadFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;

        String fileType = originalFilename.substring(originalFilename.lastIndexOf('.'));
        if (!ALLOW_FILE_TYPE.contains(fileType)) {
            throw new RuntimeException("不支持的文件类型！");
        }
        try {
            String fileNo = IdUtil.getSnowflakeNextIdStr();
            File tempFile = File.createTempFile(fileNo, fileType);
            FileUtil.writeFromStream(file.getInputStream(), tempFile, true);

            FileInfoRespVo fileInfoRespVo = new FileInfoRespVo();
            fileInfoRespVo.setFileNo(fileNo);
            fileInfoRespVo.setFileName(originalFilename);
            fileInfoRespVo.setFileSuffix(fileType);
            fileInfoRespVo.setCreateTime(new Date());

            FILE_SET.add(fileInfoRespVo);

            FileCacheVo fileCacheVo = new FileCacheVo();
            fileCacheVo.setFileNo(fileNo);
            fileCacheVo.setFileSuffix(fileType);
            fileCacheVo.setFileName(fileNo + fileType);
            fileCacheVo.setOriginalName(originalFilename);
            fileCacheVo.setPath(tempFile.getPath());

            fileViewComponent.addFileToCache(tempFile, fileNo);

            tempFilePathCache.put(fileNo, fileCacheVo);

            return fileInfoRespVo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("上传文件失败");
        }
    }

    @Override
    public void viewFile(String fileNo, HttpServletResponse response) {

        FileCacheVo fileCacheVo = tempFilePathCache.get(fileNo);
        if (fileCacheVo == null) {
            downloadFileFailure("文件不存在！", response);
            return;
        }
        boolean existFile = fileViewComponent.cacheExistFile(fileNo);
        if (!existFile) {
            downloadFileFailure("文件已经不存在！", response);
            return;
        }
        fileViewComponent.viewCacheFile(response, fileNo);
    }

    @Override
    public List<FileInfoRespVo> queryFileList() {
        return FILE_SET.stream().sorted(Comparator.comparing(FileInfoRespVo::getCreateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void removeFileInfo(FileInfoRespVo fileInfoRespVo) {
        FILE_SET.remove(fileInfoRespVo);
    }

    private void downloadFileFailure(String msg, HttpServletResponse response) {
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        try (PrintWriter pw = response.getWriter()) {
            String result = objectMapper.writeValueAsString(R.fail(msg));
            pw.write(result);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

}
