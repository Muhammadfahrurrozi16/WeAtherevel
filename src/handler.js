const db = require('./connection_db'); // Koneksi ke database

// Get all places
const getPlaces = async (req, res) => {
    try {
        const [results] = await db.execute('SELECT * FROM places');
        res.json(results);
    } catch (error) {
        res.status(500).json({ error: 'Failed to fetch places' });
    }
};

// Get place by ID
const getPlaceById = async (req, res) => {
    const { id } = req.params;
    try {
        const [results] = await db.execute('SELECT * FROM places WHERE Place_Id = ?', [id]);
        if (results.length === 0) {
            return res.status(404).json({ error: 'Place not found' });
        }
        res.json(results[0]);
    } catch (error) {
        res.status(500).json({ error: 'Failed to fetch place by ID' });
    }
};

// Get place by Category
const getPlacesByCategory = async (req, res) => {
    const { city } = req.params;
    try {
        const [results] = await db.execute('SELECT * FROM places WHERE City = ?', [city]);
        if (results.length === 0) {
            return res.status(404).json({ error: 'Place not found' });
        }
        res.json(results);
    } catch (error) {
        res.status(500).json({ error: 'Failed to fetch place by City' });
    }
};

module.exports = {
    getPlaces,
    getPlaceById,
    getPlacesByCategory
};
