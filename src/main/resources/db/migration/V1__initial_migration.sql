CREATE TABLE users (
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    email       VARCHAR(255)            NOT NULL,
    password    VARCHAR(255)                    ,                                              #NULL b/c Oauth
    orcid_id    VARCHAR(255),                                                                  #optional
    created_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, #info to display to the user
    CONSTRAINT `pk_users` PRIMARY KEY (id)
);

CREATE TABLE profiles (
    id              BIGINT                  NOT NULL,
    public_id       CHAR(36)                NOT NULL DEFAULT (uuid()),
    name            VARCHAR(255)            NOT NULL,
    position        VARCHAR(255)            NOT NULL,
    description     LONGTEXT                NOT NULL,
    profile_picture VARCHAR(512)            NOT NULL,              #profile picture functionality

    CONSTRAINT `pk_profiles` PRIMARY KEY (id),
    CONSTRAINT `uq_profiles_public_id` UNIQUE (public_id),

    CONSTRAINT `fk_profiles_user`                                  #OnetoOne relationship with user
        FOREIGN KEY (id) REFERENCES users(id)
        ON DELETE CASCADE
);

CREATE TABLE posts (
    id          BIGINT AUTO_INCREMENT               NOT NULL,
    profile_id  BIGINT                              NOT NULL,
    post_type   VARCHAR(255)                        NOT NULL,
    view        ENUM('PUBLIC', 'CONNECTIONS_ONLY')  NOT NULL,
    description LONGTEXT                            NOT NULL,
    created_at  TIMESTAMP                           DEFAULT CURRENT_TIMESTAMP,       #useful for sorting for recency

    CONSTRAINT `pk_posts` PRIMARY KEY (id),

    CONSTRAINT `fk_posts_profiles`                           #profile ID keeps track of who owns each post
        FOREIGN KEY (profile_id) REFERENCES profiles(id)
        ON DELETE CASCADE
);

CREATE TABLE swipe_cards (
    id          BIGINT                  NOT NULL,
    name        VARCHAR(255)            NOT NULL,
    position    VARCHAR(255)            NOT NULL,
    description LONGTEXT                NOT NULL,
    CONSTRAINT `pk_swipe_cards` PRIMARY KEY (id),

    CONSTRAINT `fk_swipe_cards_profiles`                      #OnetoOne relationship between SwipeCard and Profile
        FOREIGN KEY (id) REFERENCES profiles(id)
        ON DELETE CASCADE
);

CREATE TABLE relevant_experiences (
    id              BIGINT AUTO_INCREMENT   NOT NULL,
    card_id         BIGINT                  NOT NULL,
    title           VARCHAR(255)            NOT NULL,
    start_at        TIMESTAMP               NOT NULL,
    end_at          TIMESTAMP                   NULL,           #Null implies that the role is current
    description     LONGTEXT                NOT NULL,

    CONSTRAINT `pk_relevant_experiences` PRIMARY KEY (id),

    CONSTRAINT `fk_relevant_experiences_swipe_cards`                          #ZeroToThree relationship
        FOREIGN KEY (card_id) REFERENCES swipe_cards(id)
        ON DELETE CASCADE

);

CREATE TABLE connections (
    connector_id      BIGINT                       NOT NULL,
    connectee_id      BIGINT                       NOT NULL,
    requester_id      BIGINT                       NOT NULL,
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

    CONSTRAINT `fk_connections_requester`                                   #requester id
        FOREIGN KEY (requester_id) REFERENCES profiles(id)
        ON DELETE CASCADE,

    CONSTRAINT `chk_connections_ordering`                                   #Enforces symmetry
        CHECK (connector_id < connectee_id)
);



#Indexing here allows us to query for a profile's posts quickly (displaying posts on their profile)
CREATE INDEX `idx_posts_profile_id` ON posts(profile_id);

#Indexing here allows us to query for a user's connections (since they can either be a connector or connectee)
CREATE INDEX `idx_connections_connector_id` ON connections(connector_id);
CREATE INDEX `idx_connections_connectee_id` ON connections(connectee_id);

#Indexing here allows us to query for a swipecard's relevant experiences to sort by time
CREATE INDEX `idx_re_card_sort` ON relevant_experiences(card_id, start_at, end_at);

#Indexing here allows us to make sure each email and username is unique
CREATE UNIQUE INDEX `idx_user_email` ON users(email);


#Indexing here allows me to search for connections that have been ACCEPTED
CREATE INDEX `idx_connections_status` ON connections(status)


#ADD MORE KEYS HERE IF I THINK OF THINGS I WILL NEED TO ACCESS QUICKLY IN MY DB