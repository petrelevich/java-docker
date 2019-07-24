CREATE TABLE account(
    user_id serial PRIMARY KEY,
    user_name VARCHAR (50) UNIQUE NOT NULL
);