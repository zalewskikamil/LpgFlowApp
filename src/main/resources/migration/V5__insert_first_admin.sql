DO $$
DECLARE
   v_user_id BIGINT;
   v_role_id BIGINT;
BEGIN

SELECT id INTO v_role_id FROM role WHERE name = 'ADMIN';

INSERT INTO users (name, last_name, email, password, phone_number)
VALUES ('Kamil', 'Zalewski', 'zalewskikamil89@gmail.com',
        '$2a$10$J.o7Ck9kJq5mkUHqPuEU8eNsEI4ebg1c5Q3dRkthTK58.pCXx/E3y', '123456789')
    RETURNING id INTO v_user_id;

INSERT INTO user_roles (user_id, role_id)
VALUES (v_user_id, v_role_id);

END $$;