const mysql = require('mysql2/promise');

// Buat pool koneksi ke database
const pool = mysql.createPool({
    host: 'localhost',         // Ganti sesuai host database Anda
    user: 'root',              // Username database Anda
    password: '',              // Password database Anda
    database: 'capstone_db',    // Nama database yang digunakan
    waitForConnections: true,
    connectionLimit: 10,
    queueLimit: 0
});

module.exports = pool;
