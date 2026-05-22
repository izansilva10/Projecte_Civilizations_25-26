package civilizations.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import civilizations.Civilization;
import civilizations.MilitaryUnit;
import java.util.ArrayList;

public class CivilizationInfoPanel {
    private Civilization civilization;
    private Runnable updateUICallback;

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
    private final String[] unitIcons = {
        "⚔️", "🗡️", "🏹", "💥",
        "🏹", "💣", "🚀", "✨", "🙏"
    };

    public CivilizationInfoPanel(Civilization civilization, Runnable updateUICallback) {
        this.civilization = civilization;
        this.updateUICallback = updateUICallback;

        // INICIALIZAR LOS LABELS AQUÍ PARA EVITAR NullPointerException
        this.foodValueLabel = new Label("0");
        this.woodValueLabel = new Label("0");
        this.ironValueLabel = new Label("0");
        this.manaValueLabel = new Label("0");
        this.techDefLabel = new Label("0");
        this.techAtkLabel = new Label("0");
        this.farmLabel = new Label("0");
        this.carpentryLabel = new Label("0");
        this.smithyLabel = new Label("0");
        this.magicTowerLabel = new Label("0");
        this.churchLabel = new Label("0");
        for (int i = 0; i < 9; i++) {
            this.unitCountLabels[i] = new Label("0");
        }
    }

    public VBox getPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(15));
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setStyle("-fx-background-color: #0f1720;");

        // Barra de recursos
        HBox resourcesBar = createResourceBar();

        // Contenedor de dos columnas
        HBox columnsContainer = new HBox(30);
        columnsContainer.setAlignment(Pos.TOP_CENTER);
        columnsContainer.setPadding(new Insets(10));
        columnsContainer.setMaxWidth(Double.MAX_VALUE);

        // Columna izquierda: Tecnologías y Edificios
        VBox leftColumn = new VBox(15);
        leftColumn.setAlignment(Pos.TOP_CENTER);
        leftColumn.setPrefWidth(450);
        leftColumn.setMaxWidth(500);

        // Panel de Tecnologías
        VBox techCard = createInfoCard("⚙️ TECNOLOGÍAS");
        GridPane techGrid = new GridPane();
        techGrid.setHgap(20);
        techGrid.setVgap(10);
        techGrid.setPadding(new Insets(5));

        techDefLabel = new Label("0");
        techDefLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold;");
        techAtkLabel = new Label("0");
        techAtkLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold;");

        Label defIcon = new Label("🛡️");
        defIcon.setStyle("-fx-font-size: 20px;");
        Label atkIcon = new Label("⚔️");
        atkIcon.setStyle("-fx-font-size: 20px;");

        techGrid.add(defIcon, 0, 0);
        techGrid.add(new Label("Defensa:"), 1, 0);
        techGrid.add(techDefLabel, 2, 0);
        techGrid.add(atkIcon, 0, 1);
        techGrid.add(new Label("Ataque:"), 1, 1);
        techGrid.add(techAtkLabel, 2, 1);
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

        Label farmIcon = new Label("🏚️");
        farmIcon.setStyle("-fx-font-size: 18px;");
        Label carpIcon = new Label("🪚");
        carpIcon.setStyle("-fx-font-size: 18px;");
        Label smithIcon = new Label("⚒️");
        smithIcon.setStyle("-fx-font-size: 18px;");
        Label magicIcon = new Label("🔮");
        magicIcon.setStyle("-fx-font-size: 18px;");
        Label churchIcon = new Label("⛪");
        churchIcon.setStyle("-fx-font-size: 18px;");

        buildGrid.add(farmIcon, 0, 0);
        buildGrid.add(new Label("Granjas:"), 1, 0);
        buildGrid.add(farmLabel, 2, 0);
        buildGrid.add(carpIcon, 0, 1);
        buildGrid.add(new Label("Carpinterías:"), 1, 1);
        buildGrid.add(carpentryLabel, 2, 1);
        buildGrid.add(smithIcon, 0, 2);
        buildGrid.add(new Label("Herrerías:"), 1, 2);
        buildGrid.add(smithyLabel, 2, 2);
        buildGrid.add(magicIcon, 0, 3);
        buildGrid.add(new Label("Torres Mágicas:"), 1, 3);
        buildGrid.add(magicTowerLabel, 2, 3);
        buildGrid.add(churchIcon, 0, 4);
        buildGrid.add(new Label("Iglesias:"), 1, 4);
        buildGrid.add(churchLabel, 2, 4);

        buildCard.getChildren().add(buildGrid);
        leftColumn.getChildren().addAll(techCard, buildCard);

        // Columna derecha: Ejército
        VBox rightColumn = new VBox(15);
        rightColumn.setAlignment(Pos.TOP_CENTER);
        rightColumn.setPrefWidth(450);
        rightColumn.setMaxWidth(500);

        VBox armyCard = createInfoCard("⚔️ EJÉRCITO");
        GridPane armyGrid = new GridPane();
        armyGrid.setHgap(20);
        armyGrid.setVgap(6);
        armyGrid.setPadding(new Insets(5));

        for (int i = 0; i < 9; i++) {
            Label iconLabel = new Label(unitIcons[i]);
            iconLabel.setStyle("-fx-font-size: 18px;");
            Label nameLabel = new Label(unitNames[i]);
            nameLabel.setStyle("-fx-text-fill: #b2bec3; -fx-font-size: 15px;");
            unitCountLabels[i] = new Label("0");
            unitCountLabels[i].setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold;");

            armyGrid.add(iconLabel, 0, i);
            armyGrid.add(nameLabel, 1, i);
            armyGrid.add(unitCountLabels[i], 2, i);
        }

        armyCard.getChildren().add(armyGrid);
        rightColumn.getChildren().add(armyCard);

        columnsContainer.getChildren().addAll(leftColumn, rightColumn);

        ScrollPane scrollPane = new ScrollPane(columnsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setPrefHeight(600);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        panel.getChildren().addAll(resourcesBar, scrollPane);
        return panel;
    }

    private HBox createResourceBar() {
        return ResourceBar.create(civilization, foodValueLabel, woodValueLabel, ironValueLabel, manaValueLabel);
    }

    private VBox createInfoCard(String title) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #1c2833; -fx-border-color: #4ea5d9; -fx-border-radius: 12; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 8, 0, 0, 4);");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-text-fill: #ecf0f1; -fx-font-size: 18px; -fx-font-weight: bold; -fx-padding: 0 0 8 0; -fx-border-color: #4ea5d9; -fx-border-width: 0 0 1 0;");

        card.getChildren().add(titleLabel);
        return card;
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