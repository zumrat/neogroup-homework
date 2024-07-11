CREATE TABLE countries
(
    id   BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50)
);

CREATE TABLE country_codes
(
    id           BIGINT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    country_code VARCHAR(10) NOT NULL,
    country_id   BIGINT      NOT NULL,

    FOREIGN KEY (country_id) REFERENCES countries (id)
)