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

import dao.PostDao;
import dao.UserDao;
import exceptions.PostException;
import exceptions.RegisterException;
import exceptions.UserException;
import post.Post;
import user.User;

@WebServlet("/PostServlet")
public class PostServlet extends HttpServlet{
	private static final long serialVersionUID = 4596326868090054671L;
	
	private PostDao postDao; 
    public PostServlet() throws ClassNotFoundException, SQLException {
       postDao = new PostDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getSession(false) == null || 
				request.getSession().getAttribute("USER") == null) {
			response.sendRedirect("./");
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String text = request.getParameter("postText");
		String pictureUrl = request.getParameter("pictureUrl");
		String picture = "";
		
		if(pictureUrl.length() > 0) {
			int symb = pictureUrl.length()-1;
			for(;symb>0;symb--) {
				if(pictureUrl.charAt(symb) == '/' || pictureUrl.charAt(symb) == '\\') {
					symb++;
					break;
				}
			}
			picture = pictureUrl.substring(symb);
		}		
		
		try {
		if(!((text == null || text.length() == 0) && (picture == null || picture.length() == 0))) {			
			HttpSession session = request.getSession();			
			int userId = (int) session.getAttribute("USERID");			
			Post p = new Post(0, userId);
			p.setText(text.trim());
			p.setPictureUrl(picture.trim());
			postDao.publish(p);	
			response.sendRedirect("ProfileServlet");
		}			
			doGet(request, response);
		}
		catch(PostException e) {
			e.printStackTrace();
			response.sendRedirect("ErrorForm.html");
		}

	}

}
