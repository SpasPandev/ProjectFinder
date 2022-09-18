-- users
INSERT INTO users (id, name, username, password, email, description)
VALUES (1, 'Admin', 'admin', '123321', 'admin@admin', 'Description for Admin lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque explicabo nemo
                    asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo voluptate quae rem
                    nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus
                    saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores
                    molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur
                    adipisicing elit. ');

INSERT INTO users (id, name, username, password, email, description)
VALUES (2, 'Pesho', 'pesho', '123321', 'pesho@pesho', 'Description for Pesho lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque explicabo nemo
                    asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo voluptate quae rem
                    nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus
                    saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores
                    molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur
                    adipisicing elit. ');

INSERT INTO users (id, name, username, password, email, description)
VALUES (3, 'Gosho', 'gosho', '123321', 'gosho@gosho', 'Description for Gosho lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque explicabo nemo
                    asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo voluptate quae rem
                    nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus
                    saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores
                    molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur
                    adipisicing elit. ');

INSERT INTO users (id, name, username, password, email, description)
VALUES (4, 'Bob', 'bob', '123321', 'bob@bob', 'Description for Bob lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque explicabo nemo
                    asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo voluptate quae rem
                    nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus
                    saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores
                    molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur
                    adipisicing elit. ');

INSERT INTO users (id, name, username, password, email, description)
VALUES (5, 'Dimo', 'dimo', '123321', 'dimo@dimo', 'Description for Dimo lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque explicabo nemo
                    asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo voluptate quae rem
                    nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus
                    saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores
                    molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur
                    adipisicing elit. ');
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

INSERT INTO users_roles (`user_entity_id`, `roles_id`)
VALUES (4, 2);

INSERT INTO users_roles (`user_entity_id`, `roles_id`)
VALUES (5, 1);

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
VALUES (4, 2);

INSERT INTO users_technologies (`user_entity_id`, `technologies_id`)
VALUES (5, 3);

-- project
INSERT INTO project (id, title, author_id, project_description)
VALUES (1, 'Gosho''s first project title', 3, 'Gosho''s first project description lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. ');

INSERT INTO project (id, title, author_id, project_description)
VALUES (2, 'Gosho''s second project title', 3, 'Gosho''s second project description lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. ');

INSERT INTO project (id, title, author_id, project_description)
VALUES (3, 'Bob''s first project title', 4, 'Bob''s first project description lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. ');

INSERT INTO project (id, title, author_id, project_description)
VALUES (4, 'Gosho''s third project title', 3, 'Gosho''s third project description lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. ');

INSERT INTO project (id, title, author_id, project_description)
VALUES (5, 'Bob''s second project title', 4, 'Bob''s second project description lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. ');

INSERT INTO project (id, title, author_id, project_description)
VALUES (6, 'Bob''s third project title', 4, 'Bob''s third project description lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur voluptatibus saepe, doloremque
                    explicabo nemo asperiores at corporis voluptatum sint blanditiis maiores dolores molestias illo
                    voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit amet consectetur adipisicing elit.
                    Consequuntur voluptatibus saepe, doloremque explicabo nemo asperiores at corporis voluptatum sint
                    blanditiis maiores dolores molestias illo voluptate quae rem nulla nobis sequi.Lorem ipsum dolor sit
                    amet consectetur adipisicing elit. ');

-- project technologies
INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (1, 1);

INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (2, 3);

INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (3, 2);

INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (4, 5);

INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (5, 4);

INSERT INTO project_technologies (`project_entity_id`, `technologies_id`)
VALUES (6, 1);

-- project participant
INSERT INTO project_participant (`project_id`, `participant_id`, `link`)
VALUES (1, 2, 'https://github.com/');

INSERT INTO project_participant (`project_id`, `participant_id`)
VALUES (2, 2);

INSERT INTO project_participant (`project_id`, `participant_id`, `link`)
VALUES (5, 2, 'https://github.com/');

INSERT INTO project_participant (`project_id`, `participant_id`, `link`)
VALUES (2, 5, 'https://github.com/');

INSERT INTO project_participant (`project_id`, `participant_id`, `link`)
VALUES (4, 5, 'https://github.com/');