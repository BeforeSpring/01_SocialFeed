package beforespring.socialfeed.member.controller;

import beforespring.socialfeed.content.controller.ContentController;
import beforespring.socialfeed.member.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static beforespring.socialfeed.member.controller.dto.ConfirmTokenDto.ConfirmTokenRequest;
import static beforespring.socialfeed.member.controller.dto.CreateMemberDto.CreateMemberRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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