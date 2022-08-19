package com.xiaoxiz.xiaomaibu.bean;

public class Ddanxiz {
    private String xizehao;
    private String gid;
    private String orderno;
    private int gnum;
    private float gprice;
    private float total;
    private String status;


    public String getXizehao() {
        return xizehao;
    }

    public void setXizehao(String xizehao) {
        this.xizehao = xizehao;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public int getGnum() {
        return gnum;
    }

    public void setGnum(int gnum) {
        this.gnum = gnum;
    }

    public float getGprice() {
        return gprice;
    }

    public void setGprice(float gprice) {
        this.gprice = gprice;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Ddanxiz{" +
                "xizehao='" + xizehao + '\'' +
                ", gid='" + gid + '\'' +
                ", orderno='" + orderno + '\'' +
                ", gnum=" + gnum +
                ", gprice=" + gprice +
                ", total=" + total +
                ", status='" + status + '\'' +
                '}';
    }
}
