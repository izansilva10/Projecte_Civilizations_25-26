package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import civilizations.*;
import java.util.Optional;

public class ArmyPanel {
    private Civilization civilization;
    private Runnable updateUICallback;
    private Label foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel;

    public ArmyPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
    }

    public VBox getPanel() {
        VBox panel = new VBox(20);
        panel.setPadding(new Insets(20));

        HBox resourcesBar = createResourceBar();

        VBox contentContainer = new VBox(25);
        contentContainer.setAlignment(Pos.TOP_CENTER);

        // Ataque
        Label attackTitle = new Label("⚔️ UNIDADES DE ATAQUE");
        attackTitle.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 20px; -fx-font-weight: bold;");
        HBox attackBox = new HBox(15);
        attackBox.setAlignment(Pos.CENTER);
        attackBox.setPadding(new Insets(5, 0, 10, 0));
        attackBox.setMaxWidth(Double.MAX_VALUE);

        Button swordsmanBtn = createUnitButton("Espadachín", "Coste: 8,000 C | 3,000 M | 50 H", "button-attack", "/img/units/swordsman.png");
        Button spearmanBtn = createUnitButton("Lancero", "Coste: 5,000 C | 6,500 M | 50 H", "button-attack", "/img/units/spearman.png");
        Button crossbowBtn = createUnitButton("Ballesta", "Coste: 45,000 M | 7,000 H", "button-attack", "/img/units/crossbow.png");
        Button cannonBtn = createUnitButton("Cañón", "Coste: 30,000 M | 15,000 H", "button-attack", "/img/units/cannon.png");

        attackBox.getChildren().addAll(swordsmanBtn, spearmanBtn, crossbowBtn, cannonBtn);

        // Defensa
        Label defenseTitle = new Label("🛡️ UNIDADES DE DEFENSA");
        defenseTitle.setStyle("-fx-text-fill: #3498db; -fx-font-size: 20px; -fx-font-weight: bold;");
        HBox defenseBox = new HBox(15);
        defenseBox.setAlignment(Pos.CENTER);
        defenseBox.setPadding(new Insets(5, 0, 10, 0));
        defenseBox.setMaxWidth(Double.MAX_VALUE);

        Button arrowTowerBtn = createUnitButton("Torre Flechas", "Coste: 2,000 M", "button-defense", "/img/units/arrow_tower.png");
        Button catapultBtn = createUnitButton("Catapulta", "Coste: 4,000 M | 500 H", "button-defense", "/img/units/catapult.png");
        Button rocketBtn = createUnitButton("Torre Cohete", "Coste: 50,000 M | 5,000 H", "button-defense", "/img/units/rocket_launcher.png");

        defenseBox.getChildren().addAll(arrowTowerBtn, catapultBtn, rocketBtn);

        // Especiales
        Label specialTitle = new Label("✨ UNIDADES ESPECIALES");
        specialTitle.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 20px; -fx-font-weight: bold;");
        HBox specialBox = new HBox(15);
        specialBox.setAlignment(Pos.CENTER);
        specialBox.setPadding(new Insets(5, 0, 10, 0));
        specialBox.setMaxWidth(Double.MAX_VALUE);

        Button magicianBtn = createUnitButton("Mago", "Coste: 12,000 C | 2,000 M | 5,000 Ma", "button-special", "/img/units/magician.png");
        Button priestBtn = createUnitButton("Sacerdote", "Coste: 15,000 C | 15,000 Ma", "button-special", "/img/units/priest.png");

        specialBox.getChildren().addAll(magicianBtn, priestBtn);

        contentContainer.getChildren().addAll(
            attackTitle, attackBox,
            defenseTitle, defenseBox,
            specialTitle, specialBox
        );

        panel.getChildren().addAll(resourcesBar, contentContainer);
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

    private Button createUnitButton(String name, String costText, String styleClass, String imagePath) {
        Button btn = new Button();
        btn.getStyleClass().addAll("button", styleClass);
        btn.setWrapText(true);
        btn.setAlignment(Pos.CENTER);
        btn.setMinWidth(170);
        btn.setPrefWidth(180);
        btn.setMinHeight(120);
        btn.setPrefHeight(120);

        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);

        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(imagePath)));
        iconView.setFitWidth(44);
        iconView.setFitHeight(44);

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

    private void createUnit(Civilization civ, int unitType, String unitName, String costStr) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Crear " + unitName);
        dialog.setHeaderText("Coste por unidad: " + costStr);
        dialog.setContentText("Cantidad:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(value -> {
            try {
                int cantidad = Integer.parseInt(value);
                if (cantidad > 0) {
                    switch (unitType) {
                        case 0: civ.newSwordsman(cantidad); break;
                        case 1: civ.newSpearman(cantidad); break;
                        case 2: civ.newCrossbow(cantidad); break;
                        case 3: civ.newCannon(cantidad); break;
                        case 4: civ.newArrowTower(cantidad); break;
                        case 5: civ.newCatapult(cantidad); break;
                        case 6: civ.newRocketLauncher(cantidad); break;
                        case 7: civ.newMagician(cantidad); break;
                        case 8: civ.newPriest(cantidad); break;
                    }
                    showInfo("Se han añadido " + cantidad + " " + unitName + "(s) al ejército.");
                    civ.saveToDatabase();
                    Platform.runLater(() -> updateUICallback.run());
                }
            } catch (NumberFormatException ex) {
                showAlert("Cantidad no válida.");
            } catch (ResourceException | BuildingException ex) {
                showAlert(ex.getMessage());
            }
        });
    }

    public void updateUI() {
        foodValueLabel.setText(String.format("%,d", civilization.getFood()));
        woodValueLabel.setText(String.format("%,d", civilization.getWood()));
        ironValueLabel.setText(String.format("%,d", civilization.getIron()));
        manaValueLabel.setText(String.format("%,d", civilization.getMana()));
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