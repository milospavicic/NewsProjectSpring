var currentEditUser = 0;
$(document).ready(function() {
	if(currentUserId == "null" || currentUserUserType!="ADMIN"){
		window.location.href = "posts.html";
	}
	loadUsers();
});
function loadUsers(){
	var tempUrl = "http://localhost:8080/api/users/";
	console.log(tempUrl);
	$.ajax({
        url: tempUrl,
        dataType: 'json',
		cache: false,
        success: function (response) {
        	usersHeader();
			var table = $('#usersTable');
			for(var i=0; i<response.length; i++) {
				user = response[i];
				if(user.id == currentUserId){
					continue;
				}

				var userPhoto = 'http://www.personalbrandingblog.com/wp-content/uploads/2017/08/blank-profile-picture-973460_640-300x300.png';
				if(user.photo!=null){
					userPhoto = 'data:image/gif;base64,'+user.photo;
				}
				table.append('<tr class="data">'+
						'<td><img height="50" width="50" alt="photo" src="'+userPhoto+'"></td>'+
								'<td>'+user.name+'</td>'+
								'<td>'+user.username+'</td>'+
								'<td>'+user.password+'</td>'+
								'<td>'+user.userType+'</td>'+
								'<td><button class="btn btn-default" onclick="editUserAdminModal('+user.id+')">Edit</button></td>'+
								'<td><button class="btn btn-default" onclick="deleteUser('+user.id+')">Delete</button></td>'+
							'</tr>');
			}
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function usersHeader(){
	var table = $('#usersTable');
	table.empty();
	table.append('<tr>'+
			'<th>Photo</th>'+
			'<th>Name</th>'+
			'<th>Username</th>'+
			'<th>Password</th>'+
			'<th>Role</th>'+
			'<th><button class="btn btn-default" onclick="loadUsers()"><span class="glyphicon glyphicon-refresh"></span></button></th>'+
			'<th><button class="btn btn-danger" onclick="newUserModal()"><span class="glyphicon glyphicon-plus"></span></button></th>'+
		'</tr>');
}

function deleteUser(n){
	console.log(n);
	$.ajax({
        url: 'http://localhost:8080/api/users/'+n,
        contentType: "application/json",
		type: 'DELETE',
        success: function (response) {
        	console.log("user delete success: ");
        	loadUsers();
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function newUserModal(){
	$('#newUserAdmin').modal()
	$('#newPic').hide();
	$('#picUploadCheck').prop('checked', false);
	$('#usernameA').val("");
	$('#passwordA').val("");
	$('#nameA').val("");
	$('#newPic').val("");
}
function saveNewUserAdmin(){
	var username = $('#usernameA').val().trim();
	var password = $('#passwordA').val().trim();
	var name = $('#nameA').val().trim();
	var photo = $('#newPic')[0].files[0];
	var userType = $('#typeSelect').val();
	if(username=="" || password=="" || name==""){
		alert("All fields must be filled");
		return;
	}
	var checked = false;
	if($('#picUploadCheck').prop('checked')){
		checked=true;
	}
	if(checked == true && typeof photo == 'undefined'){
		alert("You must select a photo.");
		return;
	}
	console.log(username+" "+password+" "+name+" "+userType);
	var data={
			'username':username,
			'password':password,
			'name':name,
			'userType':userType
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
        	loadUsers();
        	$('#newUserAdmin').modal('toggle');
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			if(jqXHR.status=="403"){
				alert("Username already taken.");
			}
			
		}
    });
}
function picUploadChecker(){
	$('#newPic').toggle();
}
function editUserAdminModal(n){
	currentEditUser = n;
	console.log(n);
	$('#editUserAdmin').modal();
	$('#editPic').hide();
	$('#picUploadCheck1').prop('checked', false);
	$.ajax({
		type: 'GET',
        url: 'http://localhost:8080/api/users/'+n,
		cache: false,
        success: function (response) {
        	$('#passwordAEdit').val(response.password);
        	$('#nameAEdit').val(response.name);
        	$('#typeSelectEdit').val(response.userType);
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function picUploadCheckerEdit(){
	$('#editPic').toggle();
}
function saveEditUserAdmin(){
	var password = $('#passwordAEdit').val().trim();
	var name = $('#nameAEdit').val().trim();
	var photo = $('#editPic')[0].files[0];
	var userType = $('#typeSelectEdit').val();
	if(password=="" || name==""){
		alert("All fields must be filled");
		return;
	}
	var checked = false;
	if($('#picUploadCheck1').prop('checked')){
		checked=true;
	}
	console.log(checked);
	if(checked == true && typeof photo == 'undefined'){
		alert("You must select a photo.");
		return;
	}
	console.log(+password+" "+name+" "+userType);
	var data={
			'password':password,
			'name':name,
			'userType':userType
	}
	
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/users/'+currentEditUser,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	console.log(checked);
        	if(checked==true){
        		uploadPic(currentEditUser,photo);
        	}
        	loadUsers();
        	$('#editUserAdmin').modal('toggle');
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			if(jqXHR.status=="403"){
				alert("Username already taken.");
			}
			$('#editUserAdmin').modal('toggle');
		}
    });
}