package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.friendBook.model.User;
import com.friendBook.model.UserDao;

import exceptions.UserException;


@WebServlet("/SearchResult")
public class SearchResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao uDao;

    public SearchResult() throws ClassNotFoundException, SQLException {
        uDao  = new UserDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<User> users = new LinkedList<>();
		String name = request.getParameter("search");
		try {
			users = uDao.getUsersByString(name);
			request.setAttribute("users", users);
			RequestDispatcher dispatcher = request.getRequestDispatcher("SearchResult.jsp");
			dispatcher.forward(request, response);
		} catch (UserException e) {
			e.printStackTrace();
		}
		
	}


}
