package cn.vko.ring.classgroup.model;

import java.io.Serializable;
import java.util.List;

/**
 * desc:
 * Created by jiarh on 16/5/18 18:08.
 */
public class UserGroupTeamData  implements Serializable{


    /**
     * code : 0
     * msg :
     * stime : 1463566679897
     * data : [{"owner":"14454185225241561","tname":"瞅瞅、heyAndroid、hi-and等人","maxusers":200,"tid":3940735,"size":2},{"owner":"14495640431820009","tname":"2016届1班","maxusers":200,"tid":4026299,"size":8},{"owner":"14454185225241561","tname":"我是来测试建群的","maxusers":200,"tid":3941989,"size":9},{"owner":"14495640431820009","tname":"2016届2班","maxusers":200,"tid":4025613,"size":12}]
     */

    private int code;
    private String msg;
    private long stime;
    /**
     * owner : 14454185225241561
     * tname : 瞅瞅、heyAndroid、hi-and等人
     * maxusers : 200
     * tid : 3940735
     * size : 2
     */

    private List<DataEntity> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getStime() {
        return stime;
    }

    public void setStime(long stime) {
        this.stime = stime;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class DataEntity {
        private String owner;
        private String tname;
        private int maxusers;
        private int tid;
        private int size;

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public String getTname() {
            return tname;
        }

        public void setTname(String tname) {
            this.tname = tname;
        }

        public int getMaxusers() {
            return maxusers;
        }

        public void setMaxusers(int maxusers) {
            this.maxusers = maxusers;
        }

        public int getTid() {
            return tid;
        }

        public void setTid(int tid) {
            this.tid = tid;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
