	var db ;
	var table ="area2";
	function DBinit() {
		db = window.openDatabase("JEJUFARM","1.0",'InfoDB',1024*1024*3);
		db.transaction(
			function(tx){
				//tx.executeSql("DROP TABLE area2 ");
				tx.executeSql("CREATE TABLE IF NOT EXISTS " + table + " (NUMBER, TYPE, AREA, CITY)");
				 
			},
			function(tx,err){
				alert("DB생성 에러 : " + err);
			},
			function(){
				//alert("DB생성 에러2");
			}

		);
 }	
	
function newInsert(number, code, area, city)
{
	//db = window.openDatabase("MTRACE","1.0",'InfoDB',1024*1024*3);
		db.transaction(
			function(tx){
				//tx.executeSql("DROP TABLE area ");
				 
				tx.executeSql("INSERT INTO " + table + " VALUES('"+number+"','"+code+"','"+area+"','"+city+"')");
			},
			function(tx,err){
				alert("DB생성 에러 : " + err);
			},
			function(){
			}
		);
}
function deleteArea(code)
{
	//var db = window.openDatabase("MTRACE","1.0",'InfoDB',1024*1024*3);
	 
	db.transaction(
		function(tx){
			tx.executeSql("DELETE FROM " + table + " WHERE TYPE ='"+code+"'");
		},
		function(tx,err){
			alert("초기화 실패 : " + err);
		},
		function(){}
	);
}
function deleteAll()
{
	// db = window.openDatabase("MTRACE","1.0",'InfoDB',1024*1024*3);
	db.transaction(
		function(tx){
			tx.executeSql("DELETE FROM area " + table );
		},
		function(tx,err){
			alert("초기화 실패 : " + err);
		},
		function(){}
	);
}