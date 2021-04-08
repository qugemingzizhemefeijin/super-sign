CREATE TABLE `t_admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `createAdminId` bigint(11) NOT NULL COMMENT '录入员id',
  `updateAdminId` bigint(11) NOT NULL COMMENT '修改员id',
  `username` varchar(30) NOT NULL COMMENT '用户',
  `password` char(32) NOT NULL COMMENT '密码',
  `roleId` bigint(10) NOT NULL COMMENT '权限ID',
  `realname` varchar(32) NOT NULL COMMENT '真实姓名',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `mobile` varchar(50) NOT NULL COMMENT '联系方式',
  `avatar` varchar(100) DEFAULT NULL COMMENT '头像',
  `lastDate` datetime DEFAULT NULL COMMENT '最后登录时间',
  `lastip` varchar(32) DEFAULT NULL COMMENT '最后登录IP',
  `status` tinyint(11) NOT NULL COMMENT '是否有效',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `params` text COMMENT '管理员扩展参数',
  `advertiser_id` bigint(10) NOT NULL DEFAULT '0' COMMENT '关联的广告主ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

CREATE TABLE `t_admin_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `adminId` bigint(11) NOT NULL COMMENT '管理员ID',
  `type` smallint(2) NOT NULL COMMENT '记录类型,参看枚举类',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `ip` varchar(32) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`id`),
  KEY `idx_adminId` (`adminId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理员日志';

CREATE TABLE `t_admin_menu` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `createAdminId` bigint(11) NOT NULL COMMENT '录入员id',
  `updateAdminId` bigint(11) NOT NULL COMMENT '修改员id',
  `name` varchar(32) NOT NULL COMMENT '菜单名',
  `ename` varchar(32) DEFAULT NULL COMMENT '别名',
  `showName` varchar(50) NOT NULL DEFAULT '显示的名字',
  `icon` varchar(30) DEFAULT NULL COMMENT '展示的按钮ICON',
  `seat` tinyint(1) NOT NULL COMMENT '0默认，1上侧，2左侧父级，3左侧子级，4列表页上侧，5列表页操作区',
  `menuType` tinyint(3) NOT NULL DEFAULT '0' COMMENT '按钮类型seat=0，3，4，5时候，0调用JS，1打开新窗口,2直接跳转,3弹出窗体，4弹开新窗口window.open',
  `isshowlist` int(1) NOT NULL DEFAULT '0' COMMENT '是否循环显示，针对seat=4或者5，1不参与循环显示，0参与循环显示',
  `position` varchar(1024) DEFAULT NULL COMMENT '链接',
  `showurl` varchar(512) DEFAULT NULL COMMENT '此按钮显示在哪个页面中',
  `jsfunName` varchar(128) DEFAULT NULL COMMENT 'js函数名字',
  `jsfunction` text COMMENT 'js函数方法体',
  `priority` int(4) NOT NULL DEFAULT '0' COMMENT '优先级',
  `parentId` bigint(10) NOT NULL COMMENT '父按钮ID',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态0停用，1正常',
  `resource` text COMMENT '关联的url地址集合，回车换行符分割',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_parentId` (`parentId`,`ename`) USING BTREE,
  KEY `idx_showurl` (`showurl`(128)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台按钮表';

CREATE TABLE `t_admin_role` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `createAdminId` bigint(11) NOT NULL COMMENT '录入员id',
  `updateAdminId` bigint(11) NOT NULL COMMENT '修改员id',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `parentId` bigint(10) NOT NULL COMMENT '父角色ID',
  `priority` int(4) NOT NULL DEFAULT '0' COMMENT '排序优先级',
  `status` tinyint(1) NOT NULL COMMENT '是否有效',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  KEY `idx_parentId` (`parentId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台角色表';

CREATE TABLE `t_admin_role_menu` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `createDate` datetime DEFAULT NULL COMMENT '创建时间',
  `updateDate` datetime DEFAULT NULL COMMENT '修改时间',
  `createAdminId` bigint(11) NOT NULL COMMENT '录入员id',
  `updateAdminId` bigint(11) NOT NULL COMMENT '修改员id',
  `roleId` bigint(11) NOT NULL COMMENT '角色ID',
  `menuId` bigint(11) NOT NULL COMMENT '按钮ID',
  `handle` tinyint(1) NOT NULL COMMENT '权限 0=隐藏，1=可见，2=可见并可操作',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_roleId` (`roleId`,`menuId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色跟按钮的关联表';

CREATE TABLE `t_admin_query_columns` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `admin_id` bigint(11) unsigned NOT NULL COMMENT '用户ID',
  `page_type` tinyint(1) unsigned NOT NULL COMMENT '查询界面类型，参见枚举AdminQueryColumnsPageTypeEnum',
  `show_columns` varchar(1000) NOT NULL DEFAULT '' COMMENT '用户显示列',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_admin_id` (`admin_id`,`page_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='后台管理员查询列设置表';



INSERT INTO t_admin VALUES ('1', '2016-05-07 21:56:25', '2018-12-20 17:36:30', '1', '1', 'starn', '96e79218965eb72c92a549dd5a330112', '1', 'starn', 'tigerjoys@qq.com', '13888888888', '/upload/adm/2018/09/09/MTUzNjQ4MjUwN18xXzY2NV80OTM=.jpg', '2020-01-17 20:11:54', '114.240.83.70', '1', '测试专用。', '{\"left_menu\":0}', '0');

INSERT INTO t_admin_menu VALUES ('1', '2016-05-15 15:39:03', '2017-05-18 16:16:18', '1', '1', '系统设置', 'conf', '系统设置', 'fa-gear', '1', '0', '0', '#', '#', '', null, '1000', '0', '1', '', '');
INSERT INTO t_admin_menu VALUES ('2', '2016-05-15 17:29:15', '2016-06-29 09:55:31', '1', '1', '用户列表', 'admin', '用户列表', '', '2', '0', '0', '/conf/admin/list', '/conf/admin/list', '', null, '0', '1', '1', '/conf/admin/**', '');
INSERT INTO t_admin_menu VALUES ('3', '2016-05-15 17:29:46', '2016-05-16 00:15:58', '1', '1', '运营角色列表', 'role', '运营角色列表', '', '2', '0', '0', '/conf/role/list', '/conf/role/list', '', null, '0', '1', '1', '/conf/role/**', '');
INSERT INTO t_admin_menu VALUES ('4', '2016-05-15 17:31:37', '2016-05-16 00:16:02', '1', '1', '按钮权限列表', 'menu', '按钮权限列表', '', '2', '0', '0', '/conf/menu/list', '/conf/menu/list', '', null, '0', '1', '1', '/conf/menu/**', '');
INSERT INTO t_admin_menu VALUES ('5', '2018-09-18 16:58:29', '2018-09-18 16:58:29', '1', '1', '个人中心', 'user', '个人中心', '', '0', '0', '0', '#', '#', null, null, '0', '0', '1', '/user/profile/**', '');

INSERT INTO t_admin_role VALUES ('1', '2016-05-14 15:47:57', '2016-05-14 15:47:59', '1', '1', '超级管理员', '0', '0', '1', '');
