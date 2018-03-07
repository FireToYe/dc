INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100030001', '100010003', '0,1,10001,100010003,', '任务列表', '60', '/job/job', '', '', '1', '', '', '0', '1', '2017-10-11 14:06:51', '1', '2017-10-11 14:06:51', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000300010001', '1000100030001', '0,1,10001,100010003,1000100030001,', '查看权限', '30', '', '', '', '0', 'job:job:view', '', '0', '1', '2017-10-11 14:05:24', '1', '2017-10-11 14:07:42', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000300010002', '1000100030001', '0,1,10001,100010003,1000100030001,', '修改权限', '60', '', '', '', '0', 'job:job:edit', '', '0', '1', '2017-10-11 14:08:44', '1', '2017-10-11 14:08:44', '', '0');
 
-- ----------------------------
-- Table structure for dc_job
-- ----------------------------

CREATE TABLE  IF NOT  EXISTS`dc_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_name` varchar(255) DEFAULT NULL COMMENT '任务名字',
  `job_group` varchar(255) DEFAULT NULL COMMENT '任务组名',
  `detail` varchar(255) DEFAULT NULL COMMENT '描述',
  `cron_expression` varchar(255) DEFAULT NULL COMMENT '执行cron表达式',
  `job_state` varchar(255) NOT NULL DEFAULT '0' COMMENT '状态：1:启动  0：停止',
  `is_concurrent` varchar(255) DEFAULT NULL COMMENT '是否有状态',
  `execute_type` varchar(255) DEFAULT NULL COMMENT '执行方式',
  `parameter` text COMMENT '执行参数',
  `spring_id` varchar(255) DEFAULT NULL COMMENT 'springBeanId',
  `bean_class_name` varchar(255) DEFAULT NULL COMMENT 'bean的包名+类名',
  `method_name` varchar(255) DEFAULT NULL COMMENT '实现方法名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `service_code` varchar(255) DEFAULT NULL COMMENT '请求serviceCode',
  PRIMARY KEY (`id`),
  KEY `sys_job_job_name` (`job_name`) USING BTREE,
  KEY `sys_job_job_group` (`job_group`) USING BTREE,
  KEY `sys_job_job_state` (`job_state`) USING BTREE,
  KEY `sys_job_is_concurrent` (`is_concurrent`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='定时任务管理列表';

-- ----------------------------
-- Records of dc_job
-- ----------------------------
INSERT INTO `dc_job` VALUES ('1', '测试', '测试', '测试', '0/20 * * * * ?', '0', '1', '1', '{\r\n    \"std_data\": {\r\n        \"parameter\": {\r\n            \"ent\": \"99\",\r\n            \"site\": \"DSCNJ\",\r\n            \"lang\": \"zh_CN\",\r\n            \"appmodule\": \"B007\",\r\n            \"docNo\": \"\",\r\n            \"itemNo\": \"\",\r\n            \"itemName\": \"\",\r\n            \"supplierNo\": \"\",\r\n            \"customerNo\": \"\",\r\n            \"employeeNo\": \"\",\r\n            \"departmentNo\": \"\",\r\n            \"warehouseNo\": \"Z1W1\",\r\n            \"pagesize\": \"10\",\r\n            \"dateBegin\": \"2015/01/01\",\r\n            \"dateEnd\": \"2018/01/01\"\r\n        }\r\n    }\r\n}', 'jobService', 'com.zhilink.srm.manager.modules.job.service.JobService', 'test', null, '2017-10-20 11:10:35', '0', 'listGet');
INSERT INTO `dc_job` VALUES ('7', 'test2', 'test', '新增测试', '0/50 * * * * ?', '0', '1', '2', '', 'jobService', '', 'test1', null, '2017-10-13 11:18:25', '0', '');
