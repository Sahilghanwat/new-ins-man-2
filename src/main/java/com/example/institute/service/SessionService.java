package com.example.institute.service;
import jakarta.servlet.http.HttpServletRequest; import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component; import org.springframework.http.HttpStatus; import org.springframework.web.server.ResponseStatusException;
@Component
public class SessionService {
 public static final String USER_ID="userId", ROLE="role", EMAIL="email";
 public HttpSession create(HttpServletRequest request, Long id, String email, String role){
  HttpSession old=request.getSession(false); if(old!=null) old.invalidate();
  HttpSession session=request.getSession(true); session.setAttribute(USER_ID,id); session.setAttribute(EMAIL,email); session.setAttribute(ROLE,role); return session;
 }
 public Long requireId(HttpServletRequest request,String expectedRole){
  HttpSession session=request.getSession(false);
  if(session==null || session.getAttribute(USER_ID)==null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please log in first.");
  if(!expectedRole.equals(session.getAttribute(ROLE))) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You do not have permission to perform this action.");
  return (Long)session.getAttribute(USER_ID);
 }
 public void requireAdmin(HttpServletRequest request){ HttpSession session=request.getSession(false); if(session==null || !"ADMIN".equals(session.getAttribute(ROLE))) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Admin login is required."); }
 public String role(HttpServletRequest request){HttpSession s=request.getSession(false); return s==null?null:(String)s.getAttribute(ROLE);}
}
