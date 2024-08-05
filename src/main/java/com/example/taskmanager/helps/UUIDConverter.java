package com.example.taskmanager.helps;

import java.util.UUID;

public class UUIDConverter {
    public static UUID checkStringToUUID(String format) {
        if (format.length() < 34) {
            return null;
        }
        if (format.contains("-")) {
            return UUID.fromString(format);
        }
        format = format.substring(2);
        String formattedUUID = String.format(
                "%s-%s-%s-%s-%s",
                format.substring(0, 8),
                format.substring(8, 12),
                format.substring(12, 16),
                format.substring(16, 20),
                format.substring(20, 32));
        return UUID.fromString(formattedUUID);
    }
}