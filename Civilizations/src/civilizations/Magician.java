package civilizations;

public class Magician extends SpecialUnit {

    public Magician(int armor, int baseDamage) {
        super(armor, baseDamage);
    }

    public int getFoodCost() { return Variables.FOOD_COST_MAGICIAN; }
    public int getWoodCost() { return Variables.WOOD_COST_MAGICIAN; }
    public int getIronCost() { return 0; } // IRON_COST_PRIEST is 0, same for magician? PDF says IRON_COST_PRIEST = 0 but magician iron cost not explicitly set; table shows Magician cost 0 iron. Use 0.
    public int getManaCost() { return Variables.MANA_COST_MAGICIAN; }
    public int getChanceGeneratorInWaste() { return Variables.CHANCE_GENERATING_WASTE_MAGICIAN; }
    public int getChanceAttackAgain() { return Variables.CHANCE_ATTACK_AGAIN_MAGICIAN; }
}