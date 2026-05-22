package civilizations.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import civilizations.Civilization;

public class ResourceBar {
    private static final String RESOURCE_PATH = "/img/resources/";

    public static HBox create(Civilization civilization, Label foodLabel, Label woodLabel, Label ironLabel, Label manaLabel) {
        HBox bar = new HBox(25);
        bar.setAlignment(Pos.CENTER);
        bar.getStyleClass().add("resource-bar");
        bar.setMaxWidth(Double.MAX_VALUE);

        bar.getChildren().addAll(
            createResourceCard("Comida", "🍽️", foodLabel, RESOURCE_PATH + "food.png"),
            createResourceCard("Madera", "🪵", woodLabel, RESOURCE_PATH + "wood.png"),
            createResourceCard("Hierro", "⛏️", ironLabel, RESOURCE_PATH + "iron.png"),
            createResourceCard("Maná", "✦", manaLabel, RESOURCE_PATH + "mana.png")
        );
        return bar;
    }

    private static HBox createResourceCard(String name, String icon, Label valueLabel, String imagePath) {
        HBox card = new HBox(8);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("resource-card");

        // Seguridad extra: si el label es null, lo creamos aquí
        if (valueLabel == null) {
            valueLabel = new Label("0");
        }

        ImageView iconView = null;
        try {
            iconView = new ImageView(new Image(ResourceBar.class.getResourceAsStream(imagePath)));
            iconView.setFitWidth(28);
            iconView.setFitHeight(28);
        } catch (Exception e) {
            // Si la imagen no existe, usamos un label con el icono
            Label fallbackIcon = new Label(icon);
            fallbackIcon.setStyle("-fx-font-size: 24px;");
            card.getChildren().add(fallbackIcon);
        }

        VBox infoBox = new VBox(2);
        infoBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("resource-name");

        valueLabel.getStyleClass().add("resource-value");

        infoBox.getChildren().addAll(nameLabel, valueLabel);

        if (iconView != null) {
            card.getChildren().add(iconView);
        }
        card.getChildren().add(infoBox);
        return card;
    }
}