package com.coolweather.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangZ on 2015/5/11.
 */
public class CoolWeatherDB {
    /*
        数据库名
     */
    public static final String DB_NAME = "cool_weather";

    /*
        数据库版本
     */
    public static final int VERSION = 1;

    //单例
    private static CoolWeatherDB coolWeatherDB;

    private SQLiteDatabase db;

    /*
        将构造方法私有化
     */
    private CoolWeatherDB(Context context){
        CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context , DB_NAME , null , VERSION);
        db = dbHelper.getWritableDatabase();
    }
    /*
            获取CoolWeatherDB的实例
    */
    public synchronized static CoolWeatherDB getInstance(Context context){
        if(coolWeatherDB == null){
            coolWeatherDB = new CoolWeatherDB(context);
        }
        return  coolWeatherDB;
    }

    /*
        将province实例存储到数据库
     */
    public void saveProvince(Province province){
        if(province != null){
            String sql = "insert into province (province_name,province_code) values('" + province.getProvinceName() + "','" + province.getProvinceCode() + "')";
            db.execSQL(sql);
        }
    }

    /*
        从数据库读取全国所有的省份信息。
     */
    public List<Province> loadProvinces(){
        List<Province> list = new ArrayList<Province>();
        String sql = "select * from province";
        Cursor cursor = db.rawQuery(sql , null);
        if(cursor.moveToFirst()){//如果有值的话，这个方法会返回真
            while(cursor.moveToNext()) {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    /*
        将City实例存储到数据库。
     */
    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("City" , null , values);
        }
    }

    /*
        从数据库读取某省下的所有城市信息
     */
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        String sql = "select * from City where province_id = ?";
        Cursor cursor = db.rawQuery(sql , new String[]{String.valueOf(provinceId) });
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
                list.add(city);
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }

    /*
        将County实例存储到数据库
     */
    public void saveCounty(County county){
        if(county != null){
            ContentValues values = new ContentValues();
            values.put("county_name" , county.getCountyName());
            values.put("county_code" , county.getCountyCode());
            values.put("city_id" , county.getCityId());
            db.insert("County" , null , values);
        }
    }

    /*
        从数据库中读取某城市下的所有县信息
     */
    public List<County> loadCounties(int cityId){
        List<County> list = new ArrayList<County>();
        //使用自带的api
        Cursor cursor = db.query("County", null, "city_id = ?" , new String[]{String.valueOf(cityId) },null, null, null);
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
                list.add(county);
            }
        }
        if(cursor != null){
            cursor.close();
        }
        return list;
    }
}
