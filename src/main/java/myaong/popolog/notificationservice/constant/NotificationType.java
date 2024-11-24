package myaong.popolog.notificationservice.constant;

import lombok.Getter;

@Getter
public enum NotificationType {
    COMMENT("COMMENT"),
    REPLY("REPLY"),
    LIKE("LIKE"),
    FOLLOW("FOLLOW"),
    NOTICE("NOTICE"),
    INQUIRY_REPLY("INQUIRY_REPLY");

    private final String label;

    NotificationType(String label) {
        this.label = label;
    }
    public static NotificationType fromLabel(String type) {
        for (NotificationType t : NotificationType.values()) {
            if (t.label.equals(type)) {
                return t;
            }
        }
        return null;
    }

}
