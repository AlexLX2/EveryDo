package general;

import java.util.Date;


public class Task {
    static int lastID;
    private int id;
    private String header;
    private String body;
    private boolean hasReminder;
    private long createTime;

    Task(int id, String header) {
        this.id = id;
        this.header = header;
    }

    Task(int id, String header, String body, boolean hasReminder) {
        this.id = id;
        this.header = header;
        this.body = body;
        this.createTime = new Date().getTime();
        this.hasReminder = hasReminder;
    }


    boolean hasReminder() {
        return hasReminder;
    }

    public void setReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getHeader() {
        return this.header;
    }

//    public void setHeader(String header) {
//        this.header = header;
//    }

    String getBody() {
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
