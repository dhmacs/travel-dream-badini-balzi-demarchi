CREATE DATABASE travelDream;
CREATE USER 'user'@'localhost'
  IDENTIFIED BY 'password';
GRANT ALL ON travelDream.* TO 'user'@'localhost';
USE travelDream;

CREATE TABLE accomodations
(
  package_id INT NOT NULL,
  hotel_id   INT NOT NULL,
  PRIMARY KEY (package_id, hotel_id)
);
CREATE TABLE airport
(
  ID      INT PRIMARY KEY NOT NULL,
  NAME    VARCHAR(255),
  CITY_ID INT
);
CREATE TABLE city
(
  ID     INT PRIMARY KEY NOT NULL,
  NAME   VARCHAR(255),
  NATION VARCHAR(255)
);
CREATE TABLE custom_package
(
  ID                     INT PRIMARY KEY NOT NULL,
  ISAVAILABLE            TINYINT DEFAULT 0,
  accomodation_id        INT,
  excursion_id           INT,
  leaving_transport_id   INT,
  PARENTPACKAGE_ID       INT,
  returning_transport_id INT
);
CREATE TABLE desired_package
(
  user_mail         VARCHAR(255) NOT NULL,
  custom_package_id INT          NOT NULL,
  PRIMARY KEY (user_mail, custom_package_id)
);
CREATE TABLE excursion
(
  ID           INT PRIMARY KEY NOT NULL,
  DESCRIPTION  VARCHAR(255),
  ENDINGHOUR   DATETIME,
  ISACTIVE     TINYINT DEFAULT 0,
  NAME         VARCHAR(255),
  PRICE        DOUBLE,
  STARTINGHOUR DATETIME,
  CITY_ID      INT
);
CREATE TABLE hotel
(
  ID          INT PRIMARY KEY NOT NULL,
  DESCRIPTION VARCHAR(255),
  ISACTIVE    TINYINT DEFAULT 0,
  NAME        VARCHAR(255),
  PRICE       DOUBLE,
  CITY_ID     INT
);
CREATE TABLE leaving_transports
(
  package_id   INT NOT NULL,
  transport_id INT NOT NULL,
  PRIMARY KEY (package_id, transport_id)
);
CREATE TABLE purchased_excursion
(
  ID           INT PRIMARY KEY NOT NULL,
  DESCRIPTION  VARCHAR(255),
  ENDINGHOUR   DATETIME,
  NAME         VARCHAR(255),
  PRICE        DOUBLE,
  STARTINGHOUR DATETIME,
  CITY_ID      INT
);
CREATE TABLE purchased_hotel
(
  ID          INT PRIMARY KEY NOT NULL,
  DESCRIPTION VARCHAR(255),
  NAME        VARCHAR(255),
  PRICE       DOUBLE,
  CITY_ID     INT
);
CREATE TABLE purchased_package
(
  ID                    INT PRIMARY KEY NOT NULL,
  DESCRIPTION           LONGTEXT,
  DURATION              INT,
  NAME                  VARCHAR(255),
  NUMBEROFPARTICIPANTS  INT,
  BUYER_EMAIL           VARCHAR(255),
  DESTINATION_ID        INT,
  OWNER_EMAIL           VARCHAR(255),
  ACCOMODATION_ID       INT,
  EXCURSION_ID          INT,
  LEAVINGTRANSPORT_ID   INT,
  RETURNINGTRANSPORT_ID INT
);
CREATE TABLE purchased_transport
(
  ID                  INT PRIMARY KEY NOT NULL,
  DTYPE               VARCHAR(31),
  ARRIVAL             DATETIME,
  DEPARTURE           DATETIME,
  PRICE               DOUBLE,
  ARRIVALCITY_ID      INT,
  DEPARTURECITY_ID    INT,
  AIRLINE             VARCHAR(255),
  FLIGHTNUMBER        VARCHAR(255),
  ARRIVALAIRPORT_ID   INT,
  DEPARTUREAIRPORT_ID INT
);
CREATE TABLE returning_transports
(
  package_id   INT NOT NULL,
  transport_id INT NOT NULL,
  PRIMARY KEY (package_id, transport_id)
);
CREATE TABLE sequence
(
  SEQ_NAME  VARCHAR(50) PRIMARY KEY NOT NULL,
  SEQ_COUNT DECIMAL(38, 0)
);
CREATE TABLE transport
(
  ID                  INT PRIMARY KEY NOT NULL,
  DTYPE               VARCHAR(31),
  ARRIVAL             DATETIME,
  DEPARTURE           DATETIME,
  ISACTIVE            TINYINT DEFAULT 0,
  PRICE               DOUBLE,
  ARRIVALCITY_ID      INT,
  DEPARTURECITY_ID    INT,
  AIRLINE             VARCHAR(255),
  FLIGHTNUMBER        VARCHAR(255),
  ARRIVALAIRPORT_ID   INT,
  DEPARTUREAIRPORT_ID INT
);
CREATE TABLE travel_package
(
  ID                   INT PRIMARY KEY NOT NULL,
  DESCRIPTION          LONGTEXT,
  DURATION             INT,
  ISACTIVE             TINYINT DEFAULT 0,
  NAME                 VARCHAR(255),
  NUMBEROFPARTICIPANTS INT,
  DESTINATION_ID       INT
);
CREATE TABLE trip
(
  package_id   INT NOT NULL,
  excursion_id INT NOT NULL,
  PRIMARY KEY (package_id, excursion_id)
);
CREATE TABLE users
(
  EMAIL        VARCHAR(255) PRIMARY KEY NOT NULL,
  FIRSTNAME    VARCHAR(255),
  LASTNAME     VARCHAR(255),
  PASSWORD     VARCHAR(255),
  REGISTEREDON DATETIME
);
CREATE TABLE users_groups
(
  EMAIL     VARCHAR(255),
  GROUPNAME VARCHAR(255)
);
ALTER TABLE accomodations ADD FOREIGN KEY (hotel_id) REFERENCES hotel (ID);
ALTER TABLE accomodations ADD FOREIGN KEY (package_id) REFERENCES travel_package (ID);
ALTER TABLE airport ADD FOREIGN KEY (CITY_ID) REFERENCES city (ID);
ALTER TABLE custom_package ADD FOREIGN KEY (excursion_id) REFERENCES excursion (ID);
ALTER TABLE custom_package ADD FOREIGN KEY (accomodation_id) REFERENCES hotel (ID);
ALTER TABLE custom_package ADD FOREIGN KEY (leaving_transport_id) REFERENCES transport (ID);
ALTER TABLE custom_package ADD FOREIGN KEY (PARENTPACKAGE_ID) REFERENCES travel_package (ID);
ALTER TABLE custom_package ADD FOREIGN KEY (returning_transport_id) REFERENCES transport (ID);
ALTER TABLE desired_package ADD FOREIGN KEY (custom_package_id) REFERENCES custom_package (ID);
ALTER TABLE desired_package ADD FOREIGN KEY (user_mail) REFERENCES users (EMAIL);
ALTER TABLE excursion ADD FOREIGN KEY (CITY_ID) REFERENCES city (ID);
ALTER TABLE hotel ADD FOREIGN KEY (CITY_ID) REFERENCES city (ID);
ALTER TABLE leaving_transports ADD FOREIGN KEY (package_id) REFERENCES travel_package (ID);
ALTER TABLE leaving_transports ADD FOREIGN KEY (transport_id) REFERENCES transport (ID);
ALTER TABLE purchased_excursion ADD FOREIGN KEY (CITY_ID) REFERENCES city (ID);
ALTER TABLE purchased_hotel ADD FOREIGN KEY (CITY_ID) REFERENCES city (ID);
ALTER TABLE purchased_package ADD FOREIGN KEY (DESTINATION_ID) REFERENCES city (ID);
ALTER TABLE purchased_package ADD FOREIGN KEY (ACCOMODATION_ID) REFERENCES purchased_hotel (ID);
ALTER TABLE purchased_package ADD FOREIGN KEY (BUYER_EMAIL) REFERENCES users (EMAIL);
ALTER TABLE purchased_package ADD FOREIGN KEY (EXCURSION_ID) REFERENCES purchased_excursion (ID);
ALTER TABLE purchased_package ADD FOREIGN KEY (LEAVINGTRANSPORT_ID) REFERENCES purchased_transport (ID);
ALTER TABLE purchased_package ADD FOREIGN KEY (OWNER_EMAIL) REFERENCES users (EMAIL);
ALTER TABLE purchased_package ADD FOREIGN KEY (RETURNINGTRANSPORT_ID) REFERENCES purchased_transport (ID);
ALTER TABLE purchased_transport ADD FOREIGN KEY (DEPARTUREAIRPORT_ID) REFERENCES airport (ID);
ALTER TABLE purchased_transport ADD FOREIGN KEY (ARRIVALAIRPORT_ID) REFERENCES airport (ID);
ALTER TABLE purchased_transport ADD FOREIGN KEY (ARRIVALCITY_ID) REFERENCES city (ID);
ALTER TABLE purchased_transport ADD FOREIGN KEY (DEPARTURECITY_ID) REFERENCES city (ID);
ALTER TABLE returning_transports ADD FOREIGN KEY (package_id) REFERENCES travel_package (ID);
ALTER TABLE returning_transports ADD FOREIGN KEY (transport_id) REFERENCES transport (ID);
ALTER TABLE transport ADD FOREIGN KEY (ARRIVALCITY_ID) REFERENCES city (ID);
ALTER TABLE transport ADD FOREIGN KEY (ARRIVALAIRPORT_ID) REFERENCES airport (ID);
ALTER TABLE transport ADD FOREIGN KEY (DEPARTUREAIRPORT_ID) REFERENCES airport (ID);
ALTER TABLE transport ADD FOREIGN KEY (DEPARTURECITY_ID) REFERENCES city (ID);
ALTER TABLE travel_package ADD FOREIGN KEY (DESTINATION_ID) REFERENCES city (ID);
ALTER TABLE trip ADD FOREIGN KEY (excursion_id) REFERENCES excursion (ID);
ALTER TABLE trip ADD FOREIGN KEY (package_id) REFERENCES travel_package (ID);
ALTER TABLE users_groups ADD FOREIGN KEY (EMAIL) REFERENCES users (EMAIL);

