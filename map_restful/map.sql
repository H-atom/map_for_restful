/*
 Navicat MySQL Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : map

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 28/02/2024 12:59:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for point
-- ----------------------------
DROP TABLE IF EXISTS `point`;
CREATE TABLE `point`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `lat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `lng` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `open` int NOT NULL,
  `owner` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `detail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `timestamp` timestamp NOT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of point
-- ----------------------------
INSERT INTO `point` VALUES (8, '36.701759', '117.090138', 1, '15628788968', '地点一', '东风,辛祝路,花园路沿线', '山东省济南市历城区辛祝路6号', '2023-05-14 01:20:46', 1);
INSERT INTO `point` VALUES (9, '36.702446', '117.091649', 1, '15628788968', '地点二', '祝甸,东风,花园路沿线', '山东省济南市历城区', '2023-05-14 01:33:20', 1);
INSERT INTO `point` VALUES (10, '36.70168', '117.091384', 1, '15628788968', '地点三', '东风,辛祝路,花园路沿线', '山东省济南市历城区', '2023-05-15 03:12:31', 0);
INSERT INTO `point` VALUES (11, '36.702331', '117.089172', 1, '15628788968', '地点四', '东风,辛祝路,花园路沿线', '山东省济南市历城区花园路商圈辛祝路1号', '2023-05-15 03:14:32', 0);
INSERT INTO `point` VALUES (12, '36.658274', '117.052848', 1, '15628788968', '地点一', '山师地区,文化东路', '山东省济南市历下区', '2023-06-05 01:20:36', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '李光士', '15628788968', '123456', 0);

SET FOREIGN_KEY_CHECKS = 1;
