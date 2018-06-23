var currentUserId = "";
var currentUserUserType = "";
$(document).ready(function() {
	loginStatus();
});
function loginStatus(){
	currentUserId = sessionStorage.getItem("id");
	currentUserUserType = sessionStorage.getItem("userType");
	console.log("currentUserId: "+currentUserId);
	console.log("currentUserUserType: "+currentUserUserType);
	
	if(currentUserId == "null"){
		console.log("Jeste null");
		$('#logoutButton').hide();
		$('#usersLink').hide();
		$('#editPost').hide();
		$('#deletePost').hide();
		$('#editUserBasicButton').hide();
	}else{
		$('#loginButton').hide();
		$('#registerButton').hide();
		if(currentUserUserType!="ADMIN"){
			$('#editPost').hide();
			$('#deletePost').hide();
			$('#usersLink').hide();
			if(currentUserUserType!="PUBLISHER"){
				$("#newPost").prop("disabled",true);
			}
		}
	}
	
}
function logout(){
    sessionStorage.setItem("id", null);
    sessionStorage.setItem("userType", null);
    window.location.href = "posts.html";
}

function formatDate(tempDate) {
	var date = new Date(tempDate);
	var monthNames = [
	  "January", "February", "March",
	  "April", "May", "June", "July",
	  "August", "September", "October",
	  "November", "December"
	];

	var day = date.getDate();
	var monthIndex = date.getMonth();
	var year = date.getFullYear();

	return day + '. ' + monthNames[monthIndex] + ' of ' + year+'.';
	//return day + '. ' + parseInt(monthIndex+1) + '. ' + year+'.';
}

function currentDate(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!

	var yyyy = today.getFullYear();
	if(dd<10){
	    dd='0'+dd;
	} 
	if(mm<10){
	    mm='0'+mm;
	} 
	var today = yyyy+"-"+mm+"-"+dd;
	
	return today;
}
function loginModal(){
	$('#loginModal').modal();
	$('#loginUsername').val("");
	$('#loginPassword').val("");
}
function login(){
	var username =  $('#loginUsername').val().trim();
	var password = $('#loginPassword').val().trim();
	if(username=="" || password==""){
		alert("All fields must be filled.")
		return;
	}
	$.ajax({
		type: 'GET',
        url: 'http://localhost:8080/api/users/'+username+'/'+password,
		cache: false,
        success: function (response) {
        	if (typeof(Storage) !== "undefined") {
        	    sessionStorage.setItem("id", response.id);

        	    sessionStorage.setItem("userType", response.userType);
        	    location.reload();
        	} else {
        	    alert("Sorry, your browser does not support Web Storage...");
        	}
        	$('#loginModal').modal('toggle');
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			if(jqXHR.status="404"){
				alert("That user doesnt exist.");
			}
			$('#loginModal').modal('toggle');
		}
    });
}
function registerModal(){
	$('#registerModal').modal();
	$('#regPic').hide();
	$('#picUploadCheckReg').prop('checked', false);
	$('#registerUsername').val("");
	$('#registerPassword').val("");
	$('#registerName').val("");
	$('#regPic').val("");
}
function picUploadCheckerReg(){
	$('#regPic').toggle();
}
function register(){
	var username =  $('#registerUsername').val().trim();
	var password = $('#registerPassword').val().trim();
	var name =  $('#registerName').val().trim();
	var photo =  $('#regPic')[0].files[0];
	
	if(username=="" || password=="" || name==""){
		alert("All fields must be filled");
		return;
	}
	var checked = false;
	if($('#picUploadCheckReg').prop('checked')){
		checked=true;
	}
	if(checked == true && typeof photo == 'undefined'){
		alert("You must select a photo.");
		return;
	}
	console.log(username+" "+password+" "+name+" ");
	var data={
			'username':username,
			'password':password,
			'name':name,
			'userType':"USER"
	}
	
	$.ajax({
		type: 'POST',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/users/',
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	if(checked==true){
        		uploadPic(response.id,photo);
        	}
        	alert("Registration successful.")
        	$('#registerModal').modal('toggle');
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			if(jqXHR.status=="403"){
				alert("Username already taken.");
			}
			
		}
    });
}
function uploadPic(userId,photo){
	var data = new FormData();
	data.append("id",userId);
	data.append("photo",photo);
	
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/users/upload_photo',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	console.log("Pic user upload success.");
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function editUserBasic(){
	$.ajax({
		type: 'GET',
        url: 'http://localhost:8080/api/users/'+currentUserId,
		cache: false,
        success: function (response) {
        	$('#editUserBasic').modal();
        	$('#editPicBasic').hide();
        	$('#picUploadCheckEB').prop('checked', false);
        	
        	$('#passwordEditBasic').val(response.password);
        	$('#nameEditBasic').val(response.name);
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function picUploadCheckerEditBasic(){
	$('#editPicBasic').toggle();
}
function saveEditUserBasic(){
	var password =  $('#passwordEditBasic').val().trim();
	var name = $('#nameEditBasic').val().trim();
	var photo =  $('#editPicBasic')[0].files[0];
	
	if(password=="" || name==""){
		alert("All fields must be filled");
		return;
	}
	var checked = false;
	if($('#picUploadCheckEB').prop('checked')){
		checked=true;
	}
	if(checked == true && typeof photo == 'undefined'){
		alert("You must select a photo.");
		return;
	}
	console.log(password+" "+name+" ");
	var data={
			'password':password,
			'name':name,
			'userType':currentUserUserType
	}
	
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/users/'+currentUserId,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	if(checked==true){
        		uploadPic(response.id,photo);
        	}
        	$('#editUserBasic').modal('toggle');
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(jqXHR.status);
		}
    });
}