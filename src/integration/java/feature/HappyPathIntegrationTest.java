package feature;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.github.lpgflow.LpgFlowApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {LpgFlowApplication.class, FixedClockConfig.class})
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class HappyPathIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KeyPair keyPair;

    @DynamicPropertySource
    public static void propertyOverride(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
    }

    @Test
    public void happyPath() throws Exception {
// USER PART
//  1. when admin go to /users then he can see first admin only
        String firstAdminEmail = "zalewskikamil89@gmail.com";
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[*].id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.users[*].email", containsInAnyOrder(firstAdminEmail)));
//  2. when admin post to /users with user "manage1@test.pl" then user "manager1@test.pl" is returned with id 2
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "Test",
                                    "lastName": "Manager1",
                                    "email": "manager1@test.pl",
                                    "password": "testpassword",
                                    "phoneNumber" : "123456789"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id", is(2)))
                .andExpect(jsonPath("$.user.email", is("manager1@test.pl")));
//  3. when admin go to /users/2/role/4 then user "manager1@test.pl" is returned with regional manager role
        mockMvc.perform(put("/users/2/role/4")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(2)))
                .andExpect(jsonPath("$.user.email", is("manager1@test.pl")))
                .andExpect(jsonPath("$.user.roles[*].name", containsInAnyOrder("REGIONAL_MANAGER")));
//  4. when admin post to /users with user "manager2@test.pl" then user "manager2@test.pl" is returned with id 3
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "Test",
                                    "lastName": "Manager2",
                                    "email": "manager2@test.pl",
                                    "password": "testpassword",
                                    "phoneNumber" : "123456788"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id", is(3)))
                .andExpect(jsonPath("$.user.email", is("manager2@test.pl")));
//  5. when admin go to /users/3/role/4 then user "manager2@test.pl" is returned with regional manager role
        mockMvc.perform(put("/users/3/role/4")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(3)))
                .andExpect(jsonPath("$.user.email", is("manager2@test.pl")))
                .andExpect(jsonPath("$.user.roles[*].name", containsInAnyOrder("REGIONAL_MANAGER")));
//  6. when admin post to /users with user "warehouseman1@test.pl" then user "warehouseman1@test.pl" is returned with id 4
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "Test",
                                    "lastName": "Warehouseman1",
                                    "email": "warehouseman1@test.pl",
                                    "password": "testpassword",
                                    "phoneNumber" : "123456787"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id", is(4)))
                .andExpect(jsonPath("$.user.email", is("warehouseman1@test.pl")));
//  7. when admin go to /users/4/role/5 then user "warehouseman1@test.pl" is returned with warehouseman role
        mockMvc.perform(put("/users/4/role/5")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(4)))
                .andExpect(jsonPath("$.user.email", is("warehouseman1@test.pl")))
                .andExpect(jsonPath("$.user.roles[*].name", containsInAnyOrder("WAREHOUSEMAN")));
//  8. when admin post to /users with user "warehouseman2@test.pl" then user "warehouseman2@test.pl" is returned with id 5
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "Test",
                                    "lastName": "Warehouseman2",
                                    "email": "warehouseman2@test.pl",
                                    "password": "testpassword",
                                    "phoneNumber" : "123456786"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id", is(5)))
                .andExpect(jsonPath("$.user.email", is("warehouseman2@test.pl")));
//  9. when admin go to /users/5/role/5 then user "warehouseman2@test.pl" is returned with warehouseman role
        mockMvc.perform(put("/users/5/role/5")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(5)))
                .andExpect(jsonPath("$.user.email", is("warehouseman2@test.pl")))
                .andExpect(jsonPath("$.user.roles[*].name", containsInAnyOrder("WAREHOUSEMAN")));
//  10. when admin post to /users with user "warehouseman3@test.pl" then user "warehouseman3@test.pl" is returned with id 6
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "Test",
                                    "lastName": "Warehouseman3",
                                    "email": "warehouseman3@test.pl",
                                    "password": "testpassword",
                                    "phoneNumber" : "123456785"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id", is(6)))
                .andExpect(jsonPath("$.user.email", is("warehouseman3@test.pl")));
//  11. when admin go to /users/6/role/5 then user "warehouseman3@test.pl" is returned with warehouseman role
        mockMvc.perform(put("/users/6/role/5")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(6)))
                .andExpect(jsonPath("$.user.email", is("warehouseman3@test.pl")))
                .andExpect(jsonPath("$.user.roles[*].name", containsInAnyOrder("WAREHOUSEMAN")));

