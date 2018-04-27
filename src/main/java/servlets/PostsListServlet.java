package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import exceptions.PostException;
import com.friendBook.model.Post;
import com.friendBook.model.PostDao;

@WebServlet("/PostsListServlet")
public class PostsListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private PostDao postDao;
    public PostsListServlet() throws ClassNotFoundException, SQLException {
    	postDao = new PostDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession(false) == null || 
				request.getSession().getAttribute("USER") == null) {
			response.sendRedirect("./");
			return;
		}
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		int userId = (int) session.getAttribute("USERID");
		
		List<Post> postsList;
		try {
			postsList = postDao.extractPosts(userId);
			request.setAttribute("postsList", postsList);		
			RequestDispatcher rd = request.getRequestDispatcher("PostList.jsp");
			rd.include(request, response);
		} catch (PostException e) {
			e.printStackTrace();
			response.sendRedirect("ErrorForm.html");
		}
	}

}
