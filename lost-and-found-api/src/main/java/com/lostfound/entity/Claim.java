package com.lostfound.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("claim")
public class Claim {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long itemId;
    private String itemType;    // lost, found
    private Long claimerId;
    private String reason;
    private String proof;
    private Integer status;     // 0-待审核, 1-已通过, 2-已拒绝

    @TableField(exist = false)
    private String claimerNickname;

    @TableField(exist = false)
    private String claimerPhone;

    @TableField(exist = false)
    private String itemTitle;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableLogic
    private Integer deleted;
}
