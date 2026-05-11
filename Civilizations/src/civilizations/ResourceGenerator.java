package civilizations;

import java.util.TimerTask;

public class ResourceGenerator extends TimerTask {
    private Civilization civilization;

    public ResourceGenerator(Civilization civilization) {
        this.civilization = civilization;
    }

    public void run() {
        int extraFood = civilization.getFarm() * Variables.CIVILIZATION_FOOD_GENERATED_PER_FARM;
        int extraWood = civilization.getCarpentry() * Variables.CIVILIZATION_WOOD_GENERATED_PER_CARPENTRY;
        int extraIron = civilization.getSmithy() * Variables.CIVILIZATION_IRON_GENERATED_PER_SMITHY;
        int manaGen = civilization.getMagicTower() * Variables.CIVILIZATION_MANA_GENERATED_PER_MAGIC_TOWER;

        civilization.addFood(Variables.CIVILIZATION_FOOD_GENERATED + extraFood);
        civilization.addWood(Variables.CIVILIZATION_WOOD_GENERATED + extraWood);
        civilization.addIron(Variables.CIVILIZATION_IRON_GENERATED + extraIron);
        civilization.addMana(manaGen);
    }
}