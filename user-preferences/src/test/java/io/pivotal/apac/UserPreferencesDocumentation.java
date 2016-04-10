package io.pivotal.apac;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserPreferencesApplication.class)
public class UserPreferencesDocumentation {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation(
            "target/generated-snippets"
    );

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    /**
     * Set up MockMVC
     */
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    /**
     * All documentation production happens here.
     * 
     * @throws Exception 
     */
    @Test
    public void documentUsersResource() throws Exception {

        UserPreference user = new UserPreference();
        user.userId = "Sergiu";

        user.categoryKey = "application";
        user.categoryValue = "View";

        user.categoryKey2 = "page";
        user.categoryValue2 = "Transaction";

        user.subCategoryKey = "tab";
        user.subCategoryValue = "Social";


        user.preferenceKey = "transactionTypeFilter";
        user.preferenceValue = "All";

        insertUser(user);

        getUser(user.getUserId());

    }

    private void insertUser(UserPreference user) throws Exception {
        
        this.mockMvc.perform(post("/api/v1/userPreferences.json")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("insertUser",
                        requestFields(
                                fieldWithPath("userId").description(USERS_ID_DESCRIPTION),
                                fieldWithPath("categoryKey").description(USERS_CATEGORY_KEY_DESCRIPTION),
                                fieldWithPath("categoryValue").description(USERS_CATEGORY_VALLUE_DESCRIPTION),
                                fieldWithPath("categoryKey2").description(USERS_CATEGORY_KEY2_DESCRIPTION),
                                fieldWithPath("categoryValue2").description(USERS_CATEGORY_VALLUE2_DESCRIPTION),
                                fieldWithPath("subCategoryKey").description(USERS_SUB_CATEGORY_KEY_DESCRIPTION),
                                fieldWithPath("subCategoryValue").description(USERS_SUB_CATEGORY_VALUE_DESCRIPTION),
                                fieldWithPath("preferenceKey").description(USERS_PREFERENCE_KEY_DESCRIPTION),
                                fieldWithPath("preferenceValue").description(USERS_PREFERENCE_VALUE_DESCRIPTION),
                                fieldWithPath("lastModificationDate").description(USERS_LAST_MODIFICATION_DATE_DESCRIPTION),
                                fieldWithPath("lastModificationUserId").description(USERS_LAST_MODIFICATION_USER_ID_DESCRIPTION)
                        )));
    }

