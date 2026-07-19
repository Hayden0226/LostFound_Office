package com.lostfound.dto;

import lombok.Data;

@Data
public class PageDto {
    private Integer page = 1;
    private Integer size = 10;
    private String keyword;
    private String category;
    private Long publisherId;
}
