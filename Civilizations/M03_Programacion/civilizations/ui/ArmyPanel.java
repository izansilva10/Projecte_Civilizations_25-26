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

    // Constructor: inicializar todos los labels aquí
    public ArmyPanel(Civilization civilization, Runnable updateUICallback) {
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

        // Barra de recursos (usando los labels del constructor)
        HBox resourcesBar = createResourceBar();

        VBox contentContainer = new VBox(25);
        contentContainer.setAlignment(Pos.TOP_CENTER);

        // Ataque
        Label attackTitle = new Label("⚔️ UNIDADES DE ATAQUE");
        attackTitle.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane attackGrid = new GridPane();
        attackGrid.setHgap(15);
        attackGrid.setVgap(15);
        attackGrid.setPadding(new Insets(10));
        attackGrid.setAlignment(Pos.CENTER);

        Button swordsmanBtn = createUnitButton("Espadachín", "Coste: 8,000 C | 3,000 M | 50 H", "button-attack", "/img/units/swordsman.png", 0);
        Button spearmanBtn = createUnitButton("Lancero", "Coste: 5,000 C | 6,500 M | 50 H", "button-attack", "/img/units/spearman.png", 1);
        Button crossbowBtn = createUnitButton("Ballesta", "Coste: 45,000 M | 7,000 H", "button-attack", "/img/units/crossbow.png", 2);
        Button cannonBtn = createUnitButton("Cañón", "Coste: 30,000 M | 15,000 H", "button-attack", "/img/units/cannon.png", 3);

        attackGrid.add(swordsmanBtn, 0, 0);
        attackGrid.add(spearmanBtn, 1, 0);
        attackGrid.add(crossbowBtn, 2, 0);
        attackGrid.add(cannonBtn, 3, 0);

        // Defensa
        Label defenseTitle = new Label("🛡️ UNIDADES DE DEFENSA");
        defenseTitle.setStyle("-fx-text-fill: #3498db; -fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane defenseGrid = new GridPane();
        defenseGrid.setHgap(15);
        defenseGrid.setVgap(15);
        defenseGrid.setPadding(new Insets(10));
        defenseGrid.setAlignment(Pos.CENTER);

        Button arrowTowerBtn = createUnitButton("Torre Flechas", "Coste: 2,000 M", "button-defense", "/img/units/arrow_tower.png", 4);
        Button catapultBtn = createUnitButton("Catapulta", "Coste: 4,000 M | 500 H", "button-defense", "/img/units/catapult.png", 5);
        Button rocketBtn = createUnitButton("Torre Cohete", "Coste: 50,000 M | 5,000 H", "button-defense", "/img/units/rocket_launcher.png", 6);

        defenseGrid.add(arrowTowerBtn, 0, 0);
        defenseGrid.add(catapultBtn, 1, 0);
        defenseGrid.add(rocketBtn, 2, 0);

        // Especiales
        Label specialTitle = new Label("✨ UNIDADES ESPECIALES");
        specialTitle.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane specialGrid = new GridPane();
        specialGrid.setHgap(15);
        specialGrid.setVgap(15);
        specialGrid.setPadding(new Insets(10));
        specialGrid.setAlignment(Pos.CENTER);

        Button magicianBtn = createUnitButton("Mago", "Coste: 12,000 C | 2,000 M | 5,000 Ma", "button-special", "/img/units/magician.png", 7);
        Button priestBtn = createUnitButton("Sacerdote", "Coste: 15,000 C | 15,000 Ma", "button-special", "/img/units/priest.png", 8);

        specialGrid.add(magicianBtn, 0, 0);
        specialGrid.add(priestBtn, 1, 0);

        contentContainer.getChildren().addAll(
            attackTitle, attackGrid,
            new Separator(),
            defenseTitle, defenseGrid,
            new Separator(),
            specialTitle, specialGrid
        );

        panel.getChildren().addAll(resourcesBar, contentContainer);
        return panel;
    }

    private HBox createResourceBar() {
        return ResourceBar.create(civilization, foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel);
    }

    private Button createUnitButton(String name, String costText, String styleClass, String imagePath, int unitType) {
        Button btn = new Button();
        btn.getStyleClass().addAll("button", styleClass);
        btn.setWrapText(true);
        btn.setAlignment(Pos.CENTER);
        btn.setMinWidth(170);
        btn.setPrefWidth(190);
        btn.setMinHeight(130);
        btn.setPrefHeight(130);

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
        
        btn.setOnAction(e -> createUnit(civilization, unitType, name, costText));
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
        // Actualizar los valores de los labels (ya no son null)
        if (foodValueLabel != null) {
            foodValueLabel.setText(String.format("%,d", civilization.getFood()));
        }
        if (woodValueLabel != null) {
            woodValueLabel.setText(String.format("%,d", civilization.getWood()));
        }
        if (ironValueLabel != null) {
            ironValueLabel.setText(String.format("%,d", civilization.getIron()));
        }
        if (manaValueLabel != null) {
            manaValueLabel.setText(String.format("%,d", civilization.getMana()));
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