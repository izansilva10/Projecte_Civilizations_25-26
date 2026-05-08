package civilizations;

public class Crossbow extends AttackUnit {

    public Crossbow(int armor, int baseDamage) {
        super(armor, baseDamage);
    }

    public Crossbow() {
        super(Variables.ARMOR_CROSSBOW, Variables.BASE_DAMAGE_CROSSBOW);
    }

    public int getFoodCost() { return Variables.FOOD_COST_CROSSBOW; }
    public int getWoodCost() { return Variables.WOOD_COST_CROSSBOW; }
    public int getIronCost() { return Variables.IRON_COST_CROSSBOW; }
    public int getManaCost() { return Variables.MANA_COST_CROSSBOW; }
    public int getChanceGeneratorInWaste() { return Variables.CHANCE_GENERATING_WASTE_CROSSBOW; }
    public int getChanceAttackAgain() { return Variables.CHANCE_ATTACK_AGAIN_CROSSBOW; }
}