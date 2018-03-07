INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100030001', '100010003', STRINGDECODE('0,1,10001,100010003,'), STRINGDECODE('任务列表'), '60', STRINGDECODE('/job/job'), '', '', '1', '', '', '0', '1', '2017-10-11 14:06:51', '1', '2017-10-11 14:06:51', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000300010001', '1000100030001', STRINGDECODE('0,1,10001,100010003,1000100030001,'), STRINGDECODE('查看权限'), '30', '', '', '', '0', 'job:job:view', '', '0', '1', '2017-10-11 14:05:24', '1', '2017-10-11 14:07:42', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000300010002', '1000100030001', STRINGDECODE('0,1,10001,100010003,1000100030001,'), STRINGDECODE('修改权限'), '60', '', '', '', '0', 'job:job:edit', '', '0', '1', '2017-10-11 14:08:44', '1', '2017-10-11 14:08:44', '', '0');
 
-- ----------------------------
-- Table structure for dc_job
-- ----------------------------

CREATE TABLE  IF NOT  EXISTS`dc_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT STRINGDECODE('主键'),
  `job_name` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('任务名字'),
  `job_group` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('任务组名'),
  `detail` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('描述'),
  `cron_expression` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('执行cron表达式'),
  `job_state` varchar(255) NOT NULL DEFAULT '0' COMMENT STRINGDECODE('状态：1:启动  0：停止'),
  `is_concurrent` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('是否有状态'),
  `execute_type` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('执行方式'),
  `parameter` text COMMENT STRINGDECODE('执行参数'),
  `spring_id` varchar(255) DEFAULT NULL COMMENT 'springBeanId',
  `bean_class_name` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('bean的包名+类名'),
  `method_name` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('实现方法名'),
  `create_time` datetime DEFAULT NULL COMMENT STRINGDECODE('创建时间'),
  `update_time` datetime DEFAULT NULL COMMENT STRINGDECODE('更新时间'),
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT STRINGDECODE('删除标记'),
  `service_code` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('请求serviceCode'),
  PRIMARY KEY (`id`)    
) ;

-- ----------------------------
-- Records of dc_job
-- ----------------------------
INSERT INTO `dc_job` VALUES ('1', STRINGDECODE('测试'), STRINGDECODE('测试'), STRINGDECODE('测试'), STRINGDECODE('0/20 * * * * ?'), '0', '1', '1', STRINGDECODE('{\r\n    "std_data": {\r\n        "parameter": {\r\n            "ent": "99",\r\n            "site": "DSCNJ",\r\n            "lang": "zh_CN",\r\n            "appmodule": "B007",\r\n            "docNo": "",\r\n            "itemNo": "",\r\n            "itemName": "",\r\n            "supplierNo": "",\r\n            "customerNo": "",\r\n            "employeeNo": "",\r\n            "departmentNo": "",\r\n            "warehouseNo": "Z1W1",\r\n            "pagesize": "10",\r\n            "dateBegin": "2015/01/01",\r\n            "dateEnd": "2018/01/01"\r\n        }\r\n    }\r\n}'), 'jobService', 'com.zhilink.srm.manager.modules.job.service.JobService', 'test', null, '2017-10-20 11:10:35', '0', 'listGet');
INSERT INTO `dc_job` VALUES ('7', 'test2', 'test', STRINGDECODE('新增测试'), STRINGDECODE('0/50 * * * * ?'), '0', '1', '2', '', 'jobService', '', 'test1', null, '2017-10-13 11:18:25', '0', '');



INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100030002', '100010003', STRINGDECODE('0,1,10001,100010003,'), STRINGDECODE('任务调度日志'), '90', STRINGDECODE('/job/jobLog'), '', '', '1', 'job:jobLog:view', '', '0', '1', '2017-11-07 18:16:13', '1', '2017-11-07 18:16:13', '', '0');

CREATE TABLE  IF NOT EXISTS `dc_job_log`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT STRINGDECODE('主键'),
  `job_id` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('任务ID'),
  `execute_type` varchar(1) DEFAULT NULL COMMENT STRINGDECODE('执行方式'),
  `group_name` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('组名-任务名'),
  `method` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('类名+方法名'),
  `service_code` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('服务编码'),
  `res_service_code` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('输出serviceCode'),
  `request_params` text COMMENT STRINGDECODE('请求参数'),
  `result` text COMMENT STRINGDECODE('返回结果'),
  `start_time` datetime DEFAULT NULL COMMENT STRINGDECODE('开始时间'),
  `end_time` datetime DEFAULT NULL COMMENT STRINGDECODE('结束时间'),
  `res_duration` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('响应时长'),
  `is_success` int(11) DEFAULT NULL COMMENT STRINGDECODE('状态  0:失败 1:成功'),
  `pre_paramter` text COMMENT STRINGDECODE('上一周期参数'),
  `exception` text COMMENT STRINGDECODE('错误信息'),
  PRIMARY KEY (`id`) 
) ;

alter table `dc_job`
add  `depend_last` char(1) DEFAULT NULL COMMENT STRINGDECODE('是否依赖上一周期');
alter table `dc_job`
add  `res_service_code` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('输出serviceCode');
alter table `dc_job`
add  `depend_last_time` char(1) DEFAULT NULL COMMENT STRINGDECODE('依赖上一周期时间');
alter table `dc_job`
add  `date_format` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('日期格式化');
alter table `dc_job`
add  `data_parameter_name` varchar(255) DEFAULT NULL COMMENT STRINGDECODE('时间对应字段名');
