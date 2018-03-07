INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001', '1', '0,1,', '集成开发台', '5120', '', '', 'indent', '1', '', '', '0', '1', '2017-09-08 15:26:41', '1', '2017-10-20 16:03:18', '集成管理ICCN', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100010001', '10001', '0,1,10001,', '系统管理', '30', '', '', 'indent', '1', '', '', '0', '1', '2017-09-08 15:31:22', '1', '2017-09-25 14:44:14', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100010001', '100010001', '0,1,10001,100010001,', '数据库连接管理', '30', '/ica/iccnIcaDb', '', '', '1', '', NULL, '0', '1', '2017-09-08 15:32:43', '1', '2017-09-08 17:57:32', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100010001', '1000100010001', '0,1,10001,100010001,1000100010001,', '查看权限', '30', '', '', '', '0', 'ica:iccnIcaDb:view', NULL, '0', '1', '2017-09-08 17:57:52', '1', '2017-09-08 17:57:52', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100010002', '1000100010001', '0,1,10001,100010001,1000100010001,', '修改权限', '60', '', '', '', '0', 'ica:iccnIcaDb:edit', NULL, '0', '1', '2017-09-08 17:58:22', '1', '2017-09-08 17:58:22', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100010002', '100010001', '0,1,10001,100010001,', '系统服务管理', '60', '/ica/iccnIcaService', '', '', '1', '', NULL, '0', '1', '2017-09-11 10:47:46', '1', '2017-09-11 11:23:50', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100020001', '1000100010002', '0,1,10001,100010001,1000100010002,', '查看权限', '30', '', '', '', '0', 'ica:iccnIcaService:view', '', '0', '1', '2017-09-11 11:24:10', '1', '2017-10-09 18:01:55', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100020002', '1000100010002', '0,1,10001,100010001,1000100010002,', '修改权限', '60', '', '', '', '0', 'ica:iccnIcaService:edit', NULL, '0', '1', '2017-09-11 11:24:28', '1', '2017-09-11 11:24:52', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100010002', '10001', '0,1,10001,', '请求日志管理', '90', '', '', '', '1', '', '', '0', '1', '2017-09-18 15:05:20', '1', '2017-09-30 10:13:08', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100020001', '100010002', '0,1,10001,100010002,', '日志查看', '60', '/ica/icaApiLog', '', '', '1', 'ica:icaApiLog:view', '', '0', '1', '2017-09-18 15:56:16', '1', '2017-09-30 10:12:57', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100020002', '100010002', '0,1,10001,100010002,', '日志管理开关', '80', '/ica/icaApiLog/changeState', '', '', '1', 'ica:icaApiLog:edit', '', '0', '1', '2017-09-18 15:54:37', '1', '2017-09-30 10:13:27', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('100010003', '10001', '0,1,10001,', '任务调度管理', '120', '/job/job', '', '', '1', '', '', '0', '1', '2017-10-11 14:04:40', '1', '2017-10-11 14:04:40', '', '0');
  
 
-- ----------------------------
-- Table structure for iccn_ica_api_log
-- ----------------------------

CREATE TABLE  IF NOT  EXISTS`iccn_ica_api_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `service_code` varchar(255) DEFAULT NULL COMMENT '服务编码',
  `service_name` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `service_params` text COMMENT '服务参数',
  `db_sql` text COMMENT '数据库sql',
  `respone_template` text COMMENT '返回模板',
  `result` mediumtext COMMENT '返回结果',
  `ip` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `res_duration` varchar(255) DEFAULT NULL COMMENT '响应时长',
  `is_success` int(11) DEFAULT NULL COMMENT '状态  0:失败 1:成功',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `iccn_ica_api_log_create_time` (`create_time`) USING BTREE,
  KEY `iccn_ica_api_log_service_code` (`service_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16382 DEFAULT CHARSET=utf8 COMMENT='ica请求日志表';

-- ----------------------------
-- Records of iccn_ica_api_log
-- ----------------------------

-- ----------------------------
-- Table structure for iccn_ica_db
-- ----------------------------

