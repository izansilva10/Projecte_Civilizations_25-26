package civilizations;

import civilizations.ui.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class InterfazCivilization extends Application {
    private MainWindow mainWindow;

    @Override
    public void start(Stage stage) {
        mainWindow = new MainWindow();
        mainWindow.start(stage);
    }

    @Override
    public void stop() throws Exception {
        if (mainWindow != null) mainWindow.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
