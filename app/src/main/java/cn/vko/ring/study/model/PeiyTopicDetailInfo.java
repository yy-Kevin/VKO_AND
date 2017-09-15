package cn.vko.ring.study.model;

import java.io.Serializable;
import java.util.List;

import cn.vko.ring.common.base.BaseResponseInfo;
import cn.vko.ring.home.model.Pager;

/**
 * Created by A on 2016/12/5.
 */
public class PeiyTopicDetailInfo extends BaseResponseInfo {

    private PeiyDetailInfo data;


    public PeiyDetailInfo getData() {

        return data;
    }


    public void setData(PeiyDetailInfo data) {

        this.data = data;
    }

    public class PeiyDetailInfo implements Serializable {
        private Pager pager;
        private List<CourceInfo> videos;
        private TopicInfo obj;

        public Pager getPager() {

            return pager;
        }

        public void setPager(Pager pager) {

            this.pager = pager;
        }

        public List<CourceInfo> getVideos() {
            return videos;
        }

        public void setVideos(List<CourceInfo> videos) {

            this.videos = videos;
        }

        public TopicInfo getObj() {

            return obj;
        }

        public void setObj(TopicInfo obj) {

            this.obj = obj;
        }

    }
}
