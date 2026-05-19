package civilizations.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import civilizations.*;

import java.util.ArrayList;
import java.util.Optional;

public class ArmyPanel {
    private Civilization civilization;
    private Runnable updateUICallback;

    // Recursos
    private Label foodLabel, woodLabel, ironLabel, manaLabel;
    
    // Unidades: iconos y contadores
    private Label[] unitCountLabels = new Label[9];
    private final String[] unitNames = {"Espadachín", "Lancero", "Ballesta", "Cañón",
                                        "Torre Flechas", "Catapulta", "Torre Cohete", "Mago", "Sacerdote"};
    private final String[] unitIcons = {"⚔️", "🏹", "🏹", "💣", "🗼", "🏗️", "🚀", "🔮", "⛪"};
    
    // Colores de fondo
    private static final String GRASS_COLOR = "#2b5e2b";
    private static final String SKY_COLOR = "#87CEEB";

    public ArmyPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
    }

    public VBox getPanel() {
        // Panel principal con gradiente cielo-césped
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setStyle("-fx-background: linear-gradient(from 0% 0% to 0% 100%, " + SKY_COLOR + " 0%, " + GRASS_COLOR + " 100%);");
        
        // ---- Recursos (estilo cartel) ----
        GridPane resourcesGrid = new GridPane();
        resourcesGrid.setHgap(20);
        resourcesGrid.setVgap(10);
        resourcesGrid.setStyle("-fx-background-color: rgba(0,0,0,0.6); -fx-border-radius: 10; -fx-padding: 10; -fx-border-color: gold; -fx-border-width: 2;");
        foodLabel = createResourceLabel();
        woodLabel = createResourceLabel();
        ironLabel = createResourceLabel();
        manaLabel = createResourceLabel();
        resourcesGrid.add(new Label("🍽️ Comida:"), 0, 0);
        resourcesGrid.add(foodLabel, 1, 0);
        resourcesGrid.add(new Label("🪵 Madera:"), 2, 0);
        resourcesGrid.add(woodLabel, 3, 0);
        resourcesGrid.add(new Label("⛏️ Hierro:"), 0, 1);
        resourcesGrid.add(ironLabel, 1, 1);
        resourcesGrid.add(new Label("✦ Maná:"), 2, 1);
        resourcesGrid.add(manaLabel, 3, 1);
        
        // ---- Grid de unidades (3x3) ----
        GridPane unitsGrid = new GridPane();
        unitsGrid.setHgap(15);
        unitsGrid.setVgap(15);
        unitsGrid.setAlignment(Pos.CENTER);
        unitsGrid.setStyle("-fx-background-color: rgba(0,0,0,0.4); -fx-padding: 20; -fx-border-radius: 15;");
        
        int index = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                VBox unitBox = new VBox(5);
                unitBox.setAlignment(Pos.CENTER);
                unitBox.setStyle("-fx-background-color: rgba(0,0,0,0.5); -fx-border-radius: 15; -fx-padding: 10; -fx-border-color: #d4a017; -fx-border-width: 2;");
                Label iconLabel = new Label(unitIcons[index]);
                iconLabel.setFont(Font.font("Segoe UI Emoji", 40));
                Label nameLabel = new Label(unitNames[index]);
                nameLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
                unitCountLabels[index] = new Label("0");
                unitCountLabels[index].setStyle("-fx-text-fill: #ffd700; -fx-font-size: 20; -fx-font-weight: bold;");
                unitBox.getChildren().addAll(iconLabel, nameLabel, unitCountLabels[index]);
                unitsGrid.add(unitBox, col, row);
                index++;
            }
        }
        
        // ---- Botones de creación (más grandes, sin referencias a método) ----
        Label attackTitle = new Label("⚔️ CREAR UNIDADES DE ATAQUE");
        attackTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold; -fx-effect: dropshadow(one-pass-box, black, 2, 0, 0, 0);");
        GridPane attackGrid = new GridPane();
        attackGrid.setHgap(10);
        attackGrid.setVgap(10);
        attackGrid.setAlignment(Pos.CENTER);
        
        Button swordsmanBtn = createStyledButton("Espadachín", "8000🍽️ 3000🪵 50⛏️");
        Button spearmanBtn = createStyledButton("Lancero", "5000🍽️ 6500🪵 50⛏️");
        Button crossbowBtn = createStyledButton("Ballesta", "45000🪵 7000⛏️");
        Button cannonBtn = createStyledButton("Cañón", "30000🪵 15000⛏️");
        
        attackGrid.add(swordsmanBtn, 0, 0);
        attackGrid.add(spearmanBtn, 1, 0);
        attackGrid.add(crossbowBtn, 2, 0);
        attackGrid.add(cannonBtn, 3, 0);
        
        Label defenseTitle = new Label("🛡️ CREAR DEFENSAS Y ESPECIALES");
        defenseTitle.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-weight: bold; -fx-effect: dropshadow(one-pass-box, black, 2, 0, 0, 0);");
        GridPane defenseGrid = new GridPane();
        defenseGrid.setHgap(10);
        defenseGrid.setVgap(10);
        defenseGrid.setAlignment(Pos.CENTER);
        
        Button arrowTowerBtn = createStyledButton("Torre Flechas", "2000🪵");
        Button catapultBtn = createStyledButton("Catapulta", "4000🪵 500⛏️");
        Button rocketBtn = createStyledButton("Torre Cohete", "50000🪵 5000⛏️");
        Button magicianBtn = createStyledButton("Mago", "12000🍽️ 2000🪵 5000✦");
        Button priestBtn = createStyledButton("Sacerdote", "15000🍽️ 15000✦");
        
        defenseGrid.add(arrowTowerBtn, 0, 0);
        defenseGrid.add(catapultBtn, 1, 0);
        defenseGrid.add(rocketBtn, 2, 0);
        defenseGrid.add(magicianBtn, 3, 0);
        defenseGrid.add(priestBtn, 4, 0);
        
        // Botón de refresco (por si acaso)
        Button refreshBtn = new Button("🔄 REFRESCAR VISTA");
        refreshBtn.setStyle("-fx-font-size: 14; -fx-padding: 8; -fx-background-color: #ffaa00; -fx-font-weight: bold;");
        refreshBtn.setOnAction(e -> updateUICallback.run());
        
        // ---- Asignar acciones (sin referencias a método) ----
        swordsmanBtn.setOnAction(e -> createUnit(civilization, 0, "Espadachín", "8000🍽️, 3000🪵, 50⛏️"));
        spearmanBtn.setOnAction(e -> createUnit(civilization, 1, "Lancero", "5000🍽️, 6500🪵, 50⛏️"));
        crossbowBtn.setOnAction(e -> createUnit(civilization, 2, "Ballesta", "45000🪵, 7000⛏️"));
        cannonBtn.setOnAction(e -> createUnit(civilization, 3, "Cañón", "30000🪵, 15000⛏️"));
        arrowTowerBtn.setOnAction(e -> createUnit(civilization, 4, "Torre Flechas", "2000🪵"));
        catapultBtn.setOnAction(e -> createUnit(civilization, 5, "Catapulta", "4000🪵, 500⛏️"));
        rocketBtn.setOnAction(e -> createUnit(civilization, 6, "Torre Cohete", "50000🪵, 5000⛏️"));
        magicianBtn.setOnAction(e -> createUnit(civilization, 7, "Mago", "12000🍽️, 2000🪵, 5000✦"));
        priestBtn.setOnAction(e -> createUnit(civilization, 8, "Sacerdote", "15000🍽️, 15000✦"));
        
        // Organización final
        VBox bottomBox = new VBox(15);
        bottomBox.setAlignment(Pos.CENTER);
        bottomBox.getChildren().addAll(attackTitle, attackGrid, defenseTitle, defenseGrid, refreshBtn);
        
        panel.getChildren().addAll(resourcesGrid, unitsGrid, bottomBox);
        return panel;
    }
    
    private Label createResourceLabel() {
        Label label = new Label();
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14;");
        return label;
    }
    
    private Button createStyledButton(String text, String cost) {
        Button btn = new Button(text + "\n" + cost);
        btn.setStyle("-fx-font-size: 12; -fx-padding: 8; -fx-background-color: #2c3e50; -fx-text-fill: white; -fx-font-weight: bold; -fx-border-color: #f1c40f; -fx-border-radius: 8;");
        btn.setWrapText(true);
        btn.setPrefWidth(130);
        return btn;
    }
    
    // Método genérico para crear unidades SIN referencias a método
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
                    // Llamada directa según el tipo (sin referencia a método)
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
                        default: throw new IllegalArgumentException("Tipo desconocido");
                    }
                    showInfo("Se han añadido " + cantidad + " " + unitName + "(s) al ejército.");
                    Platform.runLater(() -> updateUICallback.run());
                }
            } catch (NumberFormatException ex) {
                showAlert("Cantidad no válida.");
            } catch (ResourceException | BuildingException ex) {
                showAlert(ex.getMessage());
            } catch (Exception ex) {
                showAlert("Error: " + ex.getMessage());
            }
        });
    }
    
    public void updateUI() {
        // Actualizar recursos
        foodLabel.setText(String.format("%,d", civilization.getFood()));
        woodLabel.setText(String.format("%,d", civilization.getWood()));
        ironLabel.setText(String.format("%,d", civilization.getIron()));
        manaLabel.setText(String.format("%,d", civilization.getMana()));
        
        // Actualizar contadores de unidades
        ArrayList<MilitaryUnit>[] armyGroups = civilization.getArmy();
        for (int i = 0; i < 9; i++) {
            int count = armyGroups[i].size();
            unitCountLabels[i].setText(String.valueOf(count));
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