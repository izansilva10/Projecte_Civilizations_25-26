#!/bin/bash

# 1. Define las rutas exactas desde la raíz del proyecto
SOURCE_DIR="Civilizations/M03_Programacion"
LIB_DIR="javafx/lib"
MODULE_PATH="/usr/share/openjfx/lib"

echo "Compilando..."
# 2. Compila todo (incluyendo los .java dentro de ui/)
javac -cp "$SOURCE_DIR:$LIB_DIR/*" \
      --module-path "$MODULE_PATH" \
      --add-modules javafx.controls,javafx.fxml \
      -d "$SOURCE_DIR" \
      "$SOURCE_DIR"/civilizations/*.java "$SOURCE_DIR"/civilizations/ui/*.java

# 3. Si la compilación funciona, ejecuta la interfaz gráfica
if [ $? -eq 0 ]; then
    echo "Ejecutando..."
    # 4. El Classpath (-cp) es la clave: incluye la carpeta de código y TODAS las librerías de javafx/lib
    java -cp "$SOURCE_DIR:$LIB_DIR/*" \
         --module-path "$MODULE_PATH" \
         --add-modules javafx.controls,javafx.fxml \
         civilizations.ui.MainWindow
else
    echo "Error de compilación."
fi