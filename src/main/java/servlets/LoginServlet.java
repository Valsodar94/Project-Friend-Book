package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.friendBook.model.UserDao;

import exceptions.LoginException;
import exceptions.UserException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao uDao;
       
    public LoginServlet() throws ClassNotFoundException, SQLException {
        uDao = new UserDao();
    }

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		try {
			int userId = uDao.login(username, password);
			session.setAttribute("USER", username);
			session.setAttribute("USERID", userId);
			session.setAttribute("nmb", uDao.getUsersByString("r").size());
			session.setMaxInactiveInterval(120);
			resp.sendRedirect("MainServlet");
		}
		catch (LoginException e) {
			resp.sendRedirect("InvalidLogin.html");
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
