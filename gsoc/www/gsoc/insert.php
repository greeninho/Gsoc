<?php
	$host="localhost";
	$uname="root";
	$pwd="";
	$db="gsoc";

	$con = mysqli_connect("$host","$uname","$pwd", "$db") or die("connection failed");
	//mysqli_select_db($db,$con) or die("database selection failed");
	 
	$email = $_POST['email'];
	$story = $_POST['story'];
	$vision = $_POST['vision'];
	$title = $_POST['title'];
	$sql = "INSERT INTO posts(email, stori, vision, title, timestamp) VALUES('$email', '$story', '$vision','$title', NOW())";
	$flag['code']=0;
	
	if($r=mysqli_query($con, $sql))
	{
		$flag['code']=1;
	} else
	{
		echo mysqli_error($con);
	}
	
	mysqli_close($con);
	echo json_encode($flag, JSON_PRETTY_PRINT);
?>