-- Sample data for testing
INSERT INTO users (name, email, created_at, updated_at) VALUES 
('김철수', 'kim@example.com', NOW(), NOW()),
('이영희', 'lee@example.com', NOW(), NOW()),
('박민수', 'park@example.com', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;
