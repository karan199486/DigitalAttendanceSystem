-- phpMyAdmin SQL Dump
-- version 4.5.4.1deb2ubuntu2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Jul 23, 2018 at 12:16 PM
-- Server version: 5.7.22-0ubuntu0.16.04.1
-- PHP Version: 7.0.30-0ubuntu0.16.04.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `ymcaustdblatest`
--

-- --------------------------------------------------------

--
-- Table structure for table `course1_attendance_table`
--

CREATE TABLE `course1_attendance_table` (
  `rollno` varchar(20) NOT NULL,
  `subject` varchar(10) NOT NULL,
  `attendancecount` tinyint(4) NOT NULL DEFAULT '1',
  `timestamp` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
) ;

--
-- Dumping data for table `course1_attendance_table`
--

INSERT INTO `course1_attendance_table` (`rollno`, `subject`, `attendancecount`, `timestamp`) VALUES
('K', 'sub1', 1, '2018-07-23 06:37:03.008455');

-- --------------------------------------------------------

--
-- Table structure for table `course1_student_info_table`
--

CREATE TABLE `course1_student_info_table` (
  `name` varchar(255) DEFAULT NULL,
  `rollno` varchar(50) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phoneno` varchar(10) NOT NULL,
  `password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course1_student_info_table`
--

INSERT INTO `course1_student_info_table` (`name`, `rollno`, `email`, `phoneno`, `password`) VALUES
('k', 'K', 'k', '1', 'k');

-- --------------------------------------------------------

--
-- Table structure for table `course1_student_request_table`
--

CREATE TABLE `course1_student_request_table` (
  `rollno` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `subject` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `attendancecount` int(4) NOT NULL,
  `longitude` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `latitude` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `timestamp` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
) ;

-- --------------------------------------------------------

--
-- Table structure for table `course1_subject_teacher_table`
--

CREATE TABLE `course1_subject_teacher_table` (
  `subjectname` varchar(255) NOT NULL,
  `teachername` varchar(255) NOT NULL,
  `attendancecount` tinyint(1) NOT NULL DEFAULT '0' COMMENT 'it can have values like 1,2,3.. depending on no of attendance teacher want to give',
  `timestamp` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'time stamp when attendance is allowed'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course1_subject_teacher_table`
--

INSERT INTO `course1_subject_teacher_table` (`subjectname`, `teachername`, `attendancecount`, `timestamp`) VALUES
('sub1', 'teacher1', 0, '2018-07-23 12:09:15'),
('sub2', 'teacher2', 0, '2018-07-23 01:24:53');

-- --------------------------------------------------------

--
-- Table structure for table `course1_subject_timetable`
--

CREATE TABLE `course1_subject_timetable` (
  `day` varchar(50) NOT NULL,
  `lecture1` varchar(10) DEFAULT NULL,
  `lecture2` varchar(10) DEFAULT NULL,
  `lecture3` varchar(10) DEFAULT NULL,
  `lecture4` varchar(10) DEFAULT NULL,
  `lecture5` varchar(10) DEFAULT NULL,
  `lecture6` varchar(10) DEFAULT NULL,
  `lecture7` varchar(10) DEFAULT NULL,
  `lecture8` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `course1_subject_timetable`
--

INSERT INTO `course1_subject_timetable` (`day`, `lecture1`, `lecture2`, `lecture3`, `lecture4`, `lecture5`, `lecture6`, `lecture7`, `lecture8`) VALUES
('friday', 'sub1', 'sub2', 'sub2', 'sub1', 'sub1', 'sub2', 'sub1', 'sub2'),
('monday', 'sub1', 'sub2', 'sub2', 'sub2', 'sub1', 'sub1', 'sub1', 'sub1'),
('thursday', 'sub1', 'sub1', 'sub1', 'sub1', 'sub2', 'sub2', 'sub2', 'sub1'),
('tuesday', 'sub2', 'sub1', 'sub1', 'sub1', 'sub2', 'sub2', 'sub2', 'sub2'),
('wednesday', 'sub2', 'sub2', 'sub2', 'sub2', 'sub1', 'sub2', 'sub2', 'sub1');

-- --------------------------------------------------------

--
-- Table structure for table `teacher1_timetable`
--

CREATE TABLE `teacher1_timetable` (
  `day` varchar(50) DEFAULT NULL,
  `lecture1` varchar(50) DEFAULT NULL,
  `lecture2` varchar(50) DEFAULT NULL,
  `lecture3` varchar(50) DEFAULT NULL,
  `lecture4` varchar(50) DEFAULT NULL,
  `lecture5` varchar(50) DEFAULT NULL,
  `lecture6` varchar(50) DEFAULT NULL,
  `lecture7` varchar(50) DEFAULT NULL,
  `lecture8` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teacher1_timetable`
--

INSERT INTO `teacher1_timetable` (`day`, `lecture1`, `lecture2`, `lecture3`, `lecture4`, `lecture5`, `lecture6`, `lecture7`, `lecture8`) VALUES
('monday', 'course1', 'course2', 'course2', 'course2', 'course1', 'course1', 'course1', 'course1'),
('tuesday', 'course2', 'course1', 'course1', 'course1', 'course2', 'course2', 'course2', 'course2'),
('wednesday', 'course2', 'course2', 'course2', 'course2', 'course1', 'course2', 'course2', 'course1'),
('thursday', 'course1', 'course1', 'course1', 'course1', 'course2', 'course2', 'course2', 'course1'),
('friday', 'course1', 'course2', 'course2', 'course1', 'course1', 'course2', 'course1', 'course2');

-- --------------------------------------------------------

--
-- Table structure for table `teacher2_timetable`
--

CREATE TABLE `teacher2_timetable` (
  `day` varchar(50) DEFAULT NULL,
  `lecture1` varchar(50) DEFAULT NULL,
  `lecture2` varchar(50) DEFAULT NULL,
  `lecture3` varchar(50) DEFAULT NULL,
  `lecture4` varchar(50) DEFAULT NULL,
  `lecture5` varchar(50) DEFAULT NULL,
  `lecture6` varchar(50) DEFAULT NULL,
  `lecture7` varchar(50) DEFAULT NULL,
  `lecture8` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teacher2_timetable`
--

INSERT INTO `teacher2_timetable` (`day`, `lecture1`, `lecture2`, `lecture3`, `lecture4`, `lecture5`, `lecture6`, `lecture7`, `lecture8`) VALUES
('monday', 'course2', 'course1', 'course1', 'course1', 'course2', 'course2', 'course2', 'course2'),
('tuesday', 'course1', 'course2', 'course2', 'course2', 'course1', 'course1', 'course1', 'course1'),
('wednesday', 'course1', 'course1', 'course1', 'course1', 'course2', 'course1', 'course1', 'course2'),
('thursday', 'course2', 'course2', 'course2', 'course2', 'course1', 'course1', 'course1', 'course2'),
('friday', 'course2', 'course1', 'course1', 'course2', 'course2', 'course2', 'course1', 'course1');

-- --------------------------------------------------------

--
-- Table structure for table `teachers_info_table`
--

CREATE TABLE `teachers_info_table` (
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `teachers_info_table`
--

INSERT INTO `teachers_info_table` (`name`, `email`, `password`) VALUES
('teacher1', 'teacher1@xyz.com', '123'),
('teacher2', 'teacher2@xyz.com', '123');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `course1_student_info_table`
--
ALTER TABLE `course1_student_info_table`
  ADD PRIMARY KEY (`rollno`),
  ADD UNIQUE KEY `mobile` (`phoneno`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indexes for table `course1_subject_timetable`
--
ALTER TABLE `course1_subject_timetable`
  ADD UNIQUE KEY `day` (`day`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
