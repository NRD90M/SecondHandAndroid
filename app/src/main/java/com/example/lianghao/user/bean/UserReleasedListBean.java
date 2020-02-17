package com.example.lianghao.user.bean;

import java.util.List;

public class UserReleasedListBean {
    /**
     * ret_code : 0
     * user_info : 用户存在
     * info : [{"imageUrl":"/media/display_images/1556077169190058502.jpg","desc":"魅族16s","price":1500,"message_count":0,"browse_count":0,"pk":1},{"imageUrl":"/media/display_images/ChMkJl4CxKuIR8dCAAII6qMHzVwAAv8IQLZnOcAAgkC880.jpg","desc":"测试商品","price":2000,"message_count":0,"browse_count":0,"pk":2},{"imageUrl":"/media/display_images/O1CN01FHOCMn1psZfhzFyPO_0-fleamarket.jpg_728x728.jpg","desc":"欧舒丹护手霜30ml","price":25,"message_count":0,"browse_count":0,"pk":5},{"imageUrl":"/media/display_images/76958e7c497d4c0d8fb30b6a31d0f28b.jpg","desc":"111111111","price":11,"message_count":0,"browse_count":0,"pk":6}]
     */

    private int ret_code;
    private String user_info;
    private List<InfoBean> info;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public String getUser_info() {
        return user_info;
    }

    public void setUser_info(String user_info) {
        this.user_info = user_info;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * imageUrl : /media/display_images/1556077169190058502.jpg
         * desc : 魅族16s
         * price : 1500.0
         * message_count : 0
         * browse_count : 0
         * pk : 1
         */

        private String imageUrl;
        private String desc;
        private double price;
        private int message_count;
        private int browse_count;
        private int pk;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getMessage_count() {
            return message_count;
        }

        public void setMessage_count(int message_count) {
            this.message_count = message_count;
        }

        public int getBrowse_count() {
            return browse_count;
        }

        public void setBrowse_count(int browse_count) {
            this.browse_count = browse_count;
        }

        public int getPk() {
            return pk;
        }

        public void setPk(int pk) {
            this.pk = pk;
        }
    }
}
