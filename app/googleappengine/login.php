<?php

define('DB_USER', 'root'); // db user
define('DB_PASSWORD', ''); // db password (mention your db password here)
define('DB_DATABASE', 'tickmark'); // database name
define('DB_HOST', 'localhost'); // db server


$course1["course"] = array();
$course1["courseid"] = array();
$course1["profid"] = array();
$course1["profname"] = array();
$data = file_get_contents("php://input");
if(empty($data))
{
	echo "EMPTY";
}
else
{
	$json = json_decode($data, TRUE);
}

if (isset($json['emailname']) && isset($json['passwordname'])) 
{
    $emid= $json['emailname'];
    $pswd= $json['passwordname'];
	   
	
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

$query= "SELECT * FROM user_registration where emailid = '{$emid}' AND password ='{$pswd}'";
$result = mysqli_query($dblink, $query);
$query1="select course_name, course_id from professor_course_details";
$result1 = $dblink->query($query1);
if($result)
{
if((mysqli_num_rows($result)) > 0)
{
$row = $result->fetch_assoc();
$uid = $row["user_id"];
$course1["uid"] = $uid;


if($result1) 
{
while ($row1 = $result1->fetch_assoc()) {
       		
		$coursename = $row1["course_name"];
		$courseid = $row1["course_id"];
		
	
	array_push($course1["course"],$coursename);
	array_push($course1["courseid"],$courseid);
$query2= "SELECT `firstname`, `user_id` FROM `user_registration` WHERE `user_id` in (SELECT `prof_id` FROM `professor_course_details` WHERE `course_name`='$coursename')";
$result2 = $dblink->query($query2);
if($result2) {
	while ($row2 = $result2->fetch_assoc()) {
       		
		$pname = $row2["firstname"];
		$pid = $row2["user_id"];
		
	
	array_push($course1["profname"],$pname);
	array_push($course1["profid"],$pid);
    }
}
}
$course1["success"] = 1;
echo json_encode($course1);
}
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