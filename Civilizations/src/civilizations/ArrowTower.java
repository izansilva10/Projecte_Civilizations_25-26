package civilizations;

public class ArrowTower extends DefenseUnit {

    public ArrowTower(int armor, int baseDamage) {
        super(armor, baseDamage);
    }

    public int getFoodCost() { return Variables.FOOD_COST_ARROWTOWER; }
    public int getWoodCost() { return Variables.WOOD_COST_ARROWTOWER; }
    public int getIronCost() { return Variables.IRON_COST_ARROWTOWER; }
    public int getManaCost() { return Variables.MANA_COST_ARROWTOWER; }
    public int getChanceGeneratorInWaste() { return Variables.CHANCE_GENERATING_WASTE_ARROWTOWER; }
    public int getChanceAttackAgain() { return Variables.CHANCE_ATTACK_AGAIN_ARROWTOWER; }
}