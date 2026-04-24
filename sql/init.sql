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
