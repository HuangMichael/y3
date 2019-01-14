-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 192.168.0.145    Database: dev
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_eq_batch_update_bill`
--

DROP TABLE IF EXISTS `t_eq_batch_update_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_eq_batch_update_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicant` varchar(50) DEFAULT NULL,
  `apply_date` varchar(20) DEFAULT NULL,
  `apply_dep` varchar(50) DEFAULT NULL,
  `approver` varchar(10) DEFAULT NULL,
  `bill_content` varchar(100) DEFAULT NULL,
  `data_type` varchar(10) DEFAULT NULL,
  `eq_ids` varchar(200) DEFAULT NULL,
  `handler` varchar(10) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `purpose` varchar(50) DEFAULT NULL,
  `receiver` varchar(10) DEFAULT NULL,
  `eq_class_id` bigint(20) DEFAULT NULL,
  `location_id` bigint(20) DEFAULT NULL,
  `status` varchar(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_eq_batch_update_bill`
--

LOCK TABLES `t_eq_batch_update_bill` WRITE;
/*!40000 ALTER TABLE `t_eq_batch_update_bill` DISABLE KEYS */;
INSERT INTO `t_eq_batch_update_bill` VALUES (1,'黄斌','2017-07-06','测试','张明',NULL,'未更新',NULL,'李琦','000212','设备更新','蔡明',38,NULL,'1'),(2,'黄斌','2017-07-06','综合维修部','张明',NULL,'未更新',NULL,'李琦','109890098','设备更新','蔡明',41,NULL,'1'),(3,'黄斌','2017-07-06','综合维修部','张明',NULL,'未更新',NULL,'李琦','验钞机','设备更新','蔡明',41,NULL,'1'),(4,'黄斌','2017-07-06','综合维修部','张明',NULL,'未更新','24,23,22,21,20','李琦','黑猫','设备更新','蔡明',NULL,NULL,'1'),(5,'黄斌','2017-07-05','综合维修部','张明',NULL,'未更新','1473,1472,1471,1470,1469','李琦','铁牛','设备更新','蔡明',NULL,NULL,'1'),(6,'黄斌','2017-07-05','综合维修部','张明','设备更新','未更新','1473,1472,1471,1470,1469','李琦','铁牛','设备更新','蔡明',41,433,'1'),(7,'黄斌','2017-07-06','综合维修部','张明',NULL,'未更新',NULL,'李琦','维修单申请设备更换','设备更新','蔡明',41,NULL,'1'),(8,'黄斌','2017-07-06','综合维修部','张明',NULL,'未更新',NULL,'李琦','维修单申请设备更换','设备更新','蔡明',41,NULL,'1'),(9,'黄斌','2017-07-06','综合维修部','张明',NULL,'未更新',NULL,'李琦','美的','设备更新测试验钞机','蔡明',41,165,'1'),(10,'测试设备更新','2017-07-06','测试设备更新','测试设备更新',NULL,'未更新',NULL,'测试设备更新','测试设备更新','测试设备更新','测试设备更新',41,165,'1'),(11,'黄斌','2017-07-05','综合维修部','张明','设备更新','未更新','10,','李琦','巴拿马','设备更新','蔡明',41,165,'1');
/*!40000 ALTER TABLE `t_eq_batch_update_bill` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-07-06 16:55:55
