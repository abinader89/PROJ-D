DROP DATABASE IF EXISTS StockMarket;
CREATE DATABASE IF NOT EXISTS StockMarket;
USE StockMarket;

/*

trader names as well as the traders returns and its available funds
*/
DROP TABLE IF EXISTS Traders;
CREATE TABLE Traders (

Trader_name Varchar(64) PRIMARY KEY,
stock_returns double NOT NULL,
available_funds double NOT NULL

);

/*
details of company
as well as how many shares are left,
must check if there are existing shares before the shares
for a company can be bought
*/
DROP TABLE IF EXISTS Company;
CREATE TABLE Company (
Company_ID Varchar(10) Primary Key,
Company_name Varchar(64),
CEO Varchar(64),
OutStanding_Shares INT 
);

/*
intermediary table that maps
out traders with their respective leagues
*/
DROP TABLE IF EXISTS League;
CREATE TABLE League (

Team_id INT PRIMARY KEY auto_increment,
Trader_name VARCHAR(64),
CONSTRAINT trader_name_fk FOREIGN KEY(Trader_name) references Traders(Trader_name) ON DELETE CASCADE ON UPDATE CASCADE
);


/*
list of transactions 
for when a person buys/sells a stock
*/
DROP TABLE IF EXISTS Transactions;
CREATE TABLE Transactions(

StockID INT PRIMARY KEY auto_increment,
Date_of DATE,
Company VARCHAR(64),
Trader VARCHAR(64),
Quantity INT,
Buy BOOL,
CONSTRAINT trader_fk FOREIGN KEY(Trader) references Traders(Trader_name) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT company_fk FOREIGN KEY(Company) references Company(Company_ID) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS Stock_Prices;
CREATE TABLE Stock_Prices(
Company VARCHAR(10),
Date_of DATE,
Price DOUBLE,
PRIMARY KEY(Company,Date_of),
CONSTRAINT company_fk2 FOREIGN KEY(Company) references Company(Company_ID) ON DELETE CASCADE ON UPDATE CASCADE
);


/*

how many of what stock does each player own

*/

DROP TABLE IF EXISTS Portfolio;
CREATE TABLE Portfolio(
Company VARCHAR(64),
Trader_name VARCHAR(64),
Amount Int,
PRIMARY KEY(Company, Trader_name),
CONSTRAINT company_fk3 FOREIGN KEY(Company) references Company(Company_ID) ON DELETE CASCADE ON UPDATE CASCADE,
CONSTRAINT trader_name_fk2 FOREIGN KEY(Trader_name) references Traders(Trader_name) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Company VALUES
('C', 'CitiGroup', 'Michael Corbat', 13902125),
('JPM', 'J.P. Morgan Chase', 'Jamie Diamond', 12021432),
('BABA', 'Alibaba', 'Daniel Zhang', 14003420),
('CVS', 'CVS Caremark', 'Larry Merlo', 23990234),
('BMY', 'Bristol-Myers Squibb', 'Jack Ma', 1248484),
('MRK', 'Merk', 'Daniel Webb', 3290843),
('XOM', 'Exxon Mobil', 'Rex Tillerson', 2910983),
('GOOG', 'Alphabet', 'Larry Page', 28374637),
('AMZN', 'Amazon', 'Jeff Bezos', 129384789),
('AAPL', 'Apple', 'Tim Cook', 129847382),
('TGT', 'Target', 'Brian Cornell', 1982738),
('DOW', 'Dow Checmical', 'Andrew N. Liveris', 12323478),
('TWX', 'Time Warner', 'Jeff Bewkes', 8279381),
('TSN', 'Tyson Foods', 'Donnie Smith', 23891098),
('AXP', 'American Express', 'Kenneth I. Chenault', 23891092),
('TXN', 'Texas Instruments', 'Rich Templeton', 2909081),
('CBS', 'CBS', 'Leslie Moonves', 4092841),
('APA', 'Apache', 'John J Christmann', 2983712),
('MSFT', 'Microsoft', 'Satya Nadella', 2893716),
('V', 'Visa', 'Charles Scharf', 28798371),
('WMT', 'Wal-Mart', 'C. Douglas Mcmillon', 3892718),
('PG', 'Procter & Gamble', 'David S. Taylor', 3892710),
('NKE', 'Nike', 'Phil Knight', 18927381),
('CRM', 'Salesforce', 'Marc Benioff', 2997836),
('COF', 'Capital One', 'Richard Fairbank', 38910294),
('NVS', 'Novartis', 'Joseph Jimenez', 28937192),
('FL', 'Foot Locker', 'Richard A. Johnson', 389284),
('PRU', 'Prudential', 'John Strangfeld', 5982710);


INSERT INTO Stock_Prices VALUES
('C', NOW(), 52.49),
('JPM', NOW(), 74.27),
('BABA', NOW(), 96.01),
('CVS', NOW(), 77.51),
('BMY', NOW(), 54.49),
('MRK', NOW(), 61.47),
('XOM', NOW(), 85.38),
('GOOG', NOW(), 794.46),
('AMZN', NOW(), 766.98),
('AAPL', NOW(), 111.81),
('TGT', NOW(), 77.90),
('DOW', NOW(), 54.18),
('TWX', NOW(), 92.94),
('TSN', NOW(), 58.47),
('CBS', NOW(), 60.74),
('APA', NOW(), 62.91),
('MSFT', NOW(), 60.80),
('V', NOW(), 79.72),
('WMT', NOW(), 71.13),
('PG', NOW(), 83.05),
('NKE', NOW(), 50.78),
('CRM', NOW(), 73.00),
('COF', NOW(), 83.57),
('NVS', NOW(), 69.34),
('FL', NOW(), 72.95),
('PRU', NOW(), 99.15);

	
DELIMITER //

DROP PROCEDURE IF EXISTS update_stock//
CREATE PROCEDURE update_stock
(
	stock_ID  VARCHAR(10),
    new_price DOUBLE
)
BEGIN
UPDATE Stock_Prices
	SET Date_of = now(), Price = new_price
	WHERE company = stock_ID;
END
//


# procedure that returns the info of the portfolio
DROP PROCEDURE IF EXISTS get_portfolio;

DELIMITER //
CREATE PROCEDURE get_portfolio(IN Trader Text)
BEGIN
	SELECT Company, Amount
    FROM Portfolio
    WHERE Trader = Trader_name
    GROUP By Amount;
END
//


# procedure that returns the companys_info
DROP PROCEDURE IF EXISTS get_company_info;
DELIMITER //
CREATE PROCEDURE get_company_info(In CompanyID Text)
BEGIN 
	SELECT*
    FROM Company
    WHERE CompanyID = Company_ID;
END
//

#procedure that takes in a league and returns the users in it
DROP PROCEDURE IF EXISTS users_in_league;
DELIMITER // 
CREATE PROCEDURE users_in_league(In LeagueID Int)
BEGIN
	SELECT Trader_name
    FROM League
    WHERE LeagueID = Team_ID;

END
//


# procedure that deletes a user

DROP PROCEDURE IF EXISTS delete_user;
DELIMITER // 
CREATE PROCEDURE delete_user(In user_name Text)
BEGIN
	DELETE 
    FROM TRADERS
    WHERE Trader_name = user_name;

END
//

# procedure that returns total portfolio value
DROP PROCEDURE IF EXISTS
DELIMITER //
CREATE PROCEDURE portfolio_val(IN user_name Text)
BEGIN
	SELECT SUM(Price * Amount) AS Total_Value
    FROM PORTFOLIO p , STOCK_PRICES s
    WHERE p.Trader_name = user_name AND
		p.Company_name = s.Company;
END
//

# procedure that adds a trader into the league










