package civilizations;

import java.util.ArrayList;

public class Civilization {
    private int technologyDefense;
    private int technologyAttack;
    private int wood, iron, food, mana;
    private int magicTower, church, farm, smithy, carpentry;
    private int battles;
    // Cambio: ArrayList de ArrayLists en lugar de array de ArrayLists
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

    // Devuelve un array de ArrayList para mantener compatibilidad con Battle
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
        System.out.println("*********/*********");
        System.out.println("- TECHNOLOGY - Attack Defense " + technologyAttack + " " + technologyDefense);
        System.out.println("- BUILDINGS - Farm Smithy Carpentry Magic Tower Church " + farm + " " + smithy + " " + carpentry + " " + magicTower + " " + church);
        System.out.println("- DEFENSES - Arrow Tower Catapult Rocket Launcher " + army.get(4).size() + " " + army.get(5).size() + " " + army.get(6).size());
        System.out.println("- ATTACK UNITS - Swordsman Spearman Crossbow Cannon " + army.get(0).size() + " " + army.get(1).size() + " " + army.get(2).size() + " " + army.get(3).size());
        System.out.println("- ESPECIAL UNITS - Magician Priest " + army.get(7).size() + " " + army.get(8).size());
        System.out.println("- RESOURCES - Food Wood Iron Mana " + food + " " + wood + " " + iron + " " + mana);
        int genFood = Variables.CIVILIZATION_FOOD_GENERATED + farm * Variables.CIVILIZATION_FOOD_GENERATED_PER_FARM;
        int genWood = Variables.CIVILIZATION_WOOD_GENERATED + carpentry * Variables.CIVILIZATION_WOOD_GENERATED_PER_CARPENTRY;
        int genIron = Variables.CIVILIZATION_IRON_GENERATED + smithy * Variables.CIVILIZATION_IRON_GENERATED_PER_SMITHY;
        int genMana = magicTower * Variables.CIVILIZATION_MANA_GENERATED_PER_MAGIC_TOWER;
        System.out.println("- GENERATION RESOURCES - Food Wood Iron Mana " + genFood + " " + genWood + " " + genIron + " " + genMana);
    }
}