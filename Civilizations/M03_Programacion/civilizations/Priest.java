package civilizations;

public class Priest extends SpecialUnit 
{
    public Priest(int armor, int baseDamage) 
    {
        super(armor, baseDamage);
    }

    public Priest() 
    {
        super(0, 0);
    }

    public int getFoodCost() { return Variables.FOOD_COST_PRIEST; }
    public int getWoodCost() { return Variables.WOOD_COST_PRIEST; }
    public int getIronCost() { return Variables.IRON_COST_PRIEST; }
    public int getManaCost() { return Variables.MANA_COST_PRIEST; }
    public int getChanceGeneratorInWaste() { return Variables.CHANCE_GENERATING_WASTE_PRIEST; }
    public int getChanceAttackAgain() { return Variables.CHANCE_ATTACK_AGAIN_PRIEST; }

    public int attack() 
    {
        return 0; // Los sacerdotes no atacan
    }
}