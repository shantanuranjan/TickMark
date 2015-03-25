<?php

define('DB_USER', 'root'); // db user
define('DB_PASSWORD', ''); // db password (mention your db password here)
define('DB_DATABASE', 'tickmark'); // database name
define('DB_HOST', 'localhost'); // db server

$message = array();
$data = file_get_contents("php://input");
if(empty($data))
{
	echo "EMPTY";
}
else
{
	$json = json_decode($data, TRUE);
}

if (isset($json['firstname']) && isset($json['lastname']) && isset($json['emailname']) && isset($json['passwordname']) && isset($json['typeUser']) ) 
{
 
    $fname = $json['firstname'];
    $lname= $json['lastname'];
    $emid= $json['emailname'];
    $pswd= $json['passwordname'];
    $utyp= $json['typeUser'];

	
	
$dblink = new mysqli(null,
  'root', // username
  '',     // password
  tickmark,
  null,
  '/cloudsql/sunsimengkira:sunsimeng'
  );
			if(mysqli_connect_errno())
			{
				$message="MySQL connection failed: ". mysqli_connect_error();
			}

$query="INSERT INTO `user_registration` (`firstname`, `lastname`, `emailid`, `password`, `user_type`) VALUES ('$fname', '$lname', '$emid', '$pswd', '$utyp')";
$result = $dblink->query($query);
if($result)
{
$message["status"] = 1;
$message["error"] = "No Error";
$message["type"]=$utyp;
}
else
{
$message["status"] = 0;
$message["error"] = "Did not update!";
}
echo json_encode($message);
}
else
{
    
    $message["status"] = 0;
    $message["error"] = "Required field(s) is missing";
 
    
    echo json_encode($message);
}
?>