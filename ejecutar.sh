#!/bin/bash

# Ir a la raíz del proyecto
cd "$(dirname "$0")"

echo "Compilando..."
# Encuentra todos los archivos .java dentro de M03_Programacion
SOURCE_FILES=$(find Civilizations/M03_Programacion -name "*.java")

# Compila todos los archivos .java
javac -cp "javafx/lib/*" --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -d Civilizations/M03_Programacion $SOURCE_FILES

if [ $? -eq 0 ]; then
    echo "Ejecutando..."
    # Ejecuta el juego
    java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -cp "Civilizations/M03_Programacion:javafx/lib/*" civilizations.InterfazCivilization
else
    echo "Error de compilación."
fi