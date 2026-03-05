package cat.udl.eps.softarch.demo.handler;

import cat.udl.eps.softarch.demo.domain.Portfolio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler
public class PortfolioEventHandler {

    final Logger logger = LoggerFactory.getLogger(Portfolio.class);

    @HandleBeforeCreate
    public void handlePortfolioPreCreate(Portfolio portfolio) {
        logger.info("Before creating portfolio: {}", portfolio.toString());
    }
}