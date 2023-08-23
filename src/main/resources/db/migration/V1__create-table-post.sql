CREATE TABLE post
(
    id          UUID         NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description VARCHAR(1000),
    CONSTRAINT pk_post PRIMARY KEY (id)
);