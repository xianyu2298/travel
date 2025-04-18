CREATE DATABASE IF NOT EXISTS travelfreemanage;
USE travelfreemanage;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(10) NOT NULL,
    password VARCHAR(20) NOT NULL
);

INSERT INTO user (username, password) VALUES ('admin', '123456');

CREATE TABLE travel_plan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  plan_name VARCHAR(100) NOT NULL,
  travel_date DATE NOT NULL,
  itinerary TEXT,
  FOREIGN KEY (user_id) REFERENCES user(id)
);

ALTER TABLE travel_plan
ADD COLUMN start_province VARCHAR(50),
ADD COLUMN start_city VARCHAR(50),
ADD COLUMN end_province VARCHAR(50),
ADD COLUMN end_city VARCHAR(50);

CREATE TABLE expense (
    id INT PRIMARY KEY AUTO_INCREMENT,
    travel_plan_id INT NOT NULL COMMENT '关联的旅行计划ID',
    user_id INT NOT NULL COMMENT '用户ID',
    transportation DECIMAL(10,2) DEFAULT 0 COMMENT '交通费用',
    accommodation DECIMAL(10,2) DEFAULT 0 COMMENT '住宿费用',
    dining DECIMAL(10,2) DEFAULT 0 COMMENT '餐饮费用',
    tickets DECIMAL(10,2) DEFAULT 0 COMMENT '门票费用',
    shopping DECIMAL(10,2) DEFAULT 0 COMMENT '购物费用',
    others DECIMAL(10,2) DEFAULT 0 COMMENT '其他费用',
    total DECIMAL(10,2) DEFAULT 0 COMMENT '总费用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (travel_plan_id) REFERENCES travel_plan(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

ALTER TABLE expense MODIFY COLUMN travel_plan_id INT NOT NULL;