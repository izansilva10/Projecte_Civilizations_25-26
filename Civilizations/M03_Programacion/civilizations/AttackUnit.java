package civilizations;

public abstract class AttackUnit implements MilitaryUnit 
{
    protected int armor;
    protected int initialArmor;
    protected int baseDamage;
    protected int experience;
    protected boolean sanctified;

    public AttackUnit(int armor, int baseDamage) 
    {
        this.armor = armor;
        this.initialArmor = armor;
        this.baseDamage = baseDamage;
        this.experience = 0;
        this.sanctified = false;
    }

    public int attack() 
    {
        int totalDamage = baseDamage;
        totalDamage += (experience * Variables.PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT * baseDamage) / 100;
        if (sanctified) 
        {
            totalDamage += (Variables.PLUS_ATTACK_UNIT_SANCTIFIED * baseDamage) / 100;
        }
        return totalDamage;
    }

    public void takeDamage(int receivedDamage) 
    {
        armor -= receivedDamage;
    }

    public int getActualArmor() 
    {
        return armor;
    }

    public void resetArmor() 
    {
        armor = initialArmor;
    }

    public void setExperience(int n) 
    {
        this.experience = n;
    }

    public int getExperience()
    {
        return experience;
    }

    // Métodos abstractos que implementarán las subclases
    public abstract int getFoodCost();
    public abstract int getWoodCost();
    public abstract int getIronCost();
    public abstract int getManaCost();
    public abstract int getChanceGeneratorInWaste();
    public abstract int getChanceAttackAgain();
}