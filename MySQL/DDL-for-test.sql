create
database if not exists sql_light_rail;
use
sql_light_rail;
-- Default DataSource


SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '' 主键ID '',
    `name`        varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '' 姓名 '',
    `age`         int(11) NULL DEFAULT NULL COMMENT '' 年龄 '',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '' 邮箱 '',
    `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '' 创建时间 '',
    `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '' 修改时间 '',
    `deleted`     int(1) NULL DEFAULT 0 COMMENT '' 逻辑删除 '',
    `version`     int(255) UNSIGNED NULL DEFAULT 0 COMMENT '' 0 '',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, ''neko'', 18, ''neko@qq.com'', NULL, NULL, 0, 0);
INSERT INTO `user`
VALUES (2, ''doge'', 20, ''doge@gmail.com'', NULL, NULL, 0, 0);
INSERT INTO `user`
VALUES (3, ''Tom'', 28, ''tom@outlook.com'', NULL, NULL, 0, 0);


-- # Multi DataSource - 1
create
database if not exists sql_light_rail_1;
use
sql_light_rail_1;

SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '' 主键ID '',
    `name`        varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '' 姓名 '',
    `age`         int(11) NULL DEFAULT NULL COMMENT '' 年龄 '',
    `email`       varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '' 邮箱 '',
    `create_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '' 创建时间 '',
    `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP (0) COMMENT '' 修改时间 '',
    `deleted`     int(1) NULL DEFAULT 0 COMMENT '' 逻辑删除 '',
    `version`     int(255) UNSIGNED NULL DEFAULT 0 COMMENT '' 0 '',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, ''neko'', 18, ''neko@qq.com'', NULL, NULL, 0, 0);
INSERT INTO `user`
VALUES (2, ''doge'', 20, ''doge@gmail.com'', NULL, NULL, 0, 0);
INSERT INTO `user`
VALUES (3, ''Tom'', 28, ''tom@outlook.com'', NULL, NULL, 0, 0);

