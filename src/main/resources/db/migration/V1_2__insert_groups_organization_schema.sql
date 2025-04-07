

INSERT INTO organization (id, name, address_number, address_street, address_neighborhood, address_zipcode, address_city, address_state, institution_name, country)
VALUES (NEXT VALUE FOR organization_seq, 'Organization Name', '123', 'Main Street', 'Downtown', '12345', 'City', 'State', 'Institution Name', 'Country');
-- sample.groups test data

INSERT INTO groups (id, name, organization_id) VALUES(NEXT VALUE FOR groups_seq, 'Grupo 1 - Teste', 1);
INSERT INTO groups (id, name, organization_id) VALUES(NEXT VALUE FOR groups_seq, 'Grupo 2 - Teste', 1);
INSERT INTO groups (id, name, organization_id) VALUES(NEXT VALUE FOR groups_seq, 'Grupo 3 - Teste', 1);