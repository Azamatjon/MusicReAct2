-- MySQL dump 10.13  Distrib 5.7.22, for Linux (x86_64)
--
-- Host: localhost    Database: musicdb
-- ------------------------------------------------------
-- Server version	5.7.22-0ubuntu0.16.04.1

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
-- Table structure for table `album`
--

DROP TABLE IF EXISTS `album`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album` (
  `id` int(11) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `artist_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmwc4fyyxb6tfi0qba26gcf8s1` (`artist_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album`
--

LOCK TABLES `album` WRITE;
/*!40000 ALTER TABLE `album` DISABLE KEYS */;
INSERT INTO `album` VALUES (57,'qVZVBMZ2Rp8sUD8mY7OT.JPG',NULL,51),(58,'Ydd34N5MF0BUnLB2Ggd9.JPG',NULL,51),(59,'xZgSTqmY2Nw2S3RAYugT.JPG',NULL,51),(60,'fz4TovyD6oPrYuLgPbYm.JPG',NULL,51),(61,NULL,NULL,51),(62,NULL,NULL,51),(63,NULL,'test album99',51);
/*!40000 ALTER TABLE `album` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `album_tracks`
--

DROP TABLE IF EXISTS `album_tracks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `album_tracks` (
  `album_id` int(11) NOT NULL,
  `tracks_id` int(11) NOT NULL,
  PRIMARY KEY (`album_id`,`tracks_id`),
  UNIQUE KEY `UK_pej3u6o3e1kvrm0ojj2mkt2hw` (`tracks_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `album_tracks`
--

LOCK TABLES `album_tracks` WRITE;
/*!40000 ALTER TABLE `album_tracks` DISABLE KEYS */;
/*!40000 ALTER TABLE `album_tracks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artist`
--

DROP TABLE IF EXISTS `artist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist` (
  `id` int(11) NOT NULL,
  `biography` varchar(20000) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist`
--

LOCK TABLES `artist` WRITE;
/*!40000 ALTER TABLE `artist` DISABLE KEYS */;
INSERT INTO `artist` VALUES (54,'awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span>awdfsd g<span style=\"font-family: &quot;DejaVu Sans Mono&quot;; font-size: 8.3pt;\">biographylerdfcb jijkbcxn bvkm,nxckj bm,.nkm,cvb k,m&nbsp; cvk b.,mndflkgjv,z;xkcbn ksdzvsdxb cdfcb sdbvcv jbnvxck dsfjkx ikdfxcjnbj,cfbn m,bnsdf xckmbhns ghbjn sdvxz jbjjkbnczjhb jwgev cbjnm</span><br>',NULL,'fVt9vk5fyEpb5IZON8zs.png','Test Artist2'),(55,NULL,NULL,NULL,'fd');
/*!40000 ALTER TABLE `artist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artist_albums`
--

DROP TABLE IF EXISTS `artist_albums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_albums` (
  `artist_id` int(11) NOT NULL,
  `albums_id` int(11) NOT NULL,
  PRIMARY KEY (`artist_id`,`albums_id`),
  UNIQUE KEY `UK_rib3oys16i57ecfmscw7ggeem` (`albums_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist_albums`
--

LOCK TABLES `artist_albums` WRITE;
/*!40000 ALTER TABLE `artist_albums` DISABLE KEYS */;
/*!40000 ALTER TABLE `artist_albums` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `artist_tracks`
--

DROP TABLE IF EXISTS `artist_tracks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist_tracks` (
  `artist_id` int(11) NOT NULL,
  `tracks_id` int(11) NOT NULL,
  PRIMARY KEY (`artist_id`,`tracks_id`),
  UNIQUE KEY `UK_2mc23mjx4us65dtdmnmbc0l5o` (`tracks_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `artist_tracks`
--

LOCK TABLES `artist_tracks` WRITE;
/*!40000 ALTER TABLE `artist_tracks` DISABLE KEYS */;
/*!40000 ALTER TABLE `artist_tracks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequence`
--

DROP TABLE IF EXISTS `hibernate_sequence`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequence`
--

LOCK TABLES `hibernate_sequence` WRITE;
/*!40000 ALTER TABLE `hibernate_sequence` DISABLE KEYS */;
INSERT INTO `hibernate_sequence` VALUES (71),(71),(71),(71);
/*!40000 ALTER TABLE `hibernate_sequence` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `id` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
INSERT INTO `image` VALUES (38,'cBqbNYZNBPX7UuC7yNAc.png'),(39,'WDO27ZejzVt3T0sEfkYz.png'),(40,'x4eZUHEhLoOdgsC2X4aq.png'),(41,'AHBTiVZ99DKGLMdzGSe2.png'),(42,'zJBAhgFe6F7bVhthRynP.png'),(43,'JpNqMhV7W5FbqkWfObzh.JPG'),(44,'lrZ3haM9gTplSkEJGPzH.png'),(45,'4yk7nXXSqu2ULbPfTPx2.JPG'),(46,'mgoi5FMLpXeQeSVDVjv4.png'),(47,'mzooH9Ax8XrRuhBhtJsm.JPG'),(48,'XvRysjmXXCZFL5iwW6Yo.JPG'),(49,'A4A1fIt93PsHuBUbQA3r.png'),(50,'wCglzEYTI86qOA1sCNaT.JPG');
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `id` int(11) NOT NULL,
  `role` varchar(255) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ADMIN','\0'),(2,'USER','');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `styles`
--

DROP TABLE IF EXISTS `styles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `styles` (
  `id` int(11) NOT NULL,
  `b_layout` bit(1) DEFAULT NULL,
  `c_menu` bit(1) DEFAULT NULL,
  `custom_style` varchar(255) DEFAULT NULL,
  `f_header` bit(1) DEFAULT NULL,
  `f_sidebar` bit(1) DEFAULT NULL,
  `h_bar` bit(1) DEFAULT NULL,
  `h_menu` bit(1) DEFAULT NULL,
  `t_sidebar` bit(1) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `styles`
--

LOCK TABLES `styles` WRITE;
/*!40000 ALTER TABLE `styles` DISABLE KEYS */;
INSERT INTO `styles` VALUES (23,'\0','\0','white','','\0','\0','\0','\0',21),(5,'\0','\0','white','','\0','\0','\0','\0',3),(20,'\0','\0','white','','\0','\0','\0','\0',1),(26,'\0','\0','white','','\0','\0','\0','\0',24),(29,'\0','\0','white','','\0','\0','\0','\0',27),(32,'\0','\0','white','','\0','\0','\0','\0',30),(35,'\0','\0','white','','\0','\0','\0','\0',34);
/*!40000 ALTER TABLE `styles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `track`
--

DROP TABLE IF EXISTS `track`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `track` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `album_id` int(11) DEFAULT NULL,
  `artist_id` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `bitrate` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKaxm9pbgk7ptorfyk3o6911q05` (`album_id`),
  KEY `FKi28jadqiuqk1dlxtl0me7hqh2` (`artist_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `track`
--

LOCK TABLES `track` WRITE;
/*!40000 ALTER TABLE `track` DISABLE KEYS */;
/*!40000 ALTER TABLE `track` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `active` int(11) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_locked` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (30,1,'azamatjon.98@gmail.com',NULL,'\0','Akhunzhanov','Azamatzhon','$2a$10$JDSXuu924rPE701XGwpoyuEdL5UahUbNbW4xR7sGoVSyVK/fRRVX6'),(50,1,'ajkyuhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(51,1,'ajkyuhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(52,1,'ajkyuhjtyjk@gmail.comdfrgrfgf','BrQwN1mQPcn36zbTLg9a.jpg','\0','asdfghjkl;','qwertyuiop','$2a$10$ULmdCbwBA82DU.KjSC5nOOMyt117pyOEpuOIx4DcvziTdfZezxIyO'),(54,1,'ajkyuuyljkhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(55,1,'ajkyuuiljklyuhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$Uo7JmyBbQEvg9nrX8bSBfumzrGE2.3OR0sg1moiTbTw77cw4Kn1GO'),(56,1,'ajkrtgerhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(57,1,'ajkyuhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(58,1,'ajkyuhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(59,1,'ajkyuhjtyjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(60,1,'ajkyuukj,hjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC'),(61,1,'ajkyuuyljkhjk@gmail.com','BrQwN1mQPcn36zbTLg9a.jpg','\0','qwerty','qwerty','$2a$10$YhR56mA8Z7eou77CAZqLkux386CBqKG4SFypiOCFQ8trM6X7fl2jC');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` VALUES (30,1),(50,1),(51,1),(52,1),(54,1),(55,2),(56,1),(57,1),(58,1),(59,1),(60,1),(61,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_verification_token`
--

DROP TABLE IF EXISTS `user_verification_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_verification_token` (
  `user_id` int(11) NOT NULL,
  `verification_token_id` int(11) NOT NULL,
  PRIMARY KEY (`user_id`,`verification_token_id`),
  UNIQUE KEY `UK_pdmentxd7x25owbo9110ben9a` (`verification_token_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_verification_token`
--

LOCK TABLES `user_verification_token` WRITE;
/*!40000 ALTER TABLE `user_verification_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_verification_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verification_token`
--

DROP TABLE IF EXISTS `verification_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `verification_token` (
  `id` int(11) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrdn0mss276m9jdobfhhn2qogw` (`user_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verification_token`
--

LOCK TABLES `verification_token` WRITE;
/*!40000 ALTER TABLE `verification_token` DISABLE KEYS */;
INSERT INTO `verification_token` VALUES (20,'azamatjon.98@gmail.com','2018-05-09','tTwstSNt66OyMoRq0mxKI',2,3),(12,'ahunjanovazamat@mail.ru','2018-05-09','GvPlmVrPelHfYLGdD8Y0t',3,3);
/*!40000 ALTER TABLE `verification_token` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-26 15:15:04
