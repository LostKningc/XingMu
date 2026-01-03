-- MySQL dump 10.13  Distrib 8.4.7, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: xingmu_user_1
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
-- Current Database: `xingmu_user_1`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `xingmu_user_1` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xingmu_user_1`;

--
-- Table structure for table `x_ticket_user_0`
--

DROP TABLE IF EXISTS `x_ticket_user_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_ticket_user_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `rel_name` varchar(256) NOT NULL COMMENT '用户真实名字',
  `id_type` int NOT NULL DEFAULT '1' COMMENT '证件类型 1:身份证 2:港澳台居民居住证 3:港澳居民来往内地通行证 4:台湾居民来往内地通行证 5:护照 6:外国人永久居住证',
  `id_number` varchar(512) NOT NULL COMMENT '证件号码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购票人表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_ticket_user_0`
--

LOCK TABLES `x_ticket_user_0` WRITE;
/*!40000 ALTER TABLE `x_ticket_user_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_ticket_user_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_ticket_user_1`
--

DROP TABLE IF EXISTS `x_ticket_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_ticket_user_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `rel_name` varchar(256) NOT NULL COMMENT '用户真实名字',
  `id_type` int NOT NULL DEFAULT '1' COMMENT '证件类型 1:身份证 2:港澳台居民居住证 3:港澳居民来往内地通行证 4:台湾居民来往内地通行证 5:护照 6:外国人永久居住证',
  `id_number` varchar(512) NOT NULL COMMENT '证件号码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购票人表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_ticket_user_1`
--

LOCK TABLES `x_ticket_user_1` WRITE;
/*!40000 ALTER TABLE `x_ticket_user_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_ticket_user_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_user_0`
--

DROP TABLE IF EXISTS `x_user_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_user_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(256) DEFAULT NULL COMMENT '用户名字',
  `rel_name` varchar(256) DEFAULT NULL COMMENT '用户真实名字',
  `mobile` varchar(512) NOT NULL COMMENT '手机号',
  `gender` int NOT NULL DEFAULT '1' COMMENT '1:男 2:女',
  `password` varchar(512) DEFAULT NULL COMMENT '密码',
  `email_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否邮箱认证 1:已验证 0:未验证',
  `email` varchar(256) DEFAULT NULL COMMENT '邮箱地址',
  `rel_authentication_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否实名认证 1:已验证 0:未验证',
  `id_number` varchar(512) DEFAULT NULL COMMENT '身份证号码',
  `address` varchar(256) DEFAULT NULL COMMENT '收货地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_user_0`
--

LOCK TABLES `x_user_0` WRITE;
/*!40000 ALTER TABLE `x_user_0` DISABLE KEYS */;
INSERT INTO `x_user_0` VALUES (517580650505986049,'a5wwd','Rawe22','9uJ2bryQNy1rO57KGFQ+cw==',1,'xvE0Kap8gxXlD4ETZS+K8w==',0,NULL,0,'xyJagH+w454L/zBHOC75AQ==',NULL,'2025-12-15 16:42:24','2025-12-15 16:42:24',1);
/*!40000 ALTER TABLE `x_user_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_user_1`
--

DROP TABLE IF EXISTS `x_user_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_user_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `name` varchar(256) DEFAULT NULL COMMENT '用户名字',
  `rel_name` varchar(256) DEFAULT NULL COMMENT '用户真实名字',
  `mobile` varchar(512) NOT NULL COMMENT '手机号',
  `gender` int NOT NULL DEFAULT '1' COMMENT '1:男 2:女',
  `password` varchar(512) DEFAULT NULL COMMENT '密码',
  `email_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否邮箱认证 1:已验证 0:未验证',
  `email` varchar(256) DEFAULT NULL COMMENT '邮箱地址',
  `rel_authentication_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否实名认证 1:已验证 0:未验证',
  `id_number` varchar(512) DEFAULT NULL COMMENT '身份证号码',
  `address` varchar(256) DEFAULT NULL COMMENT '收货地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_user_1`
--

LOCK TABLES `x_user_1` WRITE;
/*!40000 ALTER TABLE `x_user_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_user_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_user_email_0`
--

DROP TABLE IF EXISTS `x_user_email_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_user_email_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `email` varchar(512) NOT NULL COMMENT '邮箱',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `email_idx` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户邮箱表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_user_email_0`
--

LOCK TABLES `x_user_email_0` WRITE;
/*!40000 ALTER TABLE `x_user_email_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_user_email_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_user_email_1`
--

DROP TABLE IF EXISTS `x_user_email_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_user_email_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `email` varchar(512) NOT NULL COMMENT '邮箱',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `email_idx` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户邮箱表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_user_email_1`
--

LOCK TABLES `x_user_email_1` WRITE;
/*!40000 ALTER TABLE `x_user_email_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_user_email_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_user_mobile_0`
--

DROP TABLE IF EXISTS `x_user_mobile_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_user_mobile_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `mobile` varchar(512) NOT NULL COMMENT '手机号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `mobile_idx` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户手机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_user_mobile_0`
--

LOCK TABLES `x_user_mobile_0` WRITE;
/*!40000 ALTER TABLE `x_user_mobile_0` DISABLE KEYS */;
INSERT INTO `x_user_mobile_0` VALUES (508732468120035335,508732468120035334,'4Qme0A37znzK0s1wEP7z4w==','2025-12-09 17:49:24','2025-12-09 17:49:24',1),(509160779438702593,509160779438702592,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:33:05','2025-12-10 00:33:05',1),(509160779438702595,509160779438702594,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:33:46','2025-12-10 00:33:46',1),(509160779438702597,509160779438702596,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:33:48','2025-12-10 00:33:48',1),(509160779438702599,509160779438702598,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:33:50','2025-12-10 00:33:50',1),(509160779438702601,509160779438702600,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:33:51','2025-12-10 00:33:51',1),(509166105198157825,509166105198157824,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:36:14','2025-12-10 00:36:14',1),(509168235501944833,509168235501944832,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:38:20','2025-12-10 00:38:20',1),(509182700951805953,509182700951805952,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:52:19','2025-12-10 00:52:19',1),(509182700951805955,509182700951805954,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:52:22','2025-12-10 00:52:22',1),(509185037414023169,509185037414023168,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:54:37','2025-12-10 00:54:37',1),(509185037414023171,509185037414023170,'4Qme0A37znzK0s1wEP7z4w==','2025-12-10 00:54:39','2025-12-10 00:54:39',1),(509749069699219457,509749069699219456,'VdS/FIbEXyE2lKaxpVrXnw==','2025-12-10 10:02:26','2025-12-10 10:02:26',1);
/*!40000 ALTER TABLE `x_user_mobile_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_user_mobile_1`
--

DROP TABLE IF EXISTS `x_user_mobile_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_user_mobile_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `mobile` varchar(512) NOT NULL COMMENT '手机号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '编辑时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 0:删除',
  PRIMARY KEY (`id`),
  KEY `mobile_idx` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户手机表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_user_mobile_1`
--

LOCK TABLES `x_user_mobile_1` WRITE;
/*!40000 ALTER TABLE `x_user_mobile_1` DISABLE KEYS */;
INSERT INTO `x_user_mobile_1` VALUES (508732468120035329,508732468120035328,'6tsMlKuhlpFXnB+ePfWcYw==','2025-12-09 17:44:25','2025-12-09 17:44:25',1),(508732468120035331,508732468120035330,'6tsMlKuhlpFXnB+ePfWcYw==','2025-12-09 17:48:05','2025-12-09 17:48:05',1),(517580650505986050,517580650505986049,'9uJ2bryQNy1rO57KGFQ+cw==','2025-12-15 16:42:24','2025-12-15 16:42:24',1);
/*!40000 ALTER TABLE `x_user_mobile_1` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-03 16:02:34
