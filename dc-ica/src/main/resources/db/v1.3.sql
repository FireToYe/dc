alter table `iccn_ica_service`
add  `custom_version` char(255) DEFAULT NULL COMMENT '版本信息',
add  `system_version` varchar(255) DEFAULT NULL COMMENT '系统版本信息',
add  `force_update` char(10) DEFAULT NULL COMMENT '强制更新';

alter table `iccn_ica_api_log`
add  `db_id` char(255) DEFAULT NULL COMMENT '数据库连接id';