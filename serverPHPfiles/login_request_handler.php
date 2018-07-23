<?php
//tested ok
$dbuser = 'root';
$dbpass = 'karan123';
$dbhost = 'localhost:3306';
$dbname = 'ymcaustdblatest';

/*$dbuser = 'id4786976_karan';
$dbpass = 'karan123';
$dbhost = 'localhost';
$dbname = 'id4786976_ymcaust';*/

$output = array();
$query = "this consist of sql query";

$entity = $_POST['entity'];
$email = "this will contain email address of teacher";

$con = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);

	if($con== null)
	{
		$output['error'] = true;
		$output['message'] = 'couldnot connect to database';
	}
	else 
	{
		if($entity == 'student')  // if user is a student
		{

			$rollno = $_POST['rollno'];
			$password = $_POST['password'];
			$course = $_POST['course'];
			$table = $course."_student_info_table";
			$query = "select * from $table where rollno = '$rollno' and password = '$password'";
			
		}
		else if($entity == 'teacher')    // if user is a teacher
			{
				$name = $_POST['name'];
				$email = $_POST['email'];
				$password = $_POST['password'];
			   	$query = "select * from `teachers_info_table` where email = '$email' and name ='$name' and password = '$password'";
			}

		$queryresult = mysqli_query($con, $query);

		if($queryresult)
			{
				if(mysqli_num_rows($queryresult)==1)
				{
						$output['error']= false;
						$output['message'] = 'login successfully';
				}
				else 
				{
					$output['error']= true;

					$output['message'] = ($entity == 'student')?'invalid rollno or password ':'invalid email or password';
				}
			}
			else{
					$output['error'] = true;
					$output['message'] = 'couldnot execute the query';
				}

	
		mysqli_close($con);
		
		}
	
	/*$output['error'] = true;
	$output['message'] = "your input is ".$email.$password.$entity;*/
	echo json_encode($output);


?>