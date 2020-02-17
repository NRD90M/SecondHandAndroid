package com.example.lianghao.app.bean;

public class GoodsInfoBean {
    /**
     * ret_code : 0
     * info : {"user_head_portrait":"/media/portraits/u742459268934129855fm26gp0.jpg","username":"李霖烨","email":"13777893886@163.com","location":"浙江杭州","price":1500,"desc":"魅族16s","display_image":"/media/display_images/1556077169190058502.jpg"}
     */

    private int ret_code;
    private InfoBean info;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * user_head_portrait : /media/portraits/u742459268934129855fm26gp0.jpg
         * username : 李霖烨
         * email : 13777893886@163.com
         * location : 浙江杭州
         * price : 1500.0
         * desc : 魅族16s
         * display_image : /media/display_images/1556077169190058502.jpg
         */

        private String user_head_portrait;
        private String username;
        private String email;
        private String location;
        private double price;
        private String desc;
        private String display_image;

        public String getUser_head_portrait() {
            return user_head_portrait;
        }

        public void setUser_head_portrait(String user_head_portrait) {
            this.user_head_portrait = user_head_portrait;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDisplay_image() {
            return display_image;
        }

        public void setDisplay_image(String display_image) {
            this.display_image = display_image;
        }
    }
}
