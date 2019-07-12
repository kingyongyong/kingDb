package com.sqy.kingdb.test;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CarEntity {
    public Byte aa = 8;
    public Short bb = 12;
    public Integer cc = 321;
    public Long dd = 6521L;
    public Float ee = 78.87f;
    public Double ff = 67.980d;
    public Boolean gg = true;
    public String hh = "sunqiyong";
    public Date date = new Date(System.currentTimeMillis());
    public Time time = new Time(System.currentTimeMillis());
    public Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    public List<Byte> aas = new ArrayList<>();
    public List<Short> bbs = new ArrayList<>();
    public List<Integer> ccs = new ArrayList<>();
    public List<Long> dds = new ArrayList<>();
    public List<Float> ees = new ArrayList<>();
    public List<Double> ffs = new ArrayList<>();
    public List<Boolean> ggs = new ArrayList<>();
    public List<String> hhs = new ArrayList<>();

    public CarEntity() {
        aas.add((byte) 1);
        aas.add((byte) 2);
        bbs.add((short) 3);
        bbs.add((short) 4);
        ccs.add(5);
        ccs.add(6);
        dds.add((long) 7);
        dds.add((long) 8);
        ees.add(12.34f);
        ees.add(56.78f);
        ffs.add(123.456d);
        ffs.add(789.102d);
        ggs.add(true);
        ggs.add(false);
        hhs.add("孙其勇");
        hhs.add("哈哈哈哈");
    }

    public List<Byte> getAas() {

        return aas;
    }

    public void setAas(List<Byte> aas) {
        this.aas = aas;
    }

    public List<Short> getBbs() {

        return bbs;
    }

    public void setBbs(List<Short> bbs) {
        this.bbs = bbs;
    }

    public List<Integer> getCcs() {

        return ccs;
    }

    public void setCcs(List<Integer> ccs) {
        this.ccs = ccs;
    }

    public List<Long> getDds() {

        return dds;
    }

    public void setDds(List<Long> dds) {
        this.dds = dds;
    }

    public List<Float> getEes() {

        return ees;
    }

    public void setEes(List<Float> ees) {
        this.ees = ees;
    }

    public List<Double> getFfs() {

        return ffs;
    }

    public void setFfs(List<Double> ffs) {
        this.ffs = ffs;
    }

    public List<Boolean> getGgs() {

        return ggs;
    }

    public void setGgs(List<Boolean> ggs) {
        this.ggs = ggs;
    }

    public List<String> getHhs() {

        return hhs;
    }

    public void setHhs(List<String> hhs) {
        this.hhs = hhs;
    }
}
