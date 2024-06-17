INSERT INTO user (id, username, password, name, email) VALUES
                        (1, 'yoonsung', '$2a$10$8HVLMZgJLqQTHcFgxgQiSu2FwpUUAHALnW7Iq0PFXkWAXe.Pv6Qqe', '정윤성', 'blck1592@gmail.com');


SELECT id, name
FROM authority
WHERE id=1;


insert into user_authorities(user_id, authority_id) VALUES
                                                        (1, 1);



select *
from user_authorities;

delete
from user_authorities
;

select *
from user;

delete
from user;