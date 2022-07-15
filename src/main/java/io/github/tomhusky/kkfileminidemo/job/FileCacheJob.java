package io.github.tomhusky.kkfileminidemo.job;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.github.tomhusky.kkfileminidemo.service.FileService;
import io.github.tomhusky.kkfileminidemo.vo.FileInfoRespVo;
import io.github.tomhusky.kkfilemini.ConfigConstants;
import io.github.tomhusky.kkfilemini.KkFileUtils;
import io.github.tomhusky.kkfilemini.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 文件缓存清理任务
 * <p/>
 *
 * @author luowj
 * @version 1.0
 * @since 2022/5/10 8:59
 */
@Slf4j
@Component
public class FileCacheJob {

    @Autowired
    private CacheService cacheService;

    @Autowired
    private FileService fileService;

    private final String fileDir = ConfigConstants.getFileDir();

    @PostConstruct
    public void init() {
        log.info("Cache clean start");
        cacheService.cleanCache();
        KkFileUtils.deleteDirectory(fileDir);
        log.info("Cache clean end");
    }

    @Scheduled(cron = "${cache.clean.cron:0 0/1 * * * *}")
    public void clean() {
        log.info("Cache clean start");
        List<FileInfoRespVo> fileInfoRespVos = fileService.queryFileList();
        DateTime now = DateUtil.date();
        List<FileInfoRespVo> deletes = fileInfoRespVos.stream().filter(item -> DateUtil.between(item.getCreateTime(), now, DateUnit.MINUTE, true) >= 3)
                .collect(Collectors.toList());
        for (FileInfoRespVo respVo : deletes) {
            fileService.removeFileInfo(respVo);
            String removeCache = cacheService.removeCache(respVo.getFileNo());
            if (StrUtil.isNotEmpty(removeCache)) {
                KkFileUtils.deleteFileByPath(fileDir + removeCache);
            }
        }
        log.info("Cache clean end");
    }
}
