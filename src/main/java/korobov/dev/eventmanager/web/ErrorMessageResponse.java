package korobov.dev.eventmanager.web;

import java.time.LocalDateTime;

public record ErrorMessageResponse(
        String message,
        String detailedMessage,
        LocalDateTime dateTime
) {
}
