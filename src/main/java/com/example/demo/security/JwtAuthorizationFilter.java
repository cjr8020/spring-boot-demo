package com.example.demo.security;

import static com.example.demo.security.SecurityConstants.AUTHZ_HEADER_STRING;
import static com.example.demo.security.SecurityConstants.BEARER_TOKEN_PREFIX;
import static com.example.demo.security.SecurityConstants.SECRET;

import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private static final Logger log = LoggerFactory.getLogger(JwtAuthorizationFilter.class);

  public JwtAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
      HttpServletResponse res,
      FilterChain chain) throws IOException, ServletException {
    log.info("doFilterInternal");

    String header = req.getHeader(AUTHZ_HEADER_STRING);

    if (header == null || !header.startsWith(BEARER_TOKEN_PREFIX)) {
      res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied");
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    log.info("getAuthentication");
    String token = request.getHeader(AUTHZ_HEADER_STRING);
    if (token != null) {
      // parse the token.
      String user = Jwts.parser()
          .setSigningKey(SECRET.getBytes())
          .parseClaimsJws(token.replace(BEARER_TOKEN_PREFIX, ""))
          .getBody()
          .getSubject();

      if (user != null) {
        String requstedResource = (request.getRequestURI() != null)
            ? request.getRequestURI()
            : "";
        log.info("User {} authorized for {}.", user, requstedResource);
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }
}
