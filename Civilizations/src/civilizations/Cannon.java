package civilizations;

public class Cannon extends AttackUnit {

    public Cannon(int armor, int baseDamage) {
        super(armor, baseDamage);
    }

    public Cannon() {
        super(Variables.ARMOR_CANNON, Variables.BASE_DAMAGE_CANNON);
    }

    public int getFoodCost() { return Variables.FOOD_COST_CANNON; }
    public int getWoodCost() { return Variables.WOOD_COST_CANNON; }
    public int getIronCost() { return Variables.IRON_COST_CANNON; }
    public int getManaCost() { return Variables.MANA_COST_CANNON; }
    public int getChanceGeneratorInWaste() { return Variables.CHANCE_GENERATING_WASTE_CANNON; }
    public int getChanceAttackAgain() { return Variables.CHANCE_ATTACK_AGAIN_CANNON; }
}