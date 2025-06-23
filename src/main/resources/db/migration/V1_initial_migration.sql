CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    name        VARCHAR(255)            NOT NULL,
    email       VARCHAR(255)            NOT NULL,
    password    VARCHAR(255)            NOT NULL,
    orcidId     VARCHAR(255),           #optional
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE profiles (
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    position    VARCHAR(255)            NOT NULL,
    description LONGTEXT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE posts (
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    postType    VARCHAR(255)            NOT NULL,
    view        VARCHAR(255)            NOT NULL,
    description LONGTEXT                NOT NULL,
    date        VARCHAR(255)            NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE swipeCards (
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    name        VARCHAR(255)            NOT NULL,
    position    VARCHAR(255)            NOT NULL,
    description LONGTEXT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE Table relevantExperiences (
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    title       VARCHAR(255)            NOT NULL,
    description LONGTEXT                NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
)

