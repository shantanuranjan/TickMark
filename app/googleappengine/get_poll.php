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


if (isset($json['pid']) && isset($json['cid']) ) 
{
 
    $cid=$json['cid'];
    $pid=$json['pid'];
/*$dblink=new mysqli('173.194.241.32/:3306','root','password','sunsimeng');*/
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


$query="select poll_id, poll_question, poll_optionA, poll_optionB, poll_optionC,poll_optionD from poll where course_id='{$cid}' and prof_id='{$pid}'";

$result = $dblink->query($query);
   
if ($result) {

	while($row = $result->fetch_assoc()){
		$pollid=$row["poll_id"];
		$pollquestion=$row["poll_question"];
		$opta=$row["poll_optionA"];
		$optb=$row["poll_optionB"];
		$optc=$row["poll_optionC"];
		$optd=$row["poll_optionD"];
	}

	$response["pollid"]=$pollid;
	$response["pollquestion"]=$pollquestion;
	$response["opta"]=$opta;
	$response["optb"]=$optb;
	$response["optc"]=$optc;
	$response["optd"]=$optd;

        $response["success"] = 1;
        $response["message"] = "poll successfully getted.";
 	echo json_encode($response);
    		} 
	 
} 
else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    
    echo json_encode($response);
}
?>