/* USERS */
INSERT INTO users (EMAIL, FIRSTNAME, LASTNAME, PASSWORD, REGISTEREDON)
VALUES
  ("123456", "admin", "admin", "8C6976E5B5410415BDE908BD4DEE15DFB167A9C873FC4BB8A81F6F2AB448A918",
   "2014-01-01 00:00:00"),
  ("ale91lnd@gmail.com", "Alessandro", "Balzi", "A008BCB423EDD46DD39BF5633677D70D7E92F05295FA94AD86D67AD5A97FA4A7",
   "2014-01-01 00:00:00"),
  ("bado.91@me.com", "Federico", "Badini", "A008BCB423EDD46DD39BF5633677D70D7E92F05295FA94AD86D67AD5A97FA4A7",
   "2014-01-01 00:00:00"),
  ("massimodemarchi91@gmail.com", "Massimo", "De Marchi",
   "A008BCB423EDD46DD39BF5633677D70D7E92F05295FA94AD86D67AD5A97FA4A7", "2014-01-01 00:00:00");

/*USERS_GROUPS */
INSERT INTO users_groups (EMAIL, GROUPNAME)
VALUES
  ("ale91lnd@gmail.com", "USER"),
  ("bado.91@me.com", "USER"),
  ("massimodemarchi91@gmail.com", "USER"),
  ("123456", "ADMIN");


