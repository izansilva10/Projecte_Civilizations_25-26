-- ============================================================
-- M02 Base de Datos - Civilizations
-- Script de creación de tablas (Oracle SQL)
-- ============================================================

-- Limpiar tablas previas (orden inverso por dependencias)
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE enemy_attack_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE civilization_special_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE civilization_defense_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE civilization_attack_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE battle_log CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE battle_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE special_units_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE defense_units_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE attack_units_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE civilization_stats CASCADE CONSTRAINTS';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

-- ============================================================
-- 1. Tabla: civilization_stats
-- ============================================================
CREATE TABLE civilization_stats (
    civilization_id         NUMBER          NOT NULL,
    name                    VARCHAR2(100)   NOT NULL,
    wood_amount             NUMBER          DEFAULT 0 NOT NULL,
    iron_amount             NUMBER          DEFAULT 0 NOT NULL,
    food_amount             NUMBER          DEFAULT 0 NOT NULL,
    mana_amount             NUMBER          DEFAULT 0 NOT NULL,
    magicTower_counter      NUMBER          DEFAULT 0 NOT NULL,
    church_counter          NUMBER          DEFAULT 0 NOT NULL,
    farm_counter            NUMBER          DEFAULT 0 NOT NULL,
    smithy_counter          NUMBER          DEFAULT 0 NOT NULL,
    carpentry_counter       NUMBER          DEFAULT 0 NOT NULL,
    technology_defense_level NUMBER         DEFAULT 0 NOT NULL,
    technology_attack_level NUMBER          DEFAULT 0 NOT NULL,
    battles_counter         NUMBER          DEFAULT 0 NOT NULL,
    CONSTRAINT pk_civilization_stats PRIMARY KEY (civilization_id)
);

-- ============================================================
-- 2. Tabla: attack_units_stats
-- ============================================================
CREATE TABLE attack_units_stats (
    civilization_id NUMBER        NOT NULL,
    unit_id         NUMBER        NOT NULL,
    type            VARCHAR2(20)  NOT NULL,
    armor           NUMBER        NOT NULL,
    base_damage     NUMBER        NOT NULL,
    experience      NUMBER        DEFAULT 0 NOT NULL,
    sanctified      NUMBER(1)     DEFAULT 0 NOT NULL,  -- 0 = false, 1 = true
    CONSTRAINT pk_attack_units PRIMARY KEY (civilization_id, unit_id),
    CONSTRAINT fk_attack_units_civilization FOREIGN KEY (civilization_id)
        REFERENCES civilization_stats (civilization_id) ON DELETE CASCADE,
    CONSTRAINT chk_attack_type CHECK (type IN ('Swordsman','Spearman','Crossbow','Cannon'))
);

-- ============================================================
-- 3. Tabla: defense_units_stats
-- ============================================================
CREATE TABLE defense_units_stats (
    civilization_id NUMBER        NOT NULL,
    unit_id         NUMBER        NOT NULL,
    type            VARCHAR2(25)  NOT NULL,
    armor           NUMBER        NOT NULL,
    base_damage     NUMBER        NOT NULL,
    experience      NUMBER        DEFAULT 0 NOT NULL,
    sanctified      NUMBER(1)     DEFAULT 0 NOT NULL,
    CONSTRAINT pk_defense_units PRIMARY KEY (civilization_id, unit_id),
    CONSTRAINT fk_defense_units_civilization FOREIGN KEY (civilization_id)
        REFERENCES civilization_stats (civilization_id) ON DELETE CASCADE,
    CONSTRAINT chk_defense_type CHECK (type IN ('ArrowTower','Catapult','RocketLauncherTower'))
);

-- ============================================================
-- 4. Tabla: special_units_stats
-- ============================================================
CREATE TABLE special_units_stats (
    civilization_id NUMBER        NOT NULL,
    unit_id         NUMBER        NOT NULL,
    type            VARCHAR2(20)  NOT NULL,
    armor           NUMBER        NOT NULL,
    base_damage     NUMBER        NOT NULL,
    experience      NUMBER        DEFAULT 0 NOT NULL,
    CONSTRAINT pk_special_units PRIMARY KEY (civilization_id, unit_id),
    CONSTRAINT fk_special_units_civilization FOREIGN KEY (civilization_id)
        REFERENCES civilization_stats (civilization_id) ON DELETE CASCADE,
    CONSTRAINT chk_special_type CHECK (type IN ('Magician','Priest'))
);

