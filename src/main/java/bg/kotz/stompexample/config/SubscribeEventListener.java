package bg.kotz.stompexample.config;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
public class SubscribeEventListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        ServletRequestHandledEvent handledEvent = (ServletRequestHandledEvent) event;
        long timestamp = event.getTimestamp();
        Object source = event.getSource();

        System.out.println("onApplicationEvent:  " + timestamp + handledEvent.getDescription());

        System.out.println(handledEvent.getStatusCode() + handledEvent.getRequestUrl()
                + " " + handledEvent.getSessionId());
    }
}