CREATE TABLE  IF NOT  EXISTS`iccn_ica_db` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `db_id` varchar(255) DEFAULT NULL COMMENT '数据库链接id标识',
  `db_name` varchar(255) DEFAULT NULL COMMENT '数据库链接名称',
  `db_type` varchar(255) DEFAULT NULL COMMENT '数据库类型',
  `db_version` varchar(255) DEFAULT NULL COMMENT '数据库版本',
  `jdbc_driverclassname` varchar(255) DEFAULT NULL COMMENT '数据库驱动',
  `jdbc_url` varchar(255) DEFAULT NULL COMMENT '数据库链接url',
  `jdbc_username` varchar(255) DEFAULT NULL COMMENT '数据库用户名',
  `jdbc_password` varchar(255) DEFAULT NULL COMMENT '数据库密码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` int(11) DEFAULT NULL COMMENT '状态  0:关闭 1:启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`ID`),
  KEY `iccn_ica_db_db_id` (`db_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COMMENT='ica数据库连接表';

-- ----------------------------
-- Records of iccn_ica_db
-- ----------------------------
INSERT INTO `iccn_ica_db` VALUES ('1', '1001', 'T100', 'ORACLE', 'ORACLE12G', 'oracle.jdbc.driver.OracleDriver', 'jdbc:Oracle:thin:@172.31.75.143:1521:topprd', 'dsdemo', 'dsdemo', 'ds1', '0', '2017-09-19 16:05:59', '2017-09-30 10:03:46', '0');
INSERT INTO `iccn_ica_db` VALUES ('2', '1002', 'T100', 'ORACLE', 'ORACLE11G', 'oracle.jdbc.driver.OracleDriver', 'jdbc:Oracle:thin:@172.31.75.143:1521:topprd', 'dsdemo', 'dsdemo', '	数据库', '0', '2017-09-19 16:05:59', '2017-09-19 16:05:59', '0');
INSERT INTO `iccn_ica_db` VALUES ('3', '1003', 'E10', 'SQLSERVER', 'SQLSERVER2008', 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://172.31.75.149:1433; DatabaseName=E10_5.0', 'sa', '88888888', 'E10数据库', '0', '2017-09-19 16:05:59', '2017-10-07 23:43:31', '0');
INSERT INTO `iccn_ica_db` VALUES ('4', '1004', '易飞', 'SQLSERVER', 'SQLSERVER2008', 'com.microsoft.sqlserver.jdbc.SQLServerDriver', 'jdbc:sqlserver://192.168.9.59:1433;DatabaseName=DEMO', 'sa', '238173', '11', '0', '2017-09-19 16:05:59', '2017-10-07 23:23:02', '0');
INSERT INTO `iccn_ica_db` VALUES ('7', '1005', 'GP53', 'ORACLE', 'ORACLE11G', 'oracle.jdbc.driver.OracleDriver', 'jdbc:oracle:thin:@(DESCRIPTION = (ADDRESS = (PROTOCOL = TCP)(HOST = 172.31.75.145)(PORT = 1521)) (CONNECT_DATA = (SERVER = DEDICATED) (SERVICE_NAME = topprod)))', 'sp2', 'sp2', 'TEST', '0', '2017-09-29 14:20:08', '2017-10-13 16:06:30', '0');

-- ----------------------------
-- Table structure for iccn_ica_service
-- ----------------------------

CREATE TABLE  IF NOT  EXISTS`iccn_ica_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `db_id` varchar(255) DEFAULT NULL COMMENT '数据库链接id',
  `service_code` varchar(255) DEFAULT NULL COMMENT '服务编码',
  `service_name` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `service_params` text COMMENT '服务参数',
  `db_sql` text COMMENT '数据库sql',
  `respone_template` text COMMENT '返回模板',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` int(11) DEFAULT NULL COMMENT '状态  0:关闭 1:启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `erp_type` varchar(255) DEFAULT NULL COMMENT 'erp类型',
  PRIMARY KEY (`id`),
  KEY `iccn_ica_service_service_code` (`service_code`) USING BTREE,
  KEY `iccn_ica_service_service_name` (`service_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8 COMMENT='ica系统服务管理表';

-- ----------------------------
-- Records of iccn_ica_service
-- ----------------------------