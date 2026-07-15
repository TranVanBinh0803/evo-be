package com.binhtv.userservice.dto;

public record ApiResponse<T>(String message, int statusCode, T data) {}
