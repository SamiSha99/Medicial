package com.example.medicial.Model;

public class Data {
    int _Med_Id, _Med_Amount;
    String _Med_Name, _Med_Desc, _Med_Image, _Time, _Date;

    public Data(int _Med_Id, String _Med_Name, int _Med_Amount, String _Med_Desc, String _Med_Image, String _Time, String _Date) {
        this._Med_Id = _Med_Id;
        this._Med_Name = _Med_Name;
        this._Med_Amount = _Med_Amount;
        this._Med_Desc = _Med_Desc;
        this._Med_Image = _Med_Image;
        this._Time = _Time;
        this._Date = _Date;
    }

    public int get_Med_Id() {
        return _Med_Id;
    }

    public void set_Med_Id(int _Med_Id) {
        this._Med_Id = _Med_Id;
    }

    public int get_Med_Amount() {
        return _Med_Amount;
    }

    public void set_Med_Amount(int _Med_Amount) {
        this._Med_Amount = _Med_Amount;
    }

    public String get_Med_Name() {
        return _Med_Name;
    }

    public void set_Med_Name(String _Med_Name) {
        this._Med_Name = _Med_Name;
    }

    public String get_Med_Desc() {
        return _Med_Desc;
    }

    public void set_Med_Desc(String _Med_Desc) {
        this._Med_Desc = _Med_Desc;
    }

    public String get_Med_Image() {
        return _Med_Image;
    }

    public void set_Med_Image(String _Med_Image) {
        this._Med_Image = _Med_Image;
    }

    public String get_Time() {
        return _Time;
    }

    public void set_Time(String _Time) {
        this._Time = _Time;
    }

    public String get_Date() {
        return _Date;
    }

    public void set_Date(String _Date) {
        this._Date = _Date;
    }
}