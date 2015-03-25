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
$course=$json['course'];
$profid=$json['prof_id'];
$arr=array($json['student']);
$date = date_create();
$date1 = date_format($date, 'U');

 $my_file = $profid.'_'.$date1.'.txt';

$handle = fopen($my_file, 'w') or die('Cannot open file:  '.$my_file);
 
foreach($arr as &$value)
{
fwrite($handle, $value); 

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


$query2="select emailid from user_registration where user_id='{$profid}'";
$result2 = $dblink->query($query2);
while($row_email = $result2->fetch_assoc())
			{
		$id=$row_email["emailid"];

}	

   $response["success"] = 1;
        $response["message"] = "file loaded on web server.";
	$response["filename"]=$my_file;
	$response["professor_id"]=$id;
 	echo json_encode($response);
 fclose($handle); 

 
 ?>

