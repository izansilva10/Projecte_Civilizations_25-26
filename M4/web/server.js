const express = require('express');
const app = express();
const path = require('path');
const hbs = require('hbs');

// ==========================================
// 1. CONFIGURACIÓN DEL MOTOR DE PLANTILLAS
// ==========================================
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'));

// Registrar la carpeta de partials (para el header y footer)
hbs.registerPartials(path.join(__dirname, 'views', 'partials'));

// ==========================================
// 2. HELPERS DE HANDLEBARS
// ==========================================
// Helper para comparar la ruta actual y aplicar la clase 'active' al menú
hbs.registerHelper('activeClass', (currentRoute, activeRoute) => {
    return currentRoute === activeRoute ? 'active' : '';
});

// ==========================================
// 3. ARCHIVOS ESTÁTICOS
// ==========================================
// Servir la carpeta 'public' como raíz para CSS, imágenes y favicons
app.use(express.static(path.join(__dirname, 'public')));

// ==========================================
// 4. RUTAS DE LA APLICACIÓN
// ==========================================

// Ruta principal (Home / Portada)
app.get('/', (req, res) => {
    res.render('index', { currentRoute: '/' });
});

// Ruta de Civilització
app.get('/civilitzacio', (req, res) => {
    res.render('civilitzacio', { currentRoute: '/civilitzacio' });
});

// Ruta de Batalles (Historial)
app.get('/batalles', (req, res) => {
    res.render('batalles', { currentRoute: '/batalles' });
});

// Ruta de Informes de batalla
app.get('/informes', (req, res) => {
    res.render('informes', { currentRoute: '/informes' });
});

// Ruta de Programadors (Equipo de desarrollo)
app.get('/programadors', (req, res) => {
    res.render('programadors', { currentRoute: '/programadors' });
});

// ==========================================
// 5. ARRANQUE DEL SERVIDOR
// ==========================================
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Servidor de Civilizations corriendo en http://localhost:${PORT}`);
});