/* CITY */
INSERT INTO city (ID, NAME, NATION)
VALUES
  (1, "Milan", "Italy"),
(2, "Rome", "Italy"),
(3, "London", "UK"),
(4, "Paris", "France"),
(5, "Madrid", "Spain"),
(6, "New York", "USA"),
(7, "Berlin", "Germany"),
(8, "Tokyo", "Japan"),
(9, "Sidney", "Australia"),
(10, "Beijing", "China"),
(11, "Barcelona", "Spain"),
  (12, "Montreal", "Canada"),
  (13, "Athens", "Greece"),
  (14, "Istanbul", "Turkey"),
  (15, "Oslo", "Norway"),
  (16, "Porto", "Portugal"),
  (17, "Florence", "Italy"),
  (18, "Sao Paulo", "Brasil"),
  (19, "Zurich", "Swiss"),
  (20, "Stockholm", "Sweden");


/* AIRPORT */
INSERT INTO airport (ID, NAME, CITY_ID)
VALUES
  (1, "Malpensa", 1),
(2, "Linate", 1),
(3, "Fiumicino", 2),
(4, "Heathrow", 3),
(5, "Gatewick", 3),
(6, "Stansted", 3),
(7, "Luton", 3),
(8, "Charles De Gaulle", 4),
(9, "Barajas", 5),
(10, "Newark", 6),
(11, "Willy Brandt", 7),
(12, "Narita", 8),
(13, "Haneda", 8),
(14, "Kingsford Smith", 9),
(15, "Pechino-Capital", 10),
(16, "El Prat", 11),
(17, "Pierre Elliott Trudeau", 12),
(18, "Eleftherios Venizelos", 13),
(19, "Ataturk", 14),
(20, "Gardermoen", 15),
(21, "Francisco de Sa Carneiro", 16),
  (22, "Peretola", 17),
  (23, "Cumbica", 18),
  (24, "Flughafen", 19),
  (25, "Arlanda", 20);

