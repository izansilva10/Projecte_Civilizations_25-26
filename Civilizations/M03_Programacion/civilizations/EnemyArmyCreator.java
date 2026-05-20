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
        // Factor de dificultad: empieza en 2.5 y crece 0.4 por batalla
        double factor = 2.5 + (battles * 0.4);
        int foodBase = (int)(Variables.FOOD_BASE_ENEMY_ARMY * factor);
        int woodBase = (int)(Variables.WOOD_BASE_ENEMY_ARMY * factor);
        int ironBase = (int)(Variables.IRON_BASE_ENEMY_ARMY * factor);
        
        // Valores mínimos para evitar ejércitos ridículos
        if (foodBase < 80000) {
            foodBase = 80000;
        }
        if (woodBase < 200000) {
            woodBase = 200000;
        }
        if (ironBase < 50000) {
            ironBase = 50000;
        }

        ArrayList<MilitaryUnit> newEnemyArmy = new ArrayList<>();
        int[] probs = {35, 25, 20, 20}; // Swordsman, Spearman, Crossbow, Cannon

        // Intentamos crear muchas unidades (hasta 400 intentos)
        for (int attempt = 0; attempt < 400; attempt++) {
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
                // Si no podemos crear el tipo elegido, probamos con el más barato (Swordsman)
                if (type != 0) {
                    Swordsman cheap = new Swordsman();
                    if (foodBase >= cheap.getFoodCost() && woodBase >= cheap.getWoodCost() && ironBase >= cheap.getIronCost()) {
                        newEnemyArmy.add(new Swordsman());
                        foodBase -= cheap.getFoodCost();
                        woodBase -= cheap.getWoodCost();
                        ironBase -= cheap.getIronCost();
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        // Añadir algunas defensas (opcional, para hacerlo más difícil)
        int numDefenses = newEnemyArmy.size() / 10; // 1 defensa por cada 10 unidades
        for (int i = 0; i < numDefenses; i++) {
            // Crear torre de flechas (cuesta madera)
            if (woodBase >= Variables.WOOD_COST_ARROWTOWER) {
                newEnemyArmy.add(new ArrowTower(Variables.ARMOR_ARROWTOWER, Variables.BASE_DAMAGE_ARROWTOWER));
                woodBase -= Variables.WOOD_COST_ARROWTOWER;
            }
        }

        // Si el ejército sigue vacío, añadir 20 espadachines de emergencia
        if (newEnemyArmy.isEmpty()) {
            for (int i = 0; i < 20; i++) {
                newEnemyArmy.add(new Swordsman());
            }
        }

        enemyList.clear();
        enemyList.addAll(newEnemyArmy);
        System.out.println("Ejercito enemigo se aproxima! Tamaño: " + newEnemyArmy.size());

        Battle battle = new Battle(civilization.getArmy(), newEnemyArmy);
        battle.startBattle(civilization);  // <-- Se pasa 'civilization' para guardar la batalla en MySQL
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