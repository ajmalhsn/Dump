<?php
include 'connect.php';
error_reporting(E_ALL);
$sql = "SELECT orderdate, Region,Total FROM salesorders";
$result = mysqli_query($link, $sql);
if (mysqli_num_rows($result) > 0) {
     //output data of each row
    echo "<table border=5><tr><th>OrderDate</th><th>Region</th><th>Total</th></tr>";
    while($row = mysqli_fetch_assoc($result)) {
        
echo "<tr><td>" . $row["orderdate"]. "</td><td> " . $row["Region"]. "</td><td> " . $row["Total"]. "</td></tr>";
    }
    echo "</table>";
}
?>    