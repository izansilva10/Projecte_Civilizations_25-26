package civilizations.units;

import civilizations.interfaces.Variables;

public class Swordsman extends AttackUnit 
{
    // Constructor per a unitats del jugador //
    public Swordsman(int technologyDefense, int technologyAttack) 
    {
        super
        (
            calcularArmadura(technologyDefense),
            calcularDanyBase(technologyAttack)
        );
    }

    // Constructor per a unitats enemigues //
    public Swordsman() 
    {
        super();
        this.armor = Variables.ARMOR_SWORDSMAN;
        this.initialArmor = Variables.ARMOR_SWORDSMAN;
        this.baseDamage = Variables.BASE_DAMAGE_SWORDSMAN;
    }

    // Mètode privat per calcular l'armadura amb tecnologia de defensa //
    private static int calcularArmadura(int technologyDefense) 
    {
        int bonusPercent = technologyDefense * Variables.PLUS_ARMOR_SWORDSMAN_BY_TECHNOLOGY;
        int bonus = (Variables.ARMOR_SWORDSMAN * bonusPercent) / 100;
        return Variables.ARMOR_SWORDSMAN + bonus;
    }

    // Mètode privat per calcular el dany base amb tecnologia d'atac //
    private static int calcularDanyBase(int technologyAttack) 
    {
        int bonusPercent = technologyAttack * Variables.PLUS_ATTACK_SWORDSMAN_BY_TECHNOLOGY;
        int bonus = (Variables.BASE_DAMAGE_SWORDSMAN * bonusPercent) / 100;
        return Variables.BASE_DAMAGE_SWORDSMAN + bonus;
    }

    // Implementació dels mètodes abstractes d'AttackUnit //
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

    public int getChanceGeneratinWaste() 
    {
        return Variables.CHANCE_GENERATNG_WASTE_SWORDSMAN;
    }

    public int getChanceAttackAgain() 
    {
        return Variables.CHANCE_ATTACK_AGAIN_SWORDSMAN;
    }
}
