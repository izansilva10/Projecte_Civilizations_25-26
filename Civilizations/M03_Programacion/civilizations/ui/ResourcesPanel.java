package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import civilizations.Civilization;
import civilizations.ResourceException;
import civilizations.Variables;

public class ResourcesPanel {
    private Civilization civilization;
    private Runnable updateUICallback;
    private Label foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel;
    private Button upgradeDefenseBtn, upgradeAttackBtn;

    public ResourcesPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
        this.foodValueLabel = new Label("0");
        this.woodValueLabel = new Label("0");
        this.ironValueLabel = new Label("0");
        this.manaValueLabel = new Label("0");
    }

    public VBox getPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));
        HBox resourcesBar = createResourceBar();

        // --- CONSTRUIR EDIFICIOS ---
        Label constructionTitle = new Label("🏗️ CONSTRUIR EDIFICIOS");
        constructionTitle.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 0 0 10 0; -fx-border-color: #4ea5d9; -fx-border-width: 0 0 1 0;");
        GridPane buildGrid = new GridPane();
        buildGrid.setHgap(15);
        buildGrid.setVgap(15);
        buildGrid.setPadding(new Insets(10));
        buildGrid.setAlignment(Pos.CENTER);

        Button farmBtn = createBuildButton("🏚️ Granja", 
            String.format("Comida: %,d\nMadera: %,d\nHierro: %,d", 
                Variables.FOOD_COST_FARM, Variables.WOOD_COST_FARM, Variables.IRON_COST_FARM));
        Button carpentryBtn = createBuildButton("🪚 Carpintería", 
            String.format("Comida: %,d\nMadera: %,d\nHierro: %,d", 
                Variables.FOOD_COST_CARPENTRY, Variables.WOOD_COST_CARPENTRY, Variables.IRON_COST_CARPENTRY));
        Button smithyBtn = createBuildButton("⚒️ Herrería", 
            String.format("Comida: %,d\nMadera: %,d\nHierro: %,d", 
                Variables.FOOD_COST_SMITHY, Variables.WOOD_COST_SMITHY, Variables.IRON_COST_SMITHY));
        Button magicTowerBtn = createBuildButton("🔮 Torre Mágica", 
            String.format("Comida: %,d\nMadera: %,d\nHierro: %,d", 
                Variables.FOOD_COST_MAGICTOWER, Variables.WOOD_COST_MAGICTOWER, Variables.IRON_COST_MAGICTOWER));
        Button churchBtn = createBuildButton("⛪ Iglesia", 
            String.format("Comida: %,d\nMadera: %,d\nHierro: %,d", 
                Variables.FOOD_COST_CHURCH, Variables.WOOD_COST_CHURCH, Variables.IRON_COST_CHURCH));

        buildGrid.add(farmBtn, 0, 0);
        buildGrid.add(carpentryBtn, 1, 0);
        buildGrid.add(smithyBtn, 2, 0);
        buildGrid.add(magicTowerBtn, 0, 1);
        buildGrid.add(churchBtn, 1, 1);

        // --- MEJORAR TECNOLOGÍAS ---
        Label techTitle = new Label("⚙️ MEJORAR TECNOLOGÍA");
        techTitle.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 10 0 10 0; -fx-border-color: #4ea5d9; -fx-border-width: 0 0 1 0;");
        HBox techBox = new HBox(30);
        techBox.setAlignment(Pos.CENTER);
        techBox.setPadding(new Insets(10, 0, 20, 0));

        upgradeDefenseBtn = new Button("🛡️ Mejorar Defensa");
        upgradeAttackBtn = new Button("⚔️ Mejorar Ataque");
        updateTechButtons();
        techBox.getChildren().addAll(upgradeDefenseBtn, upgradeAttackBtn);

        // --- EVENTOS ---
        farmBtn.setOnAction(e -> construir(() -> civilization.newFarm(), "Granja"));
        carpentryBtn.setOnAction(e -> construir(() -> civilization.newCarpentry(), "Carpintería"));
        smithyBtn.setOnAction(e -> construir(() -> civilization.newSmithy(), "Herrería"));
        magicTowerBtn.setOnAction(e -> construir(() -> civilization.newMagicTower(), "Torre Mágica"));
        churchBtn.setOnAction(e -> construir(() -> civilization.newChurch(), "Iglesia"));
        upgradeDefenseBtn.setOnAction(e -> mejorar(() -> civilization.upgradeTechnologyDefense(), "Defensa"));
        upgradeAttackBtn.setOnAction(e -> mejorar(() -> civilization.upgradeTechnologyAttack(), "Ataque"));

        panel.getChildren().addAll(resourcesBar, constructionTitle, buildGrid, new Separator(), techTitle, techBox);
        return panel;
    }

    private HBox createResourceBar() {
        return ResourceBar.create(civilization, foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel);
    }

    private Button createBuildButton(String text, String costDetails) {
        Button btn = new Button();
        btn.getStyleClass().addAll("button", "button-build");
        btn.setMinWidth(200);
        btn.setPrefWidth(220);
        btn.setMinHeight(140);
        btn.setPrefHeight(140);

        VBox content = new VBox(6);
        content.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(text);
        nameLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label costLabel = new Label(costDetails);
        costLabel.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 12px; -fx-text-alignment: center;");
        costLabel.setWrapText(true);

        content.getChildren().addAll(nameLabel, costLabel);
        btn.setGraphic(content);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return btn;
    }

    private void updateTechButtons() {
        int defLevel = civilization.getTechnologyDefense();
        int atkLevel = civilization.getTechnologyAttack();

        int defIronCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST + defLevel * Variables.UPGRADEPLUS_DEFENSE_TECHNOLOGY_IRON_COST;
        int defWoodCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_WOOD_COST + defLevel * Variables.UPGRADE_PLUS_DEFENSE_TECHNOLOGY_WOOD_COST;
        int atkIronCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST + atkLevel * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST;
        int atkWoodCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_WOOD_COST + atkLevel * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_WOOD_COST;

        String defCost = "Coste: " + (defWoodCost > 0 ? "Madera: " + defWoodCost + " " : "") + "Hierro: " + defIronCost;
        String atkCost = "Coste: " + (atkWoodCost > 0 ? "Madera: " + atkWoodCost + " " : "") + "Hierro: " + atkIronCost;

        upgradeDefenseBtn.setText("🛡️ Mejorar Defensa\n" + defCost);
        upgradeDefenseBtn.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: #4f4310; -fx-border-color: #d4ac0d; -fx-border-radius: 12; -fx-background-radius: 12; -fx-min-width: 250px; -fx-min-height: 90px;");

        upgradeAttackBtn.setText("⚔️ Mejorar Ataque\n" + atkCost);
        upgradeAttackBtn.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10; -fx-background-color: #4f4310; -fx-border-color: #d4ac0d; -fx-border-radius: 12; -fx-background-radius: 12; -fx-min-width: 250px; -fx-min-height: 90px;");
    }

    // ==========================================
    // LÓGICA DE ACCIONES
    // ==========================================
    @FunctionalInterface
    private interface ActionWithException {
        void run() throws ResourceException;
    }

    private void construir(ActionWithException action, String nombre) {
        try {
            action.run();
            showInfo(nombre + " construida correctamente.");
            civilization.saveToDatabase();
            updateUICallback.run();
        } catch (ResourceException e) {
            showAlert(e.getMessage());
        }
    }

    private void mejorar(ActionWithException action, String nombre) {
        try {
            action.run();
            showInfo("Tecnología de " + nombre + " mejorada.");
            civilization.saveToDatabase();
            updateUICallback.run();
        } catch (ResourceException e) {
            showAlert(e.getMessage());
        }
    }

    public void updateUI() {
        if (foodValueLabel != null) foodValueLabel.setText(String.format("%,d", civilization.getFood()));
        if (woodValueLabel != null) woodValueLabel.setText(String.format("%,d", civilization.getWood()));
        if (ironValueLabel != null) ironValueLabel.setText(String.format("%,d", civilization.getIron()));
        if (manaValueLabel != null) manaValueLabel.setText(String.format("%,d", civilization.getMana()));
        updateTechButtons();
    }

    private void showAlert(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }

    private void showInfo(String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}