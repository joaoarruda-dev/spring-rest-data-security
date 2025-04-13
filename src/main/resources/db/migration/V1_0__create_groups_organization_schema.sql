
CREATE OR REPLACE SEQUENCE `organization_seq` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 nocache nocycle ENGINE=InnoDB;

-- Tabela Organization
CREATE TABLE IF NOT EXISTS `organization` (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              name VARCHAR(255) NOT NULL,
                                              address_number VARCHAR(50),
                                              address_street VARCHAR(255),
                                              address_neighborhood VARCHAR(255),
                                              address_zipcode VARCHAR(20),
                                              address_city VARCHAR(255),
                                              address_state VARCHAR(255),
                                              institution_name VARCHAR(255),
                                              country VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- sample.groups definition

CREATE OR REPLACE SEQUENCE `groups_seq` start with 1 minvalue 1 maxvalue 9223372036854775806 increment by 1 nocache nocycle ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `groups` (
    `id` bigint(20) NOT NULL,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`),
    organization_id INT NOT NULL,
    FOREIGN KEY (organization_id) REFERENCES `organization`(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;