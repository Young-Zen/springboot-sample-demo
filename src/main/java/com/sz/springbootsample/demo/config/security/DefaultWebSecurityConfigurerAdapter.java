package com.sz.springbootsample.demo.config.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Yanghj
 * @date 1/1/2020
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class DefaultWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    //User.withDefaultPasswordEncoder() is considered unsafe for production and is only intended for sample applications.
    /*@Bean
    public UserDetailsService userDetailsService() throws Exception {
        // ensure the passwords are encoded properly
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user").password("user").roles("USER").build());
        manager.createUser(users.username("admin").password("admin").roles("USER","ADMIN").build());
        return manager;
    }*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);    //error
        //下面注释的是旧版本的创建方式因为新的Spring security 5.0中新增了多种加密方式，也改变了密码的格式,所以无法使用
        //auth.inMemoryAuthentication().withUser("demo1").password("demo1").roles("DEMO");
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        auth.inMemoryAuthentication().passwordEncoder(bCryptPasswordEncoder)
                .withUser("user")
                .password(bCryptPasswordEncoder.encode("user"))
                .roles("USER");

        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication().passwordEncoder(encoder)
                .withUser("admin")
                .password(encoder.encode("admin"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("actuator")
                .password(encoder.encode("actuator"))
                .roles("ACTUATOR_ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(new DefaultAccessDeniedHandler());
        http.csrf()
                .ignoringAntMatchers("/actuator/**");
        http.authorizeRequests()
                .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR_ADMIN")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                .antMatchers("/demo/**").hasAnyRole("ADMIN", "USER")
                //.antMatchers("/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .httpBasic();
    }
}