package com.chinaoly.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 鞋子账单实体类
 *
 * @author liuq
 * @date 2019-11-07 17:09:05
 */
@Entity
@Table(name = "t_shoes")
public class BillShoes implements java.io.Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "SHOES_ID")
    @GeneratedValue(generator = "UUID")
    private String shoesId; //

    @Column(name = "MODEL")
    private String model; //型号
    @Column(name = "BRAND_TYPE")
    private Integer brandType; //品牌类型 0 Nike 1 Adidas 2 Converse
    @Column(name = "SHOE_SIZE")
    private String shoeSize; //码数
    @Column(name = "NUMBER")
    private Integer number; //数量
    @Column(name = "TOTAL_BUY_PRICE")
    private BigDecimal totalBuyPrice; //进货总价
    @Column(name = "TOTAL_SALES_PRICE")
    private BigDecimal totalSalesPrice; //总售价
    @Column(name = "FREIGHT")
    private BigDecimal freight; //运费
    @Column(name = "SERVICE_CHARGE")
    private BigDecimal serviceCharge; //手续费
    @Column(name = "DEPOSIT")
    private BigDecimal deposit; //押金
    @Column(name = "PROFIT")
    private BigDecimal profit; //利润 = 总售价 - 进货总价 - 运费 - 手续费 - 押金
    @Column(name = "DEDUCT")
    private Integer deduct; //押金是否扣除 0 未扣除 1 已扣除
    @Column(name = "SELL_STATUS")
    private Integer sellStatus; //售卖状态 0 未出售  1 已出售
    @Column(name = "EFFECT_FLAG")
    private Integer effectFlag; //有效标志 是否有效，1表示有效，0表示无效

    @Column(name = "CREATE_TIME")
    private Date createTime; //创建时间
    @Column(name = "UPDATE_TIME")
    private Date updateTime; //更新时间
    @Column(name = "MEMO")
    private String memo; //备注

    public String getShoesId() {
        return this.shoesId;
    }

    public void setShoesId(String shoesId) {
        this.shoesId = shoesId;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getBrandType() {
        return this.brandType;
    }

    public void setBrandType(Integer brandType) {
        this.brandType = brandType;
    }

    public String getShoeSize() {
        return shoeSize;
    }

    public void setShoeSize(String shoeSize) {
        this.shoeSize = shoeSize;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getNumber() {
        return this.number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public BigDecimal getTotalBuyPrice() {
        return this.totalBuyPrice;
    }

    public void setTotalBuyPrice(BigDecimal totalBuyPrice) {
        this.totalBuyPrice = totalBuyPrice;

    }

    public Integer getSellStatus() {
        return sellStatus;
    }

    public void setSellStatus(Integer sellStatus) {
        this.sellStatus = sellStatus;
    }

    public BigDecimal getTotalSalesPrice() {
        return this.totalSalesPrice;
    }

    public void setTotalSalesPrice(BigDecimal totalSalesPrice) {
        this.totalSalesPrice = totalSalesPrice;
    }

    public BigDecimal getFreight() {
        return this.freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getDeposit() {
        return this.deposit;
    }

    public void setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
    }

    public BigDecimal getProfit() {
        return this.profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public Integer getDeduct() {
        return this.deduct;
    }

    public void setDeduct(Integer deduct) {
        this.deduct = deduct;
    }

    public Integer getEffectFlag() {
        return this.effectFlag;
    }

    public void setEffectFlag(Integer effectFlag) {
        this.effectFlag = effectFlag;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}
