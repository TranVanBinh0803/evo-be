package com.binhtv.sendsmsservice.dto;

public record ApiResponse<T>(String message, int statusCode, T data) {}
