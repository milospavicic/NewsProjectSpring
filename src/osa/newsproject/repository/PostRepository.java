package osa.newsproject.repository;

import java.util.List;

import javax.ws.rs.PathParam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import osa.newsproject.entity.Post;

public interface PostRepository extends JpaRepository<Post, Integer>{
	List<Post> findByTags_Id(Integer tagId);

	List<Post> findAllByOrderByDateDesc();
	
	@Query(value="SELECT p.* FROM posts AS p INNER JOIN users AS u ON p.user_id=u.user_id "
			+ " LEFT OUTER JOIN post_tags AS t ON p.post_id=t.post_id LEFT OUTER JOIN tags AS ta ON"
			+ " ta.tag_id = t.tag_id WHERE ta.name like ? OR u.name like ?  GROUP BY p.post_id ORDER BY p.date DESC",nativeQuery=true)
	List<Post> findAllByOrderByDateAndSearch(String param,String param1);
	
	@Query(value="SELECT * FROM posts AS p ORDER BY (p.likes-p.dislikes) DESC",nativeQuery=true)
	List<Post> findAllByOrderByPopularity();
	
	@Query(value="SELECT p.* FROM posts AS p INNER JOIN users AS u ON p.user_id=u.user_id "
			+ " LEFT OUTER JOIN post_tags AS t ON p.post_id=t.post_id LEFT OUTER JOIN tags AS ta ON"
			+ " ta.tag_id = t.tag_id WHERE ta.name like ? OR u.name like ?  GROUP BY p.post_id ORDER BY (p.likes-p.dislikes) DESC",nativeQuery=true)
	List<Post> findAllByOrderByPopularityAndSearch(String param,String param1);
	
	@Query(value="SELECT p.* FROM posts AS p LEFT OUTER JOIN comments AS c ON p.post_id = c.post_id GROUP BY p.post_id ORDER BY COUNT(DISTINCT c.comment_id) DESC",nativeQuery=true)
	List<Post> findAllByCommentsCount();
	
	@Query(value="SELECT p.* FROM posts AS p INNER JOIN users AS u ON p.user_id=u.user_id LEFT OUTER JOIN comments AS c ON"
			+ " p.post_id = c.post_id LEFT OUTER JOIN post_tags AS t ON p.post_id=t.post_id LEFT OUTER JOIN tags AS ta ON"
			+ " ta.tag_id = t.tag_id WHERE ta.name like ? OR u.name like ?  GROUP BY p.post_id ORDER BY COUNT(DISTINCT c.comment_id) DESC",nativeQuery=true)
	List<Post> findAllByCommentsCountAndSearch(String param,String param1);
}
