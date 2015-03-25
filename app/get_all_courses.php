<?php

define('DB_USER', 'root'); // db user
define('DB_PORT', '3306');
define('DB_PASSWORD', 'root1234'); // db password (mention your db password here)
define('DB_NAME', 'Tickmark'); // database name
define('DB_HOST', 'mydbinstance.cjjhcrmxrcwx.us-west-2.rds.amazonaws.com'); // db server
/*
 * Following code will list all the products
 */

// array for JSON response
$response = array();

 // Connecting to mysql database
        $con = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME, DB_PORT) or die(mysql_error());

// get all products from products table
$result = mysql_query("SELECT COURSE_NAME FROM PROFESSOR_COURSE_DETAILS") or die(mysql_error());

// check for empty result
if (mysql_num_rows($result) > 0) {
	// looping through all results
	// products node
	$course["course"] = array();
	
	while ($row = mysql_fetch_array($result)) {
		$course=array();
		$course[] = $row["COURSE_NAME"];
	}
	// success
	$course["success"] = 1;
	
	// echoing JSON response
	echo json_encode($course);
	} else {
		// no products found
		$course["success"] = 0;
		$course["message"] = "No course found";
	
		// echo no users JSON
		echo json_encode($course);
	} 
	?>
}