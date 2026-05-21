const express = require('express');
const app = express();
const path = require('path');
const hbs = require('hbs');
const db = require('./db'); // Importa la conexión a MySQL (archivo db.js)

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

// Helper para comparar dos valores (usado en las vistas para verificar resultado)
hbs.registerHelper('eq', function (a, b) {
    return a === b;
});

// ==========================================
// 3. ARCHIVOS ESTÁTICOS
// ==========================================
// Servir la carpeta 'public' como raíz para CSS, imágenes y favicons
app.use(express.static(path.join(__dirname, 'public')));

// ==========================================
// 4. RUTAS DE LA APLICACIÓN
// ==========================================

// Ruta principal (Home / Portada) con las últimas batallas
app.get('/', async (req, res) => {
    try {
        // Obtener las 2 últimas batallas
        const [batallas] = await db.query(
            `SELECT num_battle AS id, 
                    'Civilización' AS atacante,
                    'Enemigo' AS defensor,
                    CASE WHEN civilizationWon = 1 THEN 'victoria' ELSE 'derrota' END AS resultado
             FROM battle_stats 
             WHERE civilization_id = 1 
             ORDER BY num_battle DESC LIMIT 2`
        );
        res.render('index', { 
            currentRoute: '/',
            batallas: batallas
        });
    } catch (error) {
        console.error('Error cargando home:', error);
        res.render('index', { currentRoute: '/', batallas: [] });
    }
});

// Ruta de Civilització (recursos, edificios, tecnologías)
app.get('/civilitzacio', async (req, res) => {
    try {
        const [civ] = await db.query(
            'SELECT wood_amount, iron_amount, food_amount, mana_amount, farm_counter, carpentry_counter, smithy_counter, magicTower_counter, church_counter, technology_defense_level, technology_attack_level FROM civilization_stats WHERE civilization_id = 1'
        );

        if (civ.length === 0) {
            return res.render('civilitzacio', { currentRoute: '/civilitzacio', civ: null });
        }

        const datos = civ[0];
        res.render('civilitzacio', { 
            currentRoute: '/civilitzacio',
            civ: datos
        });
    } catch (error) {
        console.error('Error cargando civilización:', error);
        res.render('civilitzacio', { currentRoute: '/civilitzacio', civ: null });
    }
});

// Ruta de Batalles (historial completo)
app.get('/batalles', async (req, res) => {
    try {
        const [batallas] = await db.query(
            `SELECT num_battle AS id, 
                    'Civilización' AS atacante,
                    'Enemigo' AS defensor,
                    CASE WHEN civilizationWon = 1 THEN 'victoria' ELSE 'derrota' END AS resultado
             FROM battle_stats 
             WHERE civilization_id = 1 
             ORDER BY num_battle DESC`
        );
        res.render('batalles', { 
            currentRoute: '/batalles',
            batallas: batallas
        });
    } catch (error) {
        console.error('Error cargando batallas:', error);
        res.render('batalles', { currentRoute: '/batalles', batallas: [] });
    }
});

// Ruta de Informes de batalla (detalle de una batalla)
app.get('/informes', async (req, res) => {
    const idBatalla = parseInt(req.query.informe) || 0;
    if (!idBatalla) {
        return res.redirect('/batalles');
    }

    try {
        // Obtener datos básicos de la batalla
        const [battle] = await db.query(
            'SELECT * FROM battle_stats WHERE civilization_id = 1 AND num_battle = ?',
            [idBatalla]
        );
        if (battle.length === 0) {
            return res.redirect('/batalles');
        }

        // Obtener el log de la batalla
        const [logLines] = await db.query(
            'SELECT log_entry FROM battle_log WHERE civilization_id = 1 AND num_battle = ? ORDER BY num_line ASC',
            [idBatalla]
        );
        const desarrollo = logLines.map(row => row.log_entry).join('<br>');

        // Obtener estadísticas de unidades (civilización)
        const [attackStats] = await db.query(
            'SELECT type, initial, drops FROM civilization_attack_stats WHERE civilization_id = 1 AND num_battle = ?',
            [idBatalla]
        );
        const [defenseStats] = await db.query(
            'SELECT type, initial, drops FROM civilization_defense_stats WHERE civilization_id = 1 AND num_battle = ?',
            [idBatalla]
        );
        const [specialStats] = await db.query(
            'SELECT type, initial, drops FROM civilization_special_stats WHERE civilization_id = 1 AND num_battle = ?',
            [idBatalla]
        );
        const [enemyStats] = await db.query(
            'SELECT type, initial, drops FROM enemy_attack_stats WHERE civilization_id = 1 AND num_battle = ?',
            [idBatalla]
        );

        // Determinar si fue victoria o derrota
        const [winners] = await db.query(
            'SELECT civilizationWon FROM battle_stats WHERE civilization_id = 1 AND num_battle = ?',
            [idBatalla]
        );
        const resultado = winners.length > 0 && winners[0].civilizationWon === 1 ? 'Victoria' : 'Derrota';

        res.render('informes', {
            currentRoute: '/informes',
            batalla: battle[0],
            desarrollo: desarrollo,
            ataque: attackStats,
            defensa: defenseStats,
            especiales: specialStats,
            enemigo: enemyStats,
            resultado: resultado
        });
    } catch (error) {
        console.error('Error cargando informe:', error);
        res.redirect('/batalles');
    }
});

// Ruta de Programadors (Equipo de desarrollo)
app.get('/programadors', (req, res) => {
    res.render('programadors', { currentRoute: '/programadors' });
});

// ==========================================
// 5. ARRANQUE DEL SERVIDOR
// ==========================================
const PORT = process.env.PORT || 3001;
app.listen(PORT, () => {
    console.log(`Servidor de Civilizations corriendo en http://localhost:${PORT}`);
});