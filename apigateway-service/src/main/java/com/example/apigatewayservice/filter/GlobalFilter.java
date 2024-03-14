package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {
    // GlobalFilter 클래스는 Spring Cloud Gateway에서 사용할 수 있는 글로벌 필터를 정의합니다.
    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // 필터 로직을 적용하는 메서드. Config 객체에 정의된 설정을 바탕으로 실제 필터링 로직을 구현합니다.
        return (((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            // 설정된 baseMessage를 로그로 기록합니다. 필터가 적용되는지 확인하기 위한 메시지입니다.
            log.info("Global Filter baseMessage: {}", config.getBaseMessage());
            // preLogger 설정이 true일 경우, 요청 시작 시 로그를 기록합니다.
            if (config.isPreLogger()) {
                log.info("Global Filter Start: request id -> {}", request.getId());
            }
            // 다음 필터 체인을 실행하고, 비동기적으로 응답 처리가 완료된 후의 로직을 정의합니다.
            return chain.filter(exchange).then(Mono.fromRunnable(()->{
                // postLogger 설정이 true일 경우, 응답 완료 시 로그를 기록합니다.
                if (config.isPostLogger()) {
                    log.info("Global Filter End: response code -> {}", response.getStatusCode());
                }
            }));
        }));
    }

    @Data
    public static class Config { // Config 내부 클래스는 필터의 설정 정보를 저장하기 위한 클래스입니다.
        // 필터링 로직에 사용될 기본 메시지
        private String baseMessage;
        // 요청 처리 시작 시 로그를 기록할지 여부를 결정하는 플래그
        private boolean preLogger;
        // 응답 처리 완료 시 로그를 기록할지 여부를 결정하는 플래그
        private boolean postLogger;
    }
}
