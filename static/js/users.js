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
								'<td><button class="btn btn-default">Delete</button></td>'+
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
		'</tr>');
}