-- ============================================================
-- 5. Tabla: battle_stats
-- ============================================================
CREATE TABLE battle_stats (
    civilization_id NUMBER NOT NULL,
    num_battle      NUMBER NOT NULL,
    wood_acquired   NUMBER DEFAULT 0 NOT NULL,
    iron_acquired   NUMBER DEFAULT 0 NOT NULL,
    CONSTRAINT pk_battle_stats PRIMARY KEY (civilization_id, num_battle),
    CONSTRAINT fk_battle_stats_civilization FOREIGN KEY (civilization_id)
        REFERENCES civilization_stats (civilization_id) ON DELETE CASCADE
);

-- ============================================================
-- 6. Tabla: battle_log
-- ============================================================
CREATE TABLE battle_log (
    civilization_id NUMBER        NOT NULL,
    num_battle      NUMBER        NOT NULL,
    num_line        NUMBER        NOT NULL,
    log_entry       VARCHAR2(4000) NOT NULL,
    CONSTRAINT pk_battle_log PRIMARY KEY (civilization_id, num_battle, num_line),
    CONSTRAINT fk_battle_log_battle FOREIGN KEY (civilization_id, num_battle)
        REFERENCES battle_stats (civilization_id, num_battle) ON DELETE CASCADE
);

-- ============================================================
-- 7. Tabla: civilization_attack_stats
-- ============================================================
CREATE TABLE civilization_attack_stats (
    civilization_id NUMBER        NOT NULL,
    num_battle      NUMBER        NOT NULL,
    type            VARCHAR2(20)  NOT NULL,
    initial         NUMBER        DEFAULT 0 NOT NULL,
    drops           NUMBER        DEFAULT 0 NOT NULL,
    CONSTRAINT pk_civilization_attack_stats PRIMARY KEY (civilization_id, num_battle, type),
    CONSTRAINT fk_civ_attack_battle FOREIGN KEY (civilization_id, num_battle)
        REFERENCES battle_stats (civilization_id, num_battle) ON DELETE CASCADE,
    CONSTRAINT chk_civ_attack_type CHECK (type IN ('Swordsman','Spearman','Crossbow','Cannon'))
);

-- ============================================================
-- 8. Tabla: civilization_defense_stats
-- ============================================================
CREATE TABLE civilization_defense_stats (
    civilization_id NUMBER        NOT NULL,
    num_battle      NUMBER        NOT NULL,
    type            VARCHAR2(25)  NOT NULL,
    initial         NUMBER        DEFAULT 0 NOT NULL,
    drops           NUMBER        DEFAULT 0 NOT NULL,
    CONSTRAINT pk_civilization_defense_stats PRIMARY KEY (civilization_id, num_battle, type),
    CONSTRAINT fk_civ_defense_battle FOREIGN KEY (civilization_id, num_battle)
        REFERENCES battle_stats (civilization_id, num_battle) ON DELETE CASCADE,
    CONSTRAINT chk_civ_defense_type CHECK (type IN ('ArrowTower','Catapult','RocketLauncherTower'))
);

-- ============================================================
-- 9. Tabla: civilization_special_stats
-- ============================================================
CREATE TABLE civilization_special_stats (
    civilization_id NUMBER        NOT NULL,
    num_battle      NUMBER        NOT NULL,
    type            VARCHAR2(20)  NOT NULL,
    initial         NUMBER        DEFAULT 0 NOT NULL,
    drops           NUMBER        DEFAULT 0 NOT NULL,
    CONSTRAINT pk_civilization_special_stats PRIMARY KEY (civilization_id, num_battle, type),
    CONSTRAINT fk_civ_special_battle FOREIGN KEY (civilization_id, num_battle)
        REFERENCES battle_stats (civilization_id, num_battle) ON DELETE CASCADE,
    CONSTRAINT chk_civ_special_type CHECK (type IN ('Magician','Priest'))
);

-- ============================================================
-- 10. Tabla: enemy_attack_stats
-- ============================================================
CREATE TABLE enemy_attack_stats (
    civilization_id NUMBER        NOT NULL,
    num_battle      NUMBER        NOT NULL,
    type            VARCHAR2(20)  NOT NULL,
    initial         NUMBER        DEFAULT 0 NOT NULL,
    drops           NUMBER        DEFAULT 0 NOT NULL,
    CONSTRAINT pk_enemy_attack_stats PRIMARY KEY (civilization_id, num_battle, type),
    CONSTRAINT fk_enemy_attack_battle FOREIGN KEY (civilization_id, num_battle)
        REFERENCES battle_stats (civilization_id, num_battle) ON DELETE CASCADE,
    CONSTRAINT chk_enemy_attack_type CHECK (type IN ('Swordsman','Spearman','Crossbow','Cannon'))
);

COMMIT;