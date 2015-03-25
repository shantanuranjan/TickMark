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

if (isset($json['uid'])) 
{
    $uid= $json['uid'];
      
	
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

$query= "SELECT `user_type` FROM `user_registration` WHERE `user_id`='$uid'";
$result = mysqli_query($dblink, $query);
if($result)
{
if((mysqli_num_rows($result)) > 0)
{
$row = $result->fetch_assoc();
$utype = $row["user_type"];
$message["utype"] = $utype;
$message["status"] = 1;
$message["error"] = "No Error";
}
else
{
$message["status"] = 0;
$message["error"] = "You have provided incorrect information";
}
echo json_encode($message);
}
else
{
    
    $message["status"] = 0;
    $message["error"] = "Required field(s) is missing";
 
    
    echo json_encode($message);
}
}
?>