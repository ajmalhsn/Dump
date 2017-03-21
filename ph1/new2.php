<!DOCTYPE html>
<html>
<head>
<style>
table {
    width: 100%;
    border-collapse: collapse;
}

table, td, th {
    border: 1px solid black;
    padding: 5px;
}

th {text-align: left;}
</style>
</head>
<body>

<?php
include 'connect.php';
error_reporting(E_ALL);
$q = $_GET['q'];
    echo $q;
$sql="SELECT OrderDate,Region,Total FROM salesorders WHERE Region='". $q."'LIMIT 5";
$result = mysqli_query($link, $sql);
if (mysqli_num_rows($result) > 0) {
     //output data of each row
    echo '<table class="bordered highlight centred"><tr><th>Orderdate</th><th>Region</th><th>Total</th></tr>';
    while($row = mysqli_fetch_assoc($result)) {
        
echo "<tr><td>" . $row["OrderDate"]. "</td><td> " . $row["Region"]. "</td><td> " . $row["Total"]. "</td></tr>";
    }
    echo "</table>";
mysqli_close($link);    
}
else
    {
        echo "no" .mysqli_error($link);
    }
?>    
</body>
</html>