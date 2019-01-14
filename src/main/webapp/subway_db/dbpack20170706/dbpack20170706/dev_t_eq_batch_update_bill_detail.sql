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
-- Table structure for table `t_eq_batch_update_bill_detail`
--

DROP TABLE IF EXISTS `t_eq_batch_update_bill_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_eq_batch_update_bill_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `memo` varchar(100) DEFAULT NULL,
  `bill_id` bigint(20) DEFAULT NULL,
  `equipments_id` bigint(20) DEFAULT NULL,
  `status` varchar(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `FK_ojjvlcelr0e1vcgornlgy58b8` (`equipments_id`),
  CONSTRAINT `FK_ojjvlcelr0e1vcgornlgy58b8` FOREIGN KEY (`equipments_id`) REFERENCES `t_equipments` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_eq_batch_update_bill_detail`
--

LOCK TABLES `t_eq_batch_update_bill_detail` WRITE;
/*!40000 ALTER TABLE `t_eq_batch_update_bill_detail` DISABLE KEYS */;
INSERT INTO `t_eq_batch_update_bill_detail` VALUES (1,'综合维修部2017-07-05黄斌申请设备更新',6,1473,'0'),(2,'综合维修部2017-07-05黄斌申请设备更新',6,1472,'0'),(3,'综合维修部2017-07-05黄斌申请设备更新',6,1471,'0'),(4,'综合维修部2017-07-05黄斌申请设备更新',6,1470,'0'),(5,'综合维修部2017-07-05黄斌申请设备更新',6,1469,'0'),(6,'综合维修部2017-07-05黄斌申请设备更新',11,10,'0');
/*!40000 ALTER TABLE `t_eq_batch_update_bill_detail` ENABLE KEYS */;
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
