package civilizations;

public class Magician extends SpecialUnit 
{

    public Magician(int armor, int baseDamage) 
    {
        super(armor, baseDamage);
    }

    public int getFoodCost() 
    { 
        return Variables.FOOD_COST_MAGICIAN; 
    }
    public int getWoodCost() 
    { 
        return Variables.WOOD_COST_MAGICIAN; 
    }
    public int getIronCost() 
    { 
        return 0; 
    } 
    public int getManaCost() 
    { 
        return Variables.MANA_COST_MAGICIAN; 
    }
    public int getChanceGeneratorInWaste() 
    { 
        return Variables.CHANCE_GENERATING_WASTE_MAGICIAN; 
    }
    public int getChanceAttackAgain() 
    { 
        return Variables.CHANCE_ATTACK_AGAIN_MAGICIAN; 
    }
}