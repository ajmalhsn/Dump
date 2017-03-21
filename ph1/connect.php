<?php
$link = @mysqli_connect('localhost', 'ajmal', 'jasaj', 'ajmal');

if (!$link) {
    die('Connect Error: ' .mysqli_connect_error());
}
else{
    echo 'Connection Successful' .mysqli_connect_error();
}
?>