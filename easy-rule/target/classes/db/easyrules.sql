DROP TABLE IF EXISTS `t_r_biz_rule`;
CREATE TABLE `t_r_biz_rule` (
  `id` bigint(22) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) NOT NULL COMMENT '规则名称',
  `description` varchar(100) DEFAULT NULL COMMENT '规则描述',
  `priority` int(11) DEFAULT NULL COMMENT '权重',
  `composite_type` tinyint(4) DEFAULT NULL COMMENT '组合类型 1-and 2-or 3-all',
  `state` tinyint(4) DEFAULT NULL COMMENT '数据状态 0-有效 1-无效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_name` (`name`) USING BTREE COMMENT '策略名称'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `t_r_biz_rule_compose`
-- ----------------------------
DROP TABLE IF EXISTS `t_r_biz_rule_compose`;
CREATE TABLE `t_r_biz_rule_compose` (
  `id` bigint(22) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` bigint(22) NOT NULL COMMENT '规则ID',
  `name` varchar(50) DEFAULT NULL COMMENT '规则名称',
  `description` varchar(100) DEFAULT NULL COMMENT '规则描述',
  `condition` varchar(1000) DEFAULT NULL COMMENT '规则条件',
  `actions` varchar(100) DEFAULT NULL COMMENT '执行动作',
  `priority` int(11) DEFAULT NULL COMMENT '规则权重',
  `state` tinyint(4) DEFAULT NULL COMMENT '数据状态 0-有效 1-无效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_rule` (`rule_id`) USING BTREE COMMENT '规则'
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;