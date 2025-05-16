ALTER TABLE `users`
ADD COLUMN uuid VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'UUID唯一标识符';

-- Update existing users with random UUIDs
UPDATE `users`
SET uuid = UUID()
WHERE uuid IS NULL;

-- Add unique constraint
ALTER TABLE `users`
ADD CONSTRAINT uk_users_uuid UNIQUE (uuid); 