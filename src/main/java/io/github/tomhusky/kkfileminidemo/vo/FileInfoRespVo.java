package io.github.tomhusky.kkfileminidemo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件上传记录
 * </p>
 *
 * @author luowj
 * @since 2022-03-31
 */
@Data
public class FileInfoRespVo implements Serializable {

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
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
