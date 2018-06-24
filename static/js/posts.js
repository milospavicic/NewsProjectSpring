var tagsBeforeEdit = "";
var currentEditPostId = 0;
var currentEditPost = null;
$(document).ready(function() {
	refresh();
});
function loadPosts(orderWay){
	var tempUrl = "http://localhost:8080/api/posts/"+orderWay;
	console.log(tempUrl);
	$.ajax({
        url: tempUrl,
        dataType: 'json',
		cache: false,
        success: function (response) {
        	postsHeader();
			var table = $('#postsTable');
			for(var i=0; i<response.length; i++) {
				post = response[i];
				var tempDate = formatDate(post.date);
				if(currentUserId == "null" || currentUserUserType!="ADMIN"){
					table.append('<tr class="data">'+
							'<td><a href="post.html?id='+post.id+'">'+post.title+'</a></td>'+
							'<td>'+tempDate+'</td>'+
							'<td>'+post.user.name+'</td>'+
							'<td>'+post.likes+'</td>'+
							'<td>'+post.dislikes+'</td>'+
						'</tr>');
					
				}else{
					table.append('<tr class="data">'+
							'<td><a href="post.html?id='+post.id+'">'+post.title+'</a></td>'+
							'<td>'+tempDate+'</td>'+
							'<td>'+post.user.name+'</td>'+
							'<td>'+post.likes+'</td>'+
							'<td>'+post.dislikes+'</td>'+
							'<td><button onclick="editPost('+post.id+')">Edit</button</td>'+
							'<td><button onclick="deletePost('+post.id+')">Delete</button</td>'+
						'</tr>');
				}
				
			}
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function postsHeader(){
	var table = $('#postsTable');
	table.find("tr:gt(1)").remove();
//	table.empty();
//	var tableHeader = '<tr class="myHeaders">'+
//			'<td><label for="sortBySelect">Sort by: </label>'+
//				'<select id="sortBySelect" onChange="sortPosts()">'+
//				'<option value="1">Most recent</option>'+
//				'<option value="2">Most popular</option>'+
//				'<option value="3">Most commented</option>'+
//				'</select>'+
//			'</td>'+
//
//			'<td><input type="text" placeholder="Search" id="searchField"><button  class="btn btn-default" onclick="sortPosts()">Submit</button></td>'+
//			'<td><button onclick="sortPosts()" class="btn btn-default">Refresh</button></td>'+
//			'<td><button onclick="showNewPostModal()" class="btn btn-danger">New</button></td>'+
//		'</tr>'+
//		'<tr class="myHeaders">'+
//			'<th>Title</th>'+
//			'<th>Author</th>'+
//			'<th>Date</th>'+
//			'<th>Likes</th>'+
//			'<th>Dislikes</th>'+
//		'</tr>'
//	table.append(tableHeader)
}
function refresh(){
	$('#sortBySelect').val(1);
	$('#searchField').val("");
	sortPosts()
}
function showNewPostModal(){
	if(currentUserId=="null"){
		loginModal();
		return;
	}
	$('#newPostModal').modal();
	$('#photoUploadCheck').prop('checked', false);
	$('#newPic').hide()
	
	$('#title').val("");
	$('#description').val("");
	$('#newPic').val("");
	$('#tagsField').val("");
}
function photoUploadChecker(){
	$('#newPic').toggle()
}
function saveNewPost(){
	var title = $('#title').val().trim();
	var desc = $('#description').val().trim();
	var photo = $('#newPic')[0].files[0];
	var tagsField = $('#tagsField').val().trim();
	var tags = tagsField.split("#");
	var checked = false;
	if($('#photoUploadCheck').prop('checked')){
		checked=true;
	}
	console.log(checked);
	if(checked == true && typeof photo == 'undefined'){
		alert("You must select a photo.");
		return;
	}
	console.log(photo);
	console.log(tags.length)
	if(title=="" || desc == "" || tags.length==0 || tags.length==1){
		alert("Sva polja moraju biti popunjena");
		return;
	}
	var data = new FormData();
	data.append('title',title);
	data.append('description',desc);
	data.append('user_id',parseInt(currentUserId));
	data.append('latitude',getRandomCordinate());
	data.append('longitude',getRandomCordinate());
	if(checked==true){
		data.append('photo',photo);
	}
	var id = 0;
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/posts/',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	console.log("reponse");
        	console.log("R1- "+response.id + " "+photo)
        	$('#newPostModal').modal('toggle');
        	//alert("Success post");
        	id = response.id;
            if(tags.length!=0 && tags.length!=1 && tags!=null){
	            for (i=1; i<tags.length; i++) {
	            	var dataTag = new FormData();
	            	dataTag.append('name',tags[i].toUpperCase().trim());
	            	createTag(dataTag,response.id);
	            }
            }

        	if(checked == true){
        		uploadPicPost(id,photo);
        	}
        	
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			$('#newPostModal').modal('toggle');
			alert(textStatus);
		}
    });
}
function createTag(data,postId){
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/tags/',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	console.log("tag success: "+ response.name+"id: "+response.id);
        	linkTagToPost(response.id,postId);
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function linkTagToPost(tagId,postId){
	console.log("tagId: "+tagId + " postId: "+postId);
	var myUrl = 'http://localhost:8080/api/posts/link_tp/'+postId+'/'+tagId
	console.log(myUrl)
	$.ajax({
		type: 'POST',
        url: myUrl,
		cache: false,
        success: function (response) {
        	console.log("link success");
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function sortPosts(){
	var sortBy = $('#sortBySelect').val();
	var input = $('#searchField').val().trim().toUpperCase();
	if(sortBy == 1){
		if(input!=""){
			loadPosts("orderbydate/"+input);
		}else{
			loadPosts("orderbydate");
		}
	}else if(sortBy == 2){
		if(input!=""){
			loadPosts("orderbypop/"+input);
		}else{
			loadPosts("orderbypop");
		}
	}else if(sortBy == 3){
		if(input!=""){
			loadPosts("orderbycommcount/"+input);
		}else{
			loadPosts("orderbycommcount");
		}
	}
}
function deletePost(n){
	console.log(n);
	$.ajax({
        url: 'http://localhost:8080/api/posts/'+n,
		type: 'DELETE',
        success: function (response) {
        	console.log("post delete success: ");
        	sortPosts();
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function editPost(n){
	console.log("editPost("+n+")");
	currentEditPostId = n;
	$.ajax({
        url: 'http://localhost:8080/api/posts/'+n,
        contentType: "application/json",
		type: 'GET',
        success: function (response) {
        	currentEditPost = response;
        	loadTags(n);
        	$('#titleEdit').val(response.title);
        	$('#descriptionEdit').val(response.description);
        	$('#titleEdit').val(response.title);
        	$('#editPic').hide();
        	$('#picUploadCheck').prop('checked', false);
        	$('#editPostModal').modal();
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function loadTags(n){
	var tempUrl = "http://localhost:8080/api/posts/tag/"+n;
	console.log(tempUrl);
	var tempString = "";
	$.ajax({
        url: tempUrl,
        dataType: 'json',
		cache: false,
        success: function (response) {
			for(var i=0; i<response.length; i++) {
				tag = response[i];
				tempString += "#"+tag.name;
			}
			tagsBeforeEdit=tempString;
			$('#tagsFieldEdit').val(tempString);
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function picUploadChecker(){
	$('#editPic').toggle();
}
function saveEditPost(){
	var title = $('#titleEdit').val().trim();
	var desc = $('#descriptionEdit').val().trim();
	var photo = $('#editPic')[0].files[0];
	var tagsField = $('#tagsFieldEdit').val().trim();
	var tags = tagsField.split("#");
	console.log(photo);
	console.log(tags.length)
	if(title=="" || desc == "" || tags.length==0 || tags.length==1){
		alert("All fields must be filled.");
		return;
	}
	var checked = false;
	if($('#picUploadCheck').is(":checked")){
		checked=true;
	}
	if(typeof photo == 'undefined' && checked==true){
		alert("Picture must be uploaded, or uncheck checkbox.");
		return;
	}
	if(checked==true){
		uploadPicPost(currentEditPostId,photo);
	}
	console.log("title: "+title+" description: "+desc);
	var data = {
			'title':title,
			'description':desc,
			'likes':currentEditPost.likes,
			'likes':currentEditPost.likes,
			'dislikes':currentEditPost.dislikes,
			'longitude':currentEditPost.longitude,
			'latitude':currentEditPost.latitude,
			'date':currentEditPost.date,
			'user':currentEditPost.user,
	}
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/posts/'+currentEditPostId,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	$('#editPostModal').modal('toggle');
        	if(tagsBeforeEdit.toUpperCase()!=tagsField.toUpperCase()){
        		removeTags(currentEditPostId);
            	//alert("Success post");
        	}
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			$('#editPostModal').modal('toggle');
			alert(textStatus);
		}
    });
}
function tagsFactory(postId){
	tags =  $('#tagsFieldEdit').val().trim().split("#");
	console.log(tags);
	if(tags.length!=0 && tags.length!=1 && tags!=null){
		console.log("123");
        for (i=1; i<tags.length; i++) {
        	var dataTag = new FormData();
        	dataTag.append('name',tags[i].toUpperCase().trim());
        	console.log("createTag");
        	createTag(dataTag,postId);
        }
    }
}
function removeTags(n){
	$.ajax({
        url: 'http://localhost:8080/api/posts/remove_tags/'+n,
		type: 'DELETE',
        success: function (response) {
        	tagsFactory(n);
        	sortPosts();
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function uploadPicPost(n,photo){
	console.log(n+" "+photo)
	var data = new FormData();
	data.append("id",n)
	data.append("photo",photo)
	
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/posts/upload_photo',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	console.log("Pic post upload success.");
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
