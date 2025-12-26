USE weibo;
UPDATE user SET password = '$2a$10$1Mr52DAvuejqznqFRMPfFeGTu3eBwyqIuDXJJi2B9xR2IyHgele.q' WHERE username = 'admin' OR username = 'testuser';