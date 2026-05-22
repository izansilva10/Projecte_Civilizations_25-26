package civilizations.ui;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import civilizations.*;
import java.util.*;

public class MainWindow extends javafx.application.Application {
    private Civilization civilization;
    private ResourcesPanel resourcesPanel;
    private ArmyPanel armyPanel;
    private ReportsPanel reportsPanel;
    private CivilizationInfoPanel infoPanel;
    private ArrayList<Battle> battleHistory;
    private ArrayList<MilitaryUnit> currentEnemyArmy;
    private Timer resourceTimer;
    private Timer enemyTimer;

@Override
public void start(Stage stage) {
    civilization = Civilization.loadFromDatabase();
    battleHistory = new ArrayList<>();
    currentEnemyArmy = new ArrayList<>();

    startTimers();

    resourcesPanel = new ResourcesPanel(civilization, this::updateUI);
    armyPanel = new ArmyPanel(civilization, this::updateUI);
    reportsPanel = new ReportsPanel(battleHistory);
    infoPanel = new CivilizationInfoPanel(civilization, this::updateUI);

    TabPane tabPane = new TabPane();
    tabPane.getStyleClass().add("tab-pane");

    Tab infoTab = new Tab("🏛️ INFORMACIÓN", infoPanel.getPanel());
    Tab resourcesTab = new Tab("🏗️ EDIFICIOS", resourcesPanel.getPanel());
    Tab armyTab = new Tab("⚔️ EJÉRCITO", armyPanel.getPanel());
    Tab reportsTab = new Tab("📜 REPORTES", reportsPanel.getPanel());

    infoTab.setClosable(false);
    resourcesTab.setClosable(false);
    armyTab.setClosable(false);
    reportsTab.setClosable(false);

    tabPane.getTabs().addAll(infoTab, resourcesTab, armyTab, reportsTab);

    Scene scene = new Scene(tabPane, 1440, 860);

    scene.setUserAgentStylesheet(null);

    scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

    stage.setTitle("Civilizations");
    stage.setScene(scene);
    stage.show();

    startUIUpdater();
    updateUI();
}
    private void startTimers() {
        resourceTimer = new Timer();
        resourceTimer.scheduleAtFixedRate(new ResourceGenerator(civilization), 0, 60000);
        enemyTimer = new Timer();
        enemyTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> createEnemyAndBattle());
            }
        }, 180000, 180000);
    }

    private void createEnemyAndBattle() {
        int battles = civilization.getBattles();
        int foodBase = Variables.FOOD_BASE_ENEMY_ARMY + (battles * Variables.ENEMY_FLEET_INCREASE * Variables.FOOD_BASE_ENEMY_ARMY / 100);
        int woodBase = Variables.WOOD_BASE_ENEMY_ARMY + (battles * Variables.ENEMY_FLEET_INCREASE * Variables.WOOD_BASE_ENEMY_ARMY / 100);
        int ironBase = Variables.IRON_BASE_ENEMY_ARMY + (battles * Variables.ENEMY_FLEET_INCREASE * Variables.IRON_BASE_ENEMY_ARMY / 100);

        ArrayList<MilitaryUnit> newEnemyArmy = new ArrayList<>();
        int[] probs = {35, 25, 20, 20};
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
                if (type == 0) break;
            }
        }
        if (newEnemyArmy.isEmpty()) return;
        currentEnemyArmy.clear();
        currentEnemyArmy.addAll(newEnemyArmy);
        startBattleWithEnemy(currentEnemyArmy);
    }

    private int selectEnemyType(int[] probs) {
        int total = 0;
        for (int p : probs) total += p;
        int r = new Random().nextInt(total);
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

    private void startBattleWithEnemy(ArrayList<MilitaryUnit> enemyArmy) {
        try {
            Battle battle = new Battle(civilization.getArmy(), enemyArmy);
            battle.startBattle(civilization);
            battleHistory.add(battle);
            if (battle.civilizationWon()) {
                int[] waste = battle.getWasteWoodIron();
                civilization.addWood(waste[0]);
                civilization.addIron(waste[1]);
                System.out.println("Has ganado la batalla! Residuos: madera " + waste[0] + ", hierro " + waste[1]);
            } else {
                System.out.println("Has perdido la batalla.");
            }
            civilization.setBattles(civilization.getBattles() + 1);
            civilization.saveToDatabase();
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startUIUpdater() {
        AnimationTimer updater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateUI();
            }
        };
        updater.start();
    }

    private void updateUI() {
        if (resourcesPanel != null) resourcesPanel.updateUI();
        if (armyPanel != null) armyPanel.updateUI();
        if (reportsPanel != null) reportsPanel.updateUI();
        if (infoPanel != null) infoPanel.updateUI();
    }

    @Override
    public void stop() {
        if (resourceTimer != null) resourceTimer.cancel();
        if (enemyTimer != null) enemyTimer.cancel();
        if (civilization != null) {
            civilization.saveToDatabase();
        }
    }
}   