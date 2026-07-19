package com.lostfound.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lostfound.common.Result;
import com.lostfound.entity.Claim;
import com.lostfound.service.ClaimService;
import com.lostfound.service.UserService;
import com.lostfound.entity.User;
import com.lostfound.service.LostItemService;
import com.lostfound.service.FoundItemService;
import com.lostfound.entity.LostItem;
import com.lostfound.entity.FoundItem;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/claim")
public class ClaimController {

    @Autowired
    private ClaimService claimService;

    @Autowired
    private UserService userService;

    @Autowired
    private LostItemService lostItemService;

    @Autowired
    private FoundItemService foundItemService;

    @PostMapping
    public Result<?> add(@RequestBody Claim claim, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        claim.setClaimerId(userId);
        claim.setStatus(0);
        claimService.save(claim);
        return Result.success();
    }

    @GetMapping("/my")
    public Result<List<Claim>> myClaims(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.success(java.util.Collections.emptyList());
        List<Claim> list = claimService.list(
                new LambdaQueryWrapper<Claim>()
                        .eq(Claim::getClaimerId, userId)
                        .orderByDesc(Claim::getCreateTime)
        );
        // Populate claimer info + item title
        for (Claim claim : list) {
            User u = userService.getById(claim.getClaimerId());
            if (u != null) {
                claim.setClaimerNickname(u.getNickname());
                claim.setClaimerPhone(u.getPhone());
            }
            if ("lost".equals(claim.getItemType())) {
                LostItem li = lostItemService.getById(claim.getItemId());
                if (li != null) claim.setItemTitle(li.getTitle());
            } else {
                FoundItem fi = foundItemService.getById(claim.getItemId());
                if (fi != null) claim.setItemTitle(fi.getTitle());
            }
        }
        return Result.success(list);
    }

    @GetMapping("/approved")
    public Result<Claim> approved(@RequestParam Long itemId, @RequestParam String itemType) {
        Claim claim = claimService.getOne(
                new LambdaQueryWrapper<Claim>()
                        .eq(Claim::getItemId, itemId)
                        .eq(Claim::getItemType, itemType)
                        .eq(Claim::getStatus, 1)
                        .orderByAsc(Claim::getCreateTime)
                        .last("LIMIT 1")
        );
        if (claim != null) {
            User u = userService.getById(claim.getClaimerId());
            if (u != null) {
                claim.setClaimerNickname(u.getNickname());
                claim.setClaimerPhone(u.getPhone());
            }
        }
        return Result.success(claim);
    }

    @GetMapping("/{itemId}")
    public Result<List<Claim>> list(@PathVariable Long itemId, @RequestParam String itemType) {
        List<Claim> list = claimService.list(
                new LambdaQueryWrapper<Claim>()
                        .eq(Claim::getItemId, itemId)
                        .eq(Claim::getItemType, itemType)
                        .orderByAsc(Claim::getCreateTime)
        );
        return Result.success(list);
    }

}