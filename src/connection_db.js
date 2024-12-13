const mysql = require('mysql2/promise');

const pool = mysql.createPool({
    host: '34.34.217.192',
    user: 'root',
    password: 'capstone124',
    database: 'capstone_db',
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0
});

module.exports = pool;
