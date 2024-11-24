var express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');

var app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cors());

app.get('/', function (req, res) {
    res.send('Hello World!');
});

app.listen(4000, function () {
    console.log('Example app listening on port 4000!');
});