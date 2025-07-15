package be15fintomatokatchupbe.user.query.service;

import be15fintomatokatchupbe.user.query.dto.response.UserAccountQueryResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserHeaderAccountResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListDTO;
import be15fintomatokatchupbe.user.query.dto.response.UserInfluencerListResponse;
import be15fintomatokatchupbe.user.query.dto.response.UserListResponse; // Added explicitly for clarity
import be15fintomatokatchupbe.user.query.dto.response.UserSearchDto; // Added explicitly for clarity
import be15fintomatokatchupbe.user.query.mapper.UserQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserQueryServiceTest {

    @Mock
    private UserQueryMapper userQueryMapper;

    @InjectMocks
    private UserQueryService userQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getMyAccount_success() {
        // Given
        Long userId = 1L;
        UserAccountQueryResponse expectedResponse = UserAccountQueryResponse.builder()
                .userId(userId)
                .email("test@example.com")
                .loginId("testuser")
                .name("테스트유저")
                .build();

        when(userQueryMapper.getMyAccount(userId)).thenReturn(expectedResponse);

        // When
        UserAccountQueryResponse actualResponse = userQueryService.getMyAccount(userId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(userQueryMapper, times(1)).getMyAccount(userId);
    }

    @Test
    void getMyAccount_userNotFound() {
        // Given
        Long userId = 99L;
        when(userQueryMapper.getMyAccount(userId)).thenReturn(null);

        // When
        UserAccountQueryResponse actualResponse = userQueryService.getMyAccount(userId);

        // Then
        assertNull(actualResponse); // Service should return null if mapper returns null
        verify(userQueryMapper, times(1)).getMyAccount(userId);
    }

    @Test
    void getMyInfluencer_success() {
        // Given
        Long userId = 1L;
        List<UserInfluencerListDTO> dtoList = List.of(
                UserInfluencerListDTO.builder().userId(2L).name("Influencer1").build(),
                UserInfluencerListDTO.builder().userId(3L).name("Influencer2").build()
        );
        UserInfluencerListResponse expectedResponse = UserInfluencerListResponse.builder()
                .userInfluencerList(dtoList)
                .build();

        when(userQueryMapper.getMyInfluencer(userId)).thenReturn(dtoList);

        // When
        UserInfluencerListResponse actualResponse = userQueryService.getMyInfluencer(userId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getUserInfluencerList(), actualResponse.getUserInfluencerList());
        verify(userQueryMapper, times(1)).getMyInfluencer(userId);
    }

    @Test
    void getMyInfluencer_emptyList() {
        // Given
        Long userId = 1L;
        List<UserInfluencerListDTO> emptyList = Collections.emptyList();
        UserInfluencerListResponse expectedResponse = UserInfluencerListResponse.builder()
                .userInfluencerList(emptyList)
                .build();

        when(userQueryMapper.getMyInfluencer(userId)).thenReturn(emptyList);

        // When
        UserInfluencerListResponse actualResponse = userQueryService.getMyInfluencer(userId);

        // Then
        assertNotNull(actualResponse);
        assertTrue(actualResponse.getUserInfluencerList().isEmpty());
        assertEquals(expectedResponse.getUserInfluencerList(), actualResponse.getUserInfluencerList());
        verify(userQueryMapper, times(1)).getMyInfluencer(userId);
    }

    @Test
    void getUserList_successWithKeyword() {
        // Given
        String keyword = "test";
        List<UserSearchDto> dtoList = List.of(
                UserSearchDto.builder().id(1L).name("testuser1").build(),
                UserSearchDto.builder().id(2L).name("anotherTest").build()
        );
        UserListResponse expectedResponse = UserListResponse.builder()
                .userList(dtoList)
                .build();

        when(userQueryMapper.getUserList(keyword)).thenReturn(dtoList);

        // When
        UserListResponse actualResponse = userQueryService.getUserList(keyword);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getUserList(), actualResponse.getUserList());
        verify(userQueryMapper, times(1)).getUserList(keyword);
    }

    @Test
    void getUserList_emptyListWithKeyword() {
        // Given
        String keyword = "nonexistent";
        List<UserSearchDto> emptyList = Collections.emptyList();
        UserListResponse expectedResponse = UserListResponse.builder()
                .userList(emptyList)
                .build();

        when(userQueryMapper.getUserList(keyword)).thenReturn(emptyList);

        // When
        UserListResponse actualResponse = userQueryService.getUserList(keyword);

        // Then
        assertNotNull(actualResponse);
        assertTrue(actualResponse.getUserList().isEmpty());
        assertEquals(expectedResponse.getUserList(), actualResponse.getUserList());
        verify(userQueryMapper, times(1)).getUserList(keyword);
    }

    @Test
    void getUserList_nullKeyword() {
        // Given
        String keyword = null;
        List<UserSearchDto> dtoList = List.of(
                UserSearchDto.builder().id(1L).name("user1").build()
        );
        UserListResponse expectedResponse = UserListResponse.builder()
                .userList(dtoList)
                .build();

        when(userQueryMapper.getUserList(null)).thenReturn(dtoList); // Mapper might handle null keyword

        // When
        UserListResponse actualResponse = userQueryService.getUserList(keyword);

        // Then
        assertNotNull(actualResponse);
        assertFalse(actualResponse.getUserList().isEmpty());
        assertEquals(expectedResponse.getUserList(), actualResponse.getUserList());
        verify(userQueryMapper, times(1)).getUserList(null);
    }

    @Test
    void getSimpleMyAccount_success() {
        // Given
        Long userId = 1L;
        UserHeaderAccountResponse expectedResponse = UserHeaderAccountResponse.builder()
                .userId(userId)
                .name("SimpleUser")
                .fileRoute("http://example.com/profile.jpg")
                .build();

        when(userQueryMapper.getSimpleMyAccount(userId)).thenReturn(expectedResponse);

        // When
        UserHeaderAccountResponse actualResponse = userQueryService.getSimpleMyAccount(userId);

        // Then
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(userQueryMapper, times(1)).getSimpleMyAccount(userId);
    }

    @Test
    void getSimpleMyAccount_userNotFound() {
        // Given
        Long userId = 99L;
        when(userQueryMapper.getSimpleMyAccount(userId)).thenReturn(null);

        // When
        UserHeaderAccountResponse actualResponse = userQueryService.getSimpleMyAccount(userId);

        // Then
        assertNull(actualResponse); // Service should return null if mapper returns null
        verify(userQueryMapper, times(1)).getSimpleMyAccount(userId);
    }
}