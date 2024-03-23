package adapter;

import androidx.annotation.NonNull;

public class NotificationsInfo {

    private String notifationsDetails;

    public NotificationsInfo(String notifationsDetails) {
        this.notifationsDetails = notifationsDetails;
    }

    public String getNotifationsDetails() {
        return notifationsDetails;
    }

    public void setNotifationsDetails(String notifationsDetails) {
        this.notifationsDetails = notifationsDetails;
    }

    @NonNull
    @Override
    public String toString() {
        return notifationsDetails;
    }
}
