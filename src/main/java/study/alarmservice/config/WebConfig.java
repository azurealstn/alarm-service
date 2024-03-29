package study.alarmservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import study.alarmservice.interceptor.LoginCheckInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * LocaleChangeInterceptor는 url 뒤에 특정 locale 파라미터를 넘겨서 변경한다.
     * 컨트롤러 호출 전에 살행되므로 스프링 인터셉터의 동작과 동일하다.
     * 따라서 LocaleChangeInterceptor를 사용하면 컨트롤러마다 locale 파라미터 처리를 따로 하지 않아도 된다.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor)
                .order(1);

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/api/v1/users", "/api/v1/users/**", "/api/v1/login", "/api/v1/logout",
                        "/css/**", "/*.ico", "/error");
    }
}
