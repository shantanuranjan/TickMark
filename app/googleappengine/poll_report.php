<?php
define('DB_USER', 'root'); // db user
define('DB_PASSWORD', 'root'); // db password (mention your db password here)
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

if (isset($json['course_id']) && isset($json['prof_id']) && isset($json['poll_id'])) 
{
 
$course_id=$json['course_id'];
$professor_id=$json['prof_id'];
$poll_id=$json['poll_id'];
$query="select course_name from professor_course_details where prof_id='$professor_id'
 and course_id='$course_id'";

$query1="select count_a,count_b,count_c,count_d from professor_poll_details where prof_id='$professor_id'
 and course_id='$course_id' and poll_id='{$poll_id}' ";

$query2="select poll_question,poll_optionA,poll_optionB,poll_optionC,poll_optionD 
from poll where prof_id='$professor_id'
 and course_id='$course_id' and poll_id='$poll_id' ";

$result = $dblink->query($query);
$result1=$dblink->query($query1);
$result2=$dblink->query($query2);


if($result && $result1 && $result2)
{
	$poll["course"] = array();
while ($row = $result->fetch_assoc()) {
       
		$coursename = $row["course_name"];
		array_push($poll["course"],$coursename);
    }
$poll["poll_count"] = array();
while ($row = $result1->fetch_assoc()) {
       		$poll_count=array();
		$poll_count[] = $row["count_a"];
		$poll_count[] = $row["count_b"];
		$poll_count[] = $row["count_c"];
		$poll_count[] = $row["count_d"];
		array_push($poll["poll_count"],$poll_count);
    }
$poll["poll_description"] = array();
while ($row = $result2->fetch_assoc()) {
       		$poll_question=array();
		$poll_question[] = $row["poll_question"];
		$poll_question[] = $row["poll_optionA"];
		$poll_question[] = $row["poll_optionB"];
		$poll_question[] = $row["poll_optionC"];
		$poll_question[] = $row["poll_optionD"];
		array_push($poll["poll_description"],$poll_question);
    }
$poll["success"] = 1;
echo json_encode($poll);
}
}
else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    
    echo json_encode($response);
}	
?>
