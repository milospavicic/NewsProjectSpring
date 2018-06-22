var tagsBeforeEdit = "";
var currentCommentEdit =null;
var currentPost = null;
var postId = 0;
$(document).ready(function() {
	postId = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(postId);
	loadPage(postId);
	loadComments("orderdate");
	loadTags(postId);
});
function loadPage(postId){
	var tempUrl = "http://localhost:8080/api/posts/"+postId;
	console.log(tempUrl);
	$.ajax({
        url: tempUrl,
        dataType: 'json',
		cache: false,
        success: function (response) {
        	var post = response;
        	currentPost = post;
        	editPostFill(post);
        	$('#postTitle').text(post.title);
        	$('#postDesc').text(post.description);
        	$('#postAuthor').text("Author: "+post.user.name);
        	$('#postDate').text("Date posted: "+formatDate(post.date));
        	$('#likeLabel').text(post.likes);
        	$('#dislikeLabel').text(post.dislikes);
        	console.log("post.photo "+post.photo);
            if(post.photo==null){
            	$('#postPic').hide();
            }else{
            	console.log("putPic");
            	$('#postPic').attr('src','data:image/gif;base64,'+post.photo);
            }
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function loadComments(orderWay){
	var tempUrl = "http://localhost:8080/api/comments/"+orderWay+"/"+postId;
	console.log(tempUrl);
	$.ajax({
        url: tempUrl,
        dataType: 'json',
		cache: false,
        success: function (response) {
        	commentsHeader();
			var table = $('#commentsDiv');
			for(var i=0; i<response.length; i++) {
				comment = response[i];
				var tempDate = formatDate(comment.date);
				table.append('<div class="comment">'+
								'<img alt="commentPic" id="commentPic">'+
								'<label for="commentPic" id="usernameLabel">'+comment.user.name+'</label>'+
								'<p id="commentDate">'+'Date posted: '+tempDate+'</p>'+
								'<h4 id="commentTitle'+comment.id+'">'+comment.title+'</h4>'+
								'<p id="commentDescription'+comment.id+'">'+comment.description+'</p>'+
								'<label for="commentlike" id="likeCommLabel'+comment.id+'">'+comment.likes+'</label>'+
								'<button id="commentlike'+comment.id+'" class="btn btn-default" onclick="likeComment('+comment.id+')">Like</button>'+
								'<label for="commentdislike" id="dislikeCommLabel'+comment.id+'">'+comment.dislikes+'</label>'+
								'<button id="commentdislike'+comment.id+'" class="btn btn-default"  onclick="dislikeComment('+comment.id+')">Dislike</button>'+
								'<br>'+
								'<button id="commentedit" class="btn btn-default" onclick="editComment('+comment.id+')">Edit</button>'+
								'<button id="commentdelete" class="btn btn-default" onclick="deleteComment('+comment.id+')">Delete</button>'+
								'<hr id="shortline">'+
								'<br>'+
							'</div>');
			}
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function loadTags(postId){
	var tempUrl = "http://localhost:8080/api/posts/tag/"+postId;
	console.log(tempUrl);
	var tempString = "Tags: ";
	$.ajax({
        url: tempUrl,
        dataType: 'json',
		cache: false,
        success: function (response) {
        	console.log("123");
			for(var i=0; i<response.length; i++) {
				tag = response[i];
				if(i==0){
					tempString += "#"+tag.name;
					tagsBeforeEdit += "#"+tag.name;
				}else{
					tempString += ", #"+tag.name
					tagsBeforeEdit += "#"+tag.name;
				}
			}
			
			$('#tagsFieldEdit').val(tagsBeforeEdit);
			$('#tags').text(tempString);
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function commentsHeader(){
	var table = $('#commentsDiv');
	table.empty();
	table.append('<hr>'+
			'<h3>Comments:</h3>'+
			'<button class="btn btn-danger" onclick="newCommentModal()">New Comment</button>');
}
function newCommentModal(){
	$('#newComment').modal();
}
function saveNewComment(){
	var title = $('#title').val().trim();
	var desc = $('#description').val().trim();
	if(title=="" || desc == ""){
		alert("Sva polja moraju biti popunjena");
		return;
	}	
	var data = new FormData();
	data.append('title',title);
	data.append('description',desc);
	data.append('user_id',1);
	data.append('post_id',postId);
	
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/comments/',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	$('#newComment').modal('toggle');
        	alert("Success comm");
        	
        	sortChange()
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
			$('#newComment').modal('toggle');
		}
    });
}
function editComment(n){
	$.ajax({
		type: 'GET',
        url: 'http://localhost:8080/api/comments/'+n,
		cache: false,
        success: function (response) {
        	currentCommentEdit=response;
        	$('#titleEditComment').val(response.title);
        	$('#descriptionEditComment').val(response.description);
        	$('#editComment').modal('toggle');
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
			$('#editComment').modal('toggle');
		}
    });
}
function saveEditComment(){

	var title =  $('#titleEditComment').val();
	var desc = $('#descriptionEditComment').val();
	if(title=="" || desc== ""){
		alert("All fields must be filled")
		return;
	}
	var data = {
			'title':title,
			'description':desc,
			'likes':currentCommentEdit.likes,
			'dislikes':currentCommentEdit.dislikes,
	}
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
        url: 'http://localhost:8080/api/comments/'+currentCommentEdit.id,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	$('#titleEditComment').val(response.title);
        	$('#descriptionEditComment').val(response.description);
        	$('#editComment').modal('toggle');
        	sortChange();
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
			$('#editComment').modal('toggle');
		}
    });
}
function sortChange(){
	var sortBy = $('#sortBySelect').val();
	console.log(sortBy);
	if(sortBy == 1){
		loadComments("orderdate");
	}else if(sortBy == 2){
		loadComments("orderpop")
	}
}
function editModal(){
	$('#editPostModal').modal();
}

function createTag(data){
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/tags/',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	console.log("tag success: "+ response.name+"id: "+response.id);
        	linkTagToPost(response.id);
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function linkTagToPost(tagId){
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

function editPostFill(response){
	$('#titleEdit').val(response.title);
	$('#descriptionEdit').val(response.description);
	$('#titleEdit').val(response.title);
	$('#editPic').hide();
	$('#picUploadCheck').prop('checked', false);
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
		uploadPic(photo);
	}
	console.log("title: "+title+" description: "+desc);
	var data = {
			'title':title,
			'description':desc,
			'likes':currentPost.likes,
			'likes':currentPost.likes,
			'dislikes':currentPost.dislikes,
			'longitude':currentPost.longitude,
			'latitude':currentPost.latitude,
			'date':currentPost.date,
			'user':currentPost.user,
	}
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/posts/'+postId,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	$('#editPostModal').modal('toggle');
        	if(tagsBeforeEdit.toUpperCase()!=tagsField.toUpperCase()){
        		removeTags();
            	//alert("Success post");
                if(tags.length!=0 && tags.length!=1 && tags!=null){
    	            for (i=1; i<tags.length; i++) {
    	            	var dataTag = new FormData();
    	            	dataTag.append('name',tags[i].toUpperCase());
    	            	createTag(dataTag);
    	            }
                }
                
        	}
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			$('#editPostModal').modal('toggle');
			alert(textStatus);
		}
    });
}
function removeTags(){
	$.ajax({
        url: 'http://localhost:8080/api/posts/remove_tags/'+postId,
		type: 'DELETE',
        success: function (response) {
        	
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function uploadPic(photo){
	var data = new FormData();
	data.append("id",postId)
	data.append("photo",photo)
	
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/posts/upload_photo',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	console.log("Pic upload success: ");
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function deletePost(){
	$.ajax({
        url: 'http://localhost:8080/api/posts/'+postId,
		type: 'DELETE',
        success: function (response) {
        	window.location.href = "posts.html";
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function deleteComment(n){
	$.ajax({
        url: 'http://localhost:8080/api/comments/'+n,
		type: 'DELETE',
        success: function (response) {
        	sortChange();
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}
function likePost(){
	var tempLike = parseInt($('#likeLabel').text());
	var tempDis = parseInt($('#dislikeLabel').text());
	if($('#dislike').prop('disabled')){
		var data = {
				'title':currentPost.title,
				'description':currentPost.description,
				'likes':tempLike+1,
				'dislikes':tempDis-1,
				'longitude':currentPost.longitude,
				'latitude':currentPost.latitude,
				'date':currentPost.date,
				'user':currentPost.user,
		}
	}else{
		var data = {
				'title':currentPost.title,
				'description':currentPost.description,
				'likes':tempLike+1,
				'dislikes':tempDis,
				'longitude':currentPost.longitude,
				'latitude':currentPost.latitude,
				'date':currentPost.date,
				'user':currentPost.user,
		}
	}
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/posts/'+postId,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	if($('#dislike').prop('disabled')){
        		$("#dislike").prop("disabled",false);
        	}
        	
        	post = response;
        	$("#like").prop("disabled",true);
        	$('#likeLabel').text(post.likes);
        	$('#dislikeLabel').text(post.dislikes);
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			$('#editPostModal').modal('toggle');
			alert(textStatus);
		}
    });
}
function dislikePost(){
	var tempLike = parseInt($('#likeLabel').text());
	var tempDis = parseInt($('#dislikeLabel').text());
	if($('#like').prop('disabled')){
		var data = {
				'title':currentPost.title,
				'description':currentPost.description,
				'likes':tempLike-1,
				'dislikes':tempDis+1,
				'longitude':currentPost.longitude,
				'latitude':currentPost.latitude,
				'date':currentPost.date,
				'user':currentPost.user,
		}
	}else{
		var data = {
				'title':currentPost.title,
				'description':currentPost.description,
				'likes':tempLike,
				'dislikes':tempDis+1,
				'longitude':currentPost.longitude,
				'latitude':currentPost.latitude,
				'date':currentPost.date,
				'user':currentPost.user,
		}
	}
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/posts/'+postId,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	if($('#like').prop('disabled')){
        		$("#like").prop("disabled",false);
        	}
        	
        	post = response;
        	$("#dislike").prop("disabled",true);
        	$('#likeLabel').text(post.likes);
        	$('#dislikeLabel').text(post.dislikes);
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			$('#editPostModal').modal('toggle');
			alert(textStatus);
		}
    });
}
function likeComment(n){
	console.log("likeComment "+n);
	var tempLike = parseInt($('#likeCommLabel'+n).text());
	var tempDis = parseInt($('#dislikeCommLabel'+n).text());
	var tempTitle = $('#commentTitle'+n).text();
	var tempDesc = $('#commentDescription'+n).text();
	if($('#commentdislike'+n).prop('disabled')){
		var data = {
				'title':tempTitle,
				'description':tempDesc,
				'likes':tempLike+1,
				'dislikes':tempDis-1
		}
	}else{
		var data = {
				'title':tempTitle,
				'description':tempDesc,
				'likes':tempLike+1,
				'dislikes':tempDis
		}
	}
	console.log("likes"+data.likes)
	console.log("dislikes"+data.dislikes)
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/comments/'+postId,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	if($('#commentdislike'+n).prop('disabled')){
        		$("#commentdislike"+n).prop("disabled",false);
        	}
        	
        	post = response;
        	$("#commentlike"+n).prop("disabled",true);
        	$('#likeCommLabel'+n).text(post.likes);
        	$('#dislikeCommLabel'+n).text(post.dislikes);
        },
		error: function (jqXHR, textStatus, errorThrown) { 
			alert(textStatus);
		}
    });
}
function dislikeComment(n){
	console.log("likeComment "+n);
	var tempLike = parseInt($('#likeCommLabel'+n).text());
	var tempDis = parseInt($('#dislikeCommLabel'+n).text());
	var tempTitle = $('#commentTitle'+n).text();
	var tempDesc = $('#commentDescription'+n).text();
	if($('#commentlike'+n).prop('disabled')){
		var data = {
				'title':tempTitle,
				'description':tempDesc,
				'likes':tempLike-1,
				'dislikes':tempDis+1
		}
	}else{
		var data = {
				'title':tempTitle,
				'description':tempDesc,
				'likes':tempLike,
				'dislikes':tempDis+1
		}
	}
	console.log("likes"+data.likes)
	console.log("dislikes"+data.dislikes)
	$.ajax({
		type: 'PUT',
        contentType: 'application/json',
        url: 'http://localhost:8080/api/comments/'+postId,
        data: JSON.stringify(data),
        dataType: 'json',
		cache: false,
		processData: false,
        success: function (response) {
        	if($('#commentlike'+n).prop('disabled')){
        		$("#commentlike"+n).prop("disabled",false);
        	}
        	
        	post = response;
        	$("#commentdislike"+n).prop("disabled",true);
        	$('#likeCommLabel'+n).text(post.likes);
        	$('#dislikeCommLabel'+n).text(post.dislikes);
        },
		error: function (jqXHR, textStatus, errorThrown) { 
			alert(textStatus);
		}
    });
}
