package beforespring.socialfeed.config;

import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.bind.DefaultValue;


@Slf4j
@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "social-feed.dummy")
public class DummyDataProperties {

    private final int totalContent;
    private final int maxHashtagCountPerContent;
    private final List<String> readableHashtags;

    public DummyDataProperties(
        @DefaultValue("1000") int totalContent,
        @DefaultValue("20") int maxHashtagCountPerContent,
        @DefaultValue({"wanted", "pre_onboarding", "backend", "before_spring"}) List<String> readableHashtags
    ) {
        this.totalContent = totalContent;
        this.maxHashtagCountPerContent = maxHashtagCountPerContent;
        this.readableHashtags = readableHashtags;
    }
}
