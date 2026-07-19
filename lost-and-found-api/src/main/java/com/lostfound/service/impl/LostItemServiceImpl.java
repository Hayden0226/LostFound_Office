package com.lostfound.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostfound.entity.LostItem;
import com.lostfound.mapper.LostItemMapper;
import com.lostfound.service.LostItemService;
import org.springframework.stereotype.Service;

@Service
public class LostItemServiceImpl extends ServiceImpl<LostItemMapper, LostItem> implements LostItemService {}
