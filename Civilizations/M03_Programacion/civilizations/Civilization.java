package civilizations;

import java.util.ArrayList;

public class Civilization {
    private int technologyDefense;
    private int technologyAttack;
    private int wood, iron, food, mana;
    private int magicTower, church, farm, smithy, carpentry;
    private int battles;
    private ArrayList<ArrayList<MilitaryUnit>> army;

    public Civilization() {
        technologyDefense = 0;
        technologyAttack = 0;
        wood = 0;
        iron = 0;
        food = 0;
        mana = 0;
        magicTower = 0;
        church = 0;
        farm = 0;
        smithy = 0;
        carpentry = 0;
        battles = 0;
        army = new ArrayList<ArrayList<MilitaryUnit>>();
        for (int i = 0; i < 9; i++) {
            army.add(new ArrayList<MilitaryUnit>());
        }
    }

    public int getTechnologyDefense() { return technologyDefense; }
    public int getTechnologyAttack() { return technologyAttack; }
    public int getWood() { return wood; }
    public int getIron() { return iron; }
    public int getFood() { return food; }
    public int getMana() { return mana; }
    public int getMagicTower() { return magicTower; }
    public int getChurch() { return church; }
    public int getFarm() { return farm; }
    public int getSmithy() { return smithy; }
    public int getCarpentry() { return carpentry; }
    public int getBattles() { return battles; }

    public ArrayList<MilitaryUnit>[] getArmy() {
        ArrayList<MilitaryUnit>[] array = (ArrayList<MilitaryUnit>[]) new ArrayList[9];
        for (int i = 0; i < 9; i++) {
            array[i] = army.get(i);
        }
        return array;
    }

    public void addWood(int w) { wood += w; }
    public void addIron(int i) { iron += i; }
    public void addFood(int f) { food += f; }
    public void addMana(int m) { mana += m; }
    public void setBattles(int b) { battles = b; }

    public void newFarm() throws ResourceException {
        if (food >= Variables.FOOD_COST_FARM && wood >= Variables.WOOD_COST_FARM && iron >= Variables.IRON_COST_FARM) {
            food -= Variables.FOOD_COST_FARM;
            wood -= Variables.WOOD_COST_FARM;
            iron -= Variables.IRON_COST_FARM;
            farm++;
        } else {
            throw new ResourceException("Recursos insuficientes para construir Granja");
        }
    }

    public void newCarpentry() throws ResourceException {
        if (food >= Variables.FOOD_COST_CARPENTRY && wood >= Variables.WOOD_COST_CARPENTRY && iron >= Variables.IRON_COST_CARPENTRY) {
            food -= Variables.FOOD_COST_CARPENTRY;
            wood -= Variables.WOOD_COST_CARPENTRY;
            iron -= Variables.IRON_COST_CARPENTRY;
            carpentry++;
        } else {
            throw new ResourceException("Recursos insuficientes para construir Carpintería");
        }
    }

    public void newSmithy() throws ResourceException {
        if (food >= Variables.FOOD_COST_SMITHY && wood >= Variables.WOOD_COST_SMITHY && iron >= Variables.IRON_COST_SMITHY) {
            food -= Variables.FOOD_COST_SMITHY;
            wood -= Variables.WOOD_COST_SMITHY;
            iron -= Variables.IRON_COST_SMITHY;
            smithy++;
        } else {
            throw new ResourceException("Recursos insuficientes para construir Herrería");
        }
    }

    public void newMagicTower() throws ResourceException {
        if (food >= Variables.FOOD_COST_MAGICTOWER && wood >= Variables.WOOD_COST_MAGICTOWER && iron >= Variables.IRON_COST_MAGICTOWER) {
            food -= Variables.FOOD_COST_MAGICTOWER;
            wood -= Variables.WOOD_COST_MAGICTOWER;
            iron -= Variables.IRON_COST_MAGICTOWER;
            magicTower++;
        } else {
            throw new ResourceException("Recursos insuficientes para construir Torre Mágica");
        }
    }

    public void newChurch() throws ResourceException {
        if (food >= Variables.FOOD_COST_CHURCH && wood >= Variables.WOOD_COST_CHURCH && iron >= Variables.IRON_COST_CHURCH) {
            food -= Variables.FOOD_COST_CHURCH;
            wood -= Variables.WOOD_COST_CHURCH;
            iron -= Variables.IRON_COST_CHURCH;
            church++;
        } else {
            throw new ResourceException("Recursos insuficientes para construir Iglesia");
        }
    }

    public void upgradeTechnologyDefense() throws ResourceException {
        int ironCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST + technologyDefense * Variables.UPGRADEPLUS_DEFENSE_TECHNOLOGY_IRON_COST;
        int woodCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_WOOD_COST + technologyDefense * Variables.UPGRADE_PLUS_DEFENSE_TECHNOLOGY_WOOD_COST;
        if (iron >= ironCost && wood >= woodCost) {
            iron -= ironCost;
            wood -= woodCost;
            technologyDefense++;
        } else {
            throw new ResourceException("Recursos insuficientes para mejorar tecnología de defensa");
        }
    }

