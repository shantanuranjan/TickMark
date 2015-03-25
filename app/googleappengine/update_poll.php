<?php


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

if (isset($json['cid']) && isset($json['pid']) && isset($json['pollid'])&& isset($json['opt'])) 
{
 
    $cid = $json['cid'];
    $pid=$json['pid'];
    $pollid=$json['pollid'];
    $opt=$json['opt'];
	

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


$query="update professor_poll_details set ".$opt."=".$opt."+1 where course_id='{$cid}' and poll_id='{$pollid}' and prof_id='{$pid}'";
// $query = "update professor_poll_details set count_a = 3 where course_id = '{$cid}'";
$result = $dblink->query($query);
  
   
if ($result) {
        
        $response["success"] = 1;
        $response["message"] = "poll successfully updated.";
 	echo json_encode($response);
    		} 
	 
} 
else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";

    echo json_encode($response);
}
?>