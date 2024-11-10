ALTER TABLE categories
RENAME COLUMN owner TO owner_id;

ALTER TABLE categories
DROP CONSTRAINT categories_owner_fkey,
ADD CONSTRAINT categories_owner_id_fkey FOREIGN KEY (owner_id) REFERENCES users (id);