-- MySQL dump 10.13  Distrib 8.0.38, for macos14 (arm64)
--
-- Host: localhost    Database: stylish
-- ------------------------------------------------------
-- Server version	8.0.38

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
-- Table structure for table `campaign`
--

DROP TABLE IF EXISTS `campaign`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `campaign` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `story` text NOT NULL,
  `picture` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `campaign_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `campaign`
--

LOCK TABLES `campaign` WRITE;
/*!40000 ALTER TABLE `campaign` DISABLE KEYS */;
INSERT INTO `campaign` VALUES (5,7,'於是\\n我也想要給你\\n一個那麼美好的自己。\\n不朽 <與自己和好如初>','/201807242228_keyvisual.jpg'),(6,2,'men-campaign','/201807242222_keyvisual.jpg');
/*!40000 ALTER TABLE `campaign` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `color`
--

DROP TABLE IF EXISTS `color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `color` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(7) DEFAULT NULL,
  `variant_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `color_ibfk_1` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `color`
--

LOCK TABLES `color` WRITE;
/*!40000 ALTER TABLE `color` DISABLE KEYS */;
INSERT INTO `color` VALUES (1,'Dark Blue','334455',1),(2,'White','FFFFFF',1),(3,'Dark Blue','334455',2),(4,'Black','000000',3),(5,'Pink','FF69B4',4),(6,'White','FFFFFF',5),(7,'Black','000000',5),(8,'Gold','FFD700',6),(9,'White','FFFFFF',6),(10,'Brown','A52A2A',7),(11,'Black','000000',7),(12,'Slate Gray','708090',8),(13,'Navy Blue','000080',9),(14,'Hot Pink','FF69B4',10),(15,'Yellow','FFFF00',10),(16,'Black','000000',11),(17,'Tomato','FF6347',12),(18,'Steel Blue','4682B4',12),(19,'White','FFFFFF',13),(20,'Black','000000',13),(21,'Orange','FF4500',14),(22,'Black','000000',15),(23,'White','FFFFFF',15),(24,'Dark Gray','A9A9A9',16),(25,'Black','000000',16),(26,'Red','FF0000',17),(27,'Green','00FF00',17),(28,'test','test',18);
/*!40000 ALTER TABLE `color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fake_color`
--

DROP TABLE IF EXISTS `fake_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fake_color` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fake_color`
--

