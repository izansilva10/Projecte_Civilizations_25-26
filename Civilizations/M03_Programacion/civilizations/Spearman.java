package civilizations;

public class Spearman extends AttackUnit {

    public Spearman(int armor, int baseDamage) {
        super(armor, baseDamage);
    }

    public Spearman() {
        super(Variables.ARMOR_SPEARMAN, Variables.BASE_DAMAGE_SPEARMAN);
    }

    public int getFoodCost() { return Variables.FOOD_COST_SPEARMAN; }
    public int getWoodCost() { return Variables.WOOD_COST_SPEARMAN; }
    public int getIronCost() { return Variables.IRON_COST_SPEARMAN; }
    public int getManaCost() { return Variables.MANA_COST_SPEARMAN; }
    public int getChanceGeneratorInWaste() { return Variables.CHANCE_GENERATING_WASTE_SPEARMAN; }
    public int getChanceAttackAgain() { return Variables.CHANCE_ATTACK_AGAIN_SPEARMAN; }
}