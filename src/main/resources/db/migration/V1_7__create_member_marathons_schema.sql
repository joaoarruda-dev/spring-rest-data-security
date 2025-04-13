-- Tabela de associação entre Members e Marathons
CREATE TABLE IF NOT EXISTS `member_marathons` (
    member_id BIGINT NOT NULL,
    marathon_id BIGINT NOT NULL,
    PRIMARY KEY (member_id, marathon_id),
    FOREIGN KEY (member_id) REFERENCES members(id),
    FOREIGN KEY (marathon_id) REFERENCES marathons(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;