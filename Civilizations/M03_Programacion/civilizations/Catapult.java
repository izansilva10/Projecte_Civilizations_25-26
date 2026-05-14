package civilizations;

public class Catapult extends DefenseUnit 
{

    public Catapult(int armor, int baseDamage) 
    
    {
        super(armor, baseDamage);
    }

    public int getFoodCost() 
    { 
        return Variables.FOOD_COST_CATAPULT; 
    }
    public int getWoodCost() 
    { 
        return Variables.WOOD_COST_CATAPULT; 
    }
    public int getIronCost() 
    { 
        return Variables.IRON_COST_CATAPULT; 
    }
    public int getManaCost() 
    { 
        return Variables.MANA_COST_CATAPULT; 
    }
    public int getChanceGeneratorInWaste() 
    { 
        return Variables.CHANCE_GENERATING_WASTE_CATAPULT; 
    }
    public int getChanceAttackAgain() 
    { 
        return Variables.CHANCE_ATTACK_AGAIN_CATAPULT; 
    }
}