    private void getUser(String userId) throws Exception {


        Map<String, String> tag = new HashMap<String, String>();
        tag.put("userId", "Sergiu");
        tag.put("categoryKey", "application");
        tag.put("categoryValue", "View");
        tag.put("categoryKey2", "page");
        tag.put("categoryValue2", "Transaction");
        tag.put("subCategoryKey", "tab");
        tag.put("subCategoryValue", "Social");
        tag.put("preferenceKey", "transactionTypeFilter");
        tag.put("preferenceValue", "All");

        this.mockMvc.perform(get("/api/v1/userPreferences.json?USERID={USERID}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(tag.get("userId"))))
                .andExpect(jsonPath("preferences[0].categoryKey", is(tag.get("categoryKey"))))
                .andExpect(jsonPath("preferences[0].categoryValue", is(tag.get("categoryValue"))))
                .andExpect(jsonPath("preferences[0].categoryKey2", is(tag.get("categoryKey2"))))
                .andExpect(jsonPath("preferences[0].categoryValue2", is(tag.get("categoryValue2"))))
                .andExpect(jsonPath("preferences[0].subCategoryKey", is(tag.get("subCategoryKey"))))
                .andExpect(jsonPath("preferences[0].subCategoryValue", is(tag.get("subCategoryValue"))))
                .andExpect(jsonPath("preferences[0].preferenceKey", is(tag.get("preferenceKey"))))
                .andExpect(jsonPath("preferences[0].preferenceValue", is(tag.get("preferenceValue"))))
                .andDo(document("getUser",
                        pathParameters(
                                parameterWithName("USERID").description(USERS_ID_DESCRIPTION)),
                        responseFields(
                                fieldWithPath("userId").description(USERS_ID_DESCRIPTION),
                                fieldWithPath("action").description("User's preference action"),
                                fieldWithPath("preferences").description("User's preference list"),
                                fieldWithPath("preferences[].userId").description(USERS_ID_DESCRIPTION),
                                fieldWithPath("preferences[].categoryKey").description(USERS_CATEGORY_KEY_DESCRIPTION),
                                fieldWithPath("preferences[].categoryValue").description(USERS_CATEGORY_VALLUE_DESCRIPTION),
                                fieldWithPath("preferences[].categoryKey2").description(USERS_CATEGORY_KEY2_DESCRIPTION),
                                fieldWithPath("preferences[].categoryValue2").description(USERS_CATEGORY_VALLUE2_DESCRIPTION),
                                fieldWithPath("preferences[].subCategoryKey").description(USERS_SUB_CATEGORY_KEY_DESCRIPTION),
                                fieldWithPath("preferences[].subCategoryValue").description(USERS_SUB_CATEGORY_VALUE_DESCRIPTION),
                                fieldWithPath("preferences[].preferenceKey").description(USERS_PREFERENCE_KEY_DESCRIPTION),
                                fieldWithPath("preferences[].preferenceValue").description(USERS_PREFERENCE_VALUE_DESCRIPTION),
                                fieldWithPath("preferences[].lastModificationDate").description(USERS_LAST_MODIFICATION_DATE_DESCRIPTION),
                                fieldWithPath("preferences[].lastModificationUserId").description(USERS_LAST_MODIFICATION_USER_ID_DESCRIPTION))
                ));
    }

//    private void getUsers() throws Exception {
//
//        RestDocumentationResultHandler document = documentPrettyPrintReqResp("getUsers");
//
//        document.snippets(
//                pathParameters(
//                        parameterWithName("page").description("Page of results"),
//                        parameterWithName("size").description("Size of results")
//                ),
//                responseFields(userFields(true))
//        );
//
//        this.mockMvc.perform(get("/api/v1/users?page={page}&size={size}", 0, 10)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$").isArray())
//                .andExpect(jsonPath("[*].userId").isNotEmpty())
//                .andExpect(jsonPath("[*].firstName").isNotEmpty())
//                .andExpect(jsonPath("[*].lastName").isNotEmpty())
//                .andExpect(jsonPath("[*].username").isNotEmpty())
//                .andDo(document);
//    }
//
//    private void deleteUser(UUID userId) throws Exception {
//
//        RestDocumentationResultHandler document =
//                documentPrettyPrintReqResp("deleteUser");
//
//        document.snippets(
//                pathParameters(userPathParams()),
//                responseFields(userFields(false))
//        );
//
//        this.mockMvc.perform(delete("/api/v1/users/{userId}", userId)
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(document);
//    }


    private static final String USERS_CATEGORY_KEY_DESCRIPTION = "User's category key";
    private static final String USERS_CATEGORY_VALLUE_DESCRIPTION = "User's category value";
    private static final String USERS_CATEGORY_KEY2_DESCRIPTION = "User's category key 2";
    private static final String USERS_CATEGORY_VALLUE2_DESCRIPTION = "User's category value 2";
    private static final String USERS_SUB_CATEGORY_KEY_DESCRIPTION = "User's sub category key";
    private static final String USERS_SUB_CATEGORY_VALUE_DESCRIPTION = "User's sub category value";
    private static final String USERS_PREFERENCE_KEY_DESCRIPTION = "User's preference key";
    private static final String USERS_PREFERENCE_VALUE_DESCRIPTION = "User's preference value";
    private static final String USERS_LAST_MODIFICATION_DATE_DESCRIPTION = "User's last modification date";
    private static final String USERS_LAST_MODIFICATION_USER_ID_DESCRIPTION = "User's last user id date";;
    private static final String USERS_ID_DESCRIPTION = "User's name";

}
