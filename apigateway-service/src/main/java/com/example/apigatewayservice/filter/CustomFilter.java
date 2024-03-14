package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j; // Lombok 라이브러리를 사용하여 Logger 객체를 자동으로 생성합니다. 클래스에 @Slf4j 어노테이션을 붙이면 'log'라는 이름의 Logger 객체가 자동으로 생성됩니다.
import org.springframework.cloud.gateway.filter.GatewayFilter; // Spring Cloud Gateway에서 사용하는 필터 인터페이스입니다. 이를 구현하여 커스텀 필터를 작성할 수 있습니다.
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory; // 커스텀 필터를 쉽게 만들 수 있도록 도와주는 추상 클래스입니다. 이 클래스를 상속받아 필터 로직을 구현합니다.
import org.springframework.http.server.reactive.ServerHttpRequest; // 비동기, 논블로킹 I/O를 처리하는 서버 측 HTTP 요청을 나타냅니다.
import org.springframework.http.server.reactive.ServerHttpResponse; // 비동기, 논블로킹 I/O를 처리하는 서버 측 HTTP 응답을 나타냅니다.
import org.springframework.stereotype.Component; // 스프링의 컴포넌트 스캔 메커니즘에 의해 빈으로 자동 등록되도록 하는 어노테이션입니다.
import reactor.core.publisher.Mono; // Reactor 라이브러리의 Mono는 0 또는 1개의 결과를 처리할 수 있는 리액티브 타입입니다.

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

