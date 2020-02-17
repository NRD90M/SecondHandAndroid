package com.example.lianghao.message.bean;

import java.util.List;

public class MessageListBean {
    /**
     * ret_code : 0
     * info : [{"pk":"3","imageUrl":"/media/display_images/O1CN01tcqGFk1kr8LcPnyif_0-fleamarket.jpg_728x728.jpg","owner_username":"tb792593312","owner_head_portrait":"/media/portraits/u742459268934129855fm26gp0_7Wr4qsw.jpg","last_message":"这是最后一条消息"}]
     * pks : ["3"]
     */

    private int ret_code;
    private List<InfoBean> info;
    private List<String> pks;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public List<String> getPks() {
        return pks;
    }

    public void setPks(List<String> pks) {
        this.pks = pks;
    }

    public static class InfoBean {
        /**
         * pk : 3
         * imageUrl : /media/display_images/O1CN01tcqGFk1kr8LcPnyif_0-fleamarket.jpg_728x728.jpg
         * owner_username : tb792593312
         * owner_head_portrait : /media/portraits/u742459268934129855fm26gp0_7Wr4qsw.jpg
         * last_message : 这是最后一条消息
         */

        private String pk;
        private String imageUrl;
        private String owner_username;
        private String owner_head_portrait;
        private String last_message;

        public String getPk() {
            return pk;
        }

        public void setPk(String pk) {
            this.pk = pk;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getOwner_username() {
            return owner_username;
        }

        public void setOwner_username(String owner_username) {
            this.owner_username = owner_username;
        }

        public String getOwner_head_portrait() {
            return owner_head_portrait;
        }

        public void setOwner_head_portrait(String owner_head_portrait) {
            this.owner_head_portrait = owner_head_portrait;
        }

        public String getLast_message() {
            return last_message;
        }

        public void setLast_message(String last_message) {
            this.last_message = last_message;
        }
    }
}
