package civilizations.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import civilizations.Civilization;
import civilizations.MilitaryUnit;

import java.io.File;
import java.util.ArrayList;

public class CivilizationInfoPanel {
    private Civilization civilization;
    private Runnable updateUICallback;
    
    // Ruta de la imagen (dentro del proyecto, carpeta resources)
    private static final String IMAGE_PATH = "resources/image.png";
    
    private Label foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel;
    private Label techDefLabel, techAtkLabel;
    private Label farmLabel, carpentryLabel, smithyLabel, magicTowerLabel, churchLabel;
    private Label[] unitCountLabels = new Label[9];
    private final String[] unitNames = {"Espadachín", "Lancero", "Ballesta", "Cañón",
                                        "Torre Flechas", "Catapulta", "Torre Cohete", "Mago", "Sacerdote"};

    public CivilizationInfoPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
    }

    public VBox getPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.TOP_CENTER);
        
        // --- CARGAR IMAGEN DE FONDO (OBLIGATORIA) ---
        boolean imagenCargada = false;
        try {
            File imgFile = new File(IMAGE_PATH);
            System.out.println("Buscando imagen en: " + imgFile.getAbsolutePath());
            if (imgFile.exists()) {
                Image backgroundImage = new Image(imgFile.toURI().toString());
                // Escala la imagen para que cubra todo el panel (100% ancho y alto)
                BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, false, true);
                BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER, bgSize);
                panel.setBackground(new Background(bgImage));
                imagenCargada = true;
                System.out.println("✅ Fondo de imagen cargado correctamente: " + IMAGE_PATH);
            } else {
                System.err.println("❌ No se encontró la imagen en: " + imgFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar la imagen: " + e.getMessage());
        }
        
        // Si no se pudo cargar la imagen, usamos un color de respaldo (rojo para que sea evidente)
        if (!imagenCargada) {
            panel.setStyle("-fx-background-color: #8b0000;"); // rojo para indicar error
        }
        
        // --- BARRA DE RECURSOS (semirtransparente) ---
        HBox resourcesBar = new HBox(25);
        resourcesBar.setAlignment(Pos.CENTER);
        resourcesBar.setStyle("-fx-background-color: rgba(112,128,144,0.7); -fx-padding: 12; -fx-border-radius: 8; -fx-border-color: #c0c0c0; -fx-border-width: 1;");
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
        
        // --- CONTENEDOR PRINCIPAL CON DOS COLUMNAS (fondos completamente transparentes) ---
        HBox columnsContainer = new HBox(30);
        columnsContainer.setAlignment(Pos.TOP_CENTER);
        columnsContainer.setPadding(new Insets(10));
        columnsContainer.setMaxWidth(Double.MAX_VALUE);
        columnsContainer.setStyle("-fx-background-color: transparent;");
        
        // COLUMNA IZQUIERDA (solo borde, sin relleno)
        VBox leftColumn = new VBox(15);
        leftColumn.setAlignment(Pos.TOP_CENTER);
        leftColumn.setStyle("-fx-background-color: transparent; -fx-border-color: #5dade2; -fx-border-radius: 10; -fx-padding: 15; -fx-border-width: 2;");
        leftColumn.setPrefWidth(400);
        
        Label leftTitle = new Label("📊 ESTADÍSTICAS");
        leftTitle.setStyle("-fx-text-fill: #e6c97e; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        GridPane leftGrid = new GridPane();
        leftGrid.setHgap(20);
        leftGrid.setVgap(12);
        leftGrid.setPadding(new Insets(10));
        leftGrid.setStyle("-fx-background-color: transparent;");
        
        // Tecnologías
        techDefLabel = new Label("0");
        techDefLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        techAtkLabel = new Label("0");
        techAtkLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        leftGrid.add(new Label("🛡️ Tecnología Defensa:"), 0, 0);
        leftGrid.add(techDefLabel, 1, 0);
        leftGrid.add(new Label("⚔️ Tecnología Ataque:"), 0, 1);
        leftGrid.add(techAtkLabel, 1, 1);
        
        Separator sep1 = new Separator();
        sep1.setStyle("-fx-background-color: #5dade2;");
        leftGrid.add(sep1, 0, 2);
        GridPane.setColumnSpan(sep1, 2);
        
        // Edificios
        farmLabel = new Label("0"); carpentryLabel = new Label("0"); smithyLabel = new Label("0");
        magicTowerLabel = new Label("0"); churchLabel = new Label("0");
        leftGrid.add(new Label("🏚️ Granjas:"), 0, 3);
        leftGrid.add(farmLabel, 1, 3);
        leftGrid.add(new Label("🪚 Carpinterías:"), 0, 4);
        leftGrid.add(carpentryLabel, 1, 4);
        leftGrid.add(new Label("⚒️ Herrerías:"), 0, 5);
        leftGrid.add(smithyLabel, 1, 5);
        leftGrid.add(new Label("🔮 Torres Mágicas:"), 0, 6);
        leftGrid.add(magicTowerLabel, 1, 6);
        leftGrid.add(new Label("⛪ Iglesias:"), 0, 7);
        leftGrid.add(churchLabel, 1, 7);
        
        leftColumn.getChildren().addAll(leftTitle, leftGrid);
        
        // COLUMNA DERECHA (solo borde)
        VBox rightColumn = new VBox(15);
        rightColumn.setAlignment(Pos.TOP_CENTER);
        rightColumn.setStyle("-fx-background-color: transparent; -fx-border-color: #5dade2; -fx-border-radius: 10; -fx-padding: 15; -fx-border-width: 2;");
        rightColumn.setPrefWidth(400);
        
        Label rightTitle = new Label("⚔️ EJÉRCITO ACTUAL");
        rightTitle.setStyle("-fx-text-fill: #e6c97e; -fx-font-size: 18px; -fx-font-weight: bold;");
        
        GridPane armyGrid = new GridPane();
        armyGrid.setHgap(20);
        armyGrid.setVgap(10);
        armyGrid.setPadding(new Insets(10));
        armyGrid.setStyle("-fx-background-color: transparent;");
        
        for (int i = 0; i < 9; i++) {
            Label nameLabel = new Label(unitNames[i]);
            nameLabel.setStyle("-fx-text-fill: #aaccff; -fx-font-size: 15px;");
            unitCountLabels[i] = new Label("0");
            unitCountLabels[i].setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
            armyGrid.add(nameLabel, 0, i);
            armyGrid.add(unitCountLabels[i], 1, i);
        }
        
        rightColumn.getChildren().addAll(rightTitle, armyGrid);
        
        columnsContainer.getChildren().addAll(leftColumn, rightColumn);
        
        // ScrollPane transparente
        ScrollPane scrollPane = new ScrollPane(columnsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(600);
        
        panel.getChildren().addAll(resourcesBar, scrollPane);
        return panel;
    }
    
    public void updateUI() {
        foodValueLabel.setText(String.format("%,d", civilization.getFood()));
        woodValueLabel.setText(String.format("%,d", civilization.getWood()));
        ironValueLabel.setText(String.format("%,d", civilization.getIron()));
        manaValueLabel.setText(String.format("%,d", civilization.getMana()));
        
        techDefLabel.setText(String.valueOf(civilization.getTechnologyDefense()));
        techAtkLabel.setText(String.valueOf(civilization.getTechnologyAttack()));
        
        farmLabel.setText(String.valueOf(civilization.getFarm()));
        carpentryLabel.setText(String.valueOf(civilization.getCarpentry()));
        smithyLabel.setText(String.valueOf(civilization.getSmithy()));
        magicTowerLabel.setText(String.valueOf(civilization.getMagicTower()));
        churchLabel.setText(String.valueOf(civilization.getChurch()));
        
        ArrayList<MilitaryUnit>[] armyGroups = civilization.getArmy();
        for (int i = 0; i < 9; i++) {
            int count = armyGroups[i].size();
            unitCountLabels[i].setText(String.valueOf(count));
        }
    }
}