package com.example.lianghao.home.bean;

import java.util.List;

// 解析请求所有商品返回的json
public class ResultBeanData {


    /**
     * code : 200
     * msg : 请求成功
     * result : [{"price":1500,"desc":"魅族16s","category":"手机","image":"/media/display_images/1556077169190058502.jpg","product_id":1,"user":"13777893886@163.com"},{"price":2000,"desc":"测试商品","category":"平板电脑","image":"/media/display_images/ChMkJl4CxKuIR8dCAAII6qMHzVwAAv8IQLZnOcAAgkC880.jpg","product_id":2,"user":"13777893886@163.com"}]
     */

    private int code;
    private String msg;
    private List<ResultBean> result;

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

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * price : 1500.0
         * desc : 魅族16s
         * category : 手机
         * image : /media/display_images/1556077169190058502.jpg
         * product_id : 1
         * user : 13777893886@163.com
         */

        private double price;
        private String desc;
        private String category;
        private String image;
        private int product_id;
        private String user;

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

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }
    }
}
