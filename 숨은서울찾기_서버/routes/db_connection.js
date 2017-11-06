//connecting DB

var mysql = require('mysql');
var express = require('express');
var router = express.Router();

global.connection = mysql.createConnection({

    host: '***',
    user: '****',
    port: 0000,
    password: '***',
    database: '***'

});

//DB error handle
connection.connect(function (err) {

    if (err) {
        console.error('MYSQL Connection Error');
        console.error(err);
        throw err;
    }
    console.log('it works!');

});

module.exports = router;