const express = require('express');
const path = require('path');
const app = express();
const db = require('./db');

// Configuración de vistas
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));

// Archivos estáticos (CSS, imágenes, JS)
app.use(express.static(path.join(__dirname, 'public')));

// Ruta: Portada principal
app.get('/', async (req, res) => {
    try {
        const connection = await db.getConnection();
        // Últimas 2 batallas
        const result = await connection.execute(
            `SELECT num_battle, wood_acquired, iron_acquired
               FROM battle_stats
              WHERE civilization_id = 1
              ORDER BY num_battle DESC
              FETCH FIRST 2 ROWS ONLY`
        );
        const ultimasBatallas = result.rows.map(row => ({
            num: row[0],
            wood: row[1],
            iron: row[2]
        }));
        await connection.close();
        res.render('index', { ultimasBatallas });
    } catch (err) {
        console.error(err);
        res.render('index', { ultimasBatallas: [] });
    }
});

// Ruta: Todas las batallas
app.get('/batallas', async (req, res) => {
    try {
        const connection = await db.getConnection();
        const result = await connection.execute(
            `SELECT num_battle, wood_acquired, iron_acquired
               FROM battle_stats
              WHERE civilization_id = 1
              ORDER BY num_battle DESC`
        );
        const batallas = result.rows.map(row => ({
            num: row[0],
            wood: row[1],
            iron: row[2]
        }));
        const total = batallas.length;
        await connection.close();
        res.render('battles', { batallas, total });
    } catch (err) {
        console.error(err);
        res.render('battles', { batallas: [], total: 0 });
    }
});

// Ruta: Informe de batalla específica
app.get('/informe', async (req, res) => {
    const numBattle = parseInt(req.query.informe) || 0;
    if (!numBattle) return res.redirect('/batallas');

    try {
        const connection = await db.getConnection();
        // Datos generales + desarrollo
        const battleResult = await connection.execute(
            `SELECT num_battle, wood_acquired, iron_acquired
               FROM battle_stats
              WHERE civilization_id = 1 AND num_battle = :num`,
            [numBattle]
        );
        const battle = battleResult.rows[0] ? {
            num: battleResult.rows[0][0],
            wood: battleResult.rows[0][1],
            iron: battleResult.rows[0][2]
        } : null;

        if (!battle) {
            await connection.close();
            return res.redirect('/batallas');
        }

        // Ataque civilización
        const civAttackResult = await connection.execute(
            `SELECT type, initial, drops
               FROM civilization_attack_stats
              WHERE civilization_id = 1 AND num_battle = :num`,
            [numBattle]
        );
        // Defensa civilización
        const civDefenseResult = await connection.execute(
            `SELECT type, initial, drops
               FROM civilization_defense_stats
              WHERE civilization_id = 1 AND num_battle = :num`,
            [numBattle]
        );
        // Especial civilización
        const civSpecialResult = await connection.execute(
            `SELECT type, initial, drops
               FROM civilization_special_stats
              WHERE civilization_id = 1 AND num_battle = :num`,
            [numBattle]
        );
        // Ataque enemigo
        const enemyResult = await connection.execute(
            `SELECT type, initial, drops
               FROM enemy_attack_stats
              WHERE civilization_id = 1 AND num_battle = :num`,
            [numBattle]
        );
        // Log de la batalla
        const logResult = await connection.execute(
            `SELECT num_line, log_entry
               FROM battle_log
              WHERE civilization_id = 1 AND num_battle = :num
              ORDER BY num_line`,
            [numBattle]
        );

        await connection.close();

        res.render('battle_report', {
            battle,
            civAttack: civAttackResult.rows.map(r => ({ type: r[0], initial: r[1], drops: r[2] })),
            civDefense: civDefenseResult.rows.map(r => ({ type: r[0], initial: r[1], drops: r[2] })),
            civSpecial: civSpecialResult.rows.map(r => ({ type: r[0], initial: r[1], drops: r[2] })),
            enemy: enemyResult.rows.map(r => ({ type: r[0], initial: r[1], drops: r[2] })),
            log: logResult.rows.map(r => ({ line: r[0], entry: r[1] }))
        });
    } catch (err) {
        console.error(err);
        res.redirect('/batallas');
    }
});

// Ruta: Civilización (recursos actuales)
app.get('/civilizacion', async (req, res) => {
    try {
        const connection = await db.getConnection();
        const result = await connection.execute(
            `SELECT wood_amount, iron_amount, food_amount, mana_amount,
                    magicTower_counter, church_counter, farm_counter,
                    smithy_counter, carpentry_counter,
                    technology_defense_level, technology_attack_level,
                    battles_counter
               FROM civilization_stats
              WHERE civilization_id = 1`
        );
        const civ = result.rows[0] ? {
            wood: result.rows[0][0],
            iron: result.rows[0][1],
            food: result.rows[0][2],
            mana: result.rows[0][3],
            magicTower: result.rows[0][4],
            church: result.rows[0][5],
            farm: result.rows[0][6],
            smithy: result.rows[0][7],
            carpentry: result.rows[0][8],
            defenseLevel: result.rows[0][9],
            attackLevel: result.rows[0][10],
            battles: result.rows[0][11]
        } : null;
        await connection.close();
        res.render('civilization', { civ });
    } catch (err) {
        console.error(err);
        res.render('civilization', { civ: null });
    }
});

// Ruta: Programadores
app.get('/programadores', (req, res) => {
    const programadores = [
        { nombre: 'Juan Pérez',     tarea: 'Base de datos Oracle' },
        { nombre: 'Ana García',     tarea: 'Clases Java del juego' },
        { nombre: 'Carlos López',   tarea: 'Lógica de batallas' },
        { nombre: 'María Rodríguez',tarea: 'Interfaz web y CSS' },
        { nombre: 'Pedro Sánchez',  tarea: 'TimerTasks y recursos' }
    ];
    res.render('programmers', { programadores });
});

// Iniciar servidor
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Servidor web en http://localhost:${PORT}`);
});