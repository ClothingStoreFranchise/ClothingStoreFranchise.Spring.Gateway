package clothingstorefranchise.spring.gateway.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;

import clothingstorefranchise.spring.common.constants.Rol;
import clothingstorefranchise.spring.common.security.config.JwtConfiguration;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.Arrays;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
    protected final JwtConfiguration jwtConfiguration;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().configurationSource(request -> {	
                	CorsConfiguration cors = new CorsConfiguration();                 
                    cors.setAllowedMethods(
                      Arrays.asList( 
                        HttpMethod.GET.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.DELETE.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.OPTIONS.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.HEAD.name()
                   ));   
                   cors.applyPermitDefaultValues();           
                   return cors;   
               })
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .authorizeRequests()
                .antMatchers(jwtConfiguration.getLoginUrl(), "/**/swagger-ui.html").permitAll()
                .antMatchers(HttpMethod.GET, "/**/swagger-resources/**", "/**/webjars/springfox-swagger-ui/**", "/**/v2/api-docs/**").permitAll()
                
                .antMatchers("/auth/user/**").permitAll()     
                
                .antMatchers(HttpMethod.GET,"/catalog/**").permitAll()
                .antMatchers(HttpMethod.POST,"/catalog/**").hasRole(Rol.ADMIN)
                .antMatchers(HttpMethod.PUT,"/catalog/**").hasRole(Rol.ADMIN)
                .antMatchers(HttpMethod.DELETE,"/catalog/**").hasRole(Rol.ADMIN)
                
                .antMatchers("/inventory/warehouses/**").hasAnyRole(Rol.WAREHOUSE_EMPLOYEE,Rol.ADMIN)
                .antMatchers("/inventory/shops/**").hasAnyRole(Rol.SHOP_EMPLOYEE,Rol.ADMIN)
                .antMatchers("/inventory/products/**/stocks-without-warehouses").permitAll()
                .antMatchers("/inventory/products/**").hasRole(Rol.ADMIN)
                
                .antMatchers(HttpMethod.POST,"/customers/customers/**").permitAll()
                .antMatchers("/customers/customers/**").hasRole(Rol.CUSTOMER)
                .antMatchers("/customers/cart/**").permitAll()
                
                .antMatchers("/employees/warehouse-employees/**").hasAnyRole(Rol.ADMIN, Rol.WAREHOUSE_EMPLOYEE)
                .antMatchers("/employees/shop-employees/**").hasAnyRole(Rol.ADMIN, Rol.SHOP_EMPLOYEE)
                .antMatchers("/employees/warehouses/**").hasRole(Rol.ADMIN)
                .antMatchers("/employees/shops/**").hasRole(Rol.ADMIN)
                
                .antMatchers("/sales/orders/**").hasRole(Rol.CUSTOMER)
                .antMatchers("/sales/order-products/**").hasAnyRole(Rol.ADMIN, Rol.WAREHOUSE_EMPLOYEE)
                //.antMatchers(HttpMethod.GET, "/auth/user/**").hasAnyRole(Rol.CUSTOMER,Rol.ADMIN)
                .anyRequest().authenticated();
    }
}
