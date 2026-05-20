#!/bin/bash
echo "Compilando..."
javac -cp . --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -d . civilizations/*.java civilizations/ui/*.java
if [ $? -eq 0 ]; then
    echo "Ejecutando..."
    java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -cp . civilizations.ui.MainWindow
else
    echo "Error de compilación."
fi