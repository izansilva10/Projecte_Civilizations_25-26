#!/bin/bash

# Encuentra el directorio donde se encuentra este script
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$SCRIPT_DIR"

# Definir rutas absolutas desde la raíz
SOURCE_DIR="Civilizations/M03_Programacion"
LIB_DIR="javafx/lib"
MODULE_PATH="/usr/share/openjfx/lib"

echo "Compilando..."
# 2. Compilar todo
javac -cp "$SOURCE_DIR:$LIB_DIR/*" \
      --module-path "$MODULE_PATH" \
      --add-modules javafx.controls,javafx.fxml \
      -d "$SOURCE_DIR" \
      "$SOURCE_DIR"/civilizations/*.java "$SOURCE_DIR"/civilizations/ui/*.java

if [ $? -eq 0 ]; then
    echo "Ejecutando..."
    java -cp "$SOURCE_DIR:$LIB_DIR/*" \
         --module-path "$MODULE_PATH" \
         --add-modules javafx.controls,javafx.fxml \
         civilizations.ui.MainWindow
else
    echo "Error de compilación."
fi