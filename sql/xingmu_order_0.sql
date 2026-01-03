-- MySQL dump 10.13  Distrib 8.4.7, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: xingmu_order_0
-- ------------------------------------------------------
-- Server version	8.4.7

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `xingmu_order_0`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `xingmu_order_0` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xingmu_order_0`;

--
-- Table structure for table `x_order_0`
--

DROP TABLE IF EXISTS `x_order_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_0`
--

LOCK TABLES `x_order_0` WRITE;
/*!40000 ALTER TABLE `x_order_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_order_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_order_1`
--

DROP TABLE IF EXISTS `x_order_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_1`
--

LOCK TABLES `x_order_1` WRITE;
/*!40000 ALTER TABLE `x_order_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_order_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_order_2`
--

DROP TABLE IF EXISTS `x_order_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_2` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_2`
--

LOCK TABLES `x_order_2` WRITE;
/*!40000 ALTER TABLE `x_order_2` DISABLE KEYS */;
INSERT INTO `x_order_2` VALUES (2004133128261017602,531397199520628738,3,'https://s21.ax1x.com/2024/06/06/pkYmcIs.webp',508732468120035330,'冬季恋歌—《请回答1988》韩剧主题曲演唱会','秦乐宫剧院','2024-07-14 19:30:00',1,NULL,NULL,338,NULL,2,'2025-12-25 18:12:27','2025-12-26 16:55:33',NULL,'2025-12-25 18:12:27','2025-12-26 16:55:33',1);
/*!40000 ALTER TABLE `x_order_2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_order_3`
--

DROP TABLE IF EXISTS `x_order_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_3` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `program_item_picture` varchar(1024) DEFAULT NULL COMMENT '节目图片介绍',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `program_title` varchar(512) DEFAULT NULL COMMENT '节目标题',
  `program_place` varchar(100) DEFAULT NULL COMMENT '节目地点',
  `program_show_time` datetime DEFAULT NULL COMMENT '节目演出时间',
  `program_permit_choose_seat` tinyint(1) NOT NULL COMMENT '节目是否允许选座 1:允许选座 0:不允许选座',
  `distribution_mode` varchar(256) DEFAULT NULL COMMENT '配送方式',
  `take_ticket_mode` varchar(256) DEFAULT NULL COMMENT '取票方式',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_order_order_number_IDX` (`order_number`) USING BTREE,
  KEY `user_id_IDX` (`user_id`) USING BTREE,
  KEY `program_id_IDX` (`program_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_3`
--

LOCK TABLES `x_order_3` WRITE;
/*!40000 ALTER TABLE `x_order_3` DISABLE KEYS */;
INSERT INTO `x_order_3` VALUES (2004464610909306882,531397199520628739,3,'https://s21.ax1x.com/2024/06/06/pkYmcIs.webp',508732468120035330,'冬季恋歌—《请回答1988》韩剧主题曲演唱会','秦乐宫剧院','2024-07-14 19:30:00',1,NULL,NULL,338,NULL,2,'2025-12-26 16:09:38','2025-12-26 16:55:33',NULL,'2025-12-26 16:09:39','2025-12-26 16:55:33',1);
/*!40000 ALTER TABLE `x_order_3` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_order_ticket_user_0`
--

DROP TABLE IF EXISTS `x_order_ticket_user_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_ticket_user_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint NOT NULL COMMENT '购票人id',
  `seat_id` bigint NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_ticket_user_0`
--

LOCK TABLES `x_order_ticket_user_0` WRITE;
/*!40000 ALTER TABLE `x_order_ticket_user_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_order_ticket_user_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_order_ticket_user_1`
--

DROP TABLE IF EXISTS `x_order_ticket_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_ticket_user_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint NOT NULL COMMENT '购票人id',
  `seat_id` bigint NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_ticket_user_1`
--

LOCK TABLES `x_order_ticket_user_1` WRITE;
/*!40000 ALTER TABLE `x_order_ticket_user_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_order_ticket_user_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_order_ticket_user_2`
--

DROP TABLE IF EXISTS `x_order_ticket_user_2`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_ticket_user_2` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint NOT NULL COMMENT '购票人id',
  `seat_id` bigint NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_ticket_user_2`
--

LOCK TABLES `x_order_ticket_user_2` WRITE;
/*!40000 ALTER TABLE `x_order_ticket_user_2` DISABLE KEYS */;
INSERT INTO `x_order_ticket_user_2` VALUES (532519869612113920,531397199520628738,3,508732468120035330,531272576749527040,64,'1排4列',9,338,NULL,NULL,2,'2025-12-25 18:12:27','2025-12-26 16:55:33',NULL,'2025-12-25 18:12:27','2025-12-26 16:55:33',1);
/*!40000 ALTER TABLE `x_order_ticket_user_2` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_order_ticket_user_3`
--

DROP TABLE IF EXISTS `x_order_ticket_user_3`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_order_ticket_user_3` (
  `id` bigint NOT NULL COMMENT '主键id',
  `order_number` bigint NOT NULL COMMENT '订单编号',
  `program_id` bigint NOT NULL COMMENT '节目表id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `ticket_user_id` bigint NOT NULL COMMENT '购票人id',
  `seat_id` bigint NOT NULL COMMENT '座位id',
  `seat_info` varchar(100) DEFAULT NULL COMMENT '座位信息',
  `ticket_category_id` bigint DEFAULT NULL COMMENT '节目票档id',
  `order_price` decimal(10,0) DEFAULT NULL COMMENT '订单价格',
  `pay_order_price` decimal(10,0) DEFAULT NULL COMMENT '支付订单价格',
  `pay_order_type` int DEFAULT NULL COMMENT '支付订单方式',
  `order_status` int DEFAULT '1' COMMENT '订单状态 1:未支付 2:已取消 3:已支付 4:已退单',
  `create_order_time` datetime DEFAULT NULL COMMENT '生成订单时间',
  `cancel_order_time` datetime DEFAULT NULL COMMENT '取消订单时间',
  `pay_order_time` datetime DEFAULT NULL COMMENT '支付订单时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `d_order_ticket_user_order_id_IDX` (`order_number`) USING BTREE,
  KEY `d_order_ticket_user_user_id_IDX` (`user_id`) USING BTREE,
  KEY `d_order_ticket_user_ticket_user_id_IDX` (`ticket_user_id`) USING BTREE,
  KEY `d_order_ticket_user_create_order_time_IDX` (`create_order_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='购票人订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_order_ticket_user_3`
--

LOCK TABLES `x_order_ticket_user_3` WRITE;
/*!40000 ALTER TABLE `x_order_ticket_user_3` DISABLE KEYS */;
INSERT INTO `x_order_ticket_user_3` VALUES (533877577493889024,531397199520628739,3,508732468120035330,531272576749527040,65,'1排5列',9,338,NULL,NULL,2,'2025-12-26 16:09:38','2025-12-26 16:55:33',NULL,'2025-12-26 16:09:39','2025-12-26 16:55:33',1);
/*!40000 ALTER TABLE `x_order_ticket_user_3` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-03 16:01:58
