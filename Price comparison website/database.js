//Import the mysql module
const mysql = require('mysql');

//Create a connection object with the user details
const connectionPool = mysql.createPool({
    connectionLimit: 1,
    host: "localhost",
    user: "root",
    password: "",
    database: "price_comparison",
    debug: false
});


/** Returns a promise to get the total number of products */
module.exports.getProductCount = async ()=> {
    //Build SQL query
    const sql = "SELECT COUNT(*) FROM comparison";

    //Execute promise to run query
    let result = await executeQuery(sql);

    //Extract the data we need from the result
    return result[0]["COUNT(*)"];
}


/** Searches for products */
module.exports.getSearch = async (searchTerm, numItems, offset) => {
    let sql = "SELECT * FROM comparison WHERE name LIKE '%" + searchTerm + "%' ";

    //Limit the number of results returned, if this has been specified in the query string
    if(numItems !== undefined && offset !== undefined ){
        sql += "ORDER BY comparison.id LIMIT " + numItems + " OFFSET " + offset;
    }

    //Return promise to run query
    return executeQuery(sql);
}


/** Returns all the products with optional pagination */
module.exports.getProducts = (numItems, offset) => {
    let sql = "SELECT * FROM comparison ";

    //Limit the number of results returned, if this has been specified in the query string
    if(numItems !== undefined && offset !== undefined ){
        sql += "ORDER BY comparison.id LIMIT " + numItems + " OFFSET " + offset;
    }

    //Return promise to run query
    return executeQuery(sql);
}


/** Returns a promise to get details about a single product */
module.exports.getProduct = (prodId) => {
    const sql = "SELECT * FROM comparison WHERE id=" + prodId;
    return executeQuery(sql);
}


/** Wraps connection pool query in a promise and returns the promise */
async function executeQuery(sql){
    //Wrap query around promise
    let queryPromise = new Promise( (resolve, reject)=> {
        //Execute the query
        connectionPool.query(sql, function (err, result) {
            //Check for errors
            if (err) {
                //Reject promise if there are errors
                reject(err);
            }

            //Resole promise with data from database.
            resolve(result);
        });
    });

    //Return promise
    return queryPromise;
}

