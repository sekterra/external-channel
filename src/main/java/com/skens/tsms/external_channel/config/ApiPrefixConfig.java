package com.skens.tsms.external_channel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 플랫폼 규칙: REST API 경로에 공통 prefix를 자동 적용하기 위한 설정.
 * 적용 범위
 *   적용 대상: {@code @RestController}가 붙은 컨트롤러만 적용됨.
 *   제외: {@code @Controller}(SSR)는 prefix 적용 없음.
 *
 * 매핑 예시
 *   컨트롤러에 {@code @RequestMapping("/forms")}만 지정 시 → 실제 URL: {@code /api/forms}
 *   따라서 API 컨트롤러에서는 "/api"를 하드코딩하지 말 것.
 *
 * 주의
 *   prefix를 받으려면 해당 컨트롤러에 반드시 {@code @RestController}를 사용해야 함.
 *   {@code @Controller}만 사용하면 /api prefix가 붙지 않아 이중 prefix(/api/api/...) 방지.
 */
@Configuration
public class ApiPrefixConfig implements WebMvcConfigurer {

	@Override
	public void configurePathMatch(@NonNull PathMatchConfigurer configurer) {
		configurer.addPathPrefix("/api", clazz -> clazz.isAnnotationPresent(RestController.class));
	}
}
