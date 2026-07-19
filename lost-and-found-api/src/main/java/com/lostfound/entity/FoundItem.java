package com.lostfound.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("found_item")
public class FoundItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDateTime foundTime;
    private String image;
    private Long publisherId;
    private String contactPhone;
    private Integer status;     // 0-待审核, 1-已发布, 2-已认领, 3-已关闭

    @TableField(exist = false)
    private String publisherNickname;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
