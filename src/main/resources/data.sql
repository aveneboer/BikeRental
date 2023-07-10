-- Insert User (Sumardika) as ADMIN
INSERT INTO users (username, password, apikey, email, enabled)
VALUES ('sumardika', '$2a$10$3so2nMV8MkOKZzKPIcYGZussrzPrXTT0o97oq5vneSstbblDKLIhu', '7847493', 'test@testy.tst', true);
INSERT INTO authorities (username, authority)
VALUES ('sumardika', 'ROLE_ADMIN');

-- Insert Customers
INSERT INTO customers (first_name, last_name, phone_no, email, address)
VALUES
    ('John', 'Doe', '123456789', 'john.doe@example.com', '123 Main Street'),
    ('Jane', 'Smith', '987654321', 'jane.smith@example.com', '456 Elm Street');

-- Insert Bikes
INSERT INTO bikes (brands, registration_numbers, is_Available)
VALUES
    ('Knaap', '12345', 'true'),
    ('Knaap', '34859', 'true'),
    ('Knaap', '841256', 'true'),
    ('PhatFour', '54321', 'true');

-- Insert Cars
INSERT INTO cars (model, capacity, quantity, is_Available)
VALUES
    ('Toyota Highlander', 6, 1, 'true'),
    ('Toyota RAV4', 3, 2, 'true');

-- Insert Reservations
INSERT INTO reservations (start_date, end_date, type, bike_quantity, customer_id)
VALUES ('2023-06-15', '2023-06-17', 'Bike', 1, 1),
       ('2023-06-15', '2023-06-17', 'Bike', 2, 2);

-- Insert Reservation-Bike mapping
INSERT INTO reservation_bike (reservation_id, bike_id)
VALUES (1, 1),
       (2, 3), (2, 4);

-- Insert ReservationLine
INSERT INTO reservation_lines (date_ordered, confirmation, duration, total_price, reservation_id)
VALUES
    ('2023-06-15 10:00:00', 'ABC123', 2, 50.0, 1),
    ('2023-06-15 10:00:00', 'XYZ789', 2, 80.0, 2);

