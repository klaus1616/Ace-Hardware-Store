BEGIN TRANSACTION;

DROP TABLE if EXISTS department, employee, vendor CASCADE;

CREATE TABLE department (
    department_id serial PRIMARY KEY,
    department_name VARCHAR(50) NOT NULL
);

CREATE TABLE employee (
    employee_id serial PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    hire_date DATE NOT NULL,
    leave_date DATE,
    phone_number VARCHAR(20) NOT NULL,
    hourly_rate DECIMAL NOT NULL,
    department_id INTEGER REFERENCES department(department_id)
);

CREATE TABLE vendor (
    vendor_id serial PRIMARY KEY,
    vendor_name VARCHAR(255) NOT NULL,
    contact_name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    department_id INTEGER REFERENCES department(department_id)
);

INSERT INTO department (department_name) VALUES ('Management');
INSERT INTO department (department_name) VALUES ('Sales');
INSERT INTO department (department_name) VALUES ('Customer Service');
INSERT INTO department (department_name) VALUES ('Outside Vendors');

INSERT INTO employee (employee_id, name, hire_date, phone_number, hourly_rate, department_id)
    VALUES  (592, 'Klay Lech', '2008-05-15', '847-000-0000', 25.00, 1),
            (400, 'Twanna Hardesty', '1998-04-20', '847-000-0000', 25.00, 1),
            (259, 'Steve Luke', '1996-01-11', '847-000-0000', 15.50, 2),
            (643, 'Tom Johnson', '2016-08-27', '847-000-0000', 14.75, 2),
            (692, 'Kyler Lambert', '2022-06-02', '847-000-0000', 14.50, 3),
            (695, 'Noah Walsh', '2023-04-16', '847-000-0000', 13.50, 3);

INSERT INTO vendor (vendor_name, contact_name, address, phone_number, department_id)
VALUES      ('Boom Window & Screen', 'Michelle', '161 Wheeling Rd. Wheeling, IL 60090', '847-459-6199', 4),
            ('M. B. Sharp LLC', 'George', '100 S Wolf Rd. Wheeling, IL 60090', '847-927-3758', 4);

COMMIT;