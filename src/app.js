const express = require('express');
const cors = require('cors');
const placesRoutes = require('./routes'); // Pastikan path sesuai

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors()); // Agar endpoint dapat diakses dari luar
app.use(express.json()); // Parsing JSON

// Routes
app.use('/api/places', placesRoutes);

// Handle 404 errors
app.use((req, res) => {
    res.status(404).json({ error: 'Route not found' });
});

// Start server
app.listen(PORT, () => {
    console.log(`Server is running on http://localhost:${PORT}`);
});
