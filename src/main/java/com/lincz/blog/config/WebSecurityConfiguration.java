package com.lincz.blog.config;

import com.lincz.blog.enums.AccountRolePermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.jdbc.JdbcOperationsSessionRepository;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled =true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private FindByIndexNameSessionRepository sessionRepository;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @Bean
    SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }


    //TODO 验证码
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutSuccessUrl("/").permitAll();
//                .formLogin()
//                .loginPage("/Login.html")
//                .failureUrl("/Login-Error.html")
//                .and()
//                .logout()
//                .logoutSuccessUrl("/Index.html")
        http
                .rememberMe()
                .rememberMeServices(rememberMeServices());
        http
                .authorizeRequests()
                .antMatchers("/","/register","/resources/**","/login").permitAll()
//                .antMatchers(AccountRolePermissionEnum.ROLE_ROOT.getPermission()).hasRole(AccountRolePermissionEnum.ROLE_ROOT.getRoleName())
//                .antMatchers(AccountRolePermissionEnum.ROLE_ADMIN.getPermission()).hasRole(AccountRolePermissionEnum.ROLE_ADMIN.getRoleName())
//                .antMatchers(AccountRolePermissionEnum.ROLE_USER.getPermission()).hasRole(AccountRolePermissionEnum.ROLE_USER.getRoleName())
                .anyRequest()
                .authenticated();
        http
                .csrf().disable();
        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .sessionRegistry(sessionRegistry());
    }






}
