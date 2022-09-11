-- users
INSERT INTO users (id, name, username, password, email, description)
VALUES (1, 'Admin', 'admin', '11111', 'admin@admin', 'Description for admin');

INSERT INTO users (id, name, username, password, email, description)
VALUES (2, 'Pesho', 'pesho', '22222', 'pesho@pesho', 'Description for pesho');

INSERT INTO users (id, name, username, password, email, description)
VALUES (3, 'Gosho', 'gosho', '33333', 'gosho@gosho', 'Description for gosho');

-- roles
INSERT INTO roles (id, role)
VALUES (1, 'STUDENT');

INSERT INTO roles (id, role)
VALUES (2, 'COMPANY');

INSERT INTO roles (id, role)
VALUES (3, 'ADMIN');

-- users roles
INSERT INTO users_roles (`user_entity_id`, `roles_id`)
VALUES (1, 3);

INSERT INTO users_roles (`user_entity_id`, `roles_id`)
VALUES (2, 1);

INSERT INTO users_roles (`user_entity_id`, `roles_id`)
VALUES (3, 2);

-- technologies
INSERT INTO technologies (id, technologies)
VALUES (1, 'JAVA');

INSERT INTO technologies (id, technologies)
VALUES (2, 'PYTHON');

INSERT INTO technologies (id, technologies)
VALUES (3, 'C');

INSERT INTO technologies (id, technologies)
VALUES (4, 'JAVASCRIPT');

INSERT INTO technologies (id, technologies)
VALUES (5, 'PHP');

-- users technologies
INSERT INTO users_technologies (`user_entity_id`, `technologies_id`)
VALUES (2, 1);

INSERT INTO users_technologies (`user_entity_id`, `technologies_id`)
VALUES (3, 1);

INSERT INTO users_technologies (`user_entity_id`, `technologies_id`)
VALUES (3, 2);

-- project
INSERT INTO project (id, project_description, title, author_id)
VALUES (1, '1st project description', '1st title', '3');

INSERT INTO project (id, project_description, title, author_id)
VALUES (2, '2nd project description', '2nd title', '3');

INSERT INTO project (id, project_description, title, author_id)
VALUES (3, '3-th project description', '3-th title', '3');

-- project technologies
INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (1, 1);

INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (2, 3);

INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (3, 2);

-- project participant
INSERT INTO project_participant (`project_id`, `participant_id`)
VALUES (1, 2);

INSERT INTO project_participant (`project_id`, `participant_id`)
VALUES (2, 2);