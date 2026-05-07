package civilizations.units;

import civilizations.interfaces.MilitaryUnit;
import civilizations.interfaces.Variables;

public abstract class DefenseUnit implements MilitaryUnit, Variables 
{

    protected int armor;
    protected int initialArmor;
    protected int baseDamage;
    protected int experience;
    protected boolean sanctified;

    // Constructor per a unitats defensives del jugador //
    public DefenseUnit(int armor, int baseDamage) 
    {
        this.armor = armor;
        this.initialArmor = armor;
        this.baseDamage = baseDamage;
        this.experience = 0;
        this.sanctified = false;
    }

    // Constructor per a unitats defensives enemigues //
    public DefenseUnit() 
    {
        this.armor = 0;
        this.initialArmor = 0;
        this.baseDamage = 0;
        this.experience = 0;
        this.sanctified = false;
    }

    // Implementació dels mètodes de MilitaryUnit //

    public int attack() 
    {
        int experienceBonus = (baseDamage * experience * PLUS_ATTACK_UNIT_PER_EXPERIENCE_POINT) / 100;
        int sanctifyBonus = sanctified ? (baseDamage * PLUS_ATTACK_UNIT_SANCTIFIED) / 100 : 0;
        return baseDamage + experienceBonus + sanctifyBonus;
    }

    public void takeDamage(int receivedDamage) 
    {
        this.armor -= receivedDamage;
    }

    public int getActualArmor() 
    {
        return this.armor;
    }

    public void resetArmor() 
    {
        this.armor = this.initialArmor;
    }

    public void setExperience(int n) 
    {
        this.experience = n;
    }

    public int getExperience() 
    {
        return this.experience;
    }

    // Mètodes abstractes que hauran d'implementar les subclasses //
    public abstract int getFoodCost();
    public abstract int getWoodCost();
    public abstract int getIronCost();
    public abstract int getManaCost();
    public abstract int getChanceGeneratinWaste();
    public abstract int getChanceAttackAgain();
}