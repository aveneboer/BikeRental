INSERT INTO users (username, password, enabled, apikey, email) VALUES ('henk', 'password', true, '7847493', 'test@testy.tst');
INSERT INTO authorities (username, authority) VALUES ('henk', 'ROLE_ADMIN');

-- Customer
INSERT INTO customers (first_name, last_name, phone_no, email, address)
VALUES
    ('John', 'Doe', '123456789', 'john.doe@example.com', '123 Main Street'),
    ('Jane', 'Smith', '987654321', 'jane.smith@example.com', '456 Elm Street');

-- Bike
INSERT INTO bikes (brands, registration_numbers, hourly_price, is_Available)
VALUES
    ('Knaap', '12345', '15.50', 'true'),
    ('Knaap', '34859', '15.50', 'true'),
    ('Knaap', '841256', '15.50', 'true'),
    ('PhatFour', '54321', '10.75', 'true');
-- Car
INSERT INTO cars (model, capacity, day_price, quantity)
VALUES
    ('Toyota Highlander', 6, 85.00, 1),
    ('Toyota RAV4', 3, 60.00, 2);

-- Reservation
INSERT INTO reservations (start_date, end_date, type, bike_quantity, customer_id)
VALUES ('2023-06-15', '2023-06-17', 'Bike', 1, 1),
    ('2023-06-15', '2023-06-17', 'Bike', 2, 2);

-- Reservation-Bike mapping
INSERT INTO reservation_bike (reservation_id, bike_id)
VALUES (1, 1),
    (2, 3), (2, 4);


