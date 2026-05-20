package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
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
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15, 0, 15, 0));
        panel.setStyle("-fx-background: linear-gradient(from 0% 0% to 100% 100%, #2c3e50, #1a2a3a); -fx-border-width: 0;");

        // --- BARRA DE RECURSOS ---
        HBox resourcesBar = new HBox(25);
        resourcesBar.setAlignment(Pos.CENTER);
        resourcesBar.setStyle("-fx-background-color: #708090; -fx-padding: 12; -fx-border-radius: 8; -fx-border-color: #c0c0c0; -fx-border-width: 1;");
        resourcesBar.setMaxWidth(Double.MAX_VALUE);

        String labelStyle = "-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;";

        Label foodIcon = new Label("Comida:"); foodIcon.setStyle(labelStyle);
        foodValueLabel = new Label("0"); foodValueLabel.setStyle(labelStyle);
        Label woodIcon = new Label("Madera:"); woodIcon.setStyle(labelStyle);
        woodValueLabel = new Label("0"); woodValueLabel.setStyle(labelStyle);
        Label ironIcon = new Label("Hierro:"); ironIcon.setStyle(labelStyle);
        ironValueLabel = new Label("0"); ironValueLabel.setStyle(labelStyle);
        Label manaIcon = new Label("Mana:"); manaIcon.setStyle(labelStyle);
        manaValueLabel = new Label("0"); manaValueLabel.setStyle(labelStyle);

        resourcesBar.getChildren().addAll(foodIcon, foodValueLabel, woodIcon, woodValueLabel, ironIcon, ironValueLabel, manaIcon, manaValueLabel);

        // --- SECCION DE CREACION DE UNIDADES ---

        // Unidades de ataque
        Label attackTitle = new Label("CREAR UNIDADES DE ATAQUE");
        attackTitle.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox attackBox = new HBox(15);
        attackBox.setAlignment(Pos.CENTER);
        Button swordsmanBtn = createStyledButton("Espadachin", "Comida:8000\nMadera:3000\nHierro:50", "#c0392b", "#8b0000");
        Button spearmanBtn = createStyledButton("Lancero", "Comida:5000\nMadera:6500\nHierro:50", "#c0392b", "#8b0000");
        Button crossbowBtn = createStyledButton("Ballesta", "Madera:45000\nHierro:7000", "#c0392b", "#8b0000");
        Button cannonBtn = createStyledButton("Cañon", "Madera:30000\nHierro:15000", "#c0392b", "#8b0000");
        attackBox.getChildren().addAll(swordsmanBtn, spearmanBtn, crossbowBtn, cannonBtn);

        // Defensas
        Label defenseTitle = new Label("CREAR DEFENSAS");
        defenseTitle.setStyle("-fx-text-fill: #3498db; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox defenseBox = new HBox(15);
        defenseBox.setAlignment(Pos.CENTER);
        Button arrowTowerBtn = createStyledButton("Torre Flechas", "Madera:2000", "#2980b9", "#1a5276");
        Button catapultBtn = createStyledButton("Catapulta", "Madera:4000\nHierro:500", "#2980b9", "#1a5276");
        Button rocketBtn = createStyledButton("Torre Cohete", "Madera:50000\nHierro:5000", "#2980b9", "#1a5276");
        defenseBox.getChildren().addAll(arrowTowerBtn, catapultBtn, rocketBtn);

        // Unidades especiales
        Label specialTitle = new Label("CREAR UNIDADES ESPECIALES");
        specialTitle.setStyle("-fx-text-fill: #f1c40f; -fx-font-size: 18px; -fx-font-weight: bold;");
        HBox specialBox = new HBox(15);
        specialBox.setAlignment(Pos.CENTER);
        Button magicianBtn = createStyledButton("Mago", "Comida:12000\nMadera:2000\nMana:5000", "#b8860b", "#7d6608");
        Button priestBtn = createStyledButton("Sacerdote", "Comida:15000\nMana:15000", "#b8860b", "#7d6608");
        specialBox.getChildren().addAll(magicianBtn, priestBtn);

        VBox buttonsContainer = new VBox(15);
        buttonsContainer.setAlignment(Pos.CENTER);
        buttonsContainer.getChildren().addAll(attackTitle, attackBox, defenseTitle, defenseBox, specialTitle, specialBox);

        // --- ACCIONES ---
        swordsmanBtn.setOnAction(e -> createUnit(civilization, 0, "Espadachin", "Comida:8000 Madera:3000 Hierro:50"));
        spearmanBtn.setOnAction(e -> createUnit(civilization, 1, "Lancero", "Comida:5000 Madera:6500 Hierro:50"));
        crossbowBtn.setOnAction(e -> createUnit(civilization, 2, "Ballesta", "Madera:45000 Hierro:7000"));
        cannonBtn.setOnAction(e -> createUnit(civilization, 3, "Cañon", "Madera:30000 Hierro:15000"));
        arrowTowerBtn.setOnAction(e -> createUnit(civilization, 4, "Torre Flechas", "Madera:2000"));
        catapultBtn.setOnAction(e -> createUnit(civilization, 5, "Catapulta", "Madera:4000 Hierro:500"));
        rocketBtn.setOnAction(e -> createUnit(civilization, 6, "Torre Cohete", "Madera:50000 Hierro:5000"));
        magicianBtn.setOnAction(e -> createUnit(civilization, 7, "Mago", "Comida:12000 Madera:2000 Mana:5000"));
        priestBtn.setOnAction(e -> createUnit(civilization, 8, "Sacerdote", "Comida:15000 Mana:15000"));

        panel.getChildren().addAll(resourcesBar, buttonsContainer);
        return panel;
    }

    // Boton con estilo: fondo, borde mas oscuro, esquinas redondeadas, sombra
    private Button createStyledButton(String name, String cost, String bgColor, String borderColor) {
        Button btn = new Button();
        btn.setStyle("-fx-background-color: " + bgColor + ";" +
                     "-fx-text-fill: white;" +
                     "-fx-font-size: 14px;" +
                     "-fx-font-weight: bold;" +
                     "-fx-border-color: " + borderColor + ";" +
                     "-fx-border-radius: 12;" +
                     "-fx-background-radius: 12;" +
                     "-fx-padding: 10 5;" +
                     "-fx-effect: dropshadow(gaussian, black, 5, 0.2, 0, 2);");
        btn.setWrapText(true);
        btn.setAlignment(Pos.CENTER);
        btn.setMinWidth(170);
        btn.setPrefWidth(170);
        btn.setMaxWidth(170);
        btn.setMinHeight(100);
        btn.setPrefHeight(100);
        btn.setMaxHeight(100);

        VBox content = new VBox(5);
        content.setAlignment(Pos.CENTER);
        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold;");
        Label costLabel = new Label(cost);
        costLabel.setStyle("-fx-text-fill: #f0f0f0; -fx-font-size: 12px;");
        content.getChildren().addAll(nameLabel, costLabel);
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
                    showInfo("Se han añadido " + cantidad + " " + unitName + "(s) al ejercito.");
                    civ.saveToDatabase(); // <--- GUARDAR EN MYSQL
                    Platform.runLater(() -> updateUICallback.run());
                }
            } catch (NumberFormatException ex) {
                showAlert("Cantidad no valida.");
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
            alert.setTitle("Informacion");
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.showAndWait();
        });
    }
}