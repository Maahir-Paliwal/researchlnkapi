CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    name        VARCHAR(255)            NOT NULL,
    email       VARCHAR(255)            NOT NULL,
    password    VARCHAR(255)            NOT NULL,
    orcidId     VARCHAR(255),                                                                  #optional
    created_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, #info to display to the user
    CONSTRAINT `pk_users` PRIMARY KEY (id)
);

CREATE TABLE profiles (
    id              BIGINT                  NOT NULL,
    position        VARCHAR(255)            NOT NULL,
    description     LONGTEXT                NOT NULL,
    profile_picture VARCHAR(512)            NOT NULL,              #profile picture functionality

    CONSTRAINT `pk_profiles` PRIMARY KEY (id),

    CONSTRAINT `fk_profiles_user`                                  #OnetoOne relationship with user
        FOREIGN KEY (id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE posts (
    id          BIGINT AUTO_INCREMENT               NOT NULL,
    profile_id  BIGINT                              NOT NULL,
    postType    VARCHAR(255)                        NOT NULL,
    view        ENUM('PUBLIC', 'CONNECTIONS ONLY')  NOT NULL,
    description LONGTEXT                            NOT NULL,
    date        VARCHAR(255)                        NOT NULL,
    created_at  TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,       #useful for sorting for recency

    CONSTRAINT `pk_posts` PRIMARY KEY (id),

    CONSTRAINT `fk_posts_profiles`                           #profile ID keeps track of who owns each post
        FOREIGN KEY (profile_id) REFERENCES profiles(id)
        ON DELETE CASCADE
);

CREATE TABLE swipeCards (
    id          BIGINT                  NOT NULL,
    name        VARCHAR(255)            NOT NULL,
    position    VARCHAR(255)            NOT NULL,
    description LONGTEXT                NOT NULL,

    CONSTRAINT `pk_swipeCards` PRIMARY KEY (id),

    CONSTRAINT `fk_swipeCards_profiles`                      #OnetoOne relationship between SwipeCard and Profile
        FOREIGN KEY (id) REFERENCES profiles(id)
        ON DELETE CASCADE
);

CREATE TABLE relevantExperiences (
    id              BIGINT AUTO_INCREMENT   NOT NULL,
    card_id         BIGINT                  NOT NULL,
    title           VARCHAR(255)            NOT NULL,
    date            VARCHAR(255)            NOT NULL,
    description     LONGTEXT                NOT NULL,

    CONSTRAINT `pk_relevantExperiences` PRIMARY KEY (id),

    CONSTRAINT `fk_relevantExperiences_swipeCards`                          #OneToThree relationship
        FOREIGN KEY (card_id) REFERENCES swipeCards(id)
        ON DELETE CASCADE

);

CREATE TABLE connections (
    connector_id      BIGINT                       NOT NULL,
    connectee_id      BIGINT                       NOT NULL,
    status            ENUM('PENDING', 'ACCEPTED')  DEFAULT 'PENDING',
    created_at        TIMESTAMP                    DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP          DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, #when we go from pending
                                                                                                #to accepted

    CONSTRAINT `pk_connections` PRIMARY KEY (connector_id, connectee_id),   #Each row identified by a unique connection pair

    CONSTRAINT `fk_connections_connector`                                   #connector profile
        FOREIGN KEY (connector_id) REFERENCES profiles(id)
        ON DELETE CASCADE,

    CONSTRAINT `fk_connections_connectee`                                   #connectee profile
        FOREIGN KEY (connectee_id) REFERENCES profiles(id)                  #connector and connectee are arbitrary
        ON DELETE CASCADE,                                                  #in terms of connecting

    CONSTRAINT `chk_connections_ordering`                                   #Enforces symmetry
        CHECK (connector_id < connectee_id)
);



#Indexing here allows us to query for a profile's posts quickly (displaying posts on their profile)
CREATE INDEX `idx_posts_profile_id` ON posts(profile_id);

#Indexing here allows us to query for a user's connections (since they can either be a connector or connectee)
CREATE INDEX `idx_connections_connector_id` ON connections(connector_id);
CREATE INDEX `idx_connections_connectee_id` ON connections(connectee_id);

#Indexing here allows us to query for a swipecard's relevant experiences
CREATE INDEX `idx_experiences_card_id` ON relevantExperiences(card_id);

#Indexing here allows us to make sure each email and username is unique
CREATE UNIQUE INDEX `idx_user_email` ON users(email);


#Indexing here allows me to search for connections that have been ACCEPTED
CREATE INDEX `idx_connections_status` ON connections(status)


#ADD MORE KEYS HERE IF I THINK OF THINGS I WILL NEED TO ACCESS QUICKLY IN MY DB
