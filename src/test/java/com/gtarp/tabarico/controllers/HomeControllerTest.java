package com.gtarp.tabarico.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class HomeControllerTest {
    @InjectMocks
    HomeController homeController;

    @Test
    public void getHomePageTest() {
        //GIVEN we should get this string
        String expectedString = "home";

        //WHEN we call this method
        String actualString = homeController.getHomePage();

        //THEN we get the correct string
        assertEquals(expectedString, actualString);
    }
}
