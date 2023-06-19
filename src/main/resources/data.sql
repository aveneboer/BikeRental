INSERT INTO users (username, password, enabled, apikey, email) VALUES ('henk', 'password', true, '7847493', 'test@testy.tst');
INSERT INTO authorities (username, authority) VALUES ('henk', 'ROLE_ADMIN');

-- Customer
INSERT INTO customers (first_name, last_name, phone_no, email, address)
VALUES
    ('John', 'Doe', '123456789', 'john.doe@example.com', '123 Main Street'),
    ('Jane', 'Smith', '987654321', 'jane.smith@example.com', '456 Elm Street');

-- Bike
INSERT INTO bikes (brands, quantity, registration_numbers, hourly_price)
VALUES
    ('Knaap', 5, '12345', '15.50'),
    ('PhatFour', 3, '54321', '10.75');
-- Car
INSERT INTO cars (model, capacity, day_price, quantity)
VALUES
    ('Toyota Highlander', 6, 85.00, 1),
    ('Toyota RAV4', 3, 60.00, 2);

-- Reservation
INSERT INTO reservations (start_date, end_date, type, customer_id)
VALUES
    ('2023-06-15 09:00:00', '2023-06-17 18:00:00', 'Bike', 1),
    ('2023-06-20 14:00:00', '2023-06-23 12:00:00', 'Car', 2);

-- ReservationLine
INSERT INTO reservation_lines (date_ordered, confirmation, status, payment_method, duration, total_price, reservation_id)
VALUES
    ('2023-06-14 10:00:00', 'ABCD1234', 'Confirmed', 'Credit Card', 3, 50, 1),
    ('2023-06-19 16:30:00', 'EFGH5678', 'Pending', 'Cash', 2, 6, 2);
