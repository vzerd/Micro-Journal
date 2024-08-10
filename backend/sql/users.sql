SELECT * FROM users;
SELECT id FROM users WHERE token = '0506b4ae-1770-4cd8-89ed-2a2a1f05424e';
DELETE FROM users WHERE name='Tiasha Hait';
ALTER SEQUENCE users_id_seq RESTART WITH 1;
UPDATE users SET token = null WHERE token = something;