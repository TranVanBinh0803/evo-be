package com.binhtv.apigateway.dto;

public record ApiResponse<T>(String message, int statusCode, T data) {
}