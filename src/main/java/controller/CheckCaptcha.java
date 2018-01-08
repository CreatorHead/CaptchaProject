package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import captchas.CaptchasDotNet;

@WebServlet("/check")
public class CheckCaptcha extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// Construct the captchas object
		// Use same settings as in query.jsp
		CaptchasDotNet captchas = new captchas.CaptchasDotNet(
				request.getSession(true),     // Ensure session
				"demo",                       // client
				"secret"                      // secret
				);
		// Read the form values
		String message  = request.getParameter("message");
		String password = request.getParameter("password");

		// Check captcha
		String body;
		switch (captchas.check(password)) {
		case 's':
			body = "Session seems to be timed out or broken. ";
			body += "Please try again or report error to administrator.";
			break;
		case 'm':
			body = "Every CAPTCHA can only be used once. ";
			body += "The current CAPTCHA has already been used. ";
			body += "Please use back button and reload";
			break;
		case 'w':
			body = "You entered the wrong captcha. ";
			body += "Please use back button and try again. ";
			break;
		default:
			body = "Your message was verified to be entered by a Human and Your Message: "+message;
			break;
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("Message.jsp");
		request.setAttribute("msg", body);
		rd.forward(request, resp);
	}
}
