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

if (isset($json['generated_code']) && isset($json['course_name']) && isset($json['pid']) && isset($json['latitude']) && isset($json['longitude']) ) 
{
 
    $code = $json['generated_code'];
    $course=$json['course_name'];
    $professor_id=$json['pid'];
    $lat=$json['latitude'];
    $lon=$json['longitude'];
	

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


$query1="update professor_course_details set generated_code='{$code}' where course_name='{$course}' and prof_id='{$professor_id}'";
$result1 = $dblink->query($query1);

$query2="select course_id from professor_course_details where course_name='{$course}' and  prof_id='{$professor_id}'";
$result2 = $dblink->query($query2);
$row = $result2->fetch_assoc();
$courseid = $row["course_id"];

$date1=date("Y/m/d");
$query_del="select aid,course_id,date_of_attendance from attendance where course_id='{$courseid}'  and date_of_attendance='{$date1}' ";
$result_del=$dblink->query($query_del);

if($result_del)
{
		while($row_aid = $result_del->fetch_assoc())
			{
				$aid_del=$row_aid["aid"];
				
				$query_p="select prof_id from location where aid='{$aid_del}'";
				$result_p=$dblink->query($query_p);

			if($result_p)
				{
					$query_loc="delete from location where aid='{$aid_del}'";
					$result_loc=$dblink->query($query_loc);

					$query_att="delete from attendance where aid='{$aid_del}'";
					$result_att=$dblink->query($query_att);
				}
			}
}

$query3="insert into attendance(course_id,date_of_attendance) values('{$courseid}' ,'{$date1}')";
$result3=$dblink->query($query3);


$query4="select aid from attendance where course_id='{$courseid}'";
$result4=$dblink->query($query4);
$row1 = $result4->fetch_assoc();
$aid = $row1["aid"];

$query5="insert into location values('{$aid}','{$professor_id}','{$lat}' ,'{$lon}') ";
$result5=$dblink->query($query5);	
   
   
if ($result1 && $result2 && $result3 && $result4 && $result5) {
        
        $response["success"] = 1;
        $response["message"] = "code successfully updated.";
 	echo json_encode($response);
    		} 
	 
} 
else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    
    echo json_encode($response);
}
?>