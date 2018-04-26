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

import dao.UserDao;
import exceptions.RegisterException;
import exceptions.UserException;
import user.User;


@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDao uDao;
       

    public RegisterServlet() throws ClassNotFoundException, SQLException {
       uDao = new UserDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");

		
        if(!(password.equals(password2))){
            request.setAttribute("error", "passwords missmatch");
            request.getRequestDispatcher("RegistrationForm.jsp").forward(request,response);
            return;
        }
		try {
			if(uDao.checkIfUsernameExistsInDB(username)) {
	            request.setAttribute("error", "Username is taken, please enter a different username");
	            request.getRequestDispatcher("RegistrationForm.jsp").forward(request,response);
	            return;
			}
			if(uDao.checkIfEmailExistsInDB(email)) {
			    request.setAttribute("error", "Email is already used, please enter a different email");
	            request.getRequestDispatcher("RegistrationForm.jsp").forward(request,response);
	            return;
			}
			User u = new User(0,username,password,email);
			int userId = uDao.register(u);
			session.setAttribute("USER", username);
			session.setAttribute("USERID", userId);
			session.setMaxInactiveInterval(120);
			response.sendRedirect("MainServlet");
		}
		catch(RegisterException | UserException | SQLException e) {
			e.printStackTrace();
			response.sendRedirect("RegistrationForm.jsp");
		}
	}

}
