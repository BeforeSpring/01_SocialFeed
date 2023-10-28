package beforespring.socialfeed.content.service;

import static beforespring.Fixture.aContent;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import beforespring.socialfeed.content.domain.Content;
import beforespring.socialfeed.content.domain.ContentQueryParameter;
import beforespring.socialfeed.content.domain.ContentQueryRepository;
import beforespring.socialfeed.content.domain.ContentQueryResult;
import beforespring.socialfeed.content.service.dto.ContentListElement;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContentQueryServiceImplTest {

    @Mock
    ContentQueryRepository contentQueryRepository;

    @Mock
    ContentMapper contentMapper;

    @InjectMocks
    ContentQueryServiceImpl contentQueryService;

    @Captor
    ArgumentCaptor<Content> contentArgumentCaptor;

    @Test
    @DisplayName("인자가 제대로 넘어가는지 테스트")
    void getContentList() {
        ContentQueryParameter givenParam = ContentQueryParameter.builder()
                                               .size(20)
                                               .hashtag("given")
                                               .offset(0)
                                               .from(null)
                                               .build();
        List<Content> givenContents = List.of(aContent().build(), aContent().build(),
            aContent().build());
        ContentQueryResult<Content> givenContentQueryResult = ContentQueryResult.<Content>builder()
                                                                  .contents(givenContents)
                                                                  .lastContentCreatedAt(
                                                                      LocalDateTime.now())
                                                                  .hasNext(true)
                                                                  .offsetRequiredNext(true)
                                                                  .size(givenContents.size())
                                                                  .build();

        given(contentQueryRepository.findByHashtag(givenParam))
            .willReturn(givenContentQueryResult);

        given(contentMapper.mapToContentListElement(any()))
            .willAnswer(c -> ContentListElement.builder().build());

        // when
        contentQueryService.getContentList(givenParam);


        // then
        verify(contentQueryRepository)
            .findByHashtag(givenParam);

        verify(contentMapper, times(givenContentQueryResult.size()))
            .mapToContentListElement(contentArgumentCaptor.capture());

        List<Content> captured = contentArgumentCaptor.getAllValues();

        assertThat(captured)
            .describedAs("모두 변환 요청이 되었는지 확인")
            .containsExactlyInAnyOrderElementsOf(givenContents);
    }
}