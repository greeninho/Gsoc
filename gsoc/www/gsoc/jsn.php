<?php
header('Content-Type: application/json;charset=utf-8');
$response = array();
$data = array();
$host="localhost"; //replace with your hostname
$username="root"; //replace with your username
$password=""; //replace with your password
$db_name="gsoc"; //replace with your database
	$con=mysqli_connect("$host", "$username", "$password", "$db_name")or die("cannot connect");
	//mysqli_select_db("$db_name")or die("cannot select DB");
	$sql = "SELECT  posts.title, users.photo, posts.stori, posts.email,  posts.timestamp, posts.vision from posts left join users on posts.email = users.email"; //replace emp_info with your table name
	//$sq = "select title, story, email, timestamp, vision from posts";
	$result = mysqli_query($con, $sql) or die (mysqli_error());

		//if(mysqli_fetch_assoc($result)){
		//	}

		while($row=mysqli_fetch_assoc($result)){
				$title=$row['title'];
				$photo=$row['photo'];
				$story=$row['stori'];
				$email=$row['email'];
				$timestamp=$row['timestamp'];
				$vision=$row['vision'];

				$data[] = array('title'=> $title, 'photo'=> $photo, 'story'=> $story,'email'=> $email,  'timestamp'=> $timestamp, 'vision'=> $vision);			
			}
//			var_dump($data);
		//}
mysqli_close($con);
$response = $data;
//var_dump($response);
if(file_exists('results.json')) {
	$fp = fopen('results.json', 'w');
	
	$json_encode = json_encode($response, JSON_PRETTY_PRINT);
	if(fwrite($fp, $json_encode))
	{
		//echo "Successful!";
	}
	else{
		//echo "Unsuccessful";
	}
	fclose($fp);
}
else {
	//echo "Json file not found";
}

//print(json_encode($response, JSON_PRETTY_PRINT));
echo json_encode($response, JSON_PRETTY_PRINT);
// please refer to our PHP JSON encode function tutorial for learning json_encode function in detail
?>
