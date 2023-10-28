package beforespring.socialfeed.config;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DummyDataProperties.class)
public class DummyDataConfig {

    private final EntityManager em;

    private final DummyDataProperties dummyDataProperties;

    @Bean
    @Profile("withDummyData")
    public DummyContentDataInitializer dummyContentDataInitializer() {
        return DummyContentDataInitializer.builder()
                   .em(em)
                   .CONTENTS_TO_CREATE(dummyDataProperties.getTotalContent())
                   .MAX_HASHTAGS_PER_CONTENT(dummyDataProperties.getMaxHashtagCountPerContent())
                   .readableHashtags(dummyDataProperties.getReadableHashtags())
                   .build();
    }
}
