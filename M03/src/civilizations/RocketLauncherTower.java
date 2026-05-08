package civilizations;

public class RocketLauncherTower extends DefenseUnit {

    public RocketLauncherTower(int armor, int baseDamage) {
        super(armor, baseDamage);
    }

    public int getFoodCost() { return Variables.FOOD_COST_ROCKETLAUNCHERTOWER; }
    public int getWoodCost() { return Variables.WOOD_COST_ROCKETLAUNCHERTOWER; }
    public int getIronCost() { return Variables.IRON_COST_ROCKETLAUNCHERTOWER; }
    public int getManaCost() { return Variables.MANA_COST_ROCKETLAUNCHERTOWER; }
    public int getChanceGeneratorInWaste() { return Variables.CHANCE_GENERATING_WASTE_ROCKETLAUNCHERTOWER; }
    public int getChanceAttackAgain() { return Variables.CHANCE_ATTACK_AGAIN_ROCKETLAUNCHERTOWER; }
}