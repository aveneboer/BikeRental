INSERT INTO users (username, password, enabled, apikey, email) VALUES ('henk', 'password', true, '7847493', 'test@testy.tst');
INSERT INTO authorities (username, authority) VALUES ('henk', 'ROLE_ADMIN');

-- Customer
INSERT INTO customers (first_name, last_name, phone_no, email, address)
VALUES
    ('John', 'Doe', '123456789', 'john.doe@example.com', '123 Main Street'),
    ('Jane', 'Smith', '987654321', 'jane.smith@example.com', '456 Elm Street');

-- Bike
INSERT INTO bikes (brands, registration_numbers, is_Available)
VALUES
    ('Knaap', '12345', 'true'),
    ('Knaap', '34859', 'true'),
    ('Knaap', '841256', 'true'),
    ('PhatFour', '54321', 'true');
-- Car
INSERT INTO cars (model, capacity, quantity, is_Available)
VALUES
    ('Toyota Highlander', 6, 1, 'true'),
    ('Toyota RAV4', 3, 2, 'true');

-- Reservation
INSERT INTO reservations (start_date, end_date, type, bike_quantity, customer_id)
VALUES ('2023-06-15', '2023-06-17', 'Bike', 1, 1),
    ('2023-06-15', '2023-06-17', 'Bike', 2, 2);

-- Reservation-Bike mapping
INSERT INTO reservation_bike (reservation_id, bike_id)
VALUES (1, 1),
    (2, 3), (2, 4);


