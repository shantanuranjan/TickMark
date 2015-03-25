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

if (isset($json['courseid']) && isset($json['profid'])) 
{
    $cid= $json['courseid'];
	$pid= $json['profid'];
   	   
	
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
			
$querynew = "SELECT `generated_code` FROM `professor_course_details` WHERE `prof_id` = '$pid' and `course_id` = '$cid'";
$resultnew  = mysqli_query($dblink, $querynew);

if($resultnew){
$rownew = $resultnew->fetch_assoc();
$code = $rownew["generated_code"];
$course1["code"] = $code;
}

$query= "SELECT `aid` FROM `attendance` WHERE `course_id`= '$cid'";
$result = mysqli_query($dblink, $query);
if($result)
{
if((mysqli_num_rows($result)) > 0)
{
$row = $result->fetch_assoc();
$aid = $row["aid"];

$course1["aid"] = $aid ;

$query2="SELECT `prof_latitude`, `prof_longitude` FROM `location` WHERE `aid` = '$aid' and `prof_id` ='$pid'";
$result1 = $dblink->query($query2);
if($result1)
{
if((mysqli_num_rows($result1)) > 0)
{
$row1 = $result1->fetch_assoc();
$lat = $row1["prof_latitude"];
$lon = $row1["prof_longitude"];

	$course1["lat"] = $lat;
	$course1["lon"] = $lon;
    }
}
}
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
?>