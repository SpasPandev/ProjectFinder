package com.example.projectfinder.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.projectfinder.model.entity.RoleEntity;
import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.entity.enums.RoleNameEnum;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;
import com.example.projectfinder.repository.RoleRepository;
import com.example.projectfinder.repository.TechnologyRepository;
import com.example.projectfinder.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {

        RoleEntity studentRole = new RoleEntity();
        RoleEntity companyRole = new RoleEntity();
        RoleEntity adminRole = new RoleEntity();

        studentRole.setRole(RoleNameEnum.STUDENT);
        companyRole.setRole(RoleNameEnum.COMPANY);
        adminRole.setRole(RoleNameEnum.ADMIN);

        TechnologyEntity java = new TechnologyEntity();
        TechnologyEntity python = new TechnologyEntity();
        TechnologyEntity c = new TechnologyEntity();
        TechnologyEntity javascript = new TechnologyEntity();
        TechnologyEntity php = new TechnologyEntity();

        java.setTechnologies(TechnologyNameEnum.JAVA);
        python.setTechnologies(TechnologyNameEnum.PYTHON);
        c.setTechnologies(TechnologyNameEnum.C);
        javascript.setTechnologies(TechnologyNameEnum.JAVASCRIPT);
        php.setTechnologies(TechnologyNameEnum.PHP);

        roleRepository.saveAll(List.of(studentRole, companyRole, adminRole));
        technologyRepository.saveAll(List.of(java, python, c, javascript, php));

        UserEntity firstUser = new UserEntity();
        firstUser.setName("First User");
        firstUser.setUsername("firstUser");
        firstUser.setPassword("firstUser");
        firstUser.setEmail("firstUser@firstUser");
        firstUser.setDescription("Description for firstUser");
        firstUser.setRoles(List.of(studentRole));
        firstUser.setTechnologies(List.of(java, php));

        userRepository.save(firstUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        technologyRepository.deleteAll();
    }

    @Test
    void testOpenLoginPage() throws Exception {

        mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testOpenRegisterForm() throws Exception {

        mockMvc
                .perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    private static final String TEST_USER_NAME = "Pesho";
    private static final String TEST_USER_USERNAME = "pesho";
    private static final String TEST_USER_PASSWORD = "123321";
    private static final String TEST_USER_CONFIRMPASSWORD = "123321";
    private static final String TEST_USER_EMAIL = "pesho@pesho";
    private static final String TEST_USER_DESCRIPTION = "Description kjnaskjnfa ajlnkfaskf kajflnlasfnf";
    private static final String TEST_USER_ROLE = "ADMIN";
    private static final String TEST_USER_TECHNOLOGY1 = "C";
    private static final String TEST_USER_TECHNOLOGY2 = "JAVA";

    @Test
    void testRegisterUser() throws Exception {

        long userRepoCountBeforeCall = userRepository.count();

        mockMvc.perform(post("/register")
                        .param("name", TEST_USER_NAME)
                        .param("username", TEST_USER_USERNAME)
                        .param("password", TEST_USER_PASSWORD)
                        .param("confirmPassword", TEST_USER_CONFIRMPASSWORD)
                        .param("email", TEST_USER_EMAIL)
                        .param("description", TEST_USER_DESCRIPTION)
                        .param("role", TEST_USER_ROLE)
                        .param("technology", TEST_USER_TECHNOLOGY1)
                        .param("technology", TEST_USER_TECHNOLOGY2)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection());

        Assertions.assertEquals(userRepoCountBeforeCall + 1, userRepository.count());

        Optional<UserEntity> newlyCreatedUserOpt = userRepository.findByUsername(TEST_USER_USERNAME);

        Assertions.assertTrue(newlyCreatedUserOpt.isPresent());

        UserEntity newlyCreatedUser = newlyCreatedUserOpt.get();

        newlyCreatedUser.setRoles(userRepository.fetchUserWithFetchedRolesByUserId(newlyCreatedUser).getRoles());
        newlyCreatedUser.setTechnologies(userRepository.fetchUserWithFetchedTechnologiesByUserId(newlyCreatedUser).getTechnologies());

        Assertions.assertEquals(TEST_USER_NAME, newlyCreatedUser.getName());
        Assertions.assertEquals(TEST_USER_USERNAME, newlyCreatedUser.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(TEST_USER_PASSWORD, newlyCreatedUser.getPassword()));
        Assertions.assertEquals(TEST_USER_EMAIL, newlyCreatedUser.getEmail());
        Assertions.assertEquals(TEST_USER_DESCRIPTION, newlyCreatedUser.getDescription());
        Assertions.assertEquals(TEST_USER_ROLE, newlyCreatedUser.getRoles().stream().findFirst().orElseThrow().getRole().name());

    }

    @Test
    void testUsernameExistAndEmailExist() throws Exception {

        mockMvc.perform(post("/register")
                        .param("name", TEST_USER_NAME)
                        .param("username", "firstUser")
                        .param("password", TEST_USER_PASSWORD)
                        .param("confirmPassword", TEST_USER_CONFIRMPASSWORD)
                        .param("email", "firstUser@firstUser")
                        .param("description", TEST_USER_DESCRIPTION)
                        .param("role", TEST_USER_ROLE)
                        .param("technology", TEST_USER_TECHNOLOGY1)
                        .param("technology", TEST_USER_TECHNOLOGY2)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

    @Test
    void testUsernameExist() throws Exception {

        mockMvc.perform(post("/register")
                        .param("name", TEST_USER_NAME)
                        .param("username", "firstUser")
                        .param("password", TEST_USER_PASSWORD)
                        .param("confirmPassword", TEST_USER_CONFIRMPASSWORD)
                        .param("email", TEST_USER_EMAIL)
                        .param("description", TEST_USER_DESCRIPTION)
                        .param("role", TEST_USER_ROLE)
                        .param("technology", TEST_USER_TECHNOLOGY1)
                        .param("technology", TEST_USER_TECHNOLOGY2)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

    @Test
    void testEmailExist() throws Exception {

        mockMvc.perform(post("/register")
                        .param("name", TEST_USER_NAME)
                        .param("username", TEST_USER_USERNAME)
                        .param("password", TEST_USER_PASSWORD)
                        .param("confirmPassword", TEST_USER_CONFIRMPASSWORD)
                        .param("email", "firstUser@firstUser")
                        .param("description", TEST_USER_DESCRIPTION)
                        .param("role", TEST_USER_ROLE)
                        .param("technology", TEST_USER_TECHNOLOGY1)
                        .param("technology", TEST_USER_TECHNOLOGY2)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

    @Test
    void testPasswordMatchConfirmPassword() throws Exception {

        mockMvc.perform(post("/register")
                        .param("name", TEST_USER_NAME)
                        .param("username", TEST_USER_USERNAME)
                        .param("password", "password")
                        .param("confirmPassword", "confirmPassword")
                        .param("technology", TEST_USER_TECHNOLOGY1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }

    @Test
    void testBindingResultHasErrors() throws Exception {

        mockMvc.perform(post("/register")
                        .param("name", "tes")
                        .param("username", "tes")
                        .param("password", "tes")
                        .param("confirmPassword", "tes")
                        .param("technology", TEST_USER_TECHNOLOGY1)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"));
    }
}
