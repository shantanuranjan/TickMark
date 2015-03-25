<?php


define('DB_USER', 'root'); // db user
define('DB_PASSWORD', 'root'); // db password (mention your db password here)
define('DB_DATABASE', 'tickmark'); // database name
define('DB_HOST', 'localhost'); // db server
$response = array();

$data = file_get_contents("php://input");
if(empty($data))
{
	echo "EMPTY";
}
else
{
	$json = json_decode($data, TRUE);
}


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

if (isset($json['course_name']) && isset($json['pid']))
{
   $course=$json['course_name'];
   $professor_id=$json['pid'];


$query1="select course_id from professor_course_details where course_name='{$course}' and prof_id='{$professor_id}'";
$result1 = $dblink->query($query1);
$row1 = $result1->fetch_assoc();
$courseid = $row1["course_id"];

$query2="select aid from attendance where course_id='{$courseid}'";
$result2 = $dblink->query($query2);
$row2 = $result2->fetch_assoc();
$aid = $row2["aid"];


$query3="select sid from student where aid='{$aid}'";
$result3 = $dblink->query($query3);


$student["present_name"]=array();

if($result1 && $result2 && $result3)
{
	
	while ($row3 = $result3->fetch_assoc()) 
	{
		$sid=$row3["sid"];
		$query4="select firstname,lastname from user_registration where user_id='{$sid}' ";	
		$result4 = $dblink->query($query4);
			while ($row4 = $result4->fetch_assoc())
			{
       				$fname="";
				$lname="";
				$fname = $row4["firstname"];
				$lname=$row4["lastname"];
				$name=$fname." ".$lname;
				array_push($student["present_name"],$name);
			}
    	}


}

$student["success"] = 1;
echo json_encode($student);
}

else {
    
    $student["success"] = 0;
    $student["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($student);
}	
?>
