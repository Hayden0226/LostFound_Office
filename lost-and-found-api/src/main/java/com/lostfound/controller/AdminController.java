package com.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lostfound.common.Result;
import com.lostfound.dto.PageDto;
import com.lostfound.entity.*;
import com.lostfound.service.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @Autowired
    private LostItemService lostItemService;
    @Autowired
    private FoundItemService foundItemService;
    @Autowired
    private ClaimService claimService;

    @GetMapping("/stats")
    public Result<Map<String, Long>> stats() {
        Map<String, Long> map = new HashMap<>();
        map.put("userCount", userService.count());
        map.put("lostCount", lostItemService.count());
        map.put("foundCount", foundItemService.count());
        return Result.success(map);
    }

    @GetMapping("/users")
    public Result<Page<User>> users(PageDto dto) {
        return Result.success(userService.page(new Page<>(dto.getPage(), dto.getSize())));
    }

    @GetMapping("/lost-items")
    public Result<Page<LostItem>> lostItems(PageDto dto) {
        Page<LostItem> page = lostItemService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<LostItem>().orderByDesc(LostItem::getCreateTime)
        );
        populatePublisherNickname(page.getRecords());
        return Result.success(page);
    }

    @GetMapping("/found-items")
    public Result<Page<FoundItem>> foundItems(PageDto dto) {
        Page<FoundItem> page = foundItemService.page(
                new Page<>(dto.getPage(), dto.getSize()),
                new LambdaQueryWrapper<FoundItem>().orderByDesc(FoundItem::getCreateTime)
        );
        populateFoundPublisherNickname(page.getRecords());
        return Result.success(page);
    }

    @PutMapping("/review/lost/{id}")
    public Result<?> reviewLost(@PathVariable Long id, @RequestParam Integer status) {
        LostItem item = lostItemService.getById(id);
        if (item != null) {
            item.setStatus(status);
            lostItemService.updateById(item);
        }
        return Result.success();
    }

    @PutMapping("/review/found/{id}")
    public Result<?> reviewFound(@PathVariable Long id, @RequestParam Integer status) {
        FoundItem item = foundItemService.getById(id);
        if (item != null) {
            item.setStatus(status);
            foundItemService.updateById(item);
        }
        return Result.success();
    }

    @PutMapping("/review/claim/{id}")
    public Result<?> reviewClaim(@PathVariable Long id, @RequestParam Integer status) {
        Claim claim = claimService.getById(id);
        if (claim != null) {
            claim.setStatus(status);
            claimService.updateById(claim);
            if (status == 1) {
                if ("lost".equals(claim.getItemType())) {
                    LostItem item = lostItemService.getById(claim.getItemId());
                    if (item != null) { item.setStatus(2); lostItemService.updateById(item); }
                } else {
                    FoundItem item = foundItemService.getById(claim.getItemId());
                    if (item != null) { item.setStatus(2); foundItemService.updateById(item); }
                }
            }
        }
        return Result.success();
    }

    @DeleteMapping("/item/{type}/{id}")
    public Result<?> deleteItem(@PathVariable String type, @PathVariable Long id) {
        if ("lost".equals(type)) lostItemService.removeById(id);
        else if ("found".equals(type)) foundItemService.removeById(id);
        return Result.success();
    }
    private void populatePublisherNickname(List<LostItem> items) {
        if (items.isEmpty()) return;
        Set<Long> userIds = items.stream().map(LostItem::getPublisherId).collect(Collectors.toSet());
        Map<Long, String> nickMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, User::getNickname));
        items.forEach(item -> item.setPublisherNickname(nickMap.getOrDefault(item.getPublisherId(), String.valueOf(item.getPublisherId()))));
    }

    private void populateFoundPublisherNickname(List<FoundItem> items) {
        if (items.isEmpty()) return;
        Set<Long> userIds = items.stream().map(FoundItem::getPublisherId).collect(Collectors.toSet());
        Map<Long, String> nickMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, User::getNickname));
        items.forEach(item -> item.setPublisherNickname(nickMap.getOrDefault(item.getPublisherId(), String.valueOf(item.getPublisherId()))));
    }

}