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
$output['error']=true;
$output['message']="dummy";


$con = mysqli_connect($dbhost, $dbuser, $dbpass, $dbname);

	if($con== null)
	{

		$output['error'] = true;
		$output['message'] = 'couldnot connect to database';
	}
	else 
	{

		if($request_type == 'enable_attendance' )
		{

			$attendance_count = intval($_REQUEST['attendance_count']);
			$lecture = $_REQUEST['lecture'];
			$course = $_REQUEST['course'];       
			$table = $course."_subject_teacher_table";

			// checking if it is already enabled or not
			$query = "select * from $table where subjectname = '$lecture'";
			$query_result = mysqli_query($con, $query);
			if($query_result)
			{

				$row = mysqli_fetch_assoc($query_result);

				/*	$timediff = time() - strtotime($row['timestamp']);*/
				

					if($row['attendancecount'] > 0) 
					{
						// that means teacher has already allowed the request
						$output['error'] = false; 
						$output['message'] = "you have already enabled";
					}
					else
					{
						//enable the attendance and update the timestamp in course_subject_teacher_table

						 $query = "update $table set attendancecount = $attendance_count,timestamp = current_timestamp where subjectname = '$lecture'";
						$query_result = mysqli_query($con, $query);

						if($query_result)
						{
							$num = mysqli_affected_rows($con);
							if($num == 1)
							{
								$output['error']=false;
								$output['message']='successfully enabled attendance';
							}
							else 
							{
								$output['error']=true;
								$output['message']="cannot enable attendance as fetched rows = ".$num;
							}
						}
						else 
						{
							$output['error']= true;
							$output['mesage']='cannot update 2nd query';
						}

				  }
				
			}
		
			else 
			{
				$output['error']=true;
				$output['message']='cannot execute the query'.$lecture.$course;
			}
		
		}
		
		
		else if($request_type == 'disable_attendance' )
		{

			//$attendance_count = intval($_REQUEST['attendance_count']);
			$lecture = $_REQUEST['lecture'];
			$course = $_REQUEST['course'];       
			$table = $course."_subject_teacher_table";

			// checking if it is already enabled or not
			$query = "select * from $table where subjectname = '$lecture'";
			$query_result = mysqli_query($con, $query);
			if($query_result)
			{

				$row = mysqli_fetch_assoc($query_result);

					

					if($row['attendancecount']==0) 
					{
						// that means teacher has already disabled the attendance
						$output['error'] = false; 
						$output['message'] = "attendance is already disabled";
					}
					else
					{
						//disable the attendance and update the timestamp in course_subject_teacher_table

						 $query = "update $table set attendancecount = 0,timestamp = current_timestamp where subjectname = '$lecture'";
						$query_result = mysqli_query($con, $query);

						if($query_result)
						{
							$num = mysqli_affected_rows($con);
							if($num == 1)
							{
								$output['error']=false;
								$output['message']='successfully disabled the attendance';
							}
							else 
							{
								$output['error']=true;
								$output['message']="cannot disable the  attendance as fetched rows = ".$num;
							}
						}
						else 
						{
							$output['error']= true;
							$output['mesage']='cannot update 2nd query';
						}

					}
				
			}
		
			else 
			{
				$output['error']=true;
				$output['message']='cannot execute the query1';
			}
		
		}
		
		
	else if($request_type == 'mark_attendance')
		{
		   
		    $subject = $_REQUEST['subject'];
		    $course = $_REQUEST['course'];
		    $attendancecount = 1;
		    $approvedlist= json_decode($_REQUEST['approvedlist']);
		    $removedlist = json_decode($_REQUEST['removedlist']);
		    $tablerequest = $course."_student_request_table";
		    $tableattendance = $course."_attendance_table";
		    $tablegetcount = $course."_subject_teacher_table";
		    
		    //getting the attendance count
		    /* $querygetcount = "select * from $tablegetcount where subjectname = '$subject'";
		        
		        $res = mysqli_query($con,$querygetcount);
		        if($res)
		        {
		            $row = mysqli_fetch_assoc($res);
		           $attendancecount =  $row['attendancecount'];
		        }
		        else
		        {
		            $output['error']=true;
		            $output['message']="cannot get attendance count ";
		            die (json_encode($output));
		        }*/
		    
		    foreach($approvedlist as $k)
		    {
		        $list1 = json_decode($k,true);
		        
		        $rollno = $list1['rollno'];
		        $attendancecount = $list1['attendancecount'];
		       
		        
		        
		        $queryinsert = "insert into $tableattendance (`rollno`,`subject`,`attendancecount`) values ('$rollno','$subject',$attendancecount)";
		        $querydelete = "delete from `$tablerequest` where rollno = '$rollno' and subject = '$subject'";
		        
		        if(mysqli_query($con, $queryinsert))
		        {
		            if(mysqli_query($con,$querydelete))
		            {
		                $output['error']=false;
		                $output['message']="approvedlist done";
		            }
		            else 
		            {
		                $output['error']=true;
		                $output['message']="error deleting approved list";
		            }
		        }
		        else {
		            $output['error']=true;
		            $output['message']="error inserting approvedlist";
		        }
		    }
		    
		     
		    foreach($removedlist as $k)
		    {
		        $list1 = json_decode($k,true);
		        
		        $rollno = $list1['rollno'];
		        $subject = $list1['subject'];
		        $queryinsert = "insert into $tableattendance (`rollno`,`subject`,`attendancecount`) values ('$rollno','$subject',0)";
		        $querydelete = "delete from $tablerequest where rollno = '$rollno' and subject = '$subject'";
		        
		        if(mysqli_query($con, $queryinsert))
		        {
		            if(mysqli_query($con,$querydelete))
		            {
		                $output['error']=false;
		                $output['message']="removedlist done";
		            }
		            else 
		            {
		                $output['error']=true;
		                $output['message']="error deleting removedlist";
		            }
		        }
		        else {
		            $output['error']=true;
		            $output['message']="error inserting removedlist";
		        }
		    }
		    
		}

		else if($request_type == 'get_students')
		{
			// code to provide information about all students who have requested their attendane

			$subject = $_REQUEST['subject'];
			$course = $_REQUEST['course'];
			$table = $course."_student_request_table";

				$query = "select * from `$table` where subject = '$subject'";
				$queryres = mysqli_query($con,$query);
				if($queryres)
				{
					$listofstudents = array(array());
				
					$i = 0;
					while($row = mysqli_fetch_assoc($queryres))
					{
						
						    $listofstudents[$i]['rollno'] = $row['rollno'];
						    $listofstudents[$i]['course'] =	$course;
						    $listofstudents[$i]['subject'] = $subject;
						    $listofstudents[$i]['longitude']=$row['longitude'];
						    $listofstudents[$i]['latitude']=$row['latitude'];
						    $listofstudents[$i]['attendancecount']=$row['attendancecount'];
						    
							$i++;
									
					}
					$output['list'] = $listofstudents;
					$output['error'] = false;
					$output['message'] = 'list of students loaded successfully';
				}
				else 
				{
					$output['error']=true;
					$output['message'] = "cannot execute query";
				}



		}
		else if($request_type == 'check_if_attendance_enabled')
		{

			// here no need to check if teacher is registered or not.
			$course = $_REQUEST['course'];
			$lecture = $_REQUEST['lecture'];
			$table = $course.'_subject_teacher_table';

			$query = "select * from $table where subjectname = '$lecture' and attendancecount > 0";

			$query_result = mysqli_query($con, $query);

			if($query_result)
			{
				$numrows = mysqli_num_rows($query_result);

				if($numrows == 1)
				{ 
					$output['error']=false;
					$output['message'] = "you have already allowed the attendance";
					$output['isenabled'] = true;
				}
				else if($numrows == 0)
				{
					$output['error']=false;
					$output['message'] = "please allow the attendance";
					$output['isenabled'] = false;
				}
			}
			else
			{
				$output['error']=true;
				$output['message'] = "cannot execute query";
			}
		}
		else if($request_type == 'get_current_lecture')
		{
		    //date_default_timezone_set('Asia/Kolkata');
			//$today = strtolower(date('l'));
			$today = "wednesday";
			$name = $_REQUEST['name'];
			$table = $name."_timetable";
			//$lecture = getCurrentLecture();
			$lecture = "lecture5";

			$query = "select * from `$table` where day = '$today'";
			$res = mysqli_query($con, $query);

			if($res)
			{
				$row = mysqli_fetch_assoc($res);
				if($row[$lecture] != null)$course = $row[$lecture];
				else{
					$output['course'] = 'N/A';
					$output['lecture'] = 'N/A';
					$output['error']=false;
					$output['message'] = 'no lecture found ';
					echo(json_encode($output));
					exit();
				}

				$table = $course.'_subject_timetable';
				$query1 = "select * from `$table` where day = '$today'";

				$res1 = mysqli_query($con, $query1);
				if($res1)
				{
					$row = mysqli_fetch_assoc($res1);
					$output['course']= $course;
					$output['lecture'] = $row[$lecture];
					$output['message'] = 'successfully loaded lecture info ';
					$output['error'] = false;
				}
				else
				{
					$output['error'] = true;
					$output['message'] = 'cannot execute query 2';
				}

			}
			else {
				$output['error']= true;
				$output['message'] = 'cannot execute query1';
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


/*$output['error']=true;
$output['message']=$email.$course;*/
echo json_encode($output);


?>