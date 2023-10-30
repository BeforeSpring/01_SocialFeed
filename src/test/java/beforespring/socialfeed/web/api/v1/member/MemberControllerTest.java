package beforespring.socialfeed.web.api.v1.member;

import static beforespring.socialfeed.member.service.dto.ConfirmTokenDto.ConfirmTokenRequest;
import static beforespring.socialfeed.member.service.dto.CreateMemberDto.CreateMemberRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import beforespring.socialfeed.web.api.v1.content.ContentController;
import beforespring.socialfeed.member.service.MemberService;
import beforespring.socialfeed.web.api.v1.member.MemberController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
class MemberControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    MemberController memberController;

    @MockBean
    ContentController contentController;

    @MockBean
    MemberService memberService;

    @TestConfiguration
    static class SecurityConf {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            httpSecurity
                .authorizeHttpRequests(authorizeHttpRequestsCustomizer())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .cors(conf -> conf.configure(httpSecurity))
            ;

            return httpSecurity.build();
        }

        private static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> authorizeHttpRequestsCustomizer() {
            return auth -> {
                auth.anyRequest().permitAll();
            };
        }
    }


    /**
     * 멤버 등록에 적절한 데이터를 가지고 등록 URL 호출시
     * 회원 등록 서비스가 실행되어야됩니다.
     * <p>
     * Mock 테스트시 objectMapper가 json 변환에 실패할 경우
     * checked exception을 던지기 때문에 해당 함수 전체에서
     * throws Exception을 하도록 설정해놓았습니다.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("데이터를 가지고 API가 호출되면 회원 등록 서비스가 실행되어야됩니다")
    void save_member_test() throws Exception {
        CreateMemberRequest request = new CreateMemberRequest("username", "1234@etst.com", "12@12334asdf");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/member/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @DisplayName("데이터를 가지고 API가 호출되면 가입 승인 서비스가 실행되어야됩니다")
    void confirm_member_test() throws Exception {
        ConfirmTokenRequest request = new ConfirmTokenRequest("username", "1234", "1234");

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/member/confirm")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
            .andExpect(status().isOk())
            .andReturn();
    }
}