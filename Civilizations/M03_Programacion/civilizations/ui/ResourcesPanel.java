package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));

        HBox resourcesBar = createResourceBar();

        // Edificios
        Label constructionTitle = new Label("🏗️ EDIFICIOS");
        constructionTitle.getStyleClass().add("title");
        constructionTitle.setAlignment(Pos.CENTER_LEFT);
        constructionTitle.setMaxWidth(Double.MAX_VALUE);

        HBox constructionBox = new HBox(20);
        constructionBox.setAlignment(Pos.CENTER);
        constructionBox.setPadding(new Insets(10, 0, 15, 0));
        constructionBox.setMaxWidth(Double.MAX_VALUE);

        Button farmBtn = createBuildButton("Granja", 
            String.format("Comida: %,d | Madera: %,d | Hierro: %,d", 
                Variables.FOOD_COST_FARM, Variables.WOOD_COST_FARM, Variables.IRON_COST_FARM),
            "/img/buildings/farm.png");
        Button carpentryBtn = createBuildButton("Carpintería", 
            String.format("Comida: %,d | Madera: %,d | Hierro: %,d", 
                Variables.FOOD_COST_CARPENTRY, Variables.WOOD_COST_CARPENTRY, Variables.IRON_COST_CARPENTRY),
            "/img/buildings/carpentry.png");
        Button smithyBtn = createBuildButton("Herrería", 
            String.format("Comida: %,d | Madera: %,d | Hierro: %,d", 
                Variables.FOOD_COST_SMITHY, Variables.WOOD_COST_SMITHY, Variables.IRON_COST_SMITHY),
            "/img/buildings/smithy.png");
        Button magicTowerBtn = createBuildButton("Torre Mágica", 
            String.format("Comida: %,d | Madera: %,d | Hierro: %,d", 
                Variables.FOOD_COST_MAGICTOWER, Variables.WOOD_COST_MAGICTOWER, Variables.IRON_COST_MAGICTOWER),
            "/img/buildings/magic_tower.png");
        Button churchBtn = createBuildButton("Iglesia", 
            String.format("Comida: %,d | Madera: %,d | Hierro: %,d", 
                Variables.FOOD_COST_CHURCH, Variables.WOOD_COST_CHURCH, Variables.IRON_COST_CHURCH),
            "/img/buildings/church.png");

        constructionBox.getChildren().addAll(farmBtn, carpentryBtn, smithyBtn, magicTowerBtn, churchBtn);

        // Tecnologías
        Label techTitle = new Label("⚙️ TECNOLOGÍAS");
        techTitle.getStyleClass().add("title");
        techTitle.setAlignment(Pos.CENTER_LEFT);
        techTitle.setMaxWidth(Double.MAX_VALUE);

        HBox techBox = new HBox(30);
        techBox.setAlignment(Pos.CENTER);
        techBox.setPadding(new Insets(10, 0, 20, 0));

        upgradeDefenseBtn = createTechButton("🛡️ Mejorar Defensa", "Coste actual: 2000 Hierro");
        upgradeAttackBtn = createTechButton("⚔️ Mejorar Ataque", "Coste actual: 2000 Hierro");

        techBox.getChildren().addAll(upgradeDefenseBtn, upgradeAttackBtn);

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

    private HBox createResourceBar() {
        HBox bar = new HBox(25);
        bar.setAlignment(Pos.CENTER);
        bar.getStyleClass().add("resource-bar");
        bar.setMaxWidth(Double.MAX_VALUE);

        foodValueLabel = new Label("0");
        woodValueLabel = new Label("0");
        ironValueLabel = new Label("0");
        manaValueLabel = new Label("0");

        bar.getChildren().addAll(
            createResourceCard("Comida", "🍽️", foodValueLabel, "/img/resources/food.png"),
            createResourceCard("Madera", "🪵", woodValueLabel, "/img/resources/wood.png"),
            createResourceCard("Hierro", "⛏️", ironValueLabel, "/img/resources/iron.png"),
            createResourceCard("Maná", "✦", manaValueLabel, "/img/resources/mana.png")
        );
        return bar;
    }

    private HBox createResourceCard(String name, String icon, Label valueLabel, String imagePath) {
        HBox card = new HBox(8);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("resource-card");

        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        iconView.setFitWidth(28);
        iconView.setFitHeight(28);

        VBox infoBox = new VBox(2);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("resource-name");

        valueLabel.getStyleClass().add("resource-value");

        infoBox.getChildren().addAll(nameLabel, valueLabel);
        card.getChildren().addAll(iconView, infoBox);
        return card;
    }

    private Button createBuildButton(String name, String costText, String imagePath) {
        Button btn = new Button();
        btn.getStyleClass().addAll("button", "button-build");

        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);

        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        iconView.setFitWidth(48);
        iconView.setFitHeight(48);

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label costLabel = new Label(costText);
        costLabel.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 12px; -fx-text-alignment: center;");
        costLabel.setWrapText(true);
        costLabel.setAlignment(Pos.CENTER);

        content.getChildren().addAll(iconView, nameLabel, costLabel);
        btn.setGraphic(content);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return btn;
    }

    private Button createTechButton(String text, String cost) {
        Button btn = new Button();
        btn.getStyleClass().addAll("button", "button-tech");

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);

        Label nameLabel = new Label(text);
        nameLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label costLabel = new Label(cost);
        costLabel.setStyle("-fx-text-fill: #d4ac0d; -fx-font-size: 14px;");

        content.getChildren().addAll(nameLabel, costLabel);
        btn.setGraphic(content);
        btn.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        return btn;
    }

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
        foodValueLabel.setText(String.format("%,d", civilization.getFood()));
        woodValueLabel.setText(String.format("%,d", civilization.getWood()));
        ironValueLabel.setText(String.format("%,d", civilization.getIron()));
        manaValueLabel.setText(String.format("%,d", civilization.getMana()));

        int defLevel = civilization.getTechnologyDefense();
        int atkLevel = civilization.getTechnologyAttack();

        int defIronCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_IRON_COST + defLevel * Variables.UPGRADEPLUS_DEFENSE_TECHNOLOGY_IRON_COST;
        int defWoodCost = Variables.UPGRADE_BASE_DEFENSE_TECHNOLOGY_WOOD_COST + defLevel * Variables.UPGRADE_PLUS_DEFENSE_TECHNOLOGY_WOOD_COST;
        int atkIronCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_IRON_COST + atkLevel * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_IRON_COST;
        int atkWoodCost = Variables.UPGRADE_BASE_ATTACK_TECHNOLOGY_WOOD_COST + atkLevel * Variables.UPGRADE_PLUS_ATTACK_TECHNOLOGY_WOOD_COST;

        String defCost = "Coste: " + (defWoodCost > 0 ? "Madera: " + defWoodCost + " " : "") + "Hierro: " + defIronCost;
        String atkCost = "Coste: " + (atkWoodCost > 0 ? "Madera: " + atkWoodCost + " " : "") + "Hierro: " + atkIronCost;

        updateTechButton(upgradeDefenseBtn, "🛡️ Mejorar Defensa", defCost);
        updateTechButton(upgradeAttackBtn, "⚔️ Mejorar Ataque", atkCost);
    }

    private void updateTechButton(Button btn, String text, String cost) {
        if (btn.getGraphic() instanceof VBox) {
            VBox content = (VBox) btn.getGraphic();
            if (content.getChildren().size() >= 2) {
                Label nameLabel = (Label) content.getChildren().get(0);
                nameLabel.setText(text);
                Label costLabel = (Label) content.getChildren().get(1);
                costLabel.setText(cost);
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