SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS module_rsx
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

USE module_rsx;

CREATE TABLE IF NOT EXISTS sys_module (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(64) NOT NULL UNIQUE,
  name VARCHAR(128) NOT NULL,
  type VARCHAR(64) NOT NULL,
  status VARCHAR(32) NOT NULL,
  remark VARCHAR(255),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO sys_module (code, name, type, status, remark)
VALUES
  ('module', '模块管理', 'system', 'enabled', '管理系统内可用模块'),
  ('tool', '教学工具', 'business', 'enabled', '管理教学工具内容'),
  ('article', '文章管理', 'content', 'disabled', '后续会接入文章与图片上传'),
  ('order', '订单管理', 'business', 'disabled', '后续用于订单状态跟踪')
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  type = VALUES(type),
  status = VALUES(status),
  remark = VALUES(remark);

CREATE TABLE IF NOT EXISTS sys_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(64) NOT NULL UNIQUE,
  password VARCHAR(128) NOT NULL,
  nickname VARCHAR(128) NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO sys_user (username, password, nickname, status)
VALUES
  ('admin', '123456', '系统管理员', 'enabled')
ON DUPLICATE KEY UPDATE
  password = VALUES(password),
  nickname = VALUES(nickname),
  status = VALUES(status);

CREATE TABLE IF NOT EXISTS teaching_tool (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(128) NOT NULL,
  category VARCHAR(64) NOT NULL,
  url VARCHAR(255),
  description VARCHAR(255),
  status VARCHAR(32) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO teaching_tool (name, category, url, description, status)
SELECT '番茄钟', 'time', 'https://pomofocus.io', '课堂计时工具', 'enabled'
WHERE NOT EXISTS (
  SELECT 1 FROM teaching_tool WHERE name = '番茄钟'
);

INSERT INTO teaching_tool (name, category, url, description, status)
SELECT '颜色选择器', 'design', 'https://coolors.co', '配色辅助工具', 'enabled'
WHERE NOT EXISTS (
  SELECT 1 FROM teaching_tool WHERE name = '颜色选择器'
);

INSERT INTO teaching_tool (name, category, url, description, status)
SELECT 'JSON 格式化', 'dev', 'https://json.cn', '格式化 JSON 内容', 'enabled'
WHERE NOT EXISTS (
  SELECT 1 FROM teaching_tool WHERE name = 'JSON 格式化'
);

CREATE TABLE IF NOT EXISTS content_article (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  summary VARCHAR(500),
  content TEXT NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO content_article (title, summary, content, status)
SELECT 'Docker 学习记录 01', '记录容器、镜像和 Dockerfile 的基础理解', '这是第一篇文章内容，后续可以继续扩展。', 'draft'
WHERE NOT EXISTS (
  SELECT 1 FROM content_article WHERE title = 'Docker 学习记录 01'
);

INSERT INTO content_article (title, summary, content, status)
SELECT 'Spring Boot 初始化总结', '记录 Java 17、Maven、Spring Boot 项目启动过程', '这是第二篇文章内容，后续会接入图片上传。', 'published'
WHERE NOT EXISTS (
  SELECT 1 FROM content_article WHERE title = 'Spring Boot 初始化总结'
);
