package com.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostfound.common.Result;
import com.lostfound.dto.PageDto;
import com.lostfound.entity.FoundItem;
import com.lostfound.service.FoundItemService;
import com.lostfound.service.UserService;
import com.lostfound.entity.User;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/found")
public class FoundItemController {

    @Autowired
    private FoundItemService foundItemService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result<Page<FoundItem>> list(PageDto dto) {
        String kw = dto.getKeyword();
        if (kw != null) {
            kw = kw.replaceAll("[\\p{P}\\p{S}]", "")
                   .replace("。", "").replace("，", "").replace("！", "")
                   .replace("？", "").replace("、", "").replace("；", "")
                   .replace("：", "").replace("“", "").replace("”", "")
                   .replace("‘", "").replace("’", "").replace("【", "")
                   .replace("】", "").replace("《", "").replace("》", "")
                   .replace("（", "").replace("）", "").trim();
        }
        Page<FoundItem> page = foundItemService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<FoundItem>()
                        .eq(FoundItem::getStatus, 1)
                        .eq(dto.getPublisherId() != null, FoundItem::getPublisherId, dto.getPublisherId())
                        .eq(dto.getCategory() != null && !dto.getCategory().isEmpty(), FoundItem::getCategory, dto.getCategory())
                        .like(kw != null && !kw.isEmpty(), FoundItem::getTitle, kw)
                        .or(kw != null && !kw.isEmpty())
                        .like(kw != null && !kw.isEmpty(), FoundItem::getDescription, kw)
                        .orderByDesc(FoundItem::getCreateTime)
        );
        populateNickname(page.getRecords());
        return Result.success(page);
    }

    @GetMapping("/my")
    public Result<Page<FoundItem>> myList(PageDto dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.success(new Page<>());
        String kw = dto.getKeyword();
        if (kw != null) {
            kw = kw.replaceAll("[\\p{P}\\p{S}]", "")
                   .replace("。", "").replace("，", "").replace("！", "")
                   .replace("？", "").replace("、", "").replace("；", "")
                   .replace("：", "").replace("“", "").replace("”", "")
                   .replace("‘", "").replace("’", "").replace("【", "")
                   .replace("】", "").replace("《", "").replace("》", "")
                   .replace("（", "").replace("）", "").trim();
        }
        Page<FoundItem> page = foundItemService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<FoundItem>()
                        .eq(FoundItem::getPublisherId, userId)
                        .orderByDesc(FoundItem::getCreateTime)
        );
        populateNickname(page.getRecords());
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<FoundItem> detail(@PathVariable Long id) {
        FoundItem item = foundItemService.getById(id);
        if (item != null) {
            User publisher = userService.getById(item.getPublisherId());
            if (publisher != null) item.setPublisherNickname(publisher.getNickname());
        }
        return Result.success(item);
    }

    @PostMapping
    public Result<?> add(@RequestBody FoundItem item, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        item.setPublisherId(userId);
        item.setStatus(0);
        foundItemService.save(item);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody FoundItem item, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        item.setId(id);
        item.setStatus(0);
        foundItemService.updateById(item);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        foundItemService.removeById(id);
        return Result.success();
    }
    private void populateNickname(List<FoundItem> items) {
        if (items == null || items.isEmpty()) return;
        Set<Long> userIds = items.stream().map(FoundItem::getPublisherId).collect(Collectors.toSet());
        Map<Long, String> nickMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, User::getNickname));
        items.forEach(item -> item.setPublisherNickname(nickMap.getOrDefault(item.getPublisherId(), String.valueOf(item.getPublisherId()))));
    }

}