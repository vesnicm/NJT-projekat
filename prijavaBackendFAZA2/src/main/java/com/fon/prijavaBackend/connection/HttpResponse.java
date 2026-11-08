/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fon.prijavaBackend.connection;

import java.util.Map;
import org.springframework.http.HttpStatus;

/**
 *
 * @author dule1
 */
public class HttpResponse {
    
    public static Response getResponseWithData(String message, Map<?, ?> data, HttpStatus status) {
        return new Response(message, data, status);
    }

    public static Response getResponse(String message, HttpStatus status) {
        return new Response(message, status);
    }
}
