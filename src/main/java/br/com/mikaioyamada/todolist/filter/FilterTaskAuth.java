package br.com.mikaioyamada.todolist.filter;
//servlet é base de quaqluer framework no java
import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.mikaioyamada.todolist.user.IUserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

       var servletpath = request.getServletPath();
        if(servletpath.startsWith("/tasks/")){
            //pegar autenticação usuario e senha
            var authorization = request.getHeader("Authorization");


            var authEncoded = authorization.substring("Basic".length()).trim();
            byte[] authDecoded = Base64.getDecoder().decode(authEncoded);

            var authString = new String(authDecoded);
            System.out.println("Authorization");
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            System.out.println(username);
            System.out.println(password);

            //validar usuario
            var user = this.userRepository.findByUsername(username); // encontra o usuario
            if(user == null){
                response.sendError(401);
            }else{
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if (passwordVerify.verified){
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request,response);
                }else{
                    response.sendError(401);
                }

            }

        }else{
            filterChain.doFilter(request,response);
        }

        //segue o baile

    }
}
