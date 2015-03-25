<?php
 


$response = array();


$data = file_get_contents("php://input");
if(empty($data))
{
    echo "EMPTY";

}
else
{
    $json = json_decode($data, true);
}

if (isset($json['cid']) && isset($json['course_name']) && isset($json['pid']) && isset($json['ctime']) ) 
{

    $professor_id=$json['pid'];
    $course=$json['course_name'];
    $cid=$json['cid'];
    $ctime=$json['ctime'];
    

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


$query="insert into professor_course_details (prof_id, course_name, course_id, course_time, generated_code) values
( '{$professor_id}', '{$course}', '{$cid}', '{$ctime}', NULL)";
$result = $dblink->query($query);
   
if ($result) {
        $response["success"] = 1;
        $response["message"] = "course successfully created.";
    echo json_encode($response);
            } 
     
} 
else {
        $response["success"] = 0;
        $response["message"] = "Missing";
        echo json_encode($response);
}
?>