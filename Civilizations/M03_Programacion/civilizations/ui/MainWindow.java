package civilizations.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import civilizations.*;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainWindow {
    private Civilization civilization;
    private ArrayList<Battle> battleHistory;
    private ArrayList<MilitaryUnit> currentEnemyArmy;
    private Timer resourceTimer;
    private Timer enemyTimer;
    private boolean battleInProgress = false;

    private ResourcesPanel resourcesPanel;
    private ArmyPanel armyPanel;
    private BattlePanel battlePanel;
    private ReportsPanel reportsPanel;

    public void start(Stage stage) {
        civilization = new Civilization();
        battleHistory = new ArrayList<>();
        currentEnemyArmy = new ArrayList<>();

        startTimers();

        // Crear paneles con sus callbacks
        resourcesPanel = new ResourcesPanel(civilization, this::updateAllUI);
        armyPanel = new ArmyPanel(civilization, this::updateAllUI);
        battlePanel = new BattlePanel(civilization, currentEnemyArmy, this::startBattleManually);
        reportsPanel = new ReportsPanel();

        TabPane tabPane = new TabPane();

        Tab resourcesTab = new Tab("Recursos y Edificios");
        resourcesTab.setClosable(false);
        resourcesTab.setContent(resourcesPanel.getPanel());

        Tab armyTab = new Tab("Ejército");
        armyTab.setClosable(false);
        armyTab.setContent(armyPanel.getPanel());

        Tab battleTab = new Tab("Batalla");
        battleTab.setClosable(false);
        battleTab.setContent(battlePanel.getPanel());

        Tab reportsTab = new Tab("Reportes");
        reportsTab.setClosable(false);
        reportsTab.setContent(reportsPanel.getPanel());

        tabPane.getTabs().addAll(resourcesTab, armyTab, battleTab, reportsTab);

        Scene scene = new Scene(tabPane, 1000, 700);
        stage.setTitle("Civilizations - Interfaz Gráfica");
        stage.setScene(scene);
        stage.show();

        startUIUpdater();
    }

    private void startUIUpdater() {
        javafx.animation.AnimationTimer updater = new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                updateAllUI();
            }
        };
        updater.start();
    }

    private void updateAllUI() {
        Platform.runLater(() -> {
            resourcesPanel.updateUI();
            armyPanel.updateUI();
            battlePanel.updateUI();
        });
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
        int r = new java.util.Random().nextInt(total);
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

    private void startBattleManually() {
        if (currentEnemyArmy.isEmpty()) {
            showAlert("No hay ejército enemigo. Espera a que llegue uno (cada 3 minutos).");
            return;
        }
        if (battleInProgress) {
            showAlert("Ya hay una batalla en curso.");
            return;
        }
        startBattleWithEnemy(currentEnemyArmy);
    }

    private void startBattleWithEnemy(ArrayList<MilitaryUnit> enemyArmy) {
        battleInProgress = true;
        try {
            Battle battle = new Battle(civilization.getArmy(), enemyArmy);
            battle.startBattle();
            battleHistory.add(battle);
            if (battle.civilizationWon()) {
                int[] waste = battle.getWasteWoodIron();
                civilization.addWood(waste[0]);
                civilization.addIron(waste[1]);
                showInfo("¡Has ganado la batalla! Has obtenido " + waste[0] + " madera y " + waste[1] + " hierro de residuos.");
            } else {
                showInfo("Has perdido la batalla. ¡Refuerza tus defensas!");
            }
            civilization.setBattles(civilization.getBattles() + 1);
            updateAllUI();

            String report = battle.getBattleReport(battleHistory.size());
            String development = battle.getBattleDevelopment();
            Platform.runLater(() -> {
                reportsPanel.appendBattleReport(report, development);
            });

            currentEnemyArmy.clear();
        } catch (Exception e) {
            showAlert("Error durante la batalla: " + e.getMessage());
            e.printStackTrace();
        } finally {
            battleInProgress = false;
        }
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void stop() {
        if (resourceTimer != null) resourceTimer.cancel();
        if (enemyTimer != null) enemyTimer.cancel();
    }
}
