ALTER TABLE user_profiles ADD CONSTRAINT email_unq UNIQUE (email);
ALTER TABLE user_profiles ADD CONSTRAINT phone_number_unq UNIQUE (phone_number);