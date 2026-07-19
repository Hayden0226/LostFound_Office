package com.lostfound.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lostfound.entity.Comment;
import com.lostfound.mapper.CommentMapper;
import com.lostfound.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {}
