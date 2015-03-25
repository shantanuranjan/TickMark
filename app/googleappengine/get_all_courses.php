<?php


$course1["course"] = array();

$data = file_get_contents("php://input");
if(empty($data))
{
	echo "EMPTY";
}
else
{
	$json = json_decode($data, TRUE);
}
$id=$json['profid'];
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

$query="select course_name from professor_course_details where prof_id='{$id}'";
$result = $dblink->query($query);

if($result)
{
	
while ($row = $result->fetch_assoc()) {
       		
		$coursename = $row["course_name"];
	
	array_push($course1["course"],$coursename);
    }


$course1["success"] = 1;
echo json_encode($course1);
}
	
?>