/* PACKAGE */
INSERT INTO travel_package (ID, DESCRIPTION, DURATION, ISACTIVE, NAME, NUMBEROFPARTICIPANTS, DESTINATION_ID)
VALUES
  (1, "Visit the City of Rome and the ruins of the great empire. Our locations will provide you with the maximum
   comfort and will allow you to stay in a place unique in the world, whether you want to explore
   tirelessly the city or to spend some time relaxing.", 5, TRUE, "Discover Rome", 2, 2),
  (2, "Some say that in new york things happen faster than anywhere else: anyone who visited Manhattan was struck by a city that
  impresses with its grandeur and modernity for its continuous movement. A unique place in the world where you  find a mixture of cultures,
  traditions, always looking to the future. You can not describe New York, it must be lived.", 7, TRUE, "The big apple",
   1, 6),
  (3, "London is just few clicks from you! Visit the city relying on a wide selection of hotels at good price. What are you doing? Hurry up
   and grab the chance. And remember! Do not forget your umbrella!", 2, TRUE, "London calling", 2, 3),
  (4, "Stunning scenery, unspoiled nature, friendly people ... What do you need more? You'll be fascinated by
  all this if you decide to pack your bags, buy the ticket and start your Australian adventure. You'll love the city of Sidney too: it's modern, young,
   dynamic and green. Aren't you fascinated by all this? Book now your journey to the other side of the world!", 10,
   TRUE, "Explore the new world", 1, 9),
  (5, "Immerse yourself in the culture and traditions of Japan. Visit ancient temples, eat sushi, drink sake
   and let yourself be seduced by the sensual geisha!", 10, TRUE, "Oriental experience", 1, 8);

/* TRANSPORT */
INSERT INTO transport (ID, DTYPE, ARRIVAL, DEPARTURE, ISACTIVE, PRICE, ARRIVALCITY_ID, DEPARTURECITY_ID, AIRLINE, FLIGHTNUMBER, ARRIVALAIRPORT_ID, DEPARTUREAIRPORT_ID)
VALUES
  (1, "Flight", "2014-01-01 10:00:00", "2014-01-01 00:00:00", TRUE, 100, 1, 2, "Alitalia", "AE538", 1, 3),
(2, "Flight", "2014-01-01 04:00:00", "2014-01-01 00:00:00", TRUE, 200, 2, 1, "Alitalia", "AL232", 3, 1),
(3, "Flight", "2014-01-01 03:00:00", "2014-01-01 00:00:00", TRUE, 300, 1, 6, "Alitalia", "BY564", 1, 10),
(4, "Flight", "2014-01-01 02:00:00", "2014-01-01 00:00:00", TRUE, 50, 4, 2, "Ryanair", "GZ328", 8, 2),
(5, "Flight", "2014-01-01 06:00:00", "2014-01-01 00:00:00", TRUE, 80, 5, 3, "Ryanair", "RY245", 9, 6),
(6, "Flight", "2014-01-01 05:00:00", "2014-01-01 00:00:00", TRUE, 40, 3, 2, "Ryanair", "RA323", 6, 3),
(7, "Flight", "2014-01-01 04:00:00", "2014-01-01 00:00:00", TRUE, 250, 8, 1, "Emirates", "EM798", 12, 1),
(8, "Flight", "2014-01-01 03:00:00", "2014-01-01 00:00:00", TRUE, 450, 8, 3, "Emirates", "FL492", 13, 4),
(9, "Flight", "2014-01-01 16:00:00", "2014-01-01 15:00:00", TRUE, 1200, 6, 3, "Emirates", "HE502", 10, 5),
(10, "Flight", "2014-01-01 10:00:00", "2014-01-01 00:00:00", TRUE, 800, 6, 8, "Air British", "DP666", 10, 12),
(11, "Flight", "2014-01-08 17:00:00", "2014-01-08 14:00:00", TRUE, 500, 3, 6, "Air British", "AB594", 5, 10),
(12, "Flight", "2014-01-13 17:00:00", "2014-01-13 14:00:00", TRUE, 100, 6, 2, "Air British", "RM334", 10, 3),
(13, "Flight", "2014-01-11 17:00:00", "2014-01-11 14:00:00", TRUE, 200, 4, 8, "Air France", "AF124", 8, 13),
(14, "Flight", "2014-01-01 17:00:00", "2014-01-01 14:00:00", TRUE, 300, 8, 4, "Air France", "AF794", 12, 8),
(15, "Flight", "2014-01-08 17:00:00", "2014-01-08 14:00:00", TRUE, 400, 2, 6, "Air France", "NY024", 3, 10),
(16, "Flight", "2014-01-08 17:00:00", "2014-01-08 14:00:00", TRUE, 400, 8, 6, "United Airlines", "UA999", 12, 10),
(17, "Flight", "2014-01-08 17:00:00", "2014-01-08 14:00:00", TRUE, 500, 8, 6, "United Airlines", "US777", 12, 10),
(18, "Flight", "2014-01-08 17:00:00", "2014-01-08 14:00:00", TRUE, 600, 2, 6, "United Airlines", "NF404", 3, 10),
(19, "Flight", "2014-01-08 17:00:00", "2014-01-08 14:00:00", TRUE, 700, 3, 6, "American Airlines", "ER121", 5, 10),
(20, "Flight", "2014-01-08 17:00:00", "2014-01-08 14:00:00", TRUE, 800, 3, 6, "American Airlines", "MPO80", 5, 10),
(21, "Flight", "2014-02-08 17:00:00", "2014-02-08 14:00:00", TRUE, 800, 6, 8, "United Airlines", "UA221", 10, 12),
  (22, "Flight", "2014-02-08 17:00:00", "2014-02-08 14:00:00", TRUE, 900, 6, 8, "United Airlines", "US343", 10, 12),
  (23, "Flight", "2014-02-08 17:00:00", "2014-02-08 14:00:00", TRUE, 1000, 6, 2, "United Airlines", "NF433", 10, 3),
  (24, "Flight", "2014-02-08 17:00:00", "2014-02-08 14:00:00", TRUE, 2200, 6, 3, "American Airlines", "ER411", 10, 5),
  (25, "Flight", "2014-02-08 17:00:00", "2014-02-08 14:00:00", TRUE, 1100, 6, 3, "American Airlines", "MP555", 10, 5),
  (26, "Flight", "2014-02-15 17:00:00", "2014-02-15 14:00:00", TRUE, 1200, 8, 6, "United Airlines", "UA322", 12, 10),
  (27, "Flight", "2014-02-15 17:00:00", "2014-02-15 14:00:00", TRUE, 1300, 8, 6, "United Airlines", "US333", 12, 10),
  (28, "Flight", "2014-02-15 17:00:00", "2014-02-15 14:00:00", TRUE, 520, 2, 6, "United Airlines", "NF808", 3, 10),
  (29, "Flight", "2014-02-15 17:00:00", "2014-02-15 14:00:00", TRUE, 300, 3, 6, "American Airlines", "ER232", 5, 10),
  (30, "Flight", "2014-02-15 17:00:00", "2014-02-15 14:00:00", TRUE, 900, 3, 6, "American Airlines", "MP444", 5, 10);


/* HOTEL */
INSERT INTO hotel (ID, DESCRIPTION, ISACTIVE, NAME, PRICE, CITY_ID)
VALUES
  (1, "4 star hotel, located in the center with a view of the cathedral.", TRUE, "Belvedere", 150, 1),
  (2, "5 star deluxe hotel. Terrace facing the Colosseumn. Stunning.", TRUE, "Colosseo", 250, 2),
  (3, "3 star hotel, located in the south of tokyo but a few steps from the metro", TRUE, "Sensei", 70, 8),
  (4, "5 stars. Famous Hilton hotel just a short walk from Westminster", TRUE, "Hilton", 180, 3),
  (5, "2 star hotel, located 2 steps from time square. unbeatable price", TRUE, "Time", 40, 6),
  (6, "3 star hotel, just a short walk from WTC", TRUE, "Splendid", 90, 6);

/* EXCURSION */
INSERT INTO excursion (ID, DESCRIPTION, ENDINGHOUR, ISACTIVE, NAME, PRICE, STARTINGHOUR, CITY_ID)
VALUES
  (1, "Visit the epic Roman gladiatorial arena", "11:00:00", TRUE, "Colosseo", 20, "10:00:00", 2),
  (2, "Side by side to the \"Madonnina\" overlook Milan in all its beauty", "11:00:00", TRUE, "\"Duomo\" of Milan", 8,
   "10:00:00", 1),
  (3, "The most famous modern art gallery in the world. Works of all the most famous artists from 800 until today",
   "16:00:00", TRUE, "Tate Modern", 15, "15:00:00", 3),
  (4, "The American most famous symbol. Follow the torch straight to NY", "17:00:00", TRUE, "Statue of liberty", 30,
   "15:00:00", 6),
  (5, "Visit the Mt. Fuji: see stunning landscapes climbing the highest mountain in Japan", "18:00:00", TRUE,
   "Mount Fuji", 50, "09:00:00", 8);

/* LEAVING TRANSPORTS */
INSERT INTO leaving_transports (package_id, transport_id)
VALUES
  (2, 9),
  (2, 10),
  (2, 12),
  (2, 21),
  (2, 22),
  (2, 23),
  (2, 24),
  (2, 25),
  (1, 15),
  (5, 14);

/* RETURNING TRANSPORTS */
INSERT INTO returning_transports (package_id, transport_id)
VALUES
  (2, 3),
(2, 11),
(2, 15),
(2, 16),
(2, 17),
(2, 18),
(2, 19),
(2, 20),
(2, 26),
(2, 27),
(2, 28),
  (2, 29),
  (2, 30),
  (1, 12),
  (5, 13);

/* ACCOMODATIONS */
INSERT INTO accomodations (package_id, hotel_id)
VALUES
  (2, 5),
  (2, 6),
  (1, 2);

/* EXCURSIONS */
INSERT INTO trip (package_id, excursion_id)
VALUES
  (2, 4);

/* TRIGGER */
delimiter $$
CREATE TRIGGER deleteForeignExcursions
BEFORE DELETE ON excursion
FOR EACH ROW
  BEGIN
    DELETE FROM trip
    WHERE excursion_id = OLD.ID;
    UPDATE custom_package
    SET isavailable = FALSE, excursion_id = null
    WHERE excursion_id = OLD.ID;
  END;
$$

delimiter $$
CREATE TRIGGER deleteForeignTransports
BEFORE DELETE ON transport
FOR EACH ROW
  BEGIN
    DELETE FROM leaving_transports
    WHERE transport_id = OLD.ID;
    DELETE FROM returning_transports
    WHERE transport_id = OLD.ID;
    UPDATE custom_package
    SET isavailable = FALSE, leaving_transport_id = null
    WHERE leaving_transport_id = OLD.ID;
    UPDATE custom_package
    SET isavailable = FALSE, returning_transport_id = null
    WHERE returning_transport_id = OLD.ID;
  END;
$$

delimiter $$
CREATE TRIGGER deleteForeignHotels
BEFORE DELETE ON hotel
FOR EACH ROW
  BEGIN
    DELETE FROM accomodations
    WHERE hotel_id = OLD.ID;
    UPDATE custom_package
    SET isavailable = FALSE, accomodation_id = null
    WHERE accomodation_id = OLD.ID;
  END;
$$
delimiter $$
CREATE TRIGGER deleteForeignPackage
BEFORE DELETE ON travel_package
FOR EACH ROW
  BEGIN
    UPDATE custom_package
    SET isavailable = FALSE, parentpackage_id = null
    WHERE parentpackage_id = OLD.ID;
  END;
$$



/* UPDATE SEQ_COUNTER */
INSERT INTO sequence (SEQ_NAME, SEQ_COUNT)
VALUES ('SEQ_GEN', 100);