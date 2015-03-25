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

if (isset($json['course_name']) && isset($json['question']) && isset($json['prof_id']) && isset($json['option1']) && isset($json['option2']) ) 
{
 
    $prof_id = $json['prof_id'];
    $course=$json['course_name'];
    $question=$json['question'];
    $option1=$json['option1'];
    $option2=$json['option2'];
    $option3=$json['option3'];
    $option4=$json['option4'];
	

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


$query1="select course_id from professor_course_details where course_name='{$course}'";
$result1 = $dblink->query($query1);
if($result1)
{
	
	while ($row = $result1->fetch_assoc()) 
		{
       		
			$courseid = $row["course_id"];
		}
}
$query2="insert into poll(prof_id,course_id,poll_question,poll_optionA,poll_optionB,poll_optionC,poll_optionD) values ('{$prof_id}','{$courseid}','{$question}','{$option1}','{$option2}','{$option3}','{$option4}')";
$result2 = $dblink->query($query2);

$query3="select poll_id from poll where poll_id=(select max(poll_id) from poll where prof_id='{$prof_id}' group by prof_id )";	
$result3= $dblink->query($query3);  

if($result3)
{
	
	while ($row = $result3->fetch_assoc()) 
		{
       		
			$pollid = $row["poll_id"];
		}

 }
 

$query4="insert into professor_poll_details(prof_id,poll_id,course_id,count_a,count_b,count_c,count_d) values ('{$prof_id}','{$pollid}','{$courseid}',0,0,0,0)";
$result4 = $dblink->query($query4);
if ($result4) {
        
        $response["success"] = 1;
        $response["message"] = "Poll successfully updated.";
	$response["pollid"]=$pollid;
	$response["courseid"]=$courseid;
 	echo json_encode($response);
    		} 
	 
} 
else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    
    echo json_encode($response);
}
?>
