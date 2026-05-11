const oracledb = require('oracledb');
oracledb.outFormat = oracledb.OUT_FORMAT_ARRAY;

const dbConfig = {
    user: "civilizations",
    password: "tu_password",
    connectString: "localhost:1521/XEPDB1"
};

async function getConnection() {
    return await oracledb.getConnection(dbConfig);
}

module.exports = { getConnection };