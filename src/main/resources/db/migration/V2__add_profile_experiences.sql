CREATE TABLE profileExperiences(
    id          BIGINT AUTO_INCREMENT   NOT NULL,
    profile_id  BIGINT                  NOT NULL,
    title       VARCHAR(255)            NOT NULL,
    date        VARCHAR(255)            NOT NULL,
    description LONGTEXT                NOT NULL,

    CONSTRAINT `pk_profileExperiences` PRIMARY KEY (id),

    CONSTRAINT `fk_profileExperiences_profiles`
        FOREIGN KEY (profile_id) REFERENCES profiles(id)
        ON DELETE CASCADE
);

#Indexing to query and show all of the user's experiences on their table
CREATE INDEX `idx_profileExperiences_profile_id` ON profileExperiences(profile_id)