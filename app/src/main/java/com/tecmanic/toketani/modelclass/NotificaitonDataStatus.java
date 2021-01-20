package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class NotificaitonDataStatus {

    @Json(name = "app_notice_id")
    private String appNoticeId;
    @Json(name = "status")
    private String status;
    @Json(name = "notice")
    private String notice;

    public String getAppNoticeId() {
        return appNoticeId;
    }

    public void setAppNoticeId(String appNoticeId) {
        this.appNoticeId = appNoticeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
