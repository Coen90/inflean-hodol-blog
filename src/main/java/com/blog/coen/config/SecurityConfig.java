package com.blog.coen.config;

//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
public class SecurityConfig /*extends WebSecurityConfigurerAdapter */{

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .failureUrl("/login?isError=true")
//                .and()
//                .logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/")
//                .and()
//                .csrf().disable()
//        ;
//
//        http.authorizeHttpRequests()
//                .antMatchers("/", "/posts/**").permitAll()
//        ;
//
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(formLoginService)
//                .passwordEncoder(passwordEncoder());
//    }
}
