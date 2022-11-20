package me.choicore.junittest.test;


import org.junit.jupiter.api.*;


public class TestLifeCycle {


    @BeforeAll
    static void beforeAll() {
        System.out.println("### call -> @BeforeAll ###");
        System.out.println("###########################");

    }

    @AfterAll
    static void afterAll() {
        System.out.println("###########################");
        System.out.println("### call -> @AfterAll ###");

    }


    @BeforeEach
    void beforeEach() {
        System.out.println("### call -> @BeforeEach ###");
    }

    @AfterEach
    void afterEach() {
        System.out.println("### call -> @AfterEach ###");
    }

    @Test
    @DisplayName("STEP 1")
    void test1() {
        System.out.println("### task -> TEST 1 ###");
    }

    @Test
    @DisplayName("STEP 2")
    void test2() {
        System.out.println("### task -> TEST 2 ###");
    }

    @Test
    @DisplayName("STEP 3")
    @Disabled
    void test3() {
        System.out.println("### task -> TEST 3 ###");
    }

}
