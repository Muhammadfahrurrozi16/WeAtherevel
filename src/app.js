const express = require('express');
const cors = require('cors');
const placesRoutes = require('./routes'); // Pastikan path sesuai

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());
app.use(express.json());

app.use('/api/places', placesRoutes);

app.use((req, res) => {
    res.status(404).json({ error: 'Route not found' });
});

app.listen(PORT, () => {
    console.log(`Server is running on ${PORT}`);
});
