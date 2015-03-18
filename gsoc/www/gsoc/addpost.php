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
	$sql = "SELECT posts.id, posts.email, posts.story, posts.vision, users.photo, posts.timestamp, users.username from posts left join users on posts.email = users.email"; //replace emp_info with your table name
	$result = mysql_query($sql) or die (mysql_error());

		//if(mysql_num_rows($result)){
/*		while($row=mysql_fetch_row($result)){
				$id=$row[0];
				$email=$row[1];
				$story=$row[2];
				$vision=$row['vision'];
				$photo="ok";//$row['photo'];
				$timestamp=$row['timestamp'];
				$username=$row['username'];
			$data[] = array('id'=> $id, 'email'=> $email, 'story'=> $story, 'vision'=> $vision, 'profilePic'=> $photo, 'timeStamp'=> $timestamp, 'username'=> $username);
			
			}
*/			

		while($row=mysql_fetch_row($result)){
				$id=$row[0];
				$email=$row[1];
				$story=$row['posts.story'];
				$vision=$row[3];
				$photo=$row[4];
				$timestamp=$row[5];
				$username=$row[6];

				$data[] = array('id'=> $id, 'email'=> $email, 'story'=> $story, 'vision'=> $vision, 'profilePic'=> $photo, 'timeStamp'=> $timestamp, 'username'=> $username);			
			}
//			var_dump($data);
		//}
mysql_close($con);
$response['feed'] = $data;
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
