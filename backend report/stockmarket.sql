-- MySQL dump 10.13  Distrib 5.7.15, for osx10.11 (x86_64)
--
-- Host: localhost    Database: stockmarket
-- ------------------------------------------------------
-- Server version	5.7.15

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
-- Table structure for table `Company`
--

DROP TABLE IF EXISTS `Company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Company` (
  `Company_ID` varchar(10) NOT NULL,
  `Company_name` varchar(64) DEFAULT NULL,
  `CEO` varchar(64) DEFAULT NULL,
  `OutStanding_Shares` int(11) DEFAULT NULL,
  PRIMARY KEY (`Company_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Company`
--

LOCK TABLES `Company` WRITE;
/*!40000 ALTER TABLE `Company` DISABLE KEYS */;
INSERT INTO `Company` VALUES ('AAPL','Apple','Tim Cook',129847382),('AMZN','Amazon','Jeff Bezos',129384789),('APA','Apache','John J Christmann',2983712),('AXP','American Express','Kenneth I. Chenault',23891092),('BABA','Alibaba','Daniel Zhang',14003420),('BMY','Bristol-Myers Squibb','Jack Ma',1248484),('C','CitiGroup','Michael Corbat',13902125),('CBS','CBS','Leslie Moonves',4092841),('COF','Capital One','Richard Fairbank',38910294),('CRM','Salesforce','Marc Benioff',2997836),('CVS','CVS Caremark','Larry Merlo',23990234),('DOW','Dow Checmical','Andrew N. Liveris',12323478),('FL','Foot Locker','Richard A. Johnson',389284),('GOOG','Alphabet','Larry Page',28374637),('JPM','J.P. Morgan Chase','Jamie Diamond',12021432),('MRK','Merk','Daniel Webb',3290843),('MSFT','Microsoft','Satya Nadella',2893716),('NKE','Nike','Phil Knight',18927381),('NVS','Novartis','Joseph Jimenez',28937192),('PG','Procter & Gamble','David S. Taylor',3892710),('PRU','Prudential','John Strangfeld',5982710),('TGT','Target','Brian Cornell',1982738),('TSN','Tyson Foods','Donnie Smith',23891098),('TWX','Time Warner','Jeff Bewkes',8279381),('TXN','Texas Instruments','Rich Templeton',2909081),('V','Visa','Charles Scharf',28798371),('WMT','Wal-Mart','C. Douglas Mcmillon',3892718),('XOM','Exxon Mobil','Rex Tillerson',2910983);
/*!40000 ALTER TABLE `Company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Portfolio`
--

DROP TABLE IF EXISTS `Portfolio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Portfolio` (
  `Company` varchar(10) NOT NULL,
  `Trader_name` varchar(64) NOT NULL,
  `Amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`Company`,`Trader_name`),
  KEY `Trader_name` (`Trader_name`),
  CONSTRAINT `portfolio_ibfk_1` FOREIGN KEY (`Company`) REFERENCES `Company` (`Company_ID`),
  CONSTRAINT `portfolio_ibfk_2` FOREIGN KEY (`Trader_name`) REFERENCES `Traders` (`Trader_Name`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Portfolio`
--

LOCK TABLES `Portfolio` WRITE;
/*!40000 ALTER TABLE `Portfolio` DISABLE KEYS */;
INSERT INTO `Portfolio` VALUES ('goog','a',1);
/*!40000 ALTER TABLE `Portfolio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Stock_Prices`
--

DROP TABLE IF EXISTS `Stock_Prices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Stock_Prices` (
  `Company` varchar(10) NOT NULL,
  `Date_of` date NOT NULL,
  `Price` double(10,2) DEFAULT NULL,
  PRIMARY KEY (`Company`,`Date_of`),
  CONSTRAINT `stock_prices_ibfk_1` FOREIGN KEY (`Company`) REFERENCES `Company` (`Company_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Stock_Prices`
--

LOCK TABLES `Stock_Prices` WRITE;
/*!40000 ALTER TABLE `Stock_Prices` DISABLE KEYS */;
INSERT INTO `Stock_Prices` VALUES ('AAPL','2012-03-16',111.81),('AMZN','2012-03-16',766.98),('APA','2012-03-16',62.91),('BABA','2012-03-16',96.01),('BMY','2012-03-16',54.49),('C','2012-03-16',52.49),('CBS','2012-03-16',60.74),('COF','2012-03-16',83.57),('CRM','2012-03-16',73.00),('CVS','2012-03-16',77.51),('DOW','2012-03-16',54.18),('FL','2012-03-16',72.95),('GOOG','2012-03-16',794.46),('JPM','2012-03-16',74.27),('MRK','2012-03-16',61.47),('MSFT','2012-03-16',60.80),('NKE','2012-03-16',50.78),('NVS','2012-03-16',69.34),('PG','2012-03-16',83.05),('PRU','2012-03-16',99.15),('TGT','2012-03-16',77.90),('TSN','2012-03-16',58.47),('TWX','2012-03-16',92.94),('V','2012-03-16',79.72),('WMT','2012-03-16',71.13),('XOM','2012-03-16',85.38);
/*!40000 ALTER TABLE `Stock_Prices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Traders`
--

DROP TABLE IF EXISTS `Traders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Traders` (
  `Trader_Name` varchar(64) NOT NULL,
  `Available_Funds` double(10,2) DEFAULT NULL,
  `League` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`Trader_Name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Traders`
--

LOCK TABLES `Traders` WRITE;
/*!40000 ALTER TABLE `Traders` DISABLE KEYS */;
INSERT INTO `Traders` VALUES ('a',4205.54,'LEAGUE_1'),('b',5000.00,'LEAGUE_1'),('c',5000.00,'LEAGUE_1'),('d',5000.00,'LEAGUE_1'),('e',5000.00,'LEAGUE_B'),('f',5000.00,'LEAGUE_B'),('g',5000.00,'LEAGUE_B'),('h',5000.00,'LEAGUE_B');
/*!40000 ALTER TABLE `Traders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Transactions`
--

DROP TABLE IF EXISTS `Transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Transactions` (
  `transaction_ID` int(11) NOT NULL AUTO_INCREMENT,
  `Date_of` date DEFAULT NULL,
  `Company_ID` varchar(10) DEFAULT NULL,
  `Trader` varchar(64) DEFAULT NULL,
  `Quantity` int(11) DEFAULT NULL,
  `Buy` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`transaction_ID`),
  KEY `trader_fk` (`Trader`),
  KEY `company_fk` (`Company_ID`),
  CONSTRAINT `company_fk` FOREIGN KEY (`Company_ID`) REFERENCES `Company` (`Company_ID`),
  CONSTRAINT `trader_fk` FOREIGN KEY (`Trader`) REFERENCES `Traders` (`Trader_Name`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Transactions`
--

LOCK TABLES `Transactions` WRITE;
/*!40000 ALTER TABLE `Transactions` DISABLE KEYS */;
INSERT INTO `Transactions` VALUES (3,'2016-12-07','goog','a',2,'TRUE'),(4,'2016-12-07','goog','a',1,'FALSE');
/*!40000 ALTER TABLE `Transactions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-12-07 12:42:48
