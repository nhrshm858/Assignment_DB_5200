package dataAccess;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import pkg.Actor;
import pkg.Comment;

public class CommentManager {
	// Let the name of the JNDI be "jdbc/movies"
	DataSource ds;

	public CommentManager() {
		try {
			Context jndi = new InitialContext();
			ds = (DataSource) jndi.lookup("java:comp/env/jdbc/movies");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	Connection connection = null;
	PreparedStatement statement = null;
	ResultSet results = null;

	public void createComment(Comment newComment) {
		String createCommentSql = "INSERT INTO COMMENT (id, comment, date,userId, movieId) VALUES (?, ?, ?, ?,?);";
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(createCommentSql);

			statement.setString(1, UUID.randomUUID().toString());
			statement.setString(2, newComment.getComment());
			Date date = new Date(newComment.getDate().getTime());
			statement.setDate(3, date);
			statement.setString(4, newComment.getUserId());
			statement.setString(5, newComment.getMovieId());
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<Comment> readAllComments() {
		String readAllCommentsSql = "SELECT * FROM COMMENT;";
		List<Comment> comments = new ArrayList<Comment>();
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(readAllCommentsSql);
			results = statement.executeQuery();
			while (results.next()) {
				Comment comment = new Comment();
				comment.setId(results.getString("id"));
				comment.setComment(results.getString("comment"));
				Date date = new Date(results.getDate("date").getTime());
				comment.setDate(date);
				comment.setUserId(results.getString("userId"));
				comment.setMovieId(results.getString("movieId"));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return comments;
	}

	public List<Comment> readAllCommentsForUsername(String username) {
		String readAllCommentsForUsernameSql = 
				"SELECT c.* FROM COMMENT c,USER u WHERE c.userId = u.Id AND u.username=?;";
		List<Comment> comments = new ArrayList<Comment>();
		try {
			connection = ds.getConnection();
			statement = connection
					.prepareStatement(readAllCommentsForUsernameSql);
			statement.setString(1, username);
			results = statement.executeQuery();
			while (results.next()) {
				Comment comment = new Comment();
				comment.setId(results.getString("id"));
				comment.setComment(results.getString("comment"));
				Date date = new Date(results.getDate("date").getTime());
				comment.setDate(date);
				comment.setUserId(results.getString("userId"));
				comment.setMovieId(results.getString("movieId"));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return comments;
	}

	public List<Comment> readAllCommentsForMovie(String movieId) {
		String readAllCommentsForMovieSql = 
				"SELECT * FROM COMMENT WHERE movieId=?;";
		List<Comment> comments = new ArrayList<Comment>();
		try {
			connection = ds.getConnection();
			statement = connection
					.prepareStatement(readAllCommentsForMovieSql);
			statement.setString(1, movieId);
			results = statement.executeQuery();
			while (results.next()) {
				Comment comment = new Comment();
				comment.setId(results.getString("id"));
				comment.setComment(results.getString("comment"));
				Date date = new Date(results.getDate("date").getTime());
				comment.setDate(date);
				comment.setUserId(results.getString("userId"));
				comment.setMovieId(results.getString("movieId"));
				comments.add(comment);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return comments;
	}

	public Comment readCommentForId(String commentId) {
		String readCommentForId = "SELECT * FROM COMMENT WHERE id=?;";
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(readCommentForId);
			statement.setString(1, commentId);
			results = statement.executeQuery();
			if (results.next()) {
				Comment comment = new Comment();
				comment.setId(results.getString("id"));
				comment.setComment(results.getString("comment"));
				Date date = new Date(results.getDate("date").getTime());
				comment.setDate(date);
				comment.setUserId(results.getString("userId"));
				comment.setMovieId(results.getString("movieId"));
				return comment;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void updateComment(String commentId, String newComment) {
		String updateCommentSql = "UPDATE COMMENT SET COMMENT=? WHERE id=?;";
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(updateCommentSql);
			statement.setString(1, newComment);
			statement.setString(2, commentId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteComment(String commentId) {
		String deleteCommentSql = "DELETE FROM COMMENT WHERE id=?";
		try {
			connection = ds.getConnection();
			statement = connection.prepareStatement(deleteCommentSql);
			statement.setString(1, commentId);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}