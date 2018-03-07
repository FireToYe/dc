INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100030002', '100010003', '0,1,10001,100010003,', '任务调度日志', '90', '/job/jobLog', '', '', '1', 'job:jobLog:view', '', '0', '1', '2017-11-07 18:16:13', '1', '2017-11-07 18:16:13', '', '0');

CREATE TABLE  IF NOT EXISTS `dc_job_log`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `job_id` varchar(255) DEFAULT NULL COMMENT '任务ID',
  `execute_type` varchar(1) DEFAULT NULL COMMENT '执行方式',
  `group_name` varchar(255) DEFAULT NULL COMMENT '组名-任务名',
  `method` varchar(255) DEFAULT NULL COMMENT '类名+方法名',
  `service_code` varchar(255) DEFAULT NULL COMMENT '服务编码',
  `res_service_code` varchar(255) DEFAULT NULL COMMENT '输出serviceCode',
  `request_params` text COMMENT '请求参数',
  `result` text COMMENT '返回结果',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `res_duration` varchar(255) DEFAULT NULL COMMENT '响应时长',
  `is_success` int(11) DEFAULT NULL COMMENT '状态  0:失败 1:成功',
  `pre_paramter` text COMMENT '上一周期参数',
  `exception` text COMMENT '错误信息',
  PRIMARY KEY (`id`),
  KEY `dc_job_log_service_code` (`service_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='定时调度日志表';

alter table `dc_job`
add  `depend_last` char(1) DEFAULT NULL COMMENT '是否依赖上一周期',
add  `res_service_code` varchar(255) DEFAULT NULL COMMENT '输出serviceCode',
add  `depend_last_time` char(1) DEFAULT NULL COMMENT '依赖上一周期时间',
add  `date_format` varchar(255) DEFAULT NULL COMMENT '日期格式化',
add  `data_parameter_name` varchar(255) DEFAULT NULL COMMENT '时间对应字段名';

