package bg.kotz.stompexample.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic/");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/handshake")
                .addInterceptors(interceptor()).withSockJS();

    }

    @Bean
    public HttpSessionHandshakeInterceptor interceptor() {
        return new HttpSessionHandshakeInterceptor(){

            private Logger logger = LoggerFactory.getLogger(HttpSessionHandshakeInterceptor.class);

            @Override
            public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                boolean proceed = super.beforeHandshake(request, response, wsHandler, attributes);

                if (request instanceof ServletServerHttpRequest) {
                    ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
                    Principal principal = serverHttpRequest.getPrincipal();
                    HttpHeaders headers = serverHttpRequest.getHeaders();
                    HttpSession servletRequestSession = serverHttpRequest.getServletRequest().getSession();
                    URI uri = serverHttpRequest.getURI();

                    logger.info("Principal: " + principal);
                    logger.info("Headers: " + headers.toString());
                    logger.info("HttpSession: " + servletRequestSession.getId());
                    logger.info("URI: " + uri);
                    logger.info("HOST: " + uri.getHost());


                }

                if (request instanceof HttpServletRequest) {
                    HttpServletRequest servletRequest = (HttpServletRequest) request;

                    HttpSession session = servletRequest.getSession();
                    attributes.put("sessionId", session.getId());                }

                return proceed;
            }

            @Override
            public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
                super.afterHandshake(request, response, wsHandler, ex);
            }
        };
    }
}
