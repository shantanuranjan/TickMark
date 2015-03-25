<?php

define('DB_USER', 'root'); // db user
define('DB_PASSWORD', ''); // db password (mention your db password here)
define('DB_DATABASE', 'tickmark'); // database name
define('DB_HOST', 'localhost'); // db server


$data = file_get_contents("php://input");
if(empty($data))
{
	echo "EMPTY";
}
else
{
	$json = json_decode($data, TRUE);
}

if (isset($json['sid']) && isset($json['aid'])) 
{
    $sid= $json['sid'];
	$aid= $json['aid'];
   	   
	
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
			
$querynew = "INSERT INTO `student_attendance_details` (`aid`, `sid`) VALUES ('$sid', '$aid')";
$resultnew  = mysqli_query($dblink, $querynew);

if($resultnew){

$course1["success"] = 1;
echo json_encode($course1);
}
}
else
{
    
    $course1["success"] = 0;
    $course1["error"] = "Required field(s) is missing";
 
    
    echo json_encode($course1);
}
}
?>