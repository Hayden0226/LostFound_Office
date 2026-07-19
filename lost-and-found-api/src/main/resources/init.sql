-- 创建数据库
CREATE DATABASE IF NOT EXISTS lost_found DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE lost_found;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(50),
    `avatar` VARCHAR(255),
    `role` VARCHAR(20) DEFAULT 'user',
    `phone` VARCHAR(20),
    `email` VARCHAR(100),
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` INT DEFAULT 0,
    INDEX `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 失物表
CREATE TABLE IF NOT EXISTS `lost_item` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `category` VARCHAR(50),
    `category` VARCHAR(50),
    `category` VARCHAR(50),
    `location` VARCHAR(200),
    `lost_time` DATETIME,
    `image` VARCHAR(255),
    `publisher_id` BIGINT NOT NULL,
    `contact_phone` VARCHAR(20),
    `status` INT DEFAULT 0 COMMENT '0-待审核 1-已发布 2-已找到 3-已关闭',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` INT DEFAULT 0,
    INDEX `idx_publisher` (`publisher_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 招领表
CREATE TABLE IF NOT EXISTS `found_item` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `description` TEXT,
    `location` VARCHAR(200),
    `found_time` DATETIME,
    `image` VARCHAR(255),
    `publisher_id` BIGINT NOT NULL,
    `contact_phone` VARCHAR(20),
    `status` INT DEFAULT 0 COMMENT '0-待审核 1-已发布 2-已认领 3-已关闭',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` INT DEFAULT 0,
    INDEX `idx_publisher` (`publisher_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `item_id` BIGINT NOT NULL,
    `item_type` VARCHAR(20) NOT NULL COMMENT 'lost/found',
    `user_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL,
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` INT DEFAULT 0,
    INDEX `idx_item` (`item_id`, `item_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 认领表
CREATE TABLE IF NOT EXISTS `claim` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `item_id` BIGINT NOT NULL,
    `item_type` VARCHAR(20) NOT NULL COMMENT 'lost/found',
    `claimer_id` BIGINT NOT NULL,
    `reason` TEXT,
    `proof` VARCHAR(255),
    `status` INT DEFAULT 0 COMMENT '0-待审核 1-已通过 2-已拒绝',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `deleted` INT DEFAULT 0,
    INDEX `idx_item` (`item_id`, `item_type`),
    INDEX `idx_claimer` (`claimer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入默认管理员
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', 'admin');
-- 密码是 123456 的 MD5

-- 给已存在的 found_item 表加 category 字段（如果还没有的话）
ALTER TABLE `found_item` ADD COLUMN IF NOT EXISTS `category` VARCHAR(50) AFTER `description`;

-- 给已存在的 found_item 表加 category 字段（如果还没有的话）
ALTER TABLE `found_item` ADD COLUMN IF NOT EXISTS `category` VARCHAR(50) AFTER `description`;
