package com.leos.contactsplugin.contacts_plugin;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * @author: Leo
 * @time: 2022/4/23
 * @desc:
 */
public class ContactsData {

    private String name;
    private List<Number> number;
    private long lastUpdate;

    private int contact_times;
    private long last_contact_time;

    private String nickname;

    private String relation;

    private String status;

    public int getContact_times() {
        return contact_times;
    }

    public void setContact_times(int contact_times) {
        this.contact_times = contact_times;
    }

    public long getLast_contact_time() {
        return last_contact_time;
    }

    public void setLast_contact_time(long last_contact_time) {
        this.last_contact_time = last_contact_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Number> getNumber() {
        return number;
    }

    public void setNumber(List<Number> number) {
        this.number = number;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @NonNull
    @Override
    public String toString() {
        return "ClientContactsData{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", lastUpdate=" + lastUpdate +
                ", contact_times=" + contact_times +
                ", last_contact_time=" + last_contact_time +
                ", nickname='" + nickname + '\'' +
                ", relation='" + relation + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public static class Number {
        private String number;
        private String last_time_used;
        private int time_used;
        private String type_label;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getLast_time_used() {
            return last_time_used;
        }

        public void setLast_time_used(String last_time_used) {
            this.last_time_used = last_time_used;
        }

        public int getTime_used() {
            return time_used;
        }

        public void setTime_used(int time_used) {
            this.time_used = time_used;
        }

        public String getType_label() {
            return type_label;
        }

        public void setType_label(String type_label) {
            this.type_label = type_label;
        }

        @NonNull
        @Override
        public String toString() {
            return "Number{" +
                    "number='" + number + '\'' +
                    ", last_time_used='" + last_time_used + '\'' +
                    ", time_used='" + time_used + '\'' +
                    ", type_label='" + type_label + '\'' +
                    '}';
        }
    }

    public static class ContactDetail {
        private int contact_times;
        private long last_contact_time;
        private String nickname;
        private String relation;

        private String status;

        public int getContact_times() {
            return contact_times;
        }

        public void setContact_times(int contact_times) {
            this.contact_times = contact_times;
        }

        public long getLast_contact_time() {
            return last_contact_time;
        }

        public void setLast_contact_time(long last_contact_time) {
            this.last_contact_time = last_contact_time;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @NonNull
        @Override
        public String toString() {
            return "ContactDetail{" +
                    "contact_times='" + contact_times + '\'' +
                    ", last_contact_time='" + last_contact_time + '\'' +
                    ", nickname=" + nickname +
                    ", relation='" + relation + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }
}