//  12. when admin post to /users with user "planner@test.pl" then user "planner@test.pl" is returned with id 7
        mockMvc.perform(post("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "Test",
                                    "lastName": "Planner",
                                    "email": "planner@test.pl",
                                    "password": "testpassword",
                                    "phoneNumber" : "123456784"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user.id", is(7)))
                .andExpect(jsonPath("$.user.email", is("planner@test.pl")));
//  13. when admin go to /users/7/role/2 then user "planner@test.pl" is returned with planner role
        mockMvc.perform(put("/users/7/role/2")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.id", is(7)))
                .andExpect(jsonPath("$.user.email", is("planner@test.pl")))
                .andExpect(jsonPath("$.user.roles[*].name", containsInAnyOrder("PLANNER")));
//  14. when admin go to /users then he can see 7 users
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[*]", hasSize(7)));
// WAREHOUSE PART
//  15. when admin go to /addresses then he can see no addresses
        mockMvc.perform(get("/addresses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses[*]", empty()));
//  16. when admin post to /addresses with address "Test 1, 00-001 City1" then address "Test 1, 00-001 City1" is returned with id 1
        mockMvc.perform(post("/addresses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "street": "Test 1",
                                    "city": "City1",
                                    "postalCode": "00-001"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address.id", is(1)))
                .andExpect(jsonPath("$.address.street", is("Test 1")))
                .andExpect(jsonPath("$.address.city", is("City1")))
                .andExpect(jsonPath("$.address.postalCode", is("00-001")));
//  17. when admin post to /addresses with address "Test 2, 00-002 City2" then address "Test 1, 00-002 City2" is returned with id 2
        mockMvc.perform(post("/addresses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "street": "Test 2",
                                    "city": "City2",
                                    "postalCode": "00-002"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address.id", is(2)))
                .andExpect(jsonPath("$.address.street", is("Test 2")))
                .andExpect(jsonPath("$.address.city", is("City2")))
                .andExpect(jsonPath("$.address.postalCode", is("00-002")));
//  18. when admin go to /addresses then he can see 2 addresses
        mockMvc.perform(get("/addresses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses[*].id", containsInAnyOrder(1, 2)));
//  19. when admin go to /warehouses then he can see no warehouses
        mockMvc.perform(get("/warehouses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouses[*]", empty()));
//  20. when admin post to /warehouses with warehouse AA then warehouse AA is returned with id 1
        mockMvc.perform(post("/warehouses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "AA",
                                    "regionalManagerEmail": "manager1@test.pl",
                                    "warehousemanEmail": "warehouseman1@test.pl",
                                    "bdfSize": "large",
                                    "maxCylindersWithoutCollarPerBdf": 60
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.warehouse.id", is(1)))
                .andExpect(jsonPath("$.warehouse.name", is("AA")))
                .andExpect(jsonPath("$.warehouse.regionalManagerEmail", is("manager1@test.pl")))
                .andExpect(jsonPath("$.warehouse.warehousemanEmail", is("warehouseman1@test.pl")))
                .andExpect(jsonPath("$.warehouse.address").value((Object) null))
                .andExpect(jsonPath("$.warehouse.bdfSize", is("LARGE")))
                .andExpect(jsonPath("$.warehouse.maxCylindersWithoutCollarPerBdf", is(60)))
                .andExpect(jsonPath("$.warehouse.active", is(false)));
//  21. when admin post to /warehouses/1/address/1?activate=true then warehouse AA is returned with id 1 and address with id 1
        mockMvc.perform(put("/warehouses/1/address/1?activate=true")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouse.id", is(1)))
                .andExpect(jsonPath("$.warehouse.name", is("AA")))
                .andExpect(jsonPath("$.warehouse.address.id", is(1)))
                .andExpect(jsonPath("$.warehouse.active", is(true)));
//  22. when admin post to /warehouses with warehouse BB then warehouse BB is returned with id 2
        mockMvc.perform(post("/warehouses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "BB",
                                    "regionalManagerEmail": "manager1@test.pl",
                                    "warehousemanEmail": "warehouseman2@test.pl",
                                    "bdfSize": "medium",
                                    "maxCylindersWithoutCollarPerBdf": 80
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.warehouse.id", is(2)))
                .andExpect(jsonPath("$.warehouse.name", is("BB")))
                .andExpect(jsonPath("$.warehouse.regionalManagerEmail", is("manager1@test.pl")))
                .andExpect(jsonPath("$.warehouse.warehousemanEmail", is("warehouseman2@test.pl")))
                .andExpect(jsonPath("$.warehouse.address").value((Object) null))
                .andExpect(jsonPath("$.warehouse.bdfSize", is("MEDIUM")))
                .andExpect(jsonPath("$.warehouse.maxCylindersWithoutCollarPerBdf", is(80)))
                .andExpect(jsonPath("$.warehouse.active", is(false)));
//  23. when admin post to /warehouses/2/address/1?activate=true then warehouse BB is returned with id 2 and address with id 1
        mockMvc.perform(put("/warehouses/2/address/1?activate=true")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouse.id", is(2)))
                .andExpect(jsonPath("$.warehouse.name", is("BB")))
                .andExpect(jsonPath("$.warehouse.address.id", is(1)))
                .andExpect(jsonPath("$.warehouse.active", is(true)));
//  24. when admin post to /warehouses with warehouse CC then warehouse CC is returned with id 3
        mockMvc.perform(post("/warehouses")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .content("""
                                {
                                    "name": "CC",
                                    "regionalManagerEmail": "manager2@test.pl",
                                    "warehousemanEmail": "warehouseman3@test.pl",
                                    "bdfSize": "large",
                                    "maxCylindersWithoutCollarPerBdf": 100
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.warehouse.id", is(3)))
                .andExpect(jsonPath("$.warehouse.name", is("CC")))
                .andExpect(jsonPath("$.warehouse.regionalManagerEmail", is("manager2@test.pl")))
                .andExpect(jsonPath("$.warehouse.warehousemanEmail", is("warehouseman3@test.pl")))
                .andExpect(jsonPath("$.warehouse.address").value((Object) null))
                .andExpect(jsonPath("$.warehouse.bdfSize", is("LARGE")))
                .andExpect(jsonPath("$.warehouse.maxCylindersWithoutCollarPerBdf", is(100)))
                .andExpect(jsonPath("$.warehouse.active", is(false)));
//  25. when admin post to /warehouses/3/address/2?activate=true then warehouse CC is returned with id 3 and address with id 2
        mockMvc.perform(put("/warehouses/3/address/2?activate=true")
                        .header("Authorization", "Bearer " +
                                generateToken(firstAdminEmail, List.of("ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.warehouse.id", is(3)))
                .andExpect(jsonPath("$.warehouse.name", is("CC")))
                .andExpect(jsonPath("$.warehouse.address.id", is(2)))
                .andExpect(jsonPath("$.warehouse.active", is(true)));
// BDF PART
//  26. when planner go to /bdfs then he can see no bdfs
        mockMvc.perform(get("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("planner@test.pl", List.of("PLANNER")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bdfs[*]", empty()));
//  27. when planner post to /bdfs then bdf is returned with id 1
        mockMvc.perform(post("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("planner@test.pl", List.of("PLANNER")))
                        .content("""
                                {
                                    "size": "large"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bdf.id", is(1)))
                .andExpect(jsonPath("$.bdf.size", is("LARGE")))
                .andExpect(jsonPath("$.bdf.slots", is(420)))
                .andExpect(jsonPath("$.bdf.ordered", is(false)))
                .andExpect(jsonPath("$.bdf.createdBy", is("planner@test.pl")))
                .andExpect(jsonPath("$.bdf.cylinders[*].cylinder.id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.bdf.cylinders[*].quantity", containsInAnyOrder(420)));
//  28. when planner  post to /bdfs then bdf is returned with id 2
        mockMvc.perform(post("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("planner@test.pl", List.of("PLANNER")))
                        .content("""
                                {
                                    "size": "large"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bdf.id", is(2)))
                .andExpect(jsonPath("$.bdf.size", is("LARGE")))
                .andExpect(jsonPath("$.bdf.slots", is(420)))
                .andExpect(jsonPath("$.bdf.ordered", is(false)))
                .andExpect(jsonPath("$.bdf.createdBy", is("planner@test.pl")))
                .andExpect(jsonPath("$.bdf.cylinders[*].cylinder.id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.bdf.cylinders[*].quantity", containsInAnyOrder(420)));
//  29. when planner go to /bdfs then he can see two bdfs
        mockMvc.perform(get("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("planner@test.pl", List.of("PLANNER")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bdfs[*]", hasSize(2)))
                .andExpect(jsonPath("$.bdfs[*].id", containsInAnyOrder(1, 2)));
//  30. when regional manager1 go to /bdfs then he can see no bdfs
        mockMvc.perform(get("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("manager1@test.pl", List.of("REGIONAL_MANAGER")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bdfs[*]", empty()));
//  31. when regional manager1 post to /bdfs then bdf is returned with id 3
        mockMvc.perform(post("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("manager1@test.pl", List.of("REGIONAL_MANAGER")))
                        .content("""
                                {
                                    "size": "medium"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bdf.id", is(3)))
                .andExpect(jsonPath("$.bdf.size", is("MEDIUM")))
                .andExpect(jsonPath("$.bdf.slots", is(406)))
                .andExpect(jsonPath("$.bdf.ordered", is(false)))
                .andExpect(jsonPath("$.bdf.createdBy", is("manager1@test.pl")))
                .andExpect(jsonPath("$.bdf.cylinders[*].cylinder.id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.bdf.cylinders[*].quantity", containsInAnyOrder(406)));
//  32. when regional manager1 go to /bdfs then he can see one bdf
        mockMvc.perform(get("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("manager1@test.pl", List.of("REGIONAL_MANAGER")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bdfs[*]", hasSize(1)))
                .andExpect(jsonPath("$.bdfs[*].id", containsInAnyOrder(3)));
//  33. when warehouseman3 go to /bdfs then he can see no bdfs
        mockMvc.perform(get("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman3@test.pl", List.of("WAREHOUSEMAN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bdfs[*]", empty()));
//  34. when warehouseman3 post to /bdfs then bdf is returned with id 4
        mockMvc.perform(post("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman3@test.pl", List.of("WAREHOUSEMAN")))
                        .content("""
                                {
                                    "size": "large"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.bdf.id", is(4)))
                .andExpect(jsonPath("$.bdf.size", is("LARGE")))
                .andExpect(jsonPath("$.bdf.slots", is(420)))
                .andExpect(jsonPath("$.bdf.ordered", is(false)))
                .andExpect(jsonPath("$.bdf.createdBy", is("warehouseman3@test.pl")))
                .andExpect(jsonPath("$.bdf.cylinders[*].cylinder.id", containsInAnyOrder(1)))
                .andExpect(jsonPath("$.bdf.cylinders[*].quantity", containsInAnyOrder(420)));
//  35. when warehouseman3 patch to /bdfs/4/cylinders/9 then cylinders with id 9 are assigned to bdf with id 4
        mockMvc.perform(patch("/bdfs/4/cylinders/9")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman3@test.pl", List.of("WAREHOUSEMAN")))
                        .content("""
                                {
                                    "newQuantity": 10
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
//  36. when warehouseman3 go to /bdfs then he can see one bdf with id 4
        mockMvc.perform(get("/bdfs")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman3@test.pl", List.of("WAREHOUSEMAN")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bdfs[*]", hasSize(1)))
                .andExpect(jsonPath("$.bdfs[*].id", containsInAnyOrder(4)))
                .andExpect(jsonPath("$.bdfs[*].cylinders[*].cylinder.id", containsInAnyOrder(1, 9)))
                .andExpect(jsonPath("$.bdfs[*].cylinders[*].quantity", containsInAnyOrder(10, 400)));
// ORDER PART
//  37. when planner post to /orders/search then he can see no orders
        mockMvc.perform(post("/orders/search")
                        .header("Authorization", "Bearer " +
                                generateToken("planner@test.pl", List.of("PLANNER")))
                        .content("""
                                {}
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*]").isEmpty());
//  38. when planner post to /orders then order is returned with id 1
        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " +
                                generateToken("planner@test.pl", List.of("PLANNER")))
                        .content("""
                                {
                                    "bdfIds": [1, 2],
                                    "scheduledCompletionDate": "06-06-2025",
                                    "warehouseName": "AA"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.order.id", is(1)))
                .andExpect(jsonPath("$.order.bdfIds[*]", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.order.createdBy", is("planner@test.pl")))
                .andExpect(jsonPath("$.order.completionDate", is("06-06-2025")))
                .andExpect(jsonPath("$.order.warehouseName", is("AA")))
                .andExpect(jsonPath("$.order.status", is("NEW")));
//  40. when regional manager1 post to /orders then order is returned with id 2
        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " +
                                generateToken("manager1@test.pl", List.of("REGIONAL_MANAGER")))
                        .content("""
                                {
                                    "bdfIds": [3],
                                    "scheduledCompletionDate": "08-06-2025",
                                    "warehouseName": "BB"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.order.id", is(2)))
                .andExpect(jsonPath("$.order.bdfIds[*]", containsInAnyOrder(3)))
                .andExpect(jsonPath("$.order.createdBy", is("manager1@test.pl")))
                .andExpect(jsonPath("$.order.completionDate", is("08-06-2025")))
                .andExpect(jsonPath("$.order.warehouseName", is("BB")))
                .andExpect(jsonPath("$.order.status", is("NEW")));
//  41. when warehouseman3 post to /orders then order is returned with id 3
        mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman3@test.pl", List.of("WAREHOUSEMAN")))
                        .content("""
                                {
                                    "bdfIds": [4],
                                    "scheduledCompletionDate": "10-06-2025",
                                    "warehouseName": "CC"
                                }
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.order.id", is(3)))
                .andExpect(jsonPath("$.order.bdfIds[*]", containsInAnyOrder(4)))
                .andExpect(jsonPath("$.order.createdBy", is("warehouseman3@test.pl")))
                .andExpect(jsonPath("$.order.completionDate", is("10-06-2025")))
                .andExpect(jsonPath("$.order.warehouseName", is("CC")))
                .andExpect(jsonPath("$.order.status", is("NEW")));
//  42. when planner post to /orders/search then he can see three orders
        mockMvc.perform(post("/orders/search")
                        .header("Authorization", "Bearer " +
                                generateToken("planner@test.pl", List.of("PLANNER")))
                        .content("""
                                {}
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*]", hasSize(3)))
                .andExpect(jsonPath("$.orders[*].id", containsInAnyOrder(1, 2, 3)));
//  43. when regional manager1 post to /orders/search then he can see two orders
        mockMvc.perform(post("/orders/search")
                        .header("Authorization", "Bearer " +
                                generateToken("manager1@test.pl", List.of("REGIONAL_MANAGER")))
                        .content("""
                                {}
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*]", hasSize(2)))
                .andExpect(jsonPath("$.orders[*].id", containsInAnyOrder(1, 2)));
//  44. when regional manager2 post to /orders/search then he can see one order
        mockMvc.perform(post("/orders/search")
                        .header("Authorization", "Bearer " +
                                generateToken("manager2@test.pl", List.of("REGIONAL_MANAGER")))
                        .content("""
                                {}
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*]", hasSize(1)))
                .andExpect(jsonPath("$.orders[*].id", containsInAnyOrder(3)));
//  45. when warehouseman1 post to /orders/search then he can see one order with id 1
        mockMvc.perform(post("/orders/search")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman1@test.pl", List.of("WAREHOUSEMAN")))
                        .content("""
                                {}
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*]", hasSize(1)))
                .andExpect(jsonPath("$.orders[*].id", containsInAnyOrder(1)));
//  46. when warehouseman2 post to /orders/search then he can see one order with id 2
        mockMvc.perform(post("/orders/search")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman2@test.pl", List.of("WAREHOUSEMAN")))
                        .content("""
                                {}
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*]", hasSize(1)))
                .andExpect(jsonPath("$.orders[*].id", containsInAnyOrder(2)));
//  47. when warehouseman3 post to /orders/search then he can see one order with id 3
        mockMvc.perform(post("/orders/search")
                        .header("Authorization", "Bearer " +
                                generateToken("warehouseman3@test.pl", List.of("WAREHOUSEMAN")))
                        .content("""
                                {}
                                """.trim())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[*]", hasSize(1)))
                .andExpect(jsonPath("$.orders[*].id", containsInAnyOrder(3)));
    }

    private String generateToken(String userEmail, List<String> roles) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(Duration.ofMinutes(10));
        Algorithm algorithm = Algorithm.RSA256(null, (RSAPrivateKey) keyPair.getPrivate());
        return JWT.create()
                .withSubject(userEmail)
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withIssuer("LPGFlow")
                .withClaim("roles", roles)
                .sign(algorithm);
    }
}
