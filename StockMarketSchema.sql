DROP DATABASE IF EXISTS StockMarket;
CREATE DATABASE IF NOT EXISTS StockMarket;
USE StockMarket;

/*
trader names as well as the traders returns and its available funds
*/
DROP TABLE IF EXISTS Traders;
CREATE TABLE Traders (
Trader_Name VARCHAR(64) PRIMARY KEY,
Available_Funds DOUBLE(10, 2),
Team VARCHAR(64)
);

/*
details of company
as well as how many shares are left,
must check if there are existing shares before the shares
for a company can be bought
*/
DROP TABLE IF EXISTS Company;
CREATE TABLE Company (
Company_ID Varchar(10) PRIMARY KEY,
Company_name Varchar(64),
CEO Varchar(64),
OutStanding_Shares INT 
);

/*
list of transactions 
for when a person buys/sells a stock
*/
DROP TABLE IF EXISTS Transactions;
CREATE TABLE Transactions(
transaction_ID INT auto_increment,
Date_of DATE,
Company_ID VARCHAR(10),
Trader VARCHAR(64),
Quantity INT,
Buy TINYINT,
PRIMARY KEY (transaction_ID),
CONSTRAINT trader_fk FOREIGN KEY(Trader) references Traders(Trader_name) ON DELETE CASCADE,
CONSTRAINT company_fk FOREIGN KEY(Company_ID) references Company(Company_ID)
);

DROP TABLE IF EXISTS Stock_Prices;
CREATE TABLE Stock_Prices(
Company VARCHAR(10),
Date_of DATE,
Price DOUBLE(10, 2),
PRIMARY KEY(Company, Date_of),
FOREIGN KEY(Company) references Company(Company_ID)
);


/*

how many of what stock does each player own

*/

DROP TABLE IF EXISTS Portfolio;
CREATE TABLE Portfolio(
Company VARCHAR(10),
Trader_name VARCHAR(64),
Amount INT(11),
PRIMARY KEY(Company, Trader_name),
FOREIGN KEY(Company) references Company(Company_ID),
FOREIGN KEY(Trader_name) references Traders(Trader_name) ON DELETE CASCADE
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
    new_price DOUBLE(10,2)
)
BEGIN
UPDATE Stock_Prices
	SET Date_of = now(), Price = new_price
	WHERE company = stock_ID;
END //

# GIVEN THE COMPANY, TRADER NAME, AND PRICE OF THE STOCK, THIS WILL INCREASE THE TRADER'S
# AVAILABLE FUNDS BY THE PRICE PASSED IN, AND DECREMENT THEIR STOCK COUNT OF THE COMPANY'S STOCK
# BY 1.
DROP PROCEDURE IF EXISTS sell_stock//
CREATE PROCEDURE sell_stock (stock_ID VARCHAR(10), trader VARCHAR(64), price DOUBLE(10, 2), qty INT)
	BEGIN
		UPDATE Portfolio
		SET Amount = Amount - qty
		WHERE Trader_name = trader AND Company = stock_ID;
		UPDATE Traders
		SET Available_Funds = Available_Funds + (price * qty)
		WHERE Trader_name = trader;
		INSERT INTO Transactions VALUES (DEFAULT, NOW(), stock_ID, trader, qty, FALSE);
	END //

# GIVEN THE COMPANY, TRADER NAME, AND PRICE OF THE STOCK, THIS PROCEDURE WILL DECREMENT THE
# TRADER'S AVAILABLE FUNDS BY THE PRICE PASSED IN, AND INCREASE THE TRADER'S STOCK COUNT OF THE GIVEN
# COMPANY BY 1.
DROP PROCEDURE IF EXISTS buy_stock//
CREATE PROCEDURE buy_stock (stock_ID VARCHAR(10), trader VARCHAR(64), price DOUBLE(10, 2), qty INT)
	BEGIN
    IF NOT EXISTS (SELECT * FROM Portfolio WHERE trader_name = trader AND company = stock_id)
    THEN
      INSERT INTO Portfolio VALUES (stock_ID, trader, 0);
    END IF;
		UPDATE Portfolio
		SET Amount = Amount + qty
		WHERE Trader_name = trader AND Company = stock_ID;
		UPDATE Traders
			SET Available_Funds = Available_Funds - (price * qty)
		WHERE Trader_name = trader;
		INSERT INTO Transactions VALUES (DEFAULT, NOW(), stock_ID, trader, qty, TRUE);
	END //

# GIVEN THE TRADER NAME, THIS FUNCTION WILL RETURN THE TOTAL VALUE OF THE TRADER.
DROP FUNCTION IF EXISTS get_trader_value//
CREATE FUNCTION get_trader_value (trader VARCHAR(64))
	RETURNS DOUBLE
	BEGIN
		DECLARE funds DOUBLE(10,2);
		DECLARE stock_value DOUBLE(10,2);
		SELECT Available_Funds
		INTO funds
		FROM(
			SELECT Available_Funds
			FROM Traders
			WHERE Trader_name = trader
		    ) AS avail_funds;
		IF EXISTS (SELECT * FROM portfolio WHERE trader_name = trader)
			THEN
		SELECT stk_value
		INTO stock_value 
		FROM(
			SELECT SUM(p.Amount * s.Price) as stk_value
			FROM Portfolio p , Stock_prices s
			WHERE p.Trader_name = trader AND
			      p.Company = s.Company
		    ) AS stk_value;
				ELSE
					SELECT 0 INTO stock_value;
					END IF;
		RETURN funds + stock_value;
	END //

DELIMITER ;






