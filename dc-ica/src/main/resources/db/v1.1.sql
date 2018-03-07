INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100020003', '100010002', '0,1,10001,100010002,', '接口服务日志', '70', '/rfa/rfaServiceLog', '', '', '1', 'rfa:rfaServiceLog:view', '', '0', '1', '2017-11-17 15:47:55', '1', '2017-11-17 16:02:09', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100010004', '100010001', '0,1,10001,100010001,', '接口地址管理', '85', '/rfa/rfaUrl', '', '', '1', '', '', '0', '1', '2017-11-16 18:28:53', '1', '2017-11-17 16:01:56', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100040002', '1000100010004', '0,1,10001,100010001,1000100010004,', '修改权限', '60', '', '', '', '1', 'rfa:rfaUrl:edit', '', '0', '1', '2017-11-16 18:30:25', '1', '2017-11-16 18:30:25', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100040001', '1000100010004', '0,1,10001,100010001,1000100010004,', '查看权限', '30', '', '', '', '0', 'rfa:rfaUrl:view', '', '0', '1', '2017-11-16 18:30:09', '1', '2017-11-16 18:30:09', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100030002', '1000100010003', '0,1,10001,100010001,1000100010003,', '修改权限', '60', '', '', '', '0', 'rfa:rfaService:edit', '', '0', '1', '2017-11-15 14:12:35', '1', '2017-11-16 18:27:40', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('1000100010003', '100010001', '0,1,10001,100010001,', '接口服务管理', '90', '/rfa/rfaService', '', '', '1', '', '', '0', '1', '2017-11-15 14:10:50', '1', '2017-11-16 18:27:39', '', '0');
INSERT INTO `sys_menu` (`id`, `parent_id`, `parent_ids`, `name`, `sort`, `href`, `target`, `icon`, `is_show`, `permission`, `code`, `menu_type`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('10001000100030001', '1000100010003', '0,1,10001,100010001,1000100010003,', '查看权限', '30', '', '', '', '0', 'rfa:rfaService:view', '', '0', '1', '2017-11-15 14:12:15', '1', '2017-11-16 18:27:39', '', '0');


CREATE TABLE  IF NOT  EXISTS `iccn_rfa_service` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url_id` varchar(50) DEFAULT NULL COMMENT 'url链接id',
  `service_code` varchar(255) DEFAULT NULL COMMENT '服务编码',
  `service_name` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `params_resolve` text COMMENT '参数解析',
  `service_params` text COMMENT '服务参数',
  `respone_template` text COMMENT '返回模板',
  `respone_example` text COMMENT '返回示范',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `headers` varchar(1024) DEFAULT NULL COMMENT '请求头信息',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方式',
  `content_type` varchar(11) DEFAULT NULL COMMENT '解析类型',
  `status` int(11) DEFAULT NULL COMMENT '状态  0:关闭 1:启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `iccn_rfa_service_service_code` (`service_code`) USING BTREE,
  KEY `iccn_rfa_service_service_name` (`service_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8 COMMENT='rfa系统服务管理表';

CREATE TABLE  IF NOT  EXISTS `iccn_rfa_service_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url_id` varchar(50) DEFAULT NULL COMMENT 'url链接id',
  `service_code` varchar(255) DEFAULT NULL COMMENT '服务编码',
  `service_name` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `request_params` text COMMENT '接口请求参数',
  `service_params` text COMMENT '服务参数',
  `result` text COMMENT '返回结果',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `url` varchar(255) DEFAULT NULL COMMENT '请求地址',
  `headers` text COMMENT '请求头信息',
  `method` varchar(255) DEFAULT NULL COMMENT '请求方式',
  `content_type` varchar(11) DEFAULT NULL COMMENT '解析类型',
  `is_success` varchar(11) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip地址',
  `res_duration` varchar(255) DEFAULT NULL COMMENT '耗时',
  `response_result` text COMMENT '接口返回报文',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=219 DEFAULT CHARSET=utf8 COMMENT='rfa系统服务请求日志表';

CREATE TABLE  IF NOT  EXISTS `iccn_rfa_url` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url_id` varchar(255) DEFAULT NULL COMMENT 'url链接id标识',
  `url` varchar(255) DEFAULT NULL COMMENT '地址',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` varchar(1) DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8 COMMENT='rfa链接管理表';