-- MySQL dump 10.13  Distrib 8.4.7, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: xingmu_pay_0
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
-- Current Database: `xingmu_pay_0`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `xingmu_pay_0` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `xingmu_pay_0`;

--
-- Table structure for table `x_pay_bill_0`
--

DROP TABLE IF EXISTS `x_pay_bill_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_pay_bill_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `pay_number` varchar(64) DEFAULT NULL COMMENT '支付流水号',
  `out_order_no` varchar(64) NOT NULL COMMENT '商户订单号',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付渠道',
  `pay_scene` varchar(64) DEFAULT NULL COMMENT '支付环境',
  `subject` varchar(512) DEFAULT NULL COMMENT '订单标题',
  `trade_number` varchar(256) DEFAULT NULL COMMENT '三方交易凭证号',
  `pay_amount` decimal(10,0) NOT NULL COMMENT '支付金额',
  `pay_bill_type` int NOT NULL COMMENT '支付种类 详细见枚举PayBillType',
  `pay_bill_status` int NOT NULL DEFAULT '1' COMMENT '账单支付状态 详细见枚举PayBillStatus',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '修改时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1：未删除 0：删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_pay_bill_out_order_no_IDX` (`out_order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_pay_bill_0`
--

LOCK TABLES `x_pay_bill_0` WRITE;
/*!40000 ALTER TABLE `x_pay_bill_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_pay_bill_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_pay_bill_1`
--

DROP TABLE IF EXISTS `x_pay_bill_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_pay_bill_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `pay_number` varchar(64) DEFAULT NULL COMMENT '支付流水号',
  `out_order_no` varchar(64) NOT NULL COMMENT '商户订单号',
  `pay_channel` varchar(64) DEFAULT NULL COMMENT '支付渠道',
  `pay_scene` varchar(64) DEFAULT NULL COMMENT '支付环境',
  `subject` varchar(512) DEFAULT NULL COMMENT '订单标题',
  `trade_number` varchar(256) DEFAULT NULL COMMENT '三方交易凭证号',
  `pay_amount` decimal(10,0) NOT NULL COMMENT '支付金额',
  `pay_bill_type` int NOT NULL COMMENT '支付种类 详细见枚举PayBillType',
  `pay_bill_status` int NOT NULL DEFAULT '1' COMMENT '账单支付状态 详细见枚举PayBillStatus',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '修改时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1：未删除 0：删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_pay_bill_out_order_no_IDX` (`out_order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_pay_bill_1`
--

LOCK TABLES `x_pay_bill_1` WRITE;
/*!40000 ALTER TABLE `x_pay_bill_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_pay_bill_1` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_refund_bill_0`
--

DROP TABLE IF EXISTS `x_refund_bill_0`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_refund_bill_0` (
  `id` bigint NOT NULL COMMENT '主键id',
  `out_order_no` varchar(64) NOT NULL COMMENT '商户订单号',
  `pay_bill_id` bigint NOT NULL COMMENT '账单id',
  `refund_amount` decimal(10,0) NOT NULL COMMENT '退款金额',
  `refund_status` int NOT NULL DEFAULT '1' COMMENT '账单退款状态 1：未退款 2：已退款',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `reason` varchar(50) DEFAULT NULL COMMENT '退款原因',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '修改时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1：未删除 0：删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_refund_bill_out_order_no_IDX` (`out_order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_refund_bill_0`
--

LOCK TABLES `x_refund_bill_0` WRITE;
/*!40000 ALTER TABLE `x_refund_bill_0` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_refund_bill_0` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `x_refund_bill_1`
--

DROP TABLE IF EXISTS `x_refund_bill_1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `x_refund_bill_1` (
  `id` bigint NOT NULL COMMENT '主键id',
  `out_order_no` varchar(64) NOT NULL COMMENT '商户订单号',
  `pay_bill_id` bigint NOT NULL COMMENT '账单id',
  `refund_amount` decimal(10,0) NOT NULL COMMENT '退款金额',
  `refund_status` int NOT NULL DEFAULT '1' COMMENT '账单退款状态 1：未退款 2：已退款',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `reason` varchar(50) DEFAULT NULL COMMENT '退款原因',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `edit_time` datetime NOT NULL COMMENT '修改时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态 1：未删除 0：删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `d_refund_bill_out_order_no_IDX` (`out_order_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `x_refund_bill_1`
--

LOCK TABLES `x_refund_bill_1` WRITE;
/*!40000 ALTER TABLE `x_refund_bill_1` DISABLE KEYS */;
/*!40000 ALTER TABLE `x_refund_bill_1` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-03 16:02:12
