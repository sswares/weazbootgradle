CREATE TABLE auth_custom_user (
  id           BIGINT generated BY DEFAULT AS IDENTITY,
  favorite_cat VARCHAR(255) NOT NULL,
  first_name   VARCHAR(255) NOT NULL,
  last_name    VARCHAR(255) NOT NULL,
  password     VARCHAR(255) NOT NULL,
  username     VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE auth_custom_user
  ADD CONSTRAINT UK_d66jngf1dx51jkmg9q2moq8cb UNIQUE (username);

INSERT INTO auth_custom_user (username, password, first_name, last_name, favorite_cat) VALUES ('walker', 'danger', 'walker', 'berry', 'poogle');