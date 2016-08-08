CREATE TABLE custom_client (
  id            BIGINT generated BY DEFAULT AS IDENTITY,
  client_name   VARCHAR(255) NOT NULL,
  client_secret VARCHAR(255) NOT NULL,
  scopes        VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE custom_client
  ADD CONSTRAINT UK_hda0p1o4gqw8axitje3rqyqv7 UNIQUE (client_name);

INSERT INTO custom_client (client_name, client_secret, scopes) VALUES ('acme', 'acmesecret', 'the_world');