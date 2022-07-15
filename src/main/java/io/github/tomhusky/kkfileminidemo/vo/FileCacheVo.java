package io.github.tomhusky.kkfileminidemo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 文件上传记录
 * </p>
 *
 * @author luowj
 * @since 2022-03-31
 */
@Data
public class FileCacheVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文件编号
     */
    private String fileNo;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 源文件名称
     */
    private String originalName;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件路径
     */
    private String path;
}
