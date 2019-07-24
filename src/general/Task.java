package general;

import java.util.Date;


public class Task {
    public static int lastID;
    private int id;
    private String header;
    private String body;
    private boolean hasReminder;
    private long createTime;

    public Task(int id, String header) {
        this.id = id;
        this.header = header;
    }

    public Task(int id, String header, String body, boolean hasReminder) {
        this.id = id;
        this.header = header;
        this.body = body;
        this.createTime = new Date().getTime();
        this.hasReminder = hasReminder;
    }

    public boolean hasReminder() {
        return hasReminder;
    }

    public void setReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getHeader() {
        return header;
    }

//    public void setHeader(String header) {
//        this.header = header;
//    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }


}
