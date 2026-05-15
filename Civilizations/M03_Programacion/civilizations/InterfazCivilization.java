package civilizations;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

import java.util.*;

public class InterfazCivilization extends Application {

    private Civilization civilization;
    private ArrayList<Battle> battleHistory;
    private ArrayList<MilitaryUnit> currentEnemyArmy;
    private Timer resourceTimer;
    private Timer enemyTimer;

    private Label foodLabel, woodLabel, ironLabel, manaLabel;
    private Label techDefLabel, techAtkLabel;
    private Label farmLabel, carpentryLabel, smithyLabel, magicTowerLabel, churchLabel;
    private TableView<String> armyTable;
    private TextArea threatArea, battleReportArea;
    private Button startBattleBtn;
    private boolean battleInProgress = false;

    @Override
    public void start(Stage stage) {
        civilization = new Civilization();
        battleHistory = new ArrayList<>();
        currentEnemyArmy = new ArrayList<>();

        startTimers();

        TabPane tabPane = new TabPane();

        Tab resourcesTab = new Tab("Recursos y Edificios");
        resourcesTab.setClosable(false);
        resourcesTab.setContent(createResourcesPanel());

        Tab armyTab = new Tab("Ejército");
        armyTab.setClosable(false);
        armyTab.setContent(createArmyPanel());

        Tab battleTab = new Tab("Batalla");
        battleTab.setClosable(false);
        battleTab.setContent(createBattlePanel());

        Tab reportsTab = new Tab("Reportes");
        reportsTab.setClosable(false);
        reportsTab.setContent(createReportsPanel());

        tabPane.getTabs().addAll(resourcesTab, armyTab, battleTab, reportsTab);

        Scene scene = new Scene(tabPane, 1000, 700);
        stage.setTitle("Civilizations - Interfaz Gráfica");
        stage.setScene(scene);
        stage.show();

        startUIUpdater();
    }

