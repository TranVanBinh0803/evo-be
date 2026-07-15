package com.binhtv.orderservice.model.dto;

public record ApiResponse<T>(String message, int statusCode, T data) {}
