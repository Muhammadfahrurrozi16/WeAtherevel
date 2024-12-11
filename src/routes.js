const express = require('express');
const { getPlaces, getPlaceById, getPlacesByCategory } = require('./handler');

const router = express.Router();

// Get all places
router.get('/', getPlaces);

// Get place by ID
router.get('/:id', getPlaceById);

// Get place by Category
router.get('/:city', getPlacesByCategory);

module.exports = router;
