/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.mobileid.paperless.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 *
 * @author 84766
 */
// bảo mật sử dụng spring security
@Configuration // để nói vs Spring rằng đây là 1 class cấu hình.
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    // có thể truy cập vào địa chỉ này mà k cần xác thực để truy cập
    /*
     * '/' là dia chỉ trang chủ
     * '/css/**' là địa chỉ các file CSS được sử dụng trong web. Dấu ** cho phép mọi
     * đường dẫn con trong thư mục '/css' cũng được cho phép truy cập.
     *
     */
    private static final String[] PUBLIC_MATCHERS = {
        "/", "/css/**",};

    @Override

    // cấu hình các quy tắc bảo mật
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf() // (cross-sute request forgery)==> kĩ thuật tấn công mạng
                .disable()
                .authorizeRequests() // yêu cầu quyền truy cập
                .antMatchers(PUBLIC_MATCHERS).permitAll(); // Cho phép tất cả mọi người truy cập vào địa chỉ này
        // .anyRequest().authenticated() // Tất cả các request khác đều cần phải xác
        // thực mới được truy cập. (xác minh là ai)
        // .and()
        // .formLogin() // Cho phép người dùng xác thực bằng form login
        // .loginPage("/login")
        // .permitAll() // Tất cả đều được truy cập vào địa chỉ này
        // .and()
        // .logout() // cho phép người dùng logout
        // .permitAll();
        http.headers()
                .contentSecurityPolicy("frame-ancestors *")
                .and()
                .frameOptions()
                .sameOrigin();
//                .addHeaderWriter(createReferrerPolicyHeaderWriter())
//                .addHeaderWriter(createPermissionsPolicyHeaderWriter());

    }

    // cấu hình xác thực người dùng
    @Autowired // annotation chú thích
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication() // cấu hình xác thực người dùng
                .withUser("admin").password("{noop}1").roles("ADMIN");
        // noop là mật khẩu chưa mã hoá.
    }

//    @Bean
//    public HeaderWriter createReferrerPolicyHeaderWriter() {
//        String referrerPolicyValue = "same-origin";
//        return new StaticHeadersWriter("Referrer-Policy", referrerPolicyValue);
//    }
//
//    @Bean
//    public HeaderWriter createPermissionsPolicyHeaderWriter() {
//        String permissionsPolicyValue = "geolocation=(), camera=(), microphone=()";
//        return new StaticHeadersWriter("Permissions-Policy", permissionsPolicyValue);
//    }
}
