package com.rempms_message_service.util;

public class ExceptionExtractor {

    /**
     * Utility method to extract the deepest cause message.
     */
    public static String getRootCauseMessage(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        return root.getMessage();
    }
}
