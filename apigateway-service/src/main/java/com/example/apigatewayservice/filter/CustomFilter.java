package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component // 이 클래스를 스프링 컨테이너가 관리하는 빈으로 등록합니다.
@Slf4j // Logger 객체를 자동으로 생성합니다. 이를 통해 로깅을 손쉽게 할 수 있습니다.
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    // AbstractGatewayFilterFactory를 상속받아 커스텀 필터를 생성합니다. 제네릭 타입으로 Config 클래스를 사용합니다.
    public CustomFilter() {
        super(Config.class); // 부모 클래스의 생성자를 호출하여 Config 클래스 타입을 넘겨줍니다. 이는 필터 설정을 위한 클래스입니다.
    }

    @Override
    public GatewayFilter apply(Config config) {
        // 필터 로직을 구현합니다. 이 메소드는 필터를 적용할 때 호출됩니다. Config 객체를 매개변수로 받아 필터 설정을 할 수 있습니다.
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest(); // 현재 HTTP 요청 정보를 가져옵니다.
            ServerHttpResponse response = exchange.getResponse(); // 현재 HTTP 응답 정보를 가져옵니다.

            log.info("Custom PRE filter: request uri -> {}", request.getId()); // 요청을 처리하기 전에 로그를 출력합니다. 요청 ID를 로깅합니다.

            // 다음 필터로 요청을 전달하고, 그 결과로 반환되는 Mono를 처리합니다.
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                log.info("Custom POST Filter: response code -> {}", response.getStatusCode()); // 응답을 처리한 후에 로그를 출력합니다. 응답 상태 코드를 로깅합니다.
            }));
        };
    }

    public static class Config {
        // 필터 구성을 위한 설정 클래스입니다. 필요한 설정 값들을 이곳에 정의할 수 있습니다.
    }
}

