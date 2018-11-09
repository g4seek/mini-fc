CREATE TABLE `STOREHOUSE` (
  `id` bigint(20) unsigned NOT NULL  COMMENT 'id',
  `name` varchar(32) NOT NULL COMMENT '仓库名称',
  `code` varchar(32) NOT NULL DEFAULT '' COMMENT '仓库编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库信息表';