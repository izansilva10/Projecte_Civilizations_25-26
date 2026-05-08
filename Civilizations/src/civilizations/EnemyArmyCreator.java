package civilizations;

import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;

public class EnemyArmyCreator extends TimerTask {
    private Civilization civilization;
    private ArrayList<Battle> battleHistory;
    private ArrayList<MilitaryUnit> enemyList;
    private Random rand;

    public EnemyArmyCreator(Civilization civilization, ArrayList<Battle> battleHistory, ArrayList<MilitaryUnit> enemyList) {
        this.civilization = civilization;
        this.battleHistory = battleHistory;
        this.enemyList = enemyList;
        this.rand = new Random();
    }

    public void run() {
        createEnemyArmy();
    }

    private void createEnemyArmy() {
        int battles = civilization.getBattles();
        int foodBase = Variables.FOOD_BASE_ENEMY_ARMY + (battles * Variables.ENEMY_FLEET_INCREASE * Variables.FOOD_BASE_ENEMY_ARMY / 100);
        int woodBase = Variables.WOOD_BASE_ENEMY_ARMY + (battles * Variables.ENEMY_FLEET_INCREASE * Variables.WOOD_BASE_ENEMY_ARMY / 100);
        int ironBase = Variables.IRON_BASE_ENEMY_ARMY + (battles * Variables.ENEMY_FLEET_INCREASE * Variables.IRON_BASE_ENEMY_ARMY / 100);

        ArrayList<MilitaryUnit> newEnemyArmy = new ArrayList<>();
        int[] probs = {35, 25, 20, 20}; // Swordsman, Spearman, Crossbow, Cannon
        while (true) {
            int type = selectEnemyType(probs);
            MilitaryUnit sample = null;
            switch (type) {
                case 0: sample = new Swordsman(); break;
                case 1: sample = new Spearman(); break;
                case 2: sample = new Crossbow(); break;
                case 3: sample = new Cannon(); break;
            }
            if (sample == null) break;
            if (foodBase >= sample.getFoodCost() && woodBase >= sample.getWoodCost() && ironBase >= sample.getIronCost()) {
                newEnemyArmy.add(createEnemyUnit(type));
                foodBase -= sample.getFoodCost();
                woodBase -= sample.getWoodCost();
                ironBase -= sample.getIronCost();
            } else {
                if (type == 0) break; // si ni el más barato, terminamos
            }
        }

        enemyList.clear();
        enemyList.addAll(newEnemyArmy);
        System.out.println("¡Un ejército enemigo se aproxima!");

        // Iniciar batalla automáticamente
        Battle battle = new Battle(civilization.getArmy(), newEnemyArmy);
        battle.startBattle();
        battleHistory.add(battle);
        if (battle.civilizationWon()) {
            int[] waste = battle.getWasteWoodIron();
            civilization.addWood(waste[0]);
            civilization.addIron(waste[1]);
        }
        civilization.setBattles(battles + 1);
    }

    private int selectEnemyType(int[] probs) {
        int total = 0;
        for (int p : probs) total += p;
        int r = rand.nextInt(total);
        int cum = 0;
        for (int i = 0; i < probs.length; i++) {
            cum += probs[i];
            if (r < cum) return i;
        }
        return 0;
    }

    private MilitaryUnit createEnemyUnit(int type) {
        switch (type) {
            case 0: return new Swordsman();
            case 1: return new Spearman();
            case 2: return new Crossbow();
            case 3: return new Cannon();
            default: return null;
        }
    }
}