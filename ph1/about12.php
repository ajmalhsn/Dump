<?php include 'header.php';
 //include 'connect.php'; ?>
<div class="container" style="padding:10px;">
    <div class="row">
        
 <?php
include 'connect.php';
error_reporting(E_ALL);
$sql = "SELECT Q1,Q2,Q3 FROM sheet1 LIMIT 10";
$result = mysqli_query($link, $sql);
if (mysqli_num_rows($result) > 0) {
     //output data of each row
    echo '<table class="bordered highlight centred"><tr><th>Q1</th><th>Q2</th><th>Q3</th></tr>';
    while($row = mysqli_fetch_assoc($result)) {
        
echo "<tr><td>" . $row["Q1"]. "</td><td> " . $row["Q2"]. "</td><td> " . $row["Q3"]. "</td></tr>";
    }
    echo "</table>";
}
?>    
        
        </div>        
    </div>
     
<?php include 'footer.php';?>
