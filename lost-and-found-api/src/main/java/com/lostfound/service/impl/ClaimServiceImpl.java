package com.lostfound.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostfound.entity.Claim;
import com.lostfound.mapper.ClaimMapper;
import com.lostfound.service.ClaimService;
import org.springframework.stereotype.Service;

@Service
public class ClaimServiceImpl extends ServiceImpl<ClaimMapper, Claim> implements ClaimService {}
