$(document).ready(function() {
	loadPosts("orderbydate");
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
				console.log(tempDate);
				table.append('<tr class="data">'+
								'<td><a href="post.html?id='+post.id+'">'+post.title+'</a></td>'+
								'<td>'+post.user.name+'</td>'+
								'<td>'+tempDate+'</td>'+
								'<td>'+post.likes+'</td>'+
								'<td>'+post.dislikes+'</td>'+
								'<td><button onclick="deletePost('+post.id+')">Delete</button</td>'+
							'</tr>');
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
//			'<td><input type="text" placeholder="Search" id="searchField"><button  class="btn btn-default" onclick="search()">Submit</button></td>'+
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
function showNewPostModal(){
	$('#newPostModal').modal();
}
function saveNewPost(){
	var title = $('#title').val();
	var desc = $('#description').val();
	var photo = $('#newPic')[0].files[0];
	var tagsField = $('#tagsField').val();
	var tags = tagsField.split("#");
	console.log(photo);
	console.log(tags.length)
	if(title=="" || desc == "" || typeof photo == 'undefined' || tags.length==0 || tags.length==1){
		alert("Sva polja moraju biti popunjena");
		return;
																																																							}
	var data = new FormData();
	data.append('title',title);
	data.append('description',desc);
	data.append('user_id',1);
	data.append('photo',photo);
	
	$.ajax({
		type: 'POST',
        url: 'http://localhost:8080/api/posts/',
        contentType: false,
        data: data,
		cache: false,
		processData: false,
        success: function (response) {
        	alert("Success post");
            if(tags.length!=0 && tags!=null){
	            for (i=1; i<tags.length; i++) {
	            	var dataTag = new FormData();
	            	dataTag.append('name',tags[i]);
	            	createTag(dataTag,response.id);
	            }
            }
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
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
	console.log(sortBy);
	if(sortBy == 1){
		loadPosts("orderbydate");
	}else if(sortBy == 2){
		loadPosts("orderbypop")
	}else if(sortBy == 3){
		loadPosts("orderbycommcount");
	}
}
function search(){
	var input = $('#searchField').val().toUpperCase();
	$("tr").each(function(){
		  if($(this).html().toUpperCase().includes(input)){
		    $(this).show();
		  }
		  else{
			  if($(this).hasClass( "data" )){
					$(this).hide();
			  }
		  }
	});
}
function deletePost(n){
	console.log(n);
	$.ajax({
        url: 'http://localhost:8080/api/posts/'+n,
        contentType: "application/json",
		type: 'DELETE',
        success: function (response) {
        	console.log("post delete success: ");
           
        },
		error: function (jqXHR, textStatus, errorThrown) {  
			alert(textStatus);
		}
    });
}

