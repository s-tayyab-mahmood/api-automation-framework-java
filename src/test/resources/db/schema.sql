CREATE TABLE guests (
    id              INT PRIMARY KEY,
    firstname       VARCHAR(50)  NOT NULL,
    lastname        VARCHAR(50)  NOT NULL,
    totalprice      INT          NOT NULL,
    depositpaid     BOOLEAN      NOT NULL,
    nights          INT          NOT NULL,
    additionalneeds VARCHAR(100),
    active          BOOLEAN      DEFAULT TRUE NOT NULL
);

-- bookings created during the run, for cleanup
CREATE TABLE created_bookings (
    booking_id  INT PRIMARY KEY,
    firstname   VARCHAR(50) NOT NULL,
    lastname    VARCHAR(50) NOT NULL,
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted     BOOLEAN     DEFAULT FALSE NOT NULL
);
