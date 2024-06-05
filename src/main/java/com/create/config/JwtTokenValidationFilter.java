package com.create.config;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader(JwtSecurityContext.JWT_HEADER);
		
		if(jwt != null) {
			try {
				jwt = jwt.substring(7);
				SecretKey key = Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
				
				String username = String.valueOf(claims.get("username"));
				String authorities = (String)claims.get("authorities");
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, auths);
				
				SecurityContextHolder.getContext().setAuthentication(auth);	
			}catch (Exception e) {
				throw new BadCredentialsException("Invalid Token recieved...error");
				
			}
		}
		filterChain.doFilter(request, response);
		
	}
	
	protected boolean shouldNotFilter(HttpServletRequest  req)throws ServletException {
		return req.getServletPath().equals("/api/auth/users/");
	}
//	Authentication authorization = SecurityContextHolder.getContext().getAuthentication();
//		if(authorization!=null) {
//		SecretKey key = Keys.hmacShaKeyFor(JwtSecurityContext.JWT_KEY.getBytes());
//		
//		String jwt = 
//				Jwts.builder()
//				.setIssuer("Kishan sahu")
//				.setIssuedAt(new Date())
//				.setExpiration(new Date(new Date().getTime() + 86400000))
//				.claim("authorities", populateAuthorities(authorization.getAuthorities()))
//				.claim("email", authorization.getName())
//				
//				.signWith(key).compact();
//		}
//		
//	}
//
//	private Object populateAuthorities(Collection<? extends GrantedAuthority> collection) {
//		Set<String> authoritiesSet = new HashSet<>();
//		for(GrantedAuthority authority : collection) {
//			authoritiesSet.add(authority.getAuthority());
//		}
//		return String.join(",", authoritiesSet);
	


}
