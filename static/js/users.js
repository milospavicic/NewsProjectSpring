$(document).ready(function() {
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
				table.append('<tr class="data">'+
								'<td><img alt="userImage"></td>'+
								'<td>'+user.name+'</td>'+
								'<td>'+user.username+'</td>'+
								'<td>'+user.password+'</td>'+
								'<td><button class="btn btn-default">Edit</button></td>'+
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
			'<th><button class="btn btn-danger" onclick="newUserModal()">New</button></th>'+
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
}
function saveNewUserAdmin(){
	var username = $('#usernameA').val();
	var password = $('#passwordA').val();
	var name = $('#nameA').val();
	if(username=="" || password=="" || name==""){
		alert("All fields must be filled");
		return;
	}
	
	
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/users/',
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	console.log("user delete success: ");
        	loadUsers();
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}