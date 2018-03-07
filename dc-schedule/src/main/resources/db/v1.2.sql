CREATE TABLE IF NOT EXISTS `dc_message_send_properties` (
  `id` varchar(64) NOT NULL COMMENT '编号ID',
  `account_number` varchar(32) NOT NULL DEFAULT '' COMMENT '账号',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `name` varchar(255) DEFAULT NULL COMMENT '发送名',
  `email` varchar(255) DEFAULT NULL COMMENT '邮件地址',
  `is_default` varchar(11) DEFAULT NULL COMMENT '是否默认',
  `type` varchar(11) DEFAULT NULL COMMENT '类别:1：短信 2：邮件',
  `smtp` varchar(64) DEFAULT NULL COMMENT 'smtp地址',
  `sms_url` varchar(64) DEFAULT NULL COMMENT '短信请求接口地址',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信息发送配置';

CREATE TABLE IF NOT EXISTS `dc_message_send_log` (
  `id` varchar(64) NOT NULL COMMENT '编号ID',
  `from_address` varchar(64) DEFAULT NULL COMMENT '发送方地址',
  `to_address` varchar(2000) NOT NULL COMMENT '接收方地址',
  `content` text COMMENT '发送内容',
  `type` varchar(11) DEFAULT NULL COMMENT '类别:1：短信 2：邮件',
  `subject` varchar(300) DEFAULT NULL COMMENT '发送主题',
  `cc_address` varchar(2000) DEFAULT NULL COMMENT '抄送人地址',
  `status` varchar(11) DEFAULT NULL COMMENT '发送状态：0 待发送 1 发送成功 2 发送失败',
  `exception` text COMMENT '失败原因',
  `send_date` datetime DEFAULT NULL COMMENT '发送时间',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='信息发送详情';

INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000100050003', '100010005', '0,1,10001,100010005,', '信息发送开关', '90', '/msg/dcMessageSendLog/changeState', '', '', '1', '', '', '0', '1', '2018-02-07 10:30:32', '1', '2018-02-07 10:32:38', '', '0', 'Message transmitter switch', '信息發送開關', '信息发送开关');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10001000500020002', '1000100050002', '0,1,10001,100010005,1000100050002,', '查看', '60', '', '', '', '0', 'msg:dcMessageSendLog:view', '', '0', '1', '2018-02-06 14:16:51', '1', '2018-02-06 14:16:51', '', '0', 'view', '查看', '查看');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10001000500020001', '1000100050002', '0,1,10001,100010005,1000100050002,', '修改', '30', '', '', '', '0', 'msg:dcMessageSendLog:edit', '', '0', '1', '2018-02-06 14:16:27', '1', '2018-02-06 14:16:27', '', '0', 'edit', '修改', '修改');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000100050002', '100010005', '0,1,10001,100010005,', '信息发送详情', '60', '/msg/dcMessageSendLog/', '', '', '1', '', '', '0', '1', '2018-02-06 14:15:55', '1', '2018-02-06 14:15:55', '', '0', 'Information sending details', '信息發送詳情', '信息发送详情');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10001000500010002', '1000100050001', '0,1,10001,100010005,1000100050001,', '查看', '60', '', '', '', '0', 'msg:dcMessageSendProperties:view', '', '0', '1', '2018-02-06 14:15:11', '1', '2018-02-06 14:15:11', '', '0', 'view', '查看', '查看');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('10001000500010001', '1000100050001', '0,1,10001,100010005,1000100050001,', '修改', '30', '', '', '', '0', 'msg:dcMessageSendProperties:edit', '', '0', '1', '2018-02-06 14:14:21', '1', '2018-02-06 14:14:21', '', '0', 'edit', '修改', '修改');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('1000100050001', '100010005', '0,1,10001,100010005,', '信息发送配置', '30', '/msg/dcMessageSendProperties', '', '', '1', '', '', '0', '1', '2018-02-06 14:13:37', '1', '2018-02-06 14:13:37', '', '0', 'Information sending configuration', '信息發送配置', '信息发送配置');
INSERT IGNORE INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`, `name_en_US`, `name_zh_TW`, `name_zh_CN`) VALUES ('100010005', '10001', '0,1,10001,', '信息发送管理', '180', '', '', 'mail-forward (alias)', '1', '', '', '0', '1', '2018-02-06 14:12:08', '1', '2018-02-06 14:12:08', '', '0', 'Information transmission management', '信息發送管理', '信息发送管理');