LOCK TABLES `fake_color` WRITE;
/*!40000 ALTER TABLE `fake_color` DISABLE KEYS */;
/*!40000 ALTER TABLE `fake_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fake_order`
--

DROP TABLE IF EXISTS `fake_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fake_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `total` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fake_order`
--

LOCK TABLES `fake_order` WRITE;
/*!40000 ALTER TABLE `fake_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `fake_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fake_product`
--

DROP TABLE IF EXISTS `fake_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fake_product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `price` bigint DEFAULT NULL,
  `color_id` bigint DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  `qty` bigint DEFAULT NULL,
  `fake_order_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `color_id` (`color_id`),
  KEY `fake_order_id` (`fake_order_id`),
  CONSTRAINT `fake_product_ibfk_1` FOREIGN KEY (`color_id`) REFERENCES `fake_color` (`id`),
  CONSTRAINT `fake_product_ibfk_2` FOREIGN KEY (`fake_order_id`) REFERENCES `fake_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fake_product`
--

LOCK TABLES `fake_product` WRITE;
/*!40000 ALTER TABLE `fake_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `fake_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fake_product_color`
--

DROP TABLE IF EXISTS `fake_product_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fake_product_color` (
  `fake_color_id` bigint NOT NULL,
  `fake_product_id` bigint NOT NULL,
  PRIMARY KEY (`fake_color_id`,`fake_product_id`),
  KEY `fake_product_id` (`fake_product_id`),
  CONSTRAINT `fake_product_color_ibfk_1` FOREIGN KEY (`fake_color_id`) REFERENCES `fake_color` (`id`),
  CONSTRAINT `fake_product_color_ibfk_2` FOREIGN KEY (`fake_product_id`) REFERENCES `fake_product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fake_product_color`
--

LOCK TABLES `fake_product_color` WRITE;
/*!40000 ALTER TABLE `fake_product_color` DISABLE KEYS */;
/*!40000 ALTER TABLE `fake_product_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `shipping` varchar(255) NOT NULL,
  `payment` varchar(255) NOT NULL,
  `subtotal` bigint NOT NULL,
  `freight` bigint NOT NULL,
  `total` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,'delivery','credit_card',2500,10,2510),(2,'delivery','credit_card',2500,10,2510),(3,'delivery','credit_card',2500,10,2510),(4,'delivery','credit_card',2500,10,2510),(5,'delivery','credit_card',2500,10,2510),(6,'delivery','credit_card',2500,10,2510),(7,'delivery','credit_card',2500,10,2510),(8,'delivery','credit_card',2500,10,2510),(9,'delivery','credit_card',2500,10,2510),(10,'delivery','credit_card',2500,10,2510),(11,'delivery','credit_card',2500,10,2510),(12,'delivery','credit_card',2500,10,2510),(13,'delivery','credit_card',2500,10,2510),(14,'delivery','credit_card',2500,10,2510),(15,'delivery','credit_card',2500,10,2510),(16,'delivery','credit_card',2500,10,2510),(17,'delivery','credit_card',2500,10,2510),(18,'delivery','credit_card',2500,10,2510),(19,'delivery','credit_card',2200,10,2210),(20,'delivery','credit_card',2200,10,2210),(21,'delivery','credit_card',2200,10,2210),(22,'delivery','credit_card',2200,10,2210),(23,'delivery','credit_card',2200,10,2210),(24,'delivery','credit_card',2200,10,2210),(25,'delivery','credit_card',2200,10,2210),(26,'delivery','credit_card',500,10,510),(27,'delivery','credit_card',2200,10,2210),(28,'delivery','credit_card',1500,10,1510),(29,'delivery','credit_card',1500,10,1510),(30,'delivery','credit_card',1500,10,1510),(31,'delivery','credit_card',500,10,510),(32,'delivery','credit_card',1500,10,1510),(33,'delivery','credit_card',2500,10,2510),(34,'delivery','credit_card',2500,10,2510);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payment`
--

DROP TABLE IF EXISTS `payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `successful` tinyint(1) NOT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payment`
--

LOCK TABLES `payment` WRITE;
/*!40000 ALTER TABLE `payment` DISABLE KEYS */;
INSERT INTO `payment` VALUES (1,1,9),(2,1,10),(3,0,11),(4,1,12),(5,1,13),(6,1,14),(7,1,15),(8,1,16),(9,0,17),(10,1,18),(11,1,19),(12,0,20),(13,1,21),(14,1,22),(15,1,23),(16,1,24),(17,1,25),(18,1,26),(19,1,27),(20,1,28),(21,1,29),(22,1,30),(23,1,31),(24,1,32),(25,1,33),(26,1,34);
/*!40000 ALTER TABLE `payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `description` text,
  `price` bigint DEFAULT NULL,
  `texture` varchar(255) DEFAULT NULL,
  `wash` varchar(255) DEFAULT NULL,
  `place` varchar(255) DEFAULT NULL,
  `note` text,
  `story` text,
  `main_image` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'men','Thick Plaid Woolen Coat','High-quality, cold-resistant material, warm and stylish',2200,'Cotton, Polyester','Hand wash (water temperature 40°C)','Korea','The actual color of the product is based on the single product shot','A must-have item you can\'t miss','/m1.jpg'),(2,'men','Thick Plaid Woolen Coat','High-quality, cold-resistant material, warm and stylish',2500,'Cotton, Polyester','Hand wash (water temperature 40°C)','Korea','The actual color of the product is based on the single product shot','A must-have item you can\'t miss','/4.jpg'),(3,'men','Lightweight Jacket','Perfect for the changing seasons',1800,'Nylon, Polyester','Nylon, Polyester','China','Colors may vary slightly from images','Stay comfortable and stylish','/7.jpg'),(4,'women','優雅絲綢洋裝','Perfect for any occasion',2500,'Silk, Polyester','Dry clean only','Italy','Actualproduct color may vary','Elegant and timeless design','/g1.jpeg'),(5,'women','花卉夏季洋裝','Comfortable and stylish',500,'Cotton','Machine wash (warm)','Vietnam','Colors may fade after washing','Perfect for everyday wear','/g3.jpeg'),(6,'women','復古蕾絲洋裝','Lightweight and breezy',1200,'Cotton, Linen','Machine wash (cold)','Thailand','Ideal for warm weather','Stay cool and chic','/g6.jpeg'),(7,'women','復古蕾絲洋裝','Cozy and warm for the chilly season',1500,'Wool, Polyester','Hand wash (cold)','Japan','Keep away from direct sunlight','Perfect for layering','/g8.jpeg'),(8,'women','時尚雞尾酒洋裝','Warm and elegant',3000,'Wool, Cashmere','Dry clean only','Italy','Store in a cool, dry place','Timeless elegance','/g11.jpeg'),(9,'women','現代A字洋裝','Comfortable and versatile',1800,'Denim','Machine wash (cold)','USA','May shrink slightly after washing','A wardrobe essential','/g14.jpeg'),(10,'women','經典晚禮洋裝','Light and flowy',1200,'Cotton, Polyester','Machine wash (cold)','France','Iron at low temperature','Perfect for spring days','/g16.jpeg'),(11,'women','休閒直筒洋裝','Edgy and fashionable',3500,'Leather','Dry clean only','USA','Avoid contact with water','For a bold statement','/g18.jpeg'),(12,'women','浪漫荷葉邊洋裝','Beautiful floral patterns',2000,'Cotton, Polyester','Machine wash (cold)','India','Patterns may vary slightly','Perfect for a sunny day','/g21.jpeg'),(13,'women','時尚裹身洋裝','Light and comfortable',1300,'Cotton','Machine wash (cold)','China','Colors may fade after washing','Perfect for everyday wear','/g23.jpeg'),(14,'women','精緻修身洋裝','Perfect for warm weather',1800,'Linen, Cotton','Hand wash (cold)','India','Ideal for beach outings','Stay cool and stylish','/g25.jpeg'),(15,'women','華麗亮片洋裝','Comfortable for workouts',1000,'Polyester, Spandex','Machine wash (cold)','China','Do not iron','Perfect for the gym','/g28.jpeg'),(16,'women','優雅高腰洋裝','Comfortable for casual wear',900,'Cotton, Polyester','Machine wash (cold)','Bangladesh','Tumble dry low','Stay comfortable all day','/g30.jpeg'),(17,'accessories','Wool Scarf','Warm and cozy',600,'Wool','Hand wash (cold)','UK','Keep away from direct heat','Stay warm and stylish','/a1.jpeg'),(18,'test','test','test',1,'test','test','test','test','test',NULL);
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_color`
--

DROP TABLE IF EXISTS `product_color`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_color` (
  `product_id` bigint NOT NULL,
  `color_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`,`color_id`),
  KEY `color_id` (`color_id`),
  CONSTRAINT `product_color_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `product_color_ibfk_2` FOREIGN KEY (`color_id`) REFERENCES `color` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_color`
--

LOCK TABLES `product_color` WRITE;
/*!40000 ALTER TABLE `product_color` DISABLE KEYS */;
INSERT INTO `product_color` VALUES (1,1),(1,2),(2,3),(3,4),(4,5),(5,6),(5,7),(6,8),(6,9),(7,10),(7,11),(8,12),(9,13),(10,14),(10,15),(11,16),(12,17),(12,18),(13,19),(13,20),(14,21),(15,22),(15,23),(16,24),(16,25),(17,26),(17,27),(18,28);
/*!40000 ALTER TABLE `product_color` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_image`
--

DROP TABLE IF EXISTS `product_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_image` (
  `product_id` bigint NOT NULL,
  `image` varchar(255) NOT NULL,
  PRIMARY KEY (`product_id`,`image`),
  CONSTRAINT `product_image_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_image`
--

LOCK TABLES `product_image` WRITE;
/*!40000 ALTER TABLE `product_image` DISABLE KEYS */;
INSERT INTO `product_image` VALUES (1,'/m2.jpg'),(2,'/5.jpg'),(2,'/6.jpg'),(3,'/8.jpg'),(3,'/9.jpg'),(4,'/g2.jpeg'),(5,'/g4.jpeg'),(5,'/g5.jpeg'),(6,'/g7.jpeg'),(7,'/g10.jpeg'),(7,'/g9.jpeg'),(8,'/g12.jpeg'),(8,'/g13.jpeg'),(9,'/g15.jpeg'),(10,'/g17.jpeg'),(11,'/g20.jpeg'),(11,'/g21.jpeg'),(12,'/g22.jpeg'),(13,'/g24.jpeg'),(14,'/g26.jpeg'),(14,'/g27.jpeg'),(15,'/g29.jpeg'),(16,'/g191.jpeg'),(17,'/a2.jpeg'),(17,'/a3.jpeg'),(18,'https://dnlxp3zjcn7kk.cloudfront.net/uploadsOIP.jpeg');
/*!40000 ALTER TABLE `product_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_order`
--

DROP TABLE IF EXISTS `product_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_order` (
  `product_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `quantity` bigint DEFAULT NULL,
  PRIMARY KEY (`product_id`,`order_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `product_order_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `product_order_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_order`
--

LOCK TABLES `product_order` WRITE;
/*!40000 ALTER TABLE `product_order` DISABLE KEYS */;
INSERT INTO `product_order` VALUES (1,19,1),(1,20,1),(1,21,1),(1,22,1),(1,23,1),(1,24,1),(1,25,1),(1,27,1),(2,14,1),(2,15,1),(2,16,1),(2,17,1),(2,18,1),(2,33,1),(4,34,1),(5,26,1),(5,31,1),(7,28,1),(7,29,1),(7,30,1),(7,32,1);
/*!40000 ALTER TABLE `product_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_size`
--

DROP TABLE IF EXISTS `product_size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_size` (
  `product_id` bigint NOT NULL,
  `size_id` bigint NOT NULL,
  PRIMARY KEY (`product_id`,`size_id`),
  KEY `size_id` (`size_id`),
  CONSTRAINT `product_size_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `product_size_ibfk_2` FOREIGN KEY (`size_id`) REFERENCES `size` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_size`
--

LOCK TABLES `product_size` WRITE;
/*!40000 ALTER TABLE `product_size` DISABLE KEYS */;
INSERT INTO `product_size` VALUES (1,1),(1,2),(2,3),(2,4),(2,5),(3,6),(3,7),(3,8),(4,9),(5,10),(5,11),(5,12),(5,13),(6,14),(6,15),(7,16),(7,17),(8,18),(8,19),(9,20),(9,21),(10,22),(10,23),(10,24),(11,25),(11,26),(11,27),(12,28),(12,29),(12,30),(13,31),(13,32),(14,33),(14,34),(14,35),(15,36),(16,37),(16,38),(16,39),(17,40),(17,41),(18,42);
/*!40000 ALTER TABLE `product_size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipient`
--

DROP TABLE IF EXISTS `recipient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipient` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `order_id` bigint NOT NULL,
  `time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `recipient_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipient`
--

LOCK TABLES `recipient` WRITE;
/*!40000 ALTER TABLE `recipient` DISABLE KEYS */;
INSERT INTO `recipient` VALUES (1,'Luke','0987654321','luke@gmail.com','市政府站',12,'morning'),(2,'Luke','0987654321','luke@gmail.com','市政府站',13,'morning'),(3,'Luke','0987654321','luke@gmail.com','市政府站',14,'morning'),(4,'Luke','0987654321','luke@gmail.com','市政府站',15,'morning'),(5,'Luke','0987654321','luke@gmail.com','市政府站',16,'morning'),(6,'Luke','0987654321','luke@gmail.com','市政府站',17,'morning'),(7,'Luke','0987654321','luke@gmail.com','市政府站',18,'morning'),(8,'Luke','0987654321','luke@gmail.com','市政府站',19,'morning'),(9,'Luke','0987654321','luke@gmail.com','市政府站',20,'morning'),(10,'Luke','0987654321','luke@gmail.com','市政府站',21,'morning'),(11,'Luke','0987654321','luke@gmail.com','市政府站',22,'morning'),(12,'Luke','0987654321','luke@gmail.com','市政府站',23,'morning'),(13,'Luke','0987654321','luke@gmail.com','市政府站',24,'morning'),(14,'Luke','0987654321','luke@gmail.com','市政府站',25,'morning'),(15,'Luke','0987654321','luke@gmail.com','市政府站',26,'morning'),(16,'Luke','0987654321','luke@gmail.com','市政府站',27,'morning'),(17,'Luke','0987654321','luke@gmail.com','市政府站',28,'morning'),(18,'Luke','0987654321','luke@gmail.com','市政府站',29,'morning'),(19,'Luke','0987654321','luke@gmail.com','市政府站',30,'morning'),(20,'Luke','0987654321','luke@gmail.com','市政府站',31,'morning'),(21,'Luke','0987654321','luke@gmail.com','市政府站',32,'morning'),(22,'Luke','0987654321','luke@gmail.com','市政府站',33,'morning'),(23,'Luke','0987654321','luke@gmail.com','市政府站',34,'morning');
/*!40000 ALTER TABLE `recipient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `size`
--

DROP TABLE IF EXISTS `size`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `size` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `variant_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `variant_id` (`variant_id`),
  CONSTRAINT `size_ibfk_1` FOREIGN KEY (`variant_id`) REFERENCES `variant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `size`
--

LOCK TABLES `size` WRITE;
/*!40000 ALTER TABLE `size` DISABLE KEYS */;
INSERT INTO `size` VALUES (1,'S',1),(2,'M',1),(3,'S',2),(4,'M',2),(5,'L',2),(6,'M',3),(7,'L',3),(8,'XL',3),(9,'S',4),(10,'S',5),(11,'M',5),(12,'L',5),(13,'XL',5),(14,'S',6),(15,'M',6),(16,'M',7),(17,'L',7),(18,'S',8),(19,'M',8),(20,'L',9),(21,'XL',9),(22,'S',10),(23,'M',10),(24,'L',10),(25,'S',11),(26,'M',11),(27,'L',11),(28,'S',12),(29,'M',12),(30,'L',12),(31,'S',13),(32,'M',13),(33,'S',14),(34,'M',14),(35,'L',14),(36,'M',15),(37,'S',16),(38,'M',16),(39,'L',16),(40,'M',17),(41,'L',17),(42,'test',18);
/*!40000 ALTER TABLE `size` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `provider` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `picture` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (6,'facebook','江彤恩','win1956win1956@yahoo.com.tw','https://platform-lookaside.fbsbx.com/platform/profilepic/?asid=7892141784200997&height=50&width=50&ext=1725291412&hash=AbZCvgKwe058UA9jXwR6bE8B','$2a$10$.y3g3OZTiOEkKHML0vaZ1ezphTdFiENKkgLCB57z5IMuqBEiNUi7W'),(7,'native','wendy','test@test.com',NULL,'$2a$10$3/RUQGoOuBy/mVYI31xgYulOBviCGA9Dtv7ZNHRexCRd35GsMv32u'),(8,'native','wendy','oijeioojio@ijeroij.iojoie',NULL,'$2a$10$0xJiAhi5Je67I3AvZSHpGuUOXtUTqWZ3oXKmlpKXebQTKTSEjjjQ6'),(9,'native','wendy','oijeioojio@ijeroij.iojoi',NULL,'$2a$10$UgMgB9iQ4lLwq9h7psarWu8uvCN9x2awCn76.k6ebv9uYxB1HN7cq'),(10,'native','wendy','oijeioojio@ijeroij.iojo',NULL,'$2a$10$Ea/lui6ulooPIbglVfMkyOOK39FGZKG/92fgu1..yWTs94OcDEyTa'),(11,'native','wendy','oijeioojio@ijeroij.ioj',NULL,'$2a$10$cIYculz1Dv8EotT6h3qLqeBJUUMF7j7O4NCK3KTLTtqsQQ2Q98QU.'),(12,'native','wendy','oiajojai@jija.nioaj',NULL,'$2a$10$/ZDpJDZ8G9V2PMq8xKzHNuluHu4gdsU5Pxu3CTZlPQaQ4TB15Y4zW'),(13,'native','wendy','oiajojai@jija.nioajd',NULL,'$2a$10$qvDdKbXS8us4dsjQgb5CAemJthm4zzrRoa315zlFoF.4znMt8GJGW'),(14,'native','wendy','aokfpokpo@kmk.mopa',NULL,'$2a$10$lN8uLe6/RMcP9fXS4B4juuF56ov0YpNrLLSjdBnDjch7ZmTw1avu2'),(15,'native','wendy','wendy900705@126.com',NULL,'$2a$10$7m68xwPwsNHZZUZ0Ps/jgeV6yGV4uYBf0wU4XKZWP83j/gZBvJ6j6'),(16,'native','wendy','108305096@nccu.edu.tw',NULL,'$2a$10$FiicmraccwRMMHVDbCWRe.lpKFX2OyRxBz1igZGV9TF5JBPC5f8be'),(17,'native','wendy','test3333@test.com',NULL,'$2a$10$rmcQCDYUZOiRlLI4M7neSOAUdu70mlGbvHZZkMIN/2TeE/xUOPl5.');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `variant`
--

DROP TABLE IF EXISTS `variant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `variant` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `product_id` bigint DEFAULT NULL,
  `stock` int DEFAULT '9999',
  PRIMARY KEY (`id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `variant_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `variant`
--

LOCK TABLES `variant` WRITE;
/*!40000 ALTER TABLE `variant` DISABLE KEYS */;
INSERT INTO `variant` VALUES (1,1,9999),(2,2,9999),(3,3,9999),(4,4,9999),(5,5,9999),(6,6,9999),(7,7,9999),(8,8,9999),(9,9,9999),(10,10,9999),(11,11,9999),(12,12,9999),(13,13,9999),(14,14,9999),(15,15,9999),(16,16,9999),(17,17,9999),(18,18,1);
/*!40000 ALTER TABLE `variant` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-10-18 16:53:25
