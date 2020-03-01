package com.chinaoly.Model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 门票账单实体类
 *
 * @author liuq
 * @date 2019-12-18 14:02:19
 */
@Entity
@Table(name = "t_ticket")
public class BillTicket implements java.io.Serializable {

    private static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "TICKET_ID")
    @GeneratedValue(generator = "UUID")
    private String ticketId; //

    @Column(name = "TICKET_NAME")
    private String ticketName; //门票名称
    @Column(name = "TICKET_DATE")
    private String ticketDate; //门票日期
    @Column(name = "TICKET_PRICE")
    private BigDecimal ticketPrice; //门票价格
    @Column(name = "NUMBER")
    private Integer number; //数量
    @Column(name = "BUY_TICKET_PRICE")
    private BigDecimal buyTicketPrice; //进票价
    @Column(name = "SELL_TICKET_PRICE")
    private BigDecimal sellTicketPrice; //卖票价
    @Column(name = "FREIGHT")
    private BigDecimal freight; //运费
    @Column(name = "PROFIT")
    private BigDecimal profit; //利润 = （卖票价 - 进票价 - 运费）* 数量
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

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSellStatus() {
        return sellStatus;
    }

    public void setSellStatus(Integer sellStatus) {
        this.sellStatus = sellStatus;
    }

    public BigDecimal getBuyTicketPrice() {
        return buyTicketPrice;
    }

    public void setBuyTicketPrice(BigDecimal buyTicketPrice) {
        this.buyTicketPrice = buyTicketPrice;
    }

    public BigDecimal getSellTicketPrice() {
        return sellTicketPrice;
    }

    public void setSellTicketPrice(BigDecimal sellTicketPrice) {
        this.sellTicketPrice = sellTicketPrice;
    }

    public BigDecimal getFreight() {
        return freight;
    }

    public void setFreight(BigDecimal freight) {
        this.freight = freight;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public Integer getEffectFlag() {
        return effectFlag;
    }

    public void setEffectFlag(Integer effectFlag) {
        this.effectFlag = effectFlag;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
