ALTER TABLE categories
RENAME COLUMN user_id TO owner_id;

ALTER TABLE categories
ADD CONSTRAINT categories_owner_id_fkey FOREIGN KEY (owner_id) REFERENCES users (id);