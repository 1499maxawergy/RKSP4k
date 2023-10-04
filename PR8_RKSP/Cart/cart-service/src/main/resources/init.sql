CREATE TABLE IF NOT EXISTS user_item
(
    user_id BIGINT REFERENCES "user" (id),
    item_id BIGINT REFERENCES item (id),
    count   INT
);

-- Создаем функцию, которая добавляет ограничение, если его нет
CREATE OR REPLACE FUNCTION add_unique_constraint_if_not_exists()
    RETURNS VOID AS '
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = ''unique_user_item'') THEN
            -- Если ограничение не существует, добавляем его
            EXECUTE ''ALTER TABLE user_item ADD CONSTRAINT unique_user_item UNIQUE (user_id, item_id)'';
        END IF;
    END;
' LANGUAGE plpgsql;

-- Вызываем функцию для добавления ограничения
SELECT add_unique_constraint_if_not_exists();

-- Удаляем временную функцию (по желанию)
DROP FUNCTION IF EXISTS add_unique_constraint_if_not_exists();





