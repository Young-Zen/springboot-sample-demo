-- Flyway 将 SQL 文件分为 Versioned 、Repeatable 和 Undo 三种：
-- Versioned 用于版本升级, 每个版本有唯一的版本号并只能执行一次.
-- Repeatable 可重复执行, 当 Flyway检测到 Repeatable 类型的 SQL 脚本的 checksum 有变动, Flyway 就会重新应用该脚本. 它并不用于版本更新, 这类的 migration 总是在 Versioned 执行之后才被执行。
-- Undo 用于撤销具有相同版本的版本化迁移带来的影响。但是该回滚过于粗暴，过于机械化，一般不推荐使用。一般建议使用 Versioned 模式来解决。
-- 这三种的命名规则如下：Versioned、Undo：Prefix+Version+Separator+Description；Repeatable：Prefix+Separator+Description
-- Prefix 可配置，前缀标识，默认值 V 表示 Versioned, R 表示 Repeatable, U 表示 Undo
-- Version 标识版本号, 由一个或多个数字构成, 数字之间的分隔符可用点 . 或下划线 _
-- Separator 可配置, 用于分隔版本标识与描述信息, 默认为两个下划线 __
-- Description 描述信息, 文字之间可以用下划线 _ 或空格 分隔
-- Suffix 可配置, 后续标识, 默认为 .sql

CREATE TABLE IF NOT EXISTS `t_boot_demo` (
    `pk_demo_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `name` varchar(64) DEFAULT NULL COMMENT '昵称',
    `age` tinyint(3) unsigned DEFAULT NULL COMMENT '年龄',
    `account` decimal(8,2) DEFAULT NULL COMMENT '账户余额',
    `create_time` datetime DEFAULT NULL COMMENT '创建时间',
    `ts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`pk_demo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='demo';

CREATE TABLE IF NOT EXISTS `t_person` (
    `pk_person_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `name` varchar(64) DEFAULT NULL,
    `age` tinyint(3) unsigned DEFAULT NULL,
    `birthday` date DEFAULT NULL,
    `account` decimal(16,2) NOT NULL DEFAULT '0.00',
    `is_deleted` tinyint(1) unsigned DEFAULT '0',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`pk_person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_user` (
    `pk_user_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `user_name` varchar(64) DEFAULT NULL,
    `password` varchar(1024) NOT NULL,
    `birthday` date DEFAULT NULL,
    `phone` char(11) DEFAULT NULL,
    `email` varchar(32) DEFAULT NULL,
    `is_disabled` tinyint(1) DEFAULT '0',
    `is_deleted` tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`pk_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_role` (
    `pk_role_id` int(11) NOT NULL AUTO_INCREMENT,
    `role_name` varchar(32) DEFAULT NULL,
    `role_name_zh` varchar(32) DEFAULT NULL,
    `is_disabled` tinyint(1) DEFAULT '0',
    `is_deleted` tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`pk_role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_user_role` (
    `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) DEFAULT NULL,
    `role_id` int(11) DEFAULT NULL,
    `is_deleted` tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_menu` (
    `pk_menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `title` varchar(32) DEFAULT NULL,
    `url` varchar(128) DEFAULT NULL,
    `parent_id` bigint(20) DEFAULT NULL,
    `is_deleted` tinyint(1) DEFAULT '0',
    `is_disabled` tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`pk_menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `t_role_menu` (
    `pk_id` bigint(20) NOT NULL AUTO_INCREMENT,
    `role_id` int(11) DEFAULT NULL,
    `menu_id` bigint(20) DEFAULT NULL,
    `is_deleted` tinyint(1) DEFAULT '0',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`pk_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
