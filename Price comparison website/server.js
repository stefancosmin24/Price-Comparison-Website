//Import the express and url modules
const express = require('express');
const url = require("url");


//Status codes defined in external file
require('./http_status.js');
const db = require('./database.js');

//The express module is a function. When it is executed it returns an app object
const app = express();

//Serve up everything in public
app.use(express.static('public'));


//GET request for all products
//Should handle errors properly!
app.get('/comparisons', async (request, response) => {
    //Get number of items and offset from URL
    let [numItems, offset] = getNumItemsOffset(request.url);

    //Get the total number of products for pagination
    let prodCount = await db.getProductCount();

    //Get the products
    let products = await db.getProducts(numItems, offset);

    //Combine into a single object and send back to client
    let returnObj = {
        count: prodCount,
        data: products
    }
    response.json(returnObj);
});


//GET search request
app.get('/search/*', (request, response) =>{
    //Get number of items and offset from URL
    let [numItems, offset] = getNumItemsOffset(request.url);

    //Get the end of the path
    const pathEnd = getPathEnd(request.url);
    console.log("SEARCH FOR: " + pathEnd);

    //Run search and then send back results
    db.getSearch(pathEnd, numItems, offset).then( searchResults => {
        response.json(searchResults)
    }).catch(error => console.log(error.message));
    
});


//GET request for product by ID
//If the last part of the path is a valid product id, return data about that product
app.get('/comparisons/*', (request, response) =>{
    //Get the last part of the path
    const pathEnd = getPathEnd(request.url);

    //Check that it is a valid ID
    let regEx = new RegExp('^[0-9]+$');//RegEx returns true if string is all digits.
    if(regEx.test(pathEnd)){
        //Run search and then send back results
        db.getProduct(pathEnd).then( product => {
            response.json(product)
        }).catch(error => console.log(error.message));;
    }
    else{
        response.status(HTTP_STATUS.NOT_FOUND);
        response.send("{error: 'Invalid product ID', url: " + request.url + "}");
    }
});

//Start the app listening on port 8080
app.listen(8080);
console.log("Server listening on port 8080");


//Returns number of items and offset
function getNumItemsOffset(urlStr){
    //Parse the URL
    let urlObj = url.parse(urlStr, true);

    //Extract object containing queries from URL object.
    let queries = urlObj.query;

    //Get the pagination properties if they have been set. Will be  undefined if not set.
    let numItems = queries['num_items'];
    let offset = queries['offset'];

    //Return numItems and offset
    return [numItems, offset];
}


//Returns the last part of the path
function getPathEnd(urlStr){
    //Parse the URL
    let urlObj = url.parse(urlStr, true);

    //Split the path of the request into its components
    let pathArray = urlObj.pathname.split("/");

    //Return the last part of the path
    return pathArray[pathArray.length - 1];
}

//Export server for testing
module.exports = app;
