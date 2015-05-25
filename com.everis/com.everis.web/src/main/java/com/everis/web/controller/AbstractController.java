package com.everis.web.controller;

import java.io.IOException;


public interface AbstractController {

    void render(String rendered) throws IOException;
}