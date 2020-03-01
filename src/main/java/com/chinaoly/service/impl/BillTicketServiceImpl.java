package com.chinaoly.service.impl;

import com.chinaoly.Model.BillTicket;
import com.chinaoly.dao.BillTicketMapper;
import com.chinaoly.frm.common.utils.CommonUtil;
import com.chinaoly.frm.core.service.AbstractService;
import com.chinaoly.service.BillTicketService;
import com.chinaoly.util.MD5Util;
import com.chinaoly.util.PageUtil;
import com.chinaoly.util.StatusEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.chinaoly.util.NumberArithmeticUtils.safeMultiply;
import static com.chinaoly.util.NumberArithmeticUtils.safeSubtract;
import static com.chinaoly.util.PageUtil.backPage;
import static com.chinaoly.util.StringUtil.getBigDecimal;


/**
 * 门票账单服务接口实现类
 *
 * @author liuq
 * @date 2019-11-07 17:09:53
 */
@Service
@Transactional
public class BillTicketServiceImpl extends AbstractService<BillTicket> implements BillTicketService {

    @Resource
    private BillTicketMapper billTicketMapper;

    @Override
    public void save(BillTicket model) {
        // 设置初始数据
        model.setEffectFlag(StatusEnum.NORMAL);
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());
        model.setTicketId(MD5Util.getMD5(""));
        if (model.getSellStatus() == 1) {
            // 利润 = （卖票价 - 进票价 - 运费）* 数量
            BigDecimal price = getBigDecimal(model.getSellTicketPrice() + ",")[0];
            // 初始化
            BigDecimal[] priceList = getBigDecimal(model.getBuyTicketPrice() + "," + model.getFreight());
            model.setProfit(safeMultiply(safeSubtract(price, priceList), model.getNumber()));
        } else {
            model.setProfit(new BigDecimal(0.00));
        }
        super.save(model);
    }

    @Override
    public int deleteByIds(String ids) {
        List<String> id = Arrays.asList(ids.split(","));
        Example example = new Example(BillTicket.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("ticketId", id);
        BillTicket ticket = new BillTicket();
        ticket.setEffectFlag(StatusEnum.DELETE);
        ticket.setUpdateTime(new Date());
        return billTicketMapper.updateByExampleSelective(ticket, example);
    }

    @Override
    public void update(BillTicket model) {
        model.setUpdateTime(new Date());
        if (model.getSellStatus() == 1) {
            // 利润 = （卖票价 - 进票价 - 运费）* 数量
            BigDecimal price = getBigDecimal(model.getSellTicketPrice() + ",")[0];
            // 初始化
            BigDecimal[] priceList = getBigDecimal(model.getBuyTicketPrice() + "," + model.getFreight());
            model.setProfit(safeMultiply(safeSubtract(price, priceList), model.getNumber()));
        } else {
            model.setProfit(new BigDecimal(0.00));
        }
        super.update(model);
    }

    @Override
    public PageUtil findAll(String ticketName, String startTime, String endTime, Integer sellStatus, Integer page, Integer limit) {
        Example example = new Example(BillTicket.class);
        Example.Criteria criteria = example.createCriteria();
        if (!(CommonUtil.isNull(startTime) && CommonUtil.isNull(endTime))) {
            if (!CommonUtil.isNull(ticketName))
                criteria.andLike("ticketName", "%" + ticketName + "%").andEqualTo("sellStatus", sellStatus).andGreaterThanOrEqualTo("createTime", startTime).andLessThanOrEqualTo("createTime", endTime).andEqualTo("effectFlag", StatusEnum.NORMAL);
            else
                criteria.andEqualTo("sellStatus", sellStatus).andGreaterThanOrEqualTo("createTime", startTime).andLessThanOrEqualTo("createTime", endTime).andEqualTo("effectFlag", StatusEnum.NORMAL);
        } else {
            if (!CommonUtil.isNull(ticketName))
                criteria.andLike("ticketName", "%" + ticketName + "%").andEqualTo("sellStatus", sellStatus).andEqualTo("effectFlag", StatusEnum.NORMAL);
            else
                criteria.andEqualTo("sellStatus", sellStatus).andEqualTo("effectFlag", StatusEnum.NORMAL);
        }
        example.setOrderByClause("CREATE_TIME DESC");
        // 获取第page页，limit条内容，默认查询总数count
        PageHelper.startPage(page, limit);
        // 紧跟着的第一个select方法会被分页
        List<BillTicket> list = billTicketMapper.selectByExample(example);
        //用PageInfo对结果进行包装
        PageInfo pages = new PageInfo(list);
        int count = (int) pages.getTotal();
        if (list == null || list.size() == 0)
            return backPage(1, "当前没有数据", count, list);
        else
            return backPage(0, "SUCCEE", count, list);
    }
}