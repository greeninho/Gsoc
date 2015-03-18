<?php
// include db handler
    require_once 'include/DB_Functions.php';
    $db = new DB_Functions();
    // Get image string posted from Android App
    $encodedString=$_REQUEST['image'];
	$email = $_REQUEST['email'];
	$storePhoto = $db->storeImage($encodedString, $email);
    if($storePhoto != false) 
		echo 'Upload successful';
	else
		echo 'Upload unsuccessful';
?>