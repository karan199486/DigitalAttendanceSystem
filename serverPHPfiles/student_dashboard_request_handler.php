<?php

// database credentials
$dbuser = 'root';
$dbpass = 'karan123';
$dbhost = 'localhost:3306';
$dbname = 'ymcaustdblatest';

/*$dbuser = 'id4786976_karan';
$dbpass = 'karan123';
$dbhost = 'localhost';
$dbname = 'id4786976_ymcaust';*/

$request_type = $_REQUEST['request_type'];
$output = array();
	
	$con = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);

	if($con== null)
	{

		$output['error'] = true;
		$output['message'] = 'could not connect to database';
	}
	else 
	{
	
		if($request_type == 'request_attendance')
		{
			
			$course = $_REQUEST['course'];
			$subject = $_REQUEST['subject'];
			$rollno = $_REQUEST['rollno'];
			$longitude = $_REQUEST['longitude'];
			$latitude = $_REQUEST['latitude'];
			$attendancecount = 1;
			$tableattendancecount = $course."_subject_teacher_table";
			
			//getting attendance count
			$query = "select * from $tableattendancecount where subjectname = '$subject'";
		   if($res = 	mysqli_query($con,$query))
		   {
		       $row = mysqli_fetch_assoc($res);
		       $attendancecount = $row['attendancecount'];
		   }
		   else
		   {
		       $output['error']=true;
		       $output['message']="cannot retrieve attendance count";
              die(json_encode($output));
		   }
			$table = $course."_student_request_table";
			$query = "insert into `$table`(rollno,subject,attendancecount,longitude,latitude)values('$rollno','$subject','$attendancecount','$longitude','$latitude')";
			$res = mysqli_query($con, $query);

			if($res)
			{
				$num_rows = mysqli_affected_rows($con);
				if($num_rows == 1)
				{
					$output['error']=false;
					$output['message']="request submitted successfully";	
				}
				else if($num_rows == 0)
				{
					$output['error']=false;
					$output['message']="you have already requested";	
				}
				
			}
			else 
			{
				$output['error']=true;
				$output['message']='cannot execute the query';
			}
		
		}



		
		else if($request_type == 'check_if_attendance_enabled')
		{
			
			$subject = $_REQUEST['subject'];
			$course = $_REQUEST['course'];

			$table = $course."_subject_teacher_table";
			$query = "select * from `$table` where attendancecount > 0 and subjectname = '$subject'";
			$res = mysqli_query($con, $query);
			if($res)
			{
				$num_rows = mysqli_num_rows($res);
			
				if($num_rows == 1)
				{
					$output['error']=false;
					$output['message']="attendance is enabled :)";
				}
				else 
				{
					$output['error'] = true;
					$output['message'] = "attendance is not enabled :(";
				}
				
			}
			else 
			{
				$output['error']=true;
				$output['message']='cannot execute the query';
			}
		}


		else if($request_type == 'check_if_already_requested')
		{
			$course = $_REQUEST['course'];
			$subject = $_REQUEST['subject'];
			$rollno = $_REQUEST['rollno'];

			$table = $course."_student_request_table";
			$query = "select * from $table where rollno = '$rollno' and subject = '$subject'";
			$res = mysqli_query($con, $query);

			if($res)
			{
				$num_rows = mysqli_num_rows($res);
				if($num_rows == 1)
				{
					$output['error'] = false;
					$output['isrequested'] = true;
					$output['message'] = "already requested";	
				}
				else {
					$output['error'] = false;
					$output['isrequested'] = false;
					$output['message'] = "you have not requested attendance";
				}
			}
			else 
			{
				$output['error']=true;
				$output['message']='cannot execute the query';
			}
		}

		else if($request_type == 'get_lecture_info') 
		{
			$course = $_REQUEST['course'];

			//date_default_timezone_set('Asia/Kolkata');

			//$today = strtolower("".date('l'));
			 $today = "wednesday";
			//$lecture = getCurrentLecture();
			$lecture = "lecture5";

			$table_subject = $course."_subject_timetable";
			$table_subjectteacher = $course."_subject_teacher_table";

			$query = "select A.$lecture as subject , B.teachername as teacher from `$table_subject` A, `$table_subjectteacher` B where A.day = '$today' and B.subjectname = A.$lecture";

			$res = mysqli_query($con,$query);
			
			if($res && ($today != 'saturday' && $today != 'sunday'))
			{
				//echo $today;
				$row = mysqli_fetch_assoc($res);
				$output['subject']= $row['subject'];	
				$output['teacher']= $row['teacher'];
				$output['error']=false;
				$output['message']="loaded successfully";
				
			}
			else 
			{
				/*$output['error']=true;
				$output['message'] = "could not execute query";*/
					$output['error']=false;
					$output['message']="no lecture found at current time";
					$output['teacher'] = "N/A";
					$output['subject'] = "N/A";
			}

		}

		mysqli_close($con);
	}

	function getCurrentLecture()
			{
			//tested ok
			$hour = intval(date('H'));
			$minute = intval(date('i'));

			if(($hour ==9 && $minute>=15) || ($hour ==10 && $minute<=10))return "lecture1";
			else if(($hour ==10 && $minute>=11) || ($hour ==11 && $minute<=5))return "lecture2";
			else if(($hour ==11 && $minute>=6) || ($hour ==12 && $minute<=0))return "lecture3";
			else if(($hour ==12 && $minute>=1) || ($hour ==12 && $minute<=55))return "lecture4";
			else if(($hour ==13 && $minute>=50) || ($hour ==14 && $minute<=45))return "lecture5";
			else if(($hour ==14 && $minute>=46) || ($hour ==15 && $minute<=40))return "lecture6";
			else if(($hour ==15 && $minute>=41) || ($hour ==16 && $minute<=35))return "lecture7";
			else if(($hour ==16 && $minute>=36) || ($hour ==17 && $minute<=30))return "lecture8";
			else return "";
			}

/*$output['error'] = false;
$output['message'] = 'sample message';*/

echo json_encode($output);