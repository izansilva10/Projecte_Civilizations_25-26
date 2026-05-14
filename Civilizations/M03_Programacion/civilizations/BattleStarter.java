package civilizations;

import java.util.ArrayList;

public class BattleStarter 
{

    public static void startBattle(Civilization civ, ArrayList<MilitaryUnit> enemyArmy, ArrayList<Battle> history) 
    {
        Battle battle = new Battle(civ.getArmy(), enemyArmy);
        battle.startBattle();
        history.add(battle);
        if (battle.civilizationWon()) 
        {
            int[] waste = battle.getWasteWoodIron();
            civ.addWood(waste[0]);
            civ.addIron(waste[1]);
        }
        civ.setBattles(civ.getBattles() + 1);
        enemyArmy.clear();
    }
}