    public void upgradeTechnologyAttack() throws ResourceException {
        int ironCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST + technologyAttack * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST;
        int woodCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_WOOD_COST + technologyAttack * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_WOOD_COST;
        if (iron >= ironCost && wood >= woodCost) {
            iron -= ironCost;
            wood -= woodCost;
            technologyAttack++;
        } else {
            throw new ResourceException("Recursos insuficientes para mejorar tecnología de ataque");
        }
    }

    private void addUnits(int type, int n) throws ResourceException, BuildingException {
        if (type == 7 && magicTower < 1) throw new BuildingException("Se necesita al menos una Torre Mágica para crear Magos");
        if (type == 8 && church < 1) throw new BuildingException("Se necesita al menos una Iglesia para crear Sacerdotes");

        int foodCost = Variables.FOOD_COST_UNITS[type];
        int woodCost = Variables.WOOD_COST_UNITS[type];
        int ironCost = Variables.IRON_COST_UNITS[type];
        int manaCost = (type == 7) ? Variables.MANA_COST_MAGICIAN : (type == 8) ? Variables.MANA_COST_PRIEST : 0;

        int created = 0;
        for (int i = 0; i < n; i++) {
            if (food >= foodCost && wood >= woodCost && iron >= ironCost && mana >= manaCost) {
                MilitaryUnit unit = createUnitByType(type);
                if (unit != null) {
                    army.get(type).add(unit);
                    food -= foodCost;
                    wood -= woodCost;
                    iron -= ironCost;
                    mana -= manaCost;
                    created++;
                }
            } else {
                break;
            }
        }
        if (created < n) {
            throw new ResourceException("Solo se pudieron añadir " + created + " de " + n + " unidades (recursos insuficientes)");
        }
    }

    private MilitaryUnit createUnitByType(int type) {
        switch (type) {
            case 0: {
                int armor = Variables.ARMOR_SWORDSMAN + (technologyDefense * Variables.PLUS_ARMOR_SWORDSMAN_BY_TECHNOLOGY * Variables.ARMOR_SWORDSMAN / 100);
                int damage = Variables.BASE_DAMAGE_SWORDSMAN + (technologyAttack * Variables.PLUS_ATTACK_SWORDSMAN_BY_TECHNOLOGY * Variables.BASE_DAMAGE_SWORDSMAN / 100);
                return new Swordsman(armor, damage);
            }
            case 1: {
                int armor = Variables.ARMOR_SPEARMAN + (technologyDefense * Variables.PLUS_ARMOR_SPEARMAN_BY_TECHNOLOGY * Variables.ARMOR_SPEARMAN / 100);
                int damage = Variables.BASE_DAMAGE_SPEARMAN + (technologyAttack * Variables.PLUS_ATTACK_SPEARMAN_BY_TECHNOLOGY * Variables.BASE_DAMAGE_SPEARMAN / 100);
                return new Spearman(armor, damage);
            }
            case 2: {
                int armor = Variables.ARMOR_CROSSBOW + (technologyDefense * Variables.PLUS_ARMOR_CROSSBOW_BY_TECHNOLOGY * Variables.ARMOR_CROSSBOW / 100);
                int damage = Variables.BASE_DAMAGE_CROSSBOW + (technologyAttack * Variables.PLUS_ATTACK_CROSSBOW_BY_TECHNOLOGY * Variables.BASE_DAMAGE_CROSSBOW / 100);
                return new Crossbow(armor, damage);
            }
            case 3: {
                int armor = Variables.ARMOR_CANNON + (technologyDefense * Variables.PLUS_ARMOR_CANNON_BY_TECHNOLOGY * Variables.ARMOR_CANNON / 100);
                int damage = Variables.BASE_DAMAGE_CANNON + (technologyAttack * Variables.PLUS_ATTACK_CANNON_BY_TECHNOLOGY * Variables.BASE_DAMAGE_CANNON / 100);
                return new Cannon(armor, damage);
            }
            case 4: {
                int armor = Variables.ARMOR_ARROWTOWER + (technologyDefense * Variables.PLUS_ARMOR_ARROWTOWER_BY_TECHNOLOGY * Variables.ARMOR_ARROWTOWER / 100);
                int damage = Variables.BASE_DAMAGE_ARROWTOWER + (technologyAttack * Variables.PLUS_ATTACK_ARROWTOWER_BY_TECHNOLOGY * Variables.BASE_DAMAGE_ARROWTOWER / 100);
                return new ArrowTower(armor, damage);
            }
            case 5: {
                int armor = Variables.ARMOR_CATAPULT + (technologyDefense * Variables.PLUS_ARMOR_CATAPULT_BY_TECHNOLOGY * Variables.ARMOR_CATAPULT / 100);
                int damage = Variables.BASE_DAMAGE_CATAPULT + (technologyAttack * Variables.PLUS_ATTACK_CATAPULT_BY_TECHNOLOGY * Variables.BASE_DAMAGE_CATAPULT / 100);
                return new Catapult(armor, damage);
            }
            case 6: {
                int armor = Variables.ARMOR_ROCKETLAUNCHERTOWER + (technologyDefense * Variables.PLUS_ARMOR_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY * Variables.ARMOR_ROCKETLAUNCHERTOWER / 100);
                int damage = Variables.BASE_DAMAGE_ROCKETLAUNCHERTOWER + (technologyAttack * Variables.PLUS_ATTACK_ROCKETLAUNCHERTOWER_BY_TECHNOLOGY * Variables.BASE_DAMAGE_ROCKETLAUNCHERTOWER / 100);
                return new RocketLauncherTower(armor, damage);
            }
            case 7: {
                int damage = Variables.BASE_DAMAGE_MAGICIAN + (technologyAttack * Variables.PLUS_ATTACK_MAGICIAN_BY_TECHNOLOGY * Variables.BASE_DAMAGE_MAGICIAN / 100);
                return new Magician(0, damage);
            }
            case 8:
                return new Priest();
            default:
                return null;
        }
    }

