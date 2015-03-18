<?php
//header('Content-Type: application/json;charset=utf-8');
$response = array();
$data = array();
$host="localhost"; //replace with your hostname
$username="root"; //replace with your username
$password=""; //replace with your password
$db_name="gsoc"; //replace with your database
	$con=mysql_connect("$host", "$username", "$password")or die("cannot connect");
	mysql_select_db("$db_name")or die("cannot select DB");
	$sql = "SELECT  posts.title, users.photo, posts.story, posts.email,  posts.timestamp, posts.vision from posts left join users on posts.email = users.email"; //replace emp_info with your table name
	//$sq = "select title, story, email, timestamp, vision from posts";
	$result = mysql_query($sql) or die (mysql_error());

		//if(mysql_num_rows($result)){
		//	}

		while($row=mysql_fetch_assoc($result)){
				$title=$row['title'];
				$photo=$row['photo'];
				$story="vv";//$row['story'];
				$email=$row['email'];
				$timestamp=$row['timestamp'];
				$vision=$row['vision'];

				$data[] = array('title'=> $title, 'photo'=> $photo, 'story'=> $story,'email'=> $email,  'timeStamp'=> $timestamp, 'vision'=> $vision);			
			}
//			var_dump($data);
		//}
mysql_close($con);
$response = $data;
//var_dump($response);
if(file_exists('results.json')) {
	$fp = fopen('results.json', 'w');
	
	$json_encode = json_encode($response, JSON_PRETTY_PRINT);
	if(fwrite($fp, $json_encode))
	{
		echo "Successful!";
	}
	else{
		echo "Unsuccessful";
	}
	fclose($fp);
}
else {
	echo "Json file not found";
}

//print(json_encode($response, JSON_PRETTY_PRINT));
echo json_encode($response, JSON_PRETTY_PRINT);
// please refer to our PHP JSON encode function tutorial for learning json_encode function in detail
?>
