package com.example.lianghao;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

public class CityBean implements IPickerViewData {
    /**
     * province : 北京
     * city_list : ["北京"]
     */

    private String province;
    private List<String> city_list;

    public String getProvince(){
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<String> getCityList() {
        return city_list;
    }

    public void setCityList(List<String> cityList) {
        this.city_list = cityList;
    }

    @Override
    public String getPickerViewText() {
        return province;
    }
}
