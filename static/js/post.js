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
								'<h4 id="commentTitle">'+comment.title+'</h4>'+
								'<p id="commentText">'+comment.description+'</p>'+
								'<label for="commentlike" id="likeCommLabel">'+comment.likes+'</label>'+
								'<button id="commentlike" class="btn btn-default">Like</button>'+
								'<label for="commentdislike" id="dislikeCommLabel">'+comment.dislikes+'</label>'+
								'<button id="commentdislike" class="btn btn-default">Dislike</button>'+
								'<br>'+
								'<button id="commentedit" class="btn btn-default">Edit</button>'+
								'<button id="commentdelete" class="btn btn-default">Delete</button>'+
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
				}else{
					tempString += ", #"+tag.name
				}
			}

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
function sortChange(){
	var sortBy = $('#sortBySelect').val();
	console.log(sortBy);
	if(sortBy == 1){
		loadComments("orderdate");
	}else if(sortBy == 2){
		loadComments("orderpop")
	}
}