DROP DATABASE IF EXISTS golfclub;
CREATE DATABASE golfclub;
USE golfclub;

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS tournament_members;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS tournaments;

CREATE TABLE members (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_name VARCHAR(50) NOT NULL,
    member_address VARCHAR(255) NOT NULL,
    member_email VARCHAR(255) UNIQUE NOT NULL,
    member_phone VARCHAR(15) UNIQUE NOT NULL,
    start_date DATE NOT NULL,
    duration INT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    total_tournaments_played INT DEFAULT 0,
    total_winnings DECIMAL(10,2) DEFAULT 0.00,
    version BIGINT DEFAULT 0
);

CREATE TABLE tournaments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    location VARCHAR(100) NOT NULL,
    entry_fee DECIMAL(10,2) NOT NULL,
    cash_prize_amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    minimum_participants INT NOT NULL DEFAULT 2,
    maximum_participants INT NOT NULL DEFAULT 100,
    version BIGINT DEFAULT 0
);

CREATE TABLE tournament_members (
    tournament_id BIGINT,
    member_id BIGINT,
    PRIMARY KEY (tournament_id, member_id),
    FOREIGN KEY (tournament_id) REFERENCES tournaments(id) ON DELETE CASCADE,
    FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE
);

-- Add indexes
CREATE INDEX idx_member_email ON members(member_email);
CREATE INDEX idx_member_phone ON members(member_phone);
CREATE INDEX idx_tournament_date ON tournaments(start_date);
CREATE INDEX idx_tournament_location ON tournaments(location);

-- Insert members
INSERT INTO members (member_name, member_address, member_email, member_phone, start_date, duration, status)
VALUES
('Brenda Armstrong', 'Witless Bay', 'hello@brendaarmstrong.ca', '709-682-6878', '2024-11-18', 12, 'ACTIVE'),
('Stephan Bendiksen', 'St Johns', 'stephan@bendiksen.net', '709-699-2680', '2024-11-19', 60, 'ACTIVE'),
('Louis Armstrong', 'Ottawa', 'smacko@gmail.com', '709-999-9999', '2024-11-19', 36, 'ACTIVE'),
('Jamie Cornick', 'Mount Pearl', 'jamie@testgolf.com', '709-888-8888', '2024-11-19', 36, 'ACTIVE'),
('Maurice Belbin', 'CBS', 'maurice@biggolf.com', '555-555-5555', '2024-11-19', 48, 'ACTIVE');

-- Insert tournaments
INSERT INTO tournaments (start_date, end_date, location, entry_fee, cash_prize_amount, minimum_participants, maximum_participants, status)
VALUES
('2024-06-15', '2024-06-18', 'Clovelly Golf Club', 150.00, 2000.00, 2, 50, 'SCHEDULED'),
('2024-07-01', '2024-07-03', 'Bally Haly Country Club', 200.00, 3000.00, 4, 40, 'SCHEDULED'),
('2024-08-15', '2024-08-17', 'Glendenning Golf', 175.00, 2500.00, 3, 45, 'SCHEDULED'),
('2024-09-01', '2024-09-03', "Admiral's Green", 225.00, 3500.00, 5, 60, 'SCHEDULED'),
('2024-10-01', '2024-10-03', 'The Wilds', 180.00, 2800.00, 3, 55, 'SCHEDULED');

-- Insert tournament_members relationships
INSERT INTO tournament_members (tournament_id, member_id)
VALUES
(1, 1), (1, 2),
(2, 3), (2, 4),
(3, 5), (3, 1),
(4, 2), (4, 3),
(5, 4), (5, 5);

SET FOREIGN_KEY_CHECKS = 1;