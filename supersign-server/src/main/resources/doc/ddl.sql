DROP TABLE IF EXISTS `t_app`;
CREATE TABLE `t_app` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `app_name` varchar(80) NOT NULL DEFAULT '' COMMENT '应用名称',
  `icon_path` varchar(255) NOT NULL COMMENT 'icon路径',
  `full_icon_path` varchar(255) NOT NULL COMMENT 'fullIcon路径',
  `mbconfig` varchar(255) NOT NULL COMMENT 'mobileconfig路径',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 0：禁用； 1：正常 ',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  unique key `idx_app_name` (`app_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用信息表';

DROP TABLE IF EXISTS `t_app_info`;
CREATE TABLE `t_app_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `app_id` bigint(20) NOT NULL COMMENT '应用ID',
  `app_name` varchar(80) NOT NULL DEFAULT '' COMMENT '应用名称',
  `bundle_id` varchar(200) NOT NULL DEFAULT '' COMMENT '应用bundle Id',
  `version` varchar(50) NOT NULL DEFAULT '' COMMENT '版本号',
  `version_code` varchar(80) NOT NULL DEFAULT '' COMMENT '版本编码',
  `path` varchar(255) NOT NULL COMMENT 'IPA路径',
  `icon_path` varchar(255) NOT NULL COMMENT 'icon路径',
  `full_icon_path` varchar(255) NOT NULL COMMENT 'fullIcon路径',
  `resign_ipa_path` varchar(128) comment '重签名后的ipa地址',
  `resign_plist_path` varchar(128) comment '重签名后的plist地址',
  `resign_mp_path` varchar(128) comment '重签名后的mobileprovision地址',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '用户状态 0：禁用； 1：正常 ',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  unique key `idx_bundle_id` (`bundle_id`),
  key `idx_app_id` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='APP子应用信息表';

DROP TABLE IF EXISTS `t_developer`;
CREATE TABLE `t_developer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `app_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应用ID',
  `app_info_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '绑定应用ID',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '帐号',
  `password` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  `real_limit` int(5) unsigned NOT NULL DEFAULT '100' COMMENT '装机数量上限',
  `virtual_limit` int(5) unsigned NOT NULL DEFAULT '100' COMMENT '虚拟可用数量',
  `install_limit` int(5) unsigned not null default '0' comment '实际已用数量',
  `checked` tinyint(1) unsigned not null default 0 comment '0未登录,1登录成功,2失败',
  PRIMARY KEY (`id`),
  unique key `idx_username` (`username`),
  key `idx_app_id` (`app_id`),
  unique key `idx_app_info_id` (`app_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开发者信息';

drop table if exists `t_developer_cer`;
CREATE TABLE `t_developer_cer` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `developer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '开发者ID',
  `certificate_id` varchar(30) NOT NULL comment '苹果证书ID',
  `public_pem_path` varchar(128) NOT NULL COMMENT '公钥文件地址',
  `private_pem_path` varchar(128) NOT NULL COMMENT '私钥文件地址',
  `expire_time` datetime DEFAULT NULL COMMENT '证书结束时间',
  `status` tinyint(1) unsigned not null DEFAULT '1' COMMENT '0禁用,1.正常',
  PRIMARY KEY (`id`),
  key `idx_developer_id` (`developer_id`),
  unique key `idx_certificate_Id` (`certificate_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开发者证书';

DROP TABLE IF EXISTS `t_install_log`;
CREATE TABLE `t_install_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `app_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应用ID',
  `app_info_Id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'app_info Id',
  `udid` varchar(50) NOT NULL COMMENT 'udid',
  `developer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '开发者ID',
  `cer_Id` bigint(20) UNSIGNED NOT NULL comment '证书ID',
  `status` tinyint(1) not null default 0 comment '状态,参见枚举InstallLogStatusEnums',
  `finish_time` datetime COMMENT '成功/失败时间',
  `ident` tinyint(1) default NULL comment '正在安装的标识,1正在安装/已经安装,NULL安装结束',
  `error` varchar(255) comment '安装失败原因',
  PRIMARY KEY (`id`),
  key `idx_app_id` (`app_id`),
  unique key `idx_app_udid` (`udid`, `ident`),
  key `idx_app_info_Id` (`app_info_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='安装日志';

DROP TABLE if exists `t_udid_success`;
CREATE TABLE `t_udid_success` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `app_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应用ID',
  `app_info_Id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'app_info Id',
  `udid` varchar(50) NOT NULL COMMENT 'udid',
  `developer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '开发者ID',
  `cer_Id` bigint(20) UNSIGNED NOT NULL comment '证书ID',
  `resign_ipa_path` varchar(128) comment '重签名后的ipa地址',
  `resign_plist_path` varchar(128) comment '重签名后的plist地址',
  PRIMARY KEY (`id`),
  unique key `idx_app_udid` (`app_id`, `udid`),
  key `idx_app_info_Id` (`app_info_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UDID安装成功信息';

drop table if exists `t_udid_add_log`;
CREATE TABLE `t_udid_add_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `app_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '应用ID',
  `app_info_Id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'app_info Id',
  `developer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '开发者ID',
  `udid` varchar(50) NOT NULL COMMENT 'udid',
  PRIMARY KEY (`id`),
  unique key `idx_app_udid` (`app_id`, `udid`),
  key `idx_app_info_Id` (`app_info_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='UDID更新日志';

