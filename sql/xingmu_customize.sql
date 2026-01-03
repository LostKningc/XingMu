-- MySQL dump 10.13  Distrib 8.4.7, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: xingmu_customize
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
-- Current Database: `xingmu_customize`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `xingmu_customize` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xingmu_customize`;

--
-- Table structure for table `x_api_data`
--

DROP TABLE IF EXISTS `x_api_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_api_data` (
  `id` bigint NOT NULL COMMENT '主键id',
  `head_version` varchar(32) DEFAULT NULL COMMENT '请求版本',
  `api_address` varchar(32) DEFAULT NULL COMMENT '客户端ip',
  `api_method` varchar(32) DEFAULT NULL COMMENT '请求方法',
  `api_body` varchar(200) DEFAULT NULL COMMENT '请求体',
  `api_params` varchar(100) DEFAULT NULL COMMENT '请求参数',
  `api_url` varchar(100) DEFAULT NULL COMMENT '请求路径',
  `call_day_time` varchar(64) DEFAULT NULL COMMENT '按天维度记录请求时间',
  `call_hour_time` varchar(64) DEFAULT NULL COMMENT '按小时维度记录请求时间',
  `call_minute_time` varchar(64) DEFAULT NULL COMMENT '按分钟维度记录请求时间',
  `call_second_time` varchar(64) DEFAULT NULL COMMENT '按秒维度记录请求时间',
  `type` int DEFAULT NULL COMMENT 'api规则生效类型 1一般规则 2深度规则',
  `status` int DEFAULT '1' COMMENT '状态 1:未删除 0:删除(默认1)',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_api_address` (`api_address`) USING BTREE,
  KEY `idx_api_url` (`api_url`) USING BTREE,
  KEY `idx_call_day_time` (`call_day_time`) USING BTREE,
  KEY `idx_call_hour_time` (`call_hour_time`) USING BTREE,
  KEY `idx_call_minute_time` (`call_minute_time`) USING BTREE,
  KEY `idx_call_second_time` (`call_second_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='api执行表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_api_data`
--

LOCK TABLES `x_api_data` WRITE;
/*!40000 ALTER TABLE `x_api_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_api_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_depth_rule`
--

DROP TABLE IF EXISTS `x_depth_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_depth_rule` (
  `id` bigint NOT NULL COMMENT 'id',
  `start_time_window` varchar(64) NOT NULL COMMENT '[限制开始时间]',
  `end_time_window` varchar(64) NOT NULL COMMENT '[限制结束时间]',
  `stat_time` int NOT NULL COMMENT '统计时间',
  `stat_time_type` int NOT NULL COMMENT '统计时间类型 1:秒 2:分钟',
  `threshold` int NOT NULL COMMENT '调用限制阈值',
  `effective_time` int NOT NULL COMMENT '限制时间',
  `effective_time_type` int NOT NULL COMMENT '限制时间类型 1:秒 2:分钟',
  `limit_api` text COMMENT '限制路径 逗号分割',
  `message` varchar(64) DEFAULT NULL COMMENT '限制访问提示语',
  `status` tinyint DEFAULT '1' COMMENT '状态标识1.正常 0. 禁用  (默认1)',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='深度调用限制规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_depth_rule`
--

LOCK TABLES `x_depth_rule` WRITE;
/*!40000 ALTER TABLE `x_depth_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_depth_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_rule`
--

DROP TABLE IF EXISTS `x_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_rule` (
  `id` bigint NOT NULL COMMENT 'id',
  `stat_time` int NOT NULL COMMENT '统计时间',
  `stat_time_type` int NOT NULL COMMENT '统计时间类型 1:秒 2:分钟',
  `threshold` int NOT NULL COMMENT '调用限制阈值',
  `effective_time` int NOT NULL COMMENT '限制时间',
  `effective_time_type` int NOT NULL COMMENT '限制时间类型 1:秒 2:分钟',
  `limit_api` text COMMENT '限制路径 逗号分割',
  `message` varchar(64) DEFAULT NULL COMMENT '限制访问提示语',
  `status` tinyint DEFAULT '1' COMMENT '状态标识1.正常 0. 禁用  (默认1)',
  `edit_time` datetime DEFAULT NULL COMMENT '编辑时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='调用限制规则表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_rule`
--

LOCK TABLES `x_rule` WRITE;
/*!40000 ALTER TABLE `x_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_rule` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-03 16:01:51
