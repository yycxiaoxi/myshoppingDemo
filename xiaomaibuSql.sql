-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: xiaomaibu
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `admin` (
  `id` varchar(64) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `pwd` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ddanxiz`
--

DROP TABLE IF EXISTS `ddanxiz`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ddanxiz` (
  `xizehao` varchar(64) NOT NULL,
  `gid` varchar(64) DEFAULT NULL,
  `orderno` varchar(64) DEFAULT NULL,
  `gnum` int DEFAULT NULL,
  `gprice` float DEFAULT NULL,
  `total` float DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`xizehao`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ddanxiz`
--

LOCK TABLES `ddanxiz` WRITE;
/*!40000 ALTER TABLE `ddanxiz` DISABLE KEYS */;
INSERT INTO `ddanxiz` VALUES ('03c00a83-b528-4a0a-baac-6d176acb7e21','45','545c162f-6a74-43bf-bfcc-1640c67dcb97',1,10,10,'1'),('052c2b28-9801-412b-9c29-76979551ab2f','1','11be393d-8840-4af9-8a2a-40f6d3399866',1,10,10,'0'),('0cba6798-c9e1-4bc8-9d11-0e14e847d8bc','45','4dd1eacd-b915-41c9-9a0c-63cc7943f6e0',1,10,10,'0'),('235b8f1c-6f45-45ef-97f5-8458b63d2f38','45','961aa5c0-1040-4f8d-9331-3c9006833b92',1,10,10,'0'),('23656acb-c889-48be-b0d2-3f00b5bfabc7','456','59de0eff-b575-4d83-8865-6bb140ba05cc',1,10,10,'0'),('2bf1b765-fea3-484b-a4bc-b6d41ee2cf6e','45','8de74584-a5c5-4607-8440-4d77061488a4',1,10,10,'0'),('2eada91e-c703-41a6-8818-2d0f2c1de668','45','08379447-f768-4343-8151-a43db601ed03',1,10,10,'0'),('319960f0-6db5-4d66-9b0f-423a808bddc3','45','d0af4daf-fa70-4e56-bc4b-80b6fde5218d',1,10,10,'0'),('33cd6bdd-4867-43f6-90ba-850c48b43ff1','1','5575ec6d-2bc2-4236-a992-0011af7e2fb3',1,10,10,'0'),('34419c37-635b-4e49-a81c-30f56091fcda','455','8a4bd5b6-bc16-44c8-81a6-4d8d0f38dc1c',2,10,20,'1'),('3763f81f-1b45-4866-a2c4-1b49853e3922','455','59de0eff-b575-4d83-8865-6bb140ba05cc',1,10,10,'0'),('3daeaa3d-1705-4145-b08a-d19ea41c996e','455','238f344a-a9f3-4de7-a9e7-d59db00d3ac5',1,10,10,'0'),('3f0dbc29-d16d-4e18-805b-d69150208e44','1','f4a27b69-6c69-483e-b7ca-44333882d5fe',1,10,10,'0'),('4555c992-f05c-43d3-80e7-2703f8727b2a','456','74481046-50a9-4984-a818-2b380f2acacc',1,10,10,'0'),('45fbc92b-c77f-4ab8-a932-6d8221309abf','1','13d84689-2799-46c5-96a7-85d3b9ee8084',1,10,10,'0'),('4b0ba45d-a0b3-48c8-b520-90d0ab791b06','45','f4a27b69-6c69-483e-b7ca-44333882d5fe',1,10,10,'0'),('51cea543-ca50-4e5e-ac35-65082423158c','455','4dd1eacd-b915-41c9-9a0c-63cc7943f6e0',1,10,10,'0'),('52e221f4-1a6a-469c-8eb6-b72c98c46888','456','4dd1eacd-b915-41c9-9a0c-63cc7943f6e0',1,10,10,'0'),('54b76048-1e03-4061-86ff-878a262f43ae','1','45bb1dff-ff35-4549-95db-dae9b84acf7e',1,10,10,'1'),('5e84956a-5fd3-45b5-ad0a-c3b7cc601864','1','4dd1eacd-b915-41c9-9a0c-63cc7943f6e0',1,10,10,'0'),('684e83f5-221f-4981-926e-b8f19ac29d62','456','d1ec424d-52c6-442d-8ada-c76d30f52cde',1,10,10,'0'),('6f5474c0-f30a-4df0-b99d-479e18cd0f8f','1','5de7ea07-f7b2-49e8-8e8d-843c3c18ed68',1,10,10,'0'),('7d3005d5-b79f-4a76-8959-0306755e2db8','456','7e97aad1-5951-4c92-8f30-b69874faf49e',1,10,10,'0'),('82036c10-1be2-4694-a156-05d26f8e1582','1','7c8bb1ee-4376-440c-accc-8da925be15db',1,10,10,'0'),('8bfb73e3-6054-4b95-aad3-a785481f356b','456','8a4bd5b6-bc16-44c8-81a6-4d8d0f38dc1c',1,10,10,'1'),('8e5222a6-14c3-49c1-b76c-72b7ec1a2fde','45','11be393d-8840-4af9-8a2a-40f6d3399866',1,10,10,'0'),('92116b7c-1acf-4397-97cb-c8e8d566ff24','455','f6c9acc3-31ac-4523-a2e8-5825d9e87af0',1,10,10,'0'),('ac1fdafe-d09b-4bfe-a0d3-f6ece1dd8eb5','45','f6c9acc3-31ac-4523-a2e8-5825d9e87af0',1,10,10,'0'),('b4f983ce-526f-426e-bc01-4317d0288c8c','45','fc8dea59-ed26-4d6b-b283-b07a4c57aa96',1,10,10,'0'),('bb27ac7e-63b6-4e72-9dc9-9eeb745ccad1','45','d1ec424d-52c6-442d-8ada-c76d30f52cde',1,10,10,'0'),('bc96536f-824e-43e7-9f08-694284fe2891','456','1cdeeae6-196e-4de9-8a49-fdb390fb56d3',1,10,10,'0'),('c10aa5e0-fc60-4577-a932-615954f2e121','1','d1ec424d-52c6-442d-8ada-c76d30f52cde',1,10,10,'0'),('c165cab9-9e55-4889-9b30-d8c5d756732e','455','d1ec424d-52c6-442d-8ada-c76d30f52cde',1,10,10,'0'),('cf24be79-3c15-46a4-9042-7593c1ac04d7','1','961aa5c0-1040-4f8d-9331-3c9006833b92',1,10,10,'0'),('d2c0f618-2abf-4e8a-8f8b-4468ed47b8b3','455','1cdeeae6-196e-4de9-8a49-fdb390fb56d3',2,10,20,'0'),('da1a957b-3911-4ce6-87d8-d0adf0df3790','1','08379447-f768-4343-8151-a43db601ed03',2,10,20,'0'),('dae58ae7-28d3-49b6-801f-7ab195da3439','456','13d84689-2799-46c5-96a7-85d3b9ee8084',1,10,10,'0'),('dcb8e6ba-1770-4692-ada3-1993710f2e71','1','3986584b-5d85-489c-940b-bad7aeef144b',1,10,10,'0'),('dded172b-9d21-4ebf-8501-cb152bd570f5','45','5de7ea07-f7b2-49e8-8e8d-843c3c18ed68',1,10,10,'0'),('e426d3af-11d7-4aa8-9319-632a2d1228a2','1','f6c9acc3-31ac-4523-a2e8-5825d9e87af0',1,10,10,'0'),('e57648c9-50de-41e0-9589-d7c5f19f9571','1','d0af4daf-fa70-4e56-bc4b-80b6fde5218d',1,10,10,'0'),('e85bc7a3-df7b-48c4-8984-ad23dc9108c3','45','7c8bb1ee-4376-440c-accc-8da925be15db',1,10,10,'0');
/*!40000 ALTER TABLE `ddanxiz` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods`
--

DROP TABLE IF EXISTS `goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `goods` (
  `gid` varchar(64) NOT NULL,
  `aid` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `img` text,
  `price` float DEFAULT NULL,
  `cost` float DEFAULT NULL,
  `num` int DEFAULT NULL,
  `sort` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`gid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods`
--

LOCK TABLES `goods` WRITE;
/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` VALUES ('1','123456','香','3927bcf541a94beca12cbce99b8f3916.jpg',10,5,10,'10'),('45','1','香蕉','20211118215821.jpg',10,2,100,'100'),('455','123456','fish','4636bb64cdd5473796de189857e4fbcf.jpg',10,4,100,'100'),('456','1','西瓜','1603777901078.jpg',10,10,100,'100');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order`
--

DROP TABLE IF EXISTS `order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order` (
  `orderno` varchar(64) NOT NULL,
  `id` varchar(64) DEFAULT NULL,
  `createtime` varchar(45) DEFAULT NULL,
  `paytime` varchar(45) DEFAULT NULL,
  `sendtime` varchar(45) DEFAULT NULL,
  `expname` varchar(45) DEFAULT NULL,
  `artime` varchar(45) DEFAULT NULL,
  `agrtime` varchar(45) DEFAULT NULL,
  `status` int DEFAULT NULL,
  `total` float DEFAULT NULL,
  `out_trade_no` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`orderno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order`
--

LOCK TABLES `order` WRITE;
/*!40000 ALTER TABLE `order` DISABLE KEYS */;
INSERT INTO `order` VALUES ('1cdeeae6-196e-4de9-8a49-fdb390fb56d3','123456','2022-06-10 17:35:05','2022-06-10 17:35:50',NULL,NULL,'','',1,30,'9c3177b637174c49990404a789693229'),('238f344a-a9f3-4de7-a9e7-d59db00d3ac5','123456','2022-06-10 16:24:01','2022-06-10 16:24:20',NULL,NULL,'','',1,10,'b6e901934d7f47a496f7fd07c63c724e'),('4dd1eacd-b915-41c9-9a0c-63cc7943f6e0','123456','2022-05-31 15:25:35','2022-05-31 15:26:04','2022-05-31 07:34:13',NULL,'2022-05-31 07:34:29','',3,40,'20067eaf079c47428c98272e49ab1e6e'),('5575ec6d-2bc2-4236-a992-0011af7e2fb3','123456','2022-05-31 15:35:46','2022-05-31 15:36:01',NULL,NULL,'','',1,10,'c33c8beb68b94aceb77964f97eaa0dcd'),('f6c9acc3-31ac-4523-a2e8-5825d9e87af0','123456','2022-05-31 16:13:26','2022-05-31 16:13:45',NULL,NULL,'','',1,30,'029aadcfff5f4671b495ba1d6b52ead8'),('fc8dea59-ed26-4d6b-b283-b07a4c57aa96','123456','2022-05-31 15:36:12','2022-05-31 15:36:26',NULL,NULL,'','',1,10,'bfae23f9294048989b2f5077899b7ff2');
/*!40000 ALTER TABLE `order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pingjia`
--

DROP TABLE IF EXISTS `pingjia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pingjia` (
  `pingjiano` varchar(64) NOT NULL,
  `gid` varchar(64) DEFAULT NULL,
  `xizehao` varchar(64) DEFAULT NULL,
  `uid` varchar(64) DEFAULT NULL,
  `info` text,
  `time` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pingjiano`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pingjia`
--

LOCK TABLES `pingjia` WRITE;
/*!40000 ALTER TABLE `pingjia` DISABLE KEYS */;
INSERT INTO `pingjia` VALUES ('123','123','123','123','123',NULL),('15c23ae8-1b78-4e17-b681-2d4418cdaecb','1','54b76048-1e03-4061-86ff-878a262f43ae','123456','op','2022-05-26 02:59:17'),('456','123','123','123','123',NULL),('69af8c4f-2dc7-44fb-8cfb-f75510366fb3','456','8bfb73e3-6054-4b95-aad3-a785481f356b','123456','456','2022-05-26 03:02:00'),('cc7974b5-5faf-452b-9a55-46177b08bf4f','455','34419c37-635b-4e49-a81c-30f56091fcda','123456','123','2022-05-26 03:01:54'),('d3be9178-c5ad-4081-8221-a1090aa5bdc4','1','54b76048-1e03-4061-86ff-878a262f43ae','123456','h','2022-05-26 02:55:40'),('dff24f80-6d6d-4f29-b2d8-1ceb63079dd5','45','03c00a83-b528-4a0a-baac-6d176acb7e21','123456','houpo','2022-05-26 02:59:45');
/*!40000 ALTER TABLE `pingjia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refund`
--

DROP TABLE IF EXISTS `refund`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refund` (
  `refundno` varchar(64) NOT NULL,
  `orderno` varchar(64) DEFAULT NULL,
  `info` text,
  `time` varchar(45) DEFAULT NULL,
  `status` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`refundno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refund`
--

LOCK TABLES `refund` WRITE;
/*!40000 ALTER TABLE `refund` DISABLE KEYS */;
INSERT INTO `refund` VALUES ('43b96f8d-28e2-4c28-a32b-3e132c00f632','1cdeeae6-196e-4de9-8a49-fdb390fb56d3','会说话','2022-06-10 17:37:15','1'),('7487e6c1-6a02-4800-ab43-4fbdbb53b3a0','4dd1eacd-b915-41c9-9a0c-63cc7943f6e0','','2022-05-31 15:34:58','1'),('c1e21ad0-7dc6-4fad-b9a5-49c33b5f46de','238f344a-a9f3-4de7-a9e7-d59db00d3ac5','不想要了','2022-06-10 16:30:15','1'),('cfd1c204-990e-44cd-a0bd-b3650cb19c9f','5575ec6d-2bc2-4236-a992-0011af7e2fb3','','2022-05-31 15:37:04','1'),('f301f81f-b308-4918-9500-057681a48975','fc8dea59-ed26-4d6b-b283-b07a4c57aa96','','2022-05-31 15:37:06','1');
/*!40000 ALTER TABLE `refund` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(64) NOT NULL,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `pnum` varchar(45) DEFAULT NULL,
  `dress` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('123456','123456','123456','123456','123456','123456'),('12456','123456','123456','123456','123456','123456'),('124567','123456','123456','123456','123456','123456'),('1256','123456','123456','123456','123456','123456'),('126','123456','123456','123456','123456','123456'),('1632624277','123456','123456','123456','123456','1632624277@qq.com'),('456','456','456','456','456','456');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-16 10:56:30
