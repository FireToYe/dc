alter table `iccn_ica_service`
add    `service_group` varchar(255) DEFAULT NULL COMMENT '分组';
update iccn_ica_service set `service_group` = 'srm' where db_id = '1001';
update iccn_ica_service set `service_group` = 'wms' where db_id = '1002';

INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('f4aa9e2c5a16480bb05950aca703d361', 'wms', 'wms', 'iccn_ica_service_group', 'ica服务组名', '20', '0', '1', '2017-11-23 15:29:56', '1', '2017-11-23 15:29:56', '', '0');
INSERT INTO `sys_dict` (`id`, `value`, `label`, `type`, `description`, `sort`, `parent_id`, `create_by`, `create_date`, `update_by`, `update_date`, `remarks`, `del_flag`) VALUES ('17771fcdd5354475b8a5247a5bf142e7', 'srm', 'srm', 'iccn_ica_service_group', 'ica服务组名', '10', '0', '1', '2017-11-23 15:29:47', '1', '2017-11-23 15:29:47', '', '0');
