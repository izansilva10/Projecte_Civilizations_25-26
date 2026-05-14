package civilizations;

public class Swordsman extends AttackUnit 
{

    public Swordsman(int armor, int baseDamage) 
    {
        super(armor, baseDamage);
    }

    public Swordsman() 
    {
        super(Variables.ARMOR_SWORDSMAN, Variables.BASE_DAMAGE_SWORDSMAN);
    }

    public int getFoodCost() 
    { 
        return Variables.FOOD_COST_SWORDSMAN; 
    }
    public int getWoodCost() 
    { 
        return Variables.WOOD_COST_SWORDSMAN; 
    }
    public int getIronCost() 
    { 
        return Variables.IRON_COST_SWORDSMAN; 
    }
    public int getManaCost() 
    { 
        return Variables.MANA_COST_SWORDSMAN; 
    }
    public int getChanceGeneratorInWaste() 
    { 
        return Variables.CHANCE_GENERATING_WASTE_SWORDSMAN; 
    }
    public int getChanceAttackAgain() 
    { 
        return Variables.CHANCE_ATTACK_AGAIN_SWORDSMAN; 
    }
}