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
    }

    public VBox getPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15, 0, 15, 0));
        panel.setStyle("-fx-background: linear-gradient(from 0% 0% to 100% 100%, #2c3e50, #1a2a3a); -fx-border-width: 0;");

        // --- BARRA DE RECURSOS (plateada, igual que en ArmyPanel) ---
        HBox resourcesBar = new HBox(25);
        resourcesBar.setAlignment(Pos.CENTER);
        resourcesBar.setStyle("-fx-background-color: #708090; -fx-padding: 12; -fx-border-radius: 8; -fx-border-color: #c0c0c0; -fx-border-width: 1;");
        resourcesBar.setMaxWidth(Double.MAX_VALUE);

        String labelStyle = "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;";

        Label foodIcon = new Label("🍽️ Comida:"); foodIcon.setStyle(labelStyle);
        foodValueLabel = new Label("0"); foodValueLabel.setStyle(labelStyle);
        Label woodIcon = new Label("🪵 Madera:"); woodIcon.setStyle(labelStyle);
        woodValueLabel = new Label("0"); woodValueLabel.setStyle(labelStyle);
        Label ironIcon = new Label("⛏️ Hierro:"); ironIcon.setStyle(labelStyle);
        ironValueLabel = new Label("0"); ironValueLabel.setStyle(labelStyle);
        Label manaIcon = new Label("✦ Maná:"); manaIcon.setStyle(labelStyle);
        manaValueLabel = new Label("0"); manaValueLabel.setStyle(labelStyle);

        resourcesBar.getChildren().addAll(foodIcon, foodValueLabel, woodIcon, woodValueLabel, ironIcon, ironValueLabel, manaIcon, manaValueLabel);

        // --- TÍTULO CONSTRUIR EDIFICIOS ---
        Label constructionTitle = new Label("🏗️ CONSTRUIR EDIFICIOS");
        constructionTitle.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        constructionTitle.setAlignment(Pos.CENTER);
        constructionTitle.setMaxWidth(Double.MAX_VALUE);

        // --- BOTONES DE CONSTRUCCIÓN EN FILA HORIZONTAL (5 botones) ---
        HBox constructionBox = new HBox(20);
        constructionBox.setAlignment(Pos.CENTER);
        constructionBox.setPadding(new Insets(15, 0, 15, 0));
        constructionBox.setMaxWidth(Double.MAX_VALUE);

        Button farmBtn = createVerticalCostButton("🍽️ Granja", 5000, 10000, 12000, 0);
        Button carpentryBtn = createVerticalCostButton("🪚 Carpintería", 10000, 10000, 12000, 0);
        Button smithyBtn = createVerticalCostButton("⚒️ Herrería", 5000, 10000, 12000, 0);
        Button magicTowerBtn = createVerticalCostButton("🔮 Torre Mágica", 5000, 10000, 12000, 0);
        Button churchBtn = createVerticalCostButton("⛪ Iglesia", 5000, 10000, 12000, 0);

        constructionBox.getChildren().addAll(farmBtn, carpentryBtn, smithyBtn, magicTowerBtn, churchBtn);

        // --- TÍTULO MEJORAR TECNOLOGÍA ---
        Label techTitle = new Label("⚙️ MEJORAR TECNOLOGÍA");
        techTitle.setStyle("-fx-text-fill: white; -fx-font-size: 22px; -fx-font-weight: bold;");
        techTitle.setAlignment(Pos.CENTER);
        techTitle.setMaxWidth(Double.MAX_VALUE);

        // --- BOTONES DE MEJORA EN FILA HORIZONTAL ---
        HBox techBox = new HBox(40);
        techBox.setAlignment(Pos.CENTER);
        techBox.setPadding(new Insets(20, 0, 10, 0));

        upgradeDefenseBtn = createTechButton("🛡️ Mejorar Defensa", "⛏️2000");
        upgradeAttackBtn = createTechButton("⚔️ Mejorar Ataque", "⛏️2000");

        techBox.getChildren().addAll(upgradeDefenseBtn, upgradeAttackBtn);

        // --- ACCIONES ---
        farmBtn.setOnAction(e -> construir(() -> civilization.newFarm(), "Granja"));
        carpentryBtn.setOnAction(e -> construir(() -> civilization.newCarpentry(), "Carpintería"));
        smithyBtn.setOnAction(e -> construir(() -> civilization.newSmithy(), "Herrería"));
        magicTowerBtn.setOnAction(e -> construir(() -> civilization.newMagicTower(), "Torre Mágica"));
        churchBtn.setOnAction(e -> construir(() -> civilization.newChurch(), "Iglesia"));
        upgradeDefenseBtn.setOnAction(e -> mejorar(() -> civilization.upgradeTechnologyDefense(), "Defensa"));
        upgradeAttackBtn.setOnAction(e -> mejorar(() -> civilization.upgradeTechnologyAttack(), "Ataque"));

        panel.getChildren().addAll(resourcesBar, constructionTitle, constructionBox, techTitle, techBox);
        return panel;
    }

    // Botón con costes en formato vertical (comida, madera, hierro)
    private Button createVerticalCostButton(String name, int food, int wood, int iron, int mana) {
        Button btn = new Button();
        btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-color: #5dade2; -fx-border-radius: 10; -fx-padding: 10 5;");
        btn.setAlignment(Pos.CENTER);
        btn.setMinWidth(180);
        btn.setPrefWidth(180);
        btn.setMaxWidth(180);

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        VBox costsBox = new VBox(2);
        costsBox.setAlignment(Pos.CENTER);
        if (food > 0) {
            Label foodLabel = new Label("🍽️ " + String.format("%,d", food));
            foodLabel.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 14px;");
            costsBox.getChildren().add(foodLabel);
        }
        if (wood > 0) {
            Label woodLabel = new Label("🪵 " + String.format("%,d", wood));
            woodLabel.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 14px;");
            costsBox.getChildren().add(woodLabel);
        }
        if (iron > 0) {
            Label ironLabel = new Label("⛏️ " + String.format("%,d", iron));
            ironLabel.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 14px;");
            costsBox.getChildren().add(ironLabel);
        }
        if (mana > 0) {
            Label manaLabel = new Label("✦ " + String.format("%,d", mana));
            manaLabel.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 14px;");
            costsBox.getChildren().add(manaLabel);
        }
        
        content.getChildren().addAll(nameLabel, costsBox);
        btn.setGraphic(content);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return btn;
    }

    // Botón de tecnología (también con fuente más grande)
    private Button createTechButton(String text, String cost) {
        Button btn = new Button();
        btn.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-border-color: #5dade2; -fx-border-radius: 10; -fx-padding: 10 5;");
        btn.setAlignment(Pos.CENTER);
        btn.setMinWidth(220);
        btn.setPrefWidth(220);
        btn.setMaxWidth(220);

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label(text);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label costLabel = new Label(cost);
        costLabel.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 14px;");
        
        content.getChildren().addAll(nameLabel, costLabel);
        btn.setGraphic(content);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return btn;
    }

    // Interfaz para acciones con excepción
    @FunctionalInterface
    private interface ActionWithException {
        void run() throws ResourceException;
    }

    private void construir(ActionWithException action, String nombre) {
        try {
            action.run();
            showInfo(nombre + " construida correctamente.");
            updateUICallback.run();
        } catch (ResourceException e) {
            showAlert(e.getMessage());
        }
    }

    private void mejorar(ActionWithException action, String nombre) {
        try {
            action.run();
            showInfo("Tecnología de " + nombre + " mejorada.");
            updateUICallback.run();
        } catch (ResourceException e) {
            showAlert(e.getMessage());
        }
    }

    public void updateUI() {
        foodValueLabel.setText(String.format("%,d", civilization.getFood()));
        woodValueLabel.setText(String.format("%,d", civilization.getWood()));
        ironValueLabel.setText(String.format("%,d", civilization.getIron()));
        manaValueLabel.setText(String.format("%,d", civilization.getMana()));

        // Calcular costes dinámicos de mejora
        int defLevel = civilization.getTechnologyDefense();
        int atkLevel = civilization.getTechnologyAttack();

        int defIronCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST + defLevel * Variables.UPGRADEPLUS_DEFENSE_TECHNOLOGY_IRON_COST;
        int defWoodCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_WOOD_COST + defLevel * Variables.UPGRADE_PLUS_DEFENSE_TECHNOLOGY_WOOD_COST;
        int atkIronCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST + atkLevel * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST;
        int atkWoodCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_WOOD_COST + atkLevel * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_WOOD_COST;

        String defCost = (defWoodCost > 0 ? "🪵" + defWoodCost + " " : "") + "⛏️" + defIronCost;
        String atkCost = (atkWoodCost > 0 ? "🪵" + atkWoodCost + " " : "") + "⛏️" + atkIronCost;

        actualizarTextoBoton(upgradeDefenseBtn, "🛡️ Mejorar Defensa", defCost);
        actualizarTextoBoton(upgradeAttackBtn, "⚔️ Mejorar Ataque", atkCost);
    }

    private void actualizarTextoBoton(Button btn, String textoBase, String coste) {
        if (btn.getGraphic() instanceof VBox) {
            VBox content = (VBox) btn.getGraphic();
            if (content.getChildren().size() >= 2) {
                Label nameLabel = (Label) content.getChildren().get(0);
                nameLabel.setText(textoBase);
                Label costLabel = (Label) content.getChildren().get(1);
                costLabel.setText(coste);
            }
        }
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