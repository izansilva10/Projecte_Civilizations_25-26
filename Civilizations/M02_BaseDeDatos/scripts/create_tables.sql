-- 1. Tabla principal de la civilización 
CREATE TABLE IF NOT EXISTS civilization_stats (
    civilization_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    wood_amount INT DEFAULT 0 NOT NULL,
    iron_amount INT DEFAULT 0 NOT NULL,
    food_amount INT DEFAULT 0 NOT NULL,
    mana_amount INT DEFAULT 0 NOT NULL,
    magicTower_counter INT DEFAULT 0 NOT NULL,
    church_counter INT DEFAULT 0 NOT NULL,
    farm_counter INT DEFAULT 0 NOT NULL,
    smithy_counter INT DEFAULT 0 NOT NULL,
    carpentry_counter INT DEFAULT 0 NOT NULL,
    technology_defense_level INT DEFAULT 0 NOT NULL,
    technology_attack_level INT DEFAULT 0 NOT NULL,
    battles_counter INT DEFAULT 0 NOT NULL
);

-- 2. Tabla: unidades de ataque
CREATE TABLE IF NOT EXISTS attack_units_stats (
    civilization_id INT NOT NULL,
    unit_id INT NOT NULL,
    type ENUM('Swordsman','Spearman','Crossbow','Cannon') NOT NULL,
    armor INT NOT NULL,
    base_damage INT NOT NULL,
    experience INT DEFAULT 0 NOT NULL,
    sanctified TINYINT(1) DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, unit_id),
    FOREIGN KEY (civilization_id) REFERENCES civilization_stats(civilization_id) ON DELETE CASCADE
);

-- 3. Tabla: unidades de defensa
CREATE TABLE IF NOT EXISTS defense_units_stats (
    civilization_id INT NOT NULL,
    unit_id INT NOT NULL,
    type ENUM('ArrowTower','Catapult','RocketLauncherTower') NOT NULL,
    armor INT NOT NULL,
    base_damage INT NOT NULL,
    experience INT DEFAULT 0 NOT NULL,
    sanctified TINYINT(1) DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, unit_id),
    FOREIGN KEY (civilization_id) REFERENCES civilization_stats(civilization_id) ON DELETE CASCADE
);

-- 4. Tabla: unidades especiales
CREATE TABLE IF NOT EXISTS special_units_stats (
    civilization_id INT NOT NULL,
    unit_id INT NOT NULL,
    type ENUM('Magician','Priest') NOT NULL,
    armor INT NOT NULL,
    base_damage INT NOT NULL,
    experience INT DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, unit_id),
    FOREIGN KEY (civilization_id) REFERENCES civilization_stats(civilization_id) ON DELETE CASCADE
);

-- 5. Tabla: estadísticas de batalla
CREATE TABLE IF NOT EXISTS battle_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    wood_acquired INT DEFAULT 0 NOT NULL,
    iron_acquired INT DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, num_battle),
    FOREIGN KEY (civilization_id) REFERENCES civilization_stats(civilization_id) ON DELETE CASCADE
);

-- 6. Tabla: log de batalla
CREATE TABLE IF NOT EXISTS battle_log (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    num_line INT NOT NULL,
    log_entry VARCHAR(4000) NOT NULL,
    PRIMARY KEY (civilization_id, num_battle, num_line),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

-- 7. Tabla: estadísticas de ataque civilización por batalla
CREATE TABLE IF NOT EXISTS civilization_attack_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type ENUM('Swordsman','Spearman','Crossbow','Cannon') NOT NULL,
    initial INT DEFAULT 0 NOT NULL,
    drops INT DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

-- 8. Tabla: estadísticas de defensa civilización por batalla
CREATE TABLE IF NOT EXISTS civilization_defense_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type ENUM('ArrowTower','Catapult','RocketLauncherTower') NOT NULL,
    initial INT DEFAULT 0 NOT NULL,
    drops INT DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

-- 9. Tabla: estadísticas de unidades especiales civilización por batalla
CREATE TABLE IF NOT EXISTS civilization_special_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type ENUM('Magician','Priest') NOT NULL,
    initial INT DEFAULT 0 NOT NULL,
    drops INT DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);

-- 10. Tabla: estadísticas de ataque enemigo por batalla
CREATE TABLE IF NOT EXISTS enemy_attack_stats (
    civilization_id INT NOT NULL,
    num_battle INT NOT NULL,
    type ENUM('Swordsman','Spearman','Crossbow','Cannon') NOT NULL,
    initial INT DEFAULT 0 NOT NULL,
    drops INT DEFAULT 0 NOT NULL,
    PRIMARY KEY (civilization_id, num_battle, type),
    FOREIGN KEY (civilization_id, num_battle) REFERENCES battle_stats(civilization_id, num_battle) ON DELETE CASCADE
);