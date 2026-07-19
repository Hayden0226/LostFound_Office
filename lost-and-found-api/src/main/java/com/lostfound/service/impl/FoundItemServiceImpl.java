package com.lostfound.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostfound.entity.FoundItem;
import com.lostfound.mapper.FoundItemMapper;
import com.lostfound.service.FoundItemService;
import org.springframework.stereotype.Service;

@Service
public class FoundItemServiceImpl extends ServiceImpl<FoundItemMapper, FoundItem> implements FoundItemService {}
