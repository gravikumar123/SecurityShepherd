package servlets.module.challenge;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import utils.Validate;
import dbProcs.Getter;
import dbProcs.Setter;

/**
 * Cross Site Request Forgery Challenge Target Two - Does not return Result key
 * <br/><br/>
 * This file is part of the Security Shepherd Project.
 * 
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 * 
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>. 
 * @author Mark Denihan
 *
 */
public class CsrfChallengeTargetTwo extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private static org.apache.log4j.Logger log = Logger.getLogger(CsrfChallengeTargetTwo.class);
	/**
	 * CSRF vulnerable function that can be used by users to force other users to mark their CSRF challenge Two as complete.
	 * @param userId User identifier to be incremented
	 */
	public void doPost (HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException
	{
		log.debug("Cross-SiteForegery Challenge Two Target Servlet");
		PrintWriter out = response.getWriter();  
		out.print(getServletInfo());
		try
		{
			boolean result = false;
			HttpSession ses = request.getSession(true);
			if(Validate.validateSession(ses))
			{
				String plusId = request.getParameter("userId");
				log.debug("User Submitted - " + plusId);
				String userId = (String)ses.getAttribute("userStamp");
				if(!userId.equals(plusId))
				{
					String ApplicationRoot = getServletContext().getRealPath("");
					String userName = (String)ses.getAttribute("userName");
					String attackerName = Getter.getUserName(ApplicationRoot, plusId);
					if(attackerName != null)
					{
						log.debug(userName + " is been CSRF'd by " + attackerName);
						
						log.debug("Attemping to Increment ");
						String moduleHash = z311736498a13604705d608fb3171ebf49bc18753b0ec34b8dff5e4f9147eb5e.class.getSimpleName();
						String moduleId = Getter.getModuleIdFromHash(ApplicationRoot, moduleHash);
						result = Setter.updateCsrfCounter(ApplicationRoot, moduleId, plusId);
					}
					else
					{
						log.error("UserId '" + plusId + "' could not be found.");
					}
				}
				
				if(result)
				{
					out.write("Increment Successful");
				}
				else
				{
					out.write("Increment Failed");
				}
			}
			else
			{
				out.write("No Session Detected");
			}
		}
		catch(Exception e)
		{
			out.write("An Error Occured! You must be getting funky!");
			log.fatal("Cross Site Request Forgery Target Challenge 2 - " + e.toString());
		}
	}
}
