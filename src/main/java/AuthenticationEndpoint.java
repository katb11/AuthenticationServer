
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/* this class authenticates that the user is a
   valid member and allowed to log in
 */
@WebServlet("/authenticate")
public class AuthenticationEndpoint extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* for traditional login */
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String hashedPassword = Hash.getHash(password);

        User user = null;
        try {
            // traditional login
            if (email != null && hashedPassword != null) {
                user = UserDao.fetchUser(email, hashedPassword);
            }

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (user == null) {
                response.getWriter().write("access denied");
            } else {
                HashMap<String, Object> claims = new HashMap<>();
                claims.put("username", user.getEmail());
                claims.put("accountID", user.getId());

                // about 8 hours until expiration
                response.getWriter().write(JWT.createJWT("Auth API", claims, 15000));
            }
        } catch (Exception e) {
            response.getWriter().write("unexpected server error");
        }
    }
}