    public void newSwordsman(int n) throws ResourceException, BuildingException { addUnits(0, n); }
    public void newSpearman(int n) throws ResourceException, BuildingException { addUnits(1, n); }
    public void newCrossbow(int n) throws ResourceException, BuildingException { addUnits(2, n); }
    public void newCannon(int n) throws ResourceException, BuildingException { addUnits(3, n); }
    public void newArrowTower(int n) throws ResourceException, BuildingException { addUnits(4, n); }
    public void newCatapult(int n) throws ResourceException, BuildingException { addUnits(5, n); }
    public void newRocketLauncher(int n) throws ResourceException, BuildingException { addUnits(6, n); }
    public void newMagician(int n) throws ResourceException, BuildingException { addUnits(7, n); }
    public void newPriest(int n) throws ResourceException, BuildingException { addUnits(8, n); }

    public void printStats() {
        // Línea separadora superior
        System.out.println("=======================================================================");
        System.out.println("                         CIVILIZATION STATS");
        System.out.println("=======================================================================");
        
        // Tecnología
        System.out.println("\n  TECHNOLOGY");
        System.out.println(String.format("    Attack.............: %4d", technologyAttack));
        System.out.println(String.format("    Defense............: %4d", technologyDefense));
        
        // Edificios
        System.out.println("\n  BUILDINGS");
        System.out.println(String.format("    Farm...............: %4d", farm));
        System.out.println(String.format("    Smithy.............: %4d", smithy));
        System.out.println(String.format("    Carpentry..........: %4d", carpentry));
        System.out.println(String.format("    Magic Tower........: %4d", magicTower));
        System.out.println(String.format("    Church.............: %4d", church));
        
        // Defensas
        System.out.println("\n  DEFENSES");
        System.out.println(String.format("    Arrow Tower........: %4d", army.get(4).size()));
        System.out.println(String.format("    Catapult...........: %4d", army.get(5).size()));
        System.out.println(String.format("    Rocket Launcher....: %4d", army.get(6).size()));
        
        // Unidades de ataque
        System.out.println("\n  ATTACK UNITS");
        System.out.println(String.format("    Swordsman..........: %4d", army.get(0).size()));
        System.out.println(String.format("    Spearman...........: %4d", army.get(1).size()));
        System.out.println(String.format("    Crossbow...........: %4d", army.get(2).size()));
        System.out.println(String.format("    Cannon.............: %4d", army.get(3).size()));
        
        // Unidades especiales
        System.out.println("\n  SPECIAL UNITS");
        System.out.println(String.format("    Magician...........: %4d", army.get(7).size()));
        System.out.println(String.format("    Priest.............: %4d", army.get(8).size()));
        
        // Recursos
        System.out.println("\n  RESOURCES");
        System.out.println(String.format("    Food...............: %4d", food));
        System.out.println(String.format("    Wood...............: %4d", wood));
        System.out.println(String.format("    Iron...............: %4d", iron));
        System.out.println(String.format("    Mana...............: %4d", mana));
        
        // Generación de recursos
        int genFood = Variables.CIVILIZATION_FOOD_GENERATED + farm * Variables.CIVILIZATION_FOOD_GENERATED_PER_FARM;
        int genWood = Variables.CIVILIZATION_WOOD_GENERATED + carpentry * Variables.CIVILIZATION_WOOD_GENERATED_PER_CARPENTRY;
        int genIron = Variables.CIVILIZATION_IRON_GENERATED + smithy * Variables.CIVILIZATION_IRON_GENERATED_PER_SMITHY;
        int genMana = magicTower * Variables.CIVILIZATION_MANA_GENERATED_PER_MAGIC_TOWER;
        System.out.println("\n  GENERATION (per minute)");
        System.out.println(String.format("    Food...............: %4d", genFood));
        System.out.println(String.format("    Wood...............: %4d", genWood));
        System.out.println(String.format("    Iron...............: %4d", genIron));
        System.out.println(String.format("    Mana...............: %4d", genMana));
        
        // Línea separadora inferior
        System.out.println("\n=======================================================================");
    }
}