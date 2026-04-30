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
  ('article', '文章管理', 'content', 'enabled', '维护文章与图片内容'),
  ('order', '订单管理', 'business', 'enabled', '维护订单生产状态与物流轨迹')
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

INSERT INTO teaching_tool (name, category, url, description, status)
SELECT '计数器教学', 'math', '/tools/calculator-teaching.html', '适合课堂演示计数和拨珠关系的 HTML 教学工具', 'enabled'
WHERE NOT EXISTS (
  SELECT 1 FROM teaching_tool WHERE name = '计数器教学'
);

INSERT INTO teaching_tool (name, category, url, description, status)
SELECT '算盘教学', 'math', '/tools/abacus-teaching.html', '适合课堂演示算盘结构和数位变化的 HTML 教学工具', 'enabled'
WHERE NOT EXISTS (
  SELECT 1 FROM teaching_tool WHERE name = '算盘教学'
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

CREATE TABLE IF NOT EXISTS file_resource (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  original_name VARCHAR(255) NOT NULL,
  stored_name VARCHAR(255) NOT NULL,
  relative_path VARCHAR(500) NOT NULL,
  url VARCHAR(500) NOT NULL,
  content_type VARCHAR(128),
  size BIGINT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS biz_order (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_no VARCHAR(64) NOT NULL UNIQUE,
  customer_name VARCHAR(128) NOT NULL,
  product_name VARCHAR(128) NOT NULL,
  quantity INT NOT NULL,
  status VARCHAR(32) NOT NULL,
  remark VARCHAR(255),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS biz_order_status_log (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  from_status VARCHAR(32),
  to_status VARCHAR(32) NOT NULL,
  operator VARCHAR(64),
  remark VARCHAR(255),
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS biz_order_express (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL UNIQUE,
  express_company_code VARCHAR(32) NOT NULL,
  express_company_name VARCHAR(64) NOT NULL,
  tracking_no VARCHAR(128) NOT NULL,
  receiver_phone_suffix VARCHAR(16),
  latest_status VARCHAR(64),
  latest_location VARCHAR(128),
  latest_trace_time DATETIME,
  raw_response TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO biz_order (order_no, customer_name, product_name, quantity, status, remark)
SELECT 'JZ202604300001', '阮同学', '手工饺子礼盒', 2, 'SHIPPING', '第一条订单示例，已进入配送中'
WHERE NOT EXISTS (
  SELECT 1 FROM biz_order WHERE order_no = 'JZ202604300001'
);

INSERT INTO biz_order_status_log (order_id, from_status, to_status, operator, remark)
SELECT o.id, NULL, 'NOT_STARTED', 'system', '创建订单'
FROM biz_order o
WHERE o.order_no = 'JZ202604300001'
  AND NOT EXISTS (
    SELECT 1 FROM biz_order_status_log l WHERE l.order_id = o.id AND l.to_status = 'NOT_STARTED'
  );

INSERT INTO biz_order_status_log (order_id, from_status, to_status, operator, remark)
SELECT o.id, 'PACKED', 'SHIPPING', 'admin', '绑定快递后开始配送'
FROM biz_order o
WHERE o.order_no = 'JZ202604300001'
  AND NOT EXISTS (
    SELECT 1 FROM biz_order_status_log l WHERE l.order_id = o.id AND l.to_status = 'SHIPPING'
  );

INSERT INTO biz_order_express (
  order_id,
  express_company_code,
  express_company_name,
  tracking_no,
  receiver_phone_suffix,
  latest_status,
  latest_location
)
SELECT o.id, 'SF', '顺丰速运', 'SF1234567890', '1234', '配送中', '深圳南山派送网点'
FROM biz_order o
WHERE o.order_no = 'JZ202604300001'
  AND NOT EXISTS (
    SELECT 1 FROM biz_order_express e WHERE e.order_id = o.id
  );
