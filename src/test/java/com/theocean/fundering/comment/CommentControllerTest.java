package com.theocean.fundering.comment;

import com.theocean.fundering.domain.comment.controller.CommentController;
import com.theocean.fundering.domain.comment.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;



    @WithMockUser(roles = {"USER"})  // USER 권한을 가진 사용자로 설정
    @Test
    public void createComment_withUserRole_shouldSucceed() throws Exception {

        this.mockMvc.perform(post("/posts/{postId}/comments", 1L).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"test comment\"}"))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "GUEST")  // GUEST 권한을 가진 사용자로 설정
    @Test
    public void createComment_withGuestRole_shouldFail() throws Exception {
        this.mockMvc.perform(post("/posts/{postId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\":\"test comment\"}"))
                .andExpect(status().isForbidden());  // 403 Forbidden 예상
    }

    @WithMockUser(roles = "USER")  // USER 권한을 가진 사용자로 설정
    @Test
    public void deleteComment_withUserRole_shouldSucceed() throws Exception {
        this.mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", 1L, 1L).with(csrf()))
                .andExpect(status().isOk());
    }

    @WithMockUser(roles = "GUEST")  // GUEST 권한을 가진 사용자로 설정
    @Test
    public void deleteComment_withGuestRole_shouldFail() throws Exception {
        this.mockMvc.perform(delete("/posts/{postId}/comments/{commentId}", 1L, 1L))
                .andExpect(status().isForbidden());  // 403 Forbidden 예상
    }
}