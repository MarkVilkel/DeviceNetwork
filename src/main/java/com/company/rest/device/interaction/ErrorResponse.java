package com.company.rest.device.interaction;

import java.util.List;

public record ErrorResponse(String message, List<String> details) {
}
