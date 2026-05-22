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
    private static final String IMAGE_PATH = "resources/image.png";

    // Recursos
    private Label foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel;
    // Tecnologías
    private Label techDefLabel, techAtkLabel;
    // Edificios
    private Label farmLabel, carpentryLabel, smithyLabel, magicTowerLabel, churchLabel;
    // Ejército
    private Label[] unitCountLabels = new Label[9];
    private final String[] unitNames = {
        "Espadachín", "Lancero", "Ballesta", "Cañón",
        "Torre de Flechas", "Catapulta", "Torre Cohete", "Mago", "Sacerdote"
    };

    public CivilizationInfoPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;
    }

    public VBox getPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.TOP_CENTER);

        // Fondo con imagen (si existe) o gradiente oscuro
        try {
            File imgFile = new File(IMAGE_PATH);
            if (imgFile.exists()) {
                Image backgroundImage = new Image(imgFile.toURI().toString());
                BackgroundSize bgSize = new BackgroundSize(100, 100, true, true, false, true);
                BackgroundImage bgImage = new BackgroundImage(backgroundImage,
                        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER, bgSize);
                panel.setBackground(new Background(bgImage));
            } else {
                panel.setStyle("-fx-background-color: #0f1720;");
            }
        } catch (Exception e) {
            panel.setStyle("-fx-background-color: #0f1720;");
        }

        // 1. Barra de recursos HUD
        HBox resourcesBar = createResourceBar();

        // 2. Contenedor de dos columnas
        HBox columnsContainer = new HBox(30);
        columnsContainer.setAlignment(Pos.TOP_CENTER);
        columnsContainer.setPadding(new Insets(10));
        columnsContainer.setMaxWidth(Double.MAX_VALUE);

        // --- COLUMNA IZQUIERDA: ESTADÍSTICAS Y EDIFICIOS ---
        VBox leftColumn = new VBox(15);
        leftColumn.setAlignment(Pos.TOP_CENTER);
        leftColumn.setPrefWidth(450);
        leftColumn.setMaxWidth(500);

        // Panel de Tecnologías
        VBox techCard = createInfoCard("⚙️ TECNOLOGÍAS");
        GridPane techGrid = new GridPane();
        techGrid.setHgap(20);
        techGrid.setVgap(8);
        techGrid.setPadding(new Insets(5));

        techDefLabel = new Label("0");
        techDefLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold;");
        techAtkLabel = new Label("0");
        techAtkLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold;");

        techGrid.add(new Label("🛡️ Defensa"), 0, 0);
        techGrid.add(techDefLabel, 1, 0);
        techGrid.add(new Label("⚔️ Ataque"), 0, 1);
        techGrid.add(techAtkLabel, 1, 1);
        techCard.getChildren().add(techGrid);

        // Panel de Edificios
        VBox buildCard = createInfoCard("🏗️ EDIFICIOS");
        GridPane buildGrid = new GridPane();
        buildGrid.setHgap(20);
        buildGrid.setVgap(6);
        buildGrid.setPadding(new Insets(5));

        farmLabel = new Label("0"); carpentryLabel = new Label("0");
        smithyLabel = new Label("0"); magicTowerLabel = new Label("0");
        churchLabel = new Label("0");

        buildGrid.add(new Label("🏚️ Granjas"), 0, 0);
        buildGrid.add(farmLabel, 1, 0);
        buildGrid.add(new Label("🪚 Carpinterías"), 0, 1);
        buildGrid.add(carpentryLabel, 1, 1);
        buildGrid.add(new Label("⚒️ Herrerías"), 0, 2);
        buildGrid.add(smithyLabel, 1, 2);
        buildGrid.add(new Label("🔮 Torres Mágicas"), 0, 3);
        buildGrid.add(magicTowerLabel, 1, 3);
        buildGrid.add(new Label("⛪ Iglesias"), 0, 4);
        buildGrid.add(churchLabel, 1, 4);

        buildCard.getChildren().add(buildGrid);

        leftColumn.getChildren().addAll(techCard, buildCard);

        // --- COLUMNA DERECHA: EJÉRCITO ---
        VBox rightColumn = new VBox(15);
        rightColumn.setAlignment(Pos.TOP_CENTER);
        rightColumn.setPrefWidth(450);
        rightColumn.setMaxWidth(500);

        // Panel de Ejército
        VBox armyCard = createInfoCard("⚔️ EJÉRCITO");
        GridPane armyGrid = new GridPane();
        armyGrid.setHgap(20);
        armyGrid.setVgap(6);
        armyGrid.setPadding(new Insets(5));

        for (int i = 0; i < 9; i++) {
            Label nameLabel = new Label(unitNames[i]);
            nameLabel.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 15px;");
            unitCountLabels[i] = new Label("0");
            unitCountLabels[i].setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold;");
            armyGrid.add(nameLabel, 0, i);
            armyGrid.add(unitCountLabels[i], 1, i);
        }

        armyCard.getChildren().add(armyGrid);
        rightColumn.getChildren().add(armyCard);

        // Agregar columnas al contenedor
        columnsContainer.getChildren().addAll(leftColumn, rightColumn);

        // 3. ScrollPane para contenido
        ScrollPane scrollPane = new ScrollPane(columnsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(600);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        panel.getChildren().addAll(resourcesBar, scrollPane);
        return panel;
    }

    // ==========================================
    // BARRA DE RECURSOS HUD
    // ==========================================
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
            createResourceCard("Comida", "🍽️", foodValueLabel),
            createResourceCard("Madera", "🪵", woodValueLabel),
            createResourceCard("Hierro", "⛏️", ironValueLabel),
            createResourceCard("Maná", "✦", manaValueLabel)
        );
        return bar;
    }

    private HBox createResourceCard(String name, String icon, Label valueLabel) {
        HBox card = new HBox(8);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("resource-card");

        Label iconLabel = new Label(icon);
        iconLabel.getStyleClass().add("resource-icon");

        VBox infoBox = new VBox(2);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("resource-name");

        valueLabel.getStyleClass().add("resource-value");

        infoBox.getChildren().addAll(nameLabel, valueLabel);
        card.getChildren().addAll(iconLabel, infoBox);
        return card;
    }

    // ==========================================
    // TARJETA DE INFORMACIÓN (PANEL ESTILO)
    // ==========================================
    private VBox createInfoCard(String title) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #1c2833; -fx-border-color: #4ea5d9; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 8, 0, 0, 4);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 8 0; -fx-border-color: #4ea5d9; -fx-border-width: 0 0 1 0;");

        card.getChildren().add(titleLabel);
        return card;
    }

    // ==========================================
    // ACTUALIZACIÓN DE UI
    // ==========================================
    public void updateUI() {
        // Recursos
        foodValueLabel.setText(String.format("%,d", civilization.getFood()));
        woodValueLabel.setText(String.format("%,d", civilization.getWood()));
        ironValueLabel.setText(String.format("%,d", civilization.getIron()));
        manaValueLabel.setText(String.format("%,d", civilization.getMana()));

        // Tecnologías
        techDefLabel.setText(String.valueOf(civilization.getTechnologyDefense()));
        techAtkLabel.setText(String.valueOf(civilization.getTechnologyAttack()));

        // Edificios
        farmLabel.setText(String.valueOf(civilization.getFarm()));
        carpentryLabel.setText(String.valueOf(civilization.getCarpentry()));
        smithyLabel.setText(String.valueOf(civilization.getSmithy()));
        magicTowerLabel.setText(String.valueOf(civilization.getMagicTower()));
        churchLabel.setText(String.valueOf(civilization.getChurch()));

        // Ejército
        ArrayList<MilitaryUnit>[] armyGroups = civilization.getArmy();
        for (int i = 0; i < 9; i++) {
            int count = armyGroups[i].size();
            unitCountLabels[i].setText(String.valueOf(count));
        }
    }
}