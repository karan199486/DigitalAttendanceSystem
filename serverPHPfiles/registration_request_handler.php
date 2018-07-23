<?php

//database credentials
$dbuser = "root";
$dbpass = "karan123";
$dbhost= 'localhost:3306';
$dbname = "ymcaustdblatest";

/*$dbuser = 'id4786976_karan';
$dbpass = 'karan123';
$dbhost = 'localhost';
$dbname = 'id4786976_ymcaust';*/

//user credentials
/*$name  = "karan";
$rollno = "MCA-456-2K16";
$course = "MCA";
$email = "example@gmail.com";
$phoneno = 123456789;
$password = "karan123";*/

$name  = $_REQUEST['name'];
$rollno = $_REQUEST['rollno'];
$course = $_REQUEST['course'];
$email = $_REQUEST['email'];
$phoneno = $_REQUEST['phoneno'];
$password = $_REQUEST['password'];

$output = array();

$con = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);

function isAlreadyRegistered()
{
	$con = $GLOBALS['con'];
	$rollno = $GLOBALS['rollno'];
	$email = $GLOBALS['email'];
	$table = $GLOBALS['course']."_student_info_table";

	$query = "select * from $table where rollno = '$rollno' OR email = '$email'";
	$val = mysqli_query($con,$query);
	$rowcount= mysqli_num_rows($val);
	//echo "$query, row count is $rowcount<br>";
	if($rowcount>0)return true;
	else return false;
}

if($con==null)
	{
		$output['error']= true;
		$output['message']="could not connect to database";
	}
else
{
	// connected to database
	if(isAlreadyRegistered())
		{
		// user is already registered
		$output['error']=true;
		$output['message'] = "you are already registered ! please login to your account";
		}
	else 
	{
		// user is not registered
		$table = $course."_student_info_table";
		$query = "insert into $table values('$name','$rollno','$email','$phoneno','$password')";
		$retval = mysqli_query($con,$query);

		if($retval){
			$output['error'] = false;
			$output['message'] = 'registered successfully';
		}
		else {
			$output['error'] = true;
			$output['message'] = 'cannot execute insert query . unable to regsiter';
		}
	}

	mysqli_close($con);	
}

echo json_encode($output);




?>