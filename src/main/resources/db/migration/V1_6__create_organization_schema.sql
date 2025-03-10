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