    private VBox createResourcesPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.TOP_CENTER);

        GridPane resourcesGrid = new GridPane();
        resourcesGrid.setHgap(20);
        resourcesGrid.setVgap(10);
        resourcesGrid.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");

        foodLabel = new Label();
        woodLabel = new Label();
        ironLabel = new Label();
        manaLabel = new Label();
        techDefLabel = new Label();
        techAtkLabel = new Label();

        resourcesGrid.add(new Label("🍽️ Comida:"), 0, 0);
        resourcesGrid.add(foodLabel, 1, 0);
        resourcesGrid.add(new Label("🪵 Madera:"), 2, 0);
        resourcesGrid.add(woodLabel, 3, 0);
        resourcesGrid.add(new Label("⛏️ Hierro:"), 0, 1);
        resourcesGrid.add(ironLabel, 1, 1);
        resourcesGrid.add(new Label("✦ Maná:"), 2, 1);
        resourcesGrid.add(manaLabel, 3, 1);
        resourcesGrid.add(new Label("🛡️ Tecnología Defensa:"), 0, 2);
        resourcesGrid.add(techDefLabel, 1, 2);
        resourcesGrid.add(new Label("⚔️ Tecnología Ataque:"), 2, 2);
        resourcesGrid.add(techAtkLabel, 3, 2);

        GridPane buildingsGrid = new GridPane();
        buildingsGrid.setHgap(20);
        buildingsGrid.setVgap(10);
        buildingsGrid.setStyle("-fx-border-color: lightgray; -fx-border-radius: 5; -fx-padding: 10;");
        farmLabel = new Label();
        carpentryLabel = new Label();
        smithyLabel = new Label();
        magicTowerLabel = new Label();
        churchLabel = new Label();

        buildingsGrid.add(new Label("🏚️ Granjas:"), 0, 0);
        buildingsGrid.add(farmLabel, 1, 0);
        buildingsGrid.add(new Label("🪚 Carpinterías:"), 2, 0);
        buildingsGrid.add(carpentryLabel, 3, 0);
        buildingsGrid.add(new Label("⚒️ Herrerías:"), 0, 1);
        buildingsGrid.add(smithyLabel, 1, 1);
        buildingsGrid.add(new Label("🏛️ Torres Mágicas:"), 2, 1);
        buildingsGrid.add(magicTowerLabel, 3, 1);
        buildingsGrid.add(new Label("⛪ Iglesias:"), 0, 2);
        buildingsGrid.add(churchLabel, 1, 2);

        GridPane buildButtons = new GridPane();
        buildButtons.setHgap(10);
        buildButtons.setVgap(10);
        Button farmBtn = new Button("Construir Granja");
        Button carpentryBtn = new Button("Construir Carpintería");
        Button smithyBtn = new Button("Construir Herrería");
        Button magicTowerBtn = new Button("Construir Torre Mágica");
        Button churchBtn = new Button("Construir Iglesia");

        farmBtn.setOnAction(e -> {
            try {
                civilization.newFarm();
                showInfo("Granja construida correctamente.");
                updateUI();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        carpentryBtn.setOnAction(e -> {
            try {
                civilization.newCarpentry();
                showInfo("Carpintería construida correctamente.");
                updateUI();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        smithyBtn.setOnAction(e -> {
            try {
                civilization.newSmithy();
                showInfo("Herrería construida correctamente.");
                updateUI();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        magicTowerBtn.setOnAction(e -> {
            try {
                civilization.newMagicTower();
                showInfo("Torre Mágica construida correctamente.");
                updateUI();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        churchBtn.setOnAction(e -> {
            try {
                civilization.newChurch();
                showInfo("Iglesia construida correctamente.");
                updateUI();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });

        buildButtons.add(farmBtn, 0, 0);
        buildButtons.add(carpentryBtn, 1, 0);
        buildButtons.add(smithyBtn, 2, 0);
        buildButtons.add(magicTowerBtn, 3, 0);
        buildButtons.add(churchBtn, 4, 0);

        HBox techBox = new HBox(10);
        Button upgradeDefenseBtn = new Button("Mejorar Defensa");
        Button upgradeAttackBtn = new Button("Mejorar Ataque");
        upgradeDefenseBtn.setOnAction(e -> {
            try {
                civilization.upgradeTechnologyDefense();
                showInfo("Tecnología de Defensa mejorada.");
                updateUI();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        upgradeAttackBtn.setOnAction(e -> {
            try {
                civilization.upgradeTechnologyAttack();
                showInfo("Tecnología de Ataque mejorada.");
                updateUI();
            } catch (ResourceException ex) {
                showAlert(ex.getMessage());
            }
        });
        techBox.getChildren().addAll(upgradeDefenseBtn, upgradeAttackBtn);

        panel.getChildren().addAll(resourcesGrid, buildingsGrid, buildButtons, techBox);
        return panel;
    }

    private VBox createArmyPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        armyTable = new TableView<>();
        TableColumn<String, String> unitCol = new TableColumn<>("Unidad");
        TableColumn<String, String> countCol = new TableColumn<>("Cantidad");
        armyTable.getColumns().addAll(unitCol, countCol);
        armyTable.setPrefHeight(300);

        Label attackLabel = new Label("⚔️ UNIDADES DE ATAQUE");
        attackLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox attackBox = new HBox(10);
        attackBox.setAlignment(Pos.CENTER_LEFT);
        Button swordsmanBtn = new Button("Espadachín");
        Button spearmanBtn = new Button("Lancero");
        Button crossbowBtn = new Button("Ballesta");
        Button cannonBtn = new Button("Cañón");
        attackBox.getChildren().addAll(swordsmanBtn, spearmanBtn, crossbowBtn, cannonBtn);

        Label defenseLabel = new Label("🛡️ DEFENSAS Y ESPECIALES");
        defenseLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        HBox defenseBox = new HBox(10);
        defenseBox.setAlignment(Pos.CENTER_LEFT);
        Button arrowTowerBtn = new Button("Torre Flechas");
        Button catapultBtn = new Button("Catapulta");
        Button rocketBtn = new Button("Torre Cohete");
        Button magicianBtn = new Button("Mago");
        Button priestBtn = new Button("Sacerdote");
        defenseBox.getChildren().addAll(arrowTowerBtn, catapultBtn, rocketBtn, magicianBtn, priestBtn);

        // Configurar acciones con actualización forzada y depuración
        swordsmanBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántos Espadachines quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newSwordsman(cantidad);
                        showInfo("Se han creado " + cantidad + " Espadachín(es).");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Espadachines = " + civilization.getArmy()[0].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        spearmanBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántos Lanceros quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newSpearman(cantidad);
                        showInfo("Se han creado " + cantidad + " Lancero(s).");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Lanceros = " + civilization.getArmy()[1].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        crossbowBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántas Ballestas quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newCrossbow(cantidad);
                        showInfo("Se han creado " + cantidad + " Ballesta(s).");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Ballestas = " + civilization.getArmy()[2].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        cannonBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad");
            dialog.setHeaderText("¿Cuántos Cañones quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newCannon(cantidad);
                        showInfo("Se han creado " + cantidad + " Cañón(es).");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Cañones = " + civilization.getArmy()[3].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        arrowTowerBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear defensa");
            dialog.setHeaderText("¿Cuántas Torres de Flechas quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newArrowTower(cantidad);
                        showInfo("Se han creado " + cantidad + " Torre(s) de Flechas.");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Torres Flechas = " + civilization.getArmy()[4].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        catapultBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear defensa");
            dialog.setHeaderText("¿Cuántas Catapultas quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newCatapult(cantidad);
                        showInfo("Se han creado " + cantidad + " Catapulta(s).");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Catapultas = " + civilization.getArmy()[5].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        rocketBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear defensa");
            dialog.setHeaderText("¿Cuántas Torres Cohete quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newRocketLauncher(cantidad);
                        showInfo("Se han creado " + cantidad + " Torre(s) Cohete.");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Torres Cohete = " + civilization.getArmy()[6].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        magicianBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad especial");
            dialog.setHeaderText("¿Cuántos Magos quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newMagician(cantidad);
                        showInfo("Se han creado " + cantidad + " Mago(s).");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Magos = " + civilization.getArmy()[7].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        priestBtn.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setTitle("Crear unidad especial");
            dialog.setHeaderText("¿Cuántos Sacerdotes quieres crear?");
            dialog.setContentText("Cantidad:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(value -> {
                try {
                    int cantidad = Integer.parseInt(value);
                    if (cantidad > 0) {
                        civilization.newPriest(cantidad);
                        showInfo("Se han creado " + cantidad + " Sacerdote(s).");
                        Platform.runLater(() -> {
                            updateUI();
                            armyTable.refresh();
                        });
                        System.out.println("DEBUG: Sacerdotes = " + civilization.getArmy()[8].size());
                    }
                } catch (NumberFormatException ex) {
                    showAlert("Cantidad no válida.");
                } catch (ResourceException | BuildingException ex) {
                    showAlert(ex.getMessage());
                }
            });
        });

        VBox buttonsContainer = new VBox(10);
        buttonsContainer.getChildren().addAll(attackLabel, attackBox, defenseLabel, defenseBox);
        buttonsContainer.setPadding(new Insets(10, 0, 0, 0));

        panel.getChildren().addAll(new Label("Ejército actual:"), armyTable, buttonsContainer);
        
        Button refreshBtn = new Button("🔄 Refrescar tabla");
            refreshBtn.setOnAction(e -> updateUI());
            panel.getChildren().add(refreshBtn);
            
        return panel;
    }

    private VBox createBattlePanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        threatArea = new TextArea();
        threatArea.setEditable(false);
        threatArea.setPrefRowCount(6);
        threatArea.setStyle("-fx-font-family: monospace;");

        startBattleBtn = new Button("Iniciar batalla ahora");
        startBattleBtn.setOnAction(e -> startBattleManually());

        panel.getChildren().addAll(new Label("⚠️ Amenaza enemiga actual:"), threatArea, startBattleBtn);
        return panel;
    }

    private VBox createReportsPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));

        battleReportArea = new TextArea();
        battleReportArea.setEditable(false);
        battleReportArea.setPrefRowCount(20);
        battleReportArea.setStyle("-fx-font-family: monospace;");

        panel.getChildren().addAll(new Label("Último reporte de batalla:"), battleReportArea);
        return panel;
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
            updateUI();

            String report = battle.getBattleReport(battleHistory.size());
            String development = battle.getBattleDevelopment();
            Platform.runLater(() -> {
                battleReportArea.setText(report + "\n\n--- DESARROLLO PASO A PASO ---\n" + development);
            });

            currentEnemyArmy.clear();
        } catch (Exception e) {
            showAlert("Error durante la batalla: " + e.getMessage());
            e.printStackTrace();
        } finally {
            battleInProgress = false;
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
        Platform.runLater(() -> {
            foodLabel.setText(String.format("%,d", civilization.getFood()));
            woodLabel.setText(String.format("%,d", civilization.getWood()));
            ironLabel.setText(String.format("%,d", civilization.getIron()));
            manaLabel.setText(String.format("%,d", civilization.getMana()));
            techDefLabel.setText(String.valueOf(civilization.getTechnologyDefense()));
            techAtkLabel.setText(String.valueOf(civilization.getTechnologyAttack()));
            farmLabel.setText(String.valueOf(civilization.getFarm()));
            carpentryLabel.setText(String.valueOf(civilization.getCarpentry()));
            smithyLabel.setText(String.valueOf(civilization.getSmithy()));
            magicTowerLabel.setText(String.valueOf(civilization.getMagicTower()));
            churchLabel.setText(String.valueOf(civilization.getChurch()));

            armyTable.getItems().clear();
            ArrayList<MilitaryUnit>[] armyGroups = civilization.getArmy();
            String[] unitNames = {"Espadachín", "Lancero", "Ballesta", "Cañón",
                                  "Torre Flechas", "Catapulta", "Torre Cohete", "Mago", "Sacerdote"};
            for (int i = 0; i < 9; i++) {
                int count = armyGroups[i].size();
                armyTable.getItems().add(unitNames[i] + " : " + count);
            }
            // Forzar refresco visual
            armyTable.refresh();

            if (currentEnemyArmy.isEmpty()) {
                threatArea.setText("No hay amenaza en este momento.\nEspera a que llegue un ejército enemigo (cada 3 minutos).");
                startBattleBtn.setDisable(true);
            } else {
                int[] counts = new int[4];
                for (MilitaryUnit u : currentEnemyArmy) {
                    if (u instanceof Swordsman) counts[0]++;
                    else if (u instanceof Spearman) counts[1]++;
                    else if (u instanceof Crossbow) counts[2]++;
                    else if (u instanceof Cannon) counts[3]++;
                }
                threatArea.setText(String.format("Swordsman: %d\nSpearman: %d\nCrossbow: %d\nCannon: %d",
                        counts[0], counts[1], counts[2], counts[3]));
                startBattleBtn.setDisable(false);
            }
        });
    }

    private void showAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    private void showInfo(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    @Override
    public void stop() {
        if (resourceTimer != null) resourceTimer.cancel();
        if (enemyTimer != null) enemyTimer.cancel();
    }

    public static void main(String[] args) {
        launch(args);
    }
}