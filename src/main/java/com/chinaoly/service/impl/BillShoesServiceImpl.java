package com.chinaoly.service.impl;

import com.chinaoly.Model.BillShoes;
import com.chinaoly.dao.BillShoesMapper;
import com.chinaoly.frm.common.utils.CommonUtil;
import com.chinaoly.frm.core.service.AbstractService;
import com.chinaoly.service.BillShoesService;
import com.chinaoly.util.MD5Util;
import com.chinaoly.util.NumberArithmeticUtils;
import com.chinaoly.util.PageUtil;
import com.chinaoly.util.StatusEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.chinaoly.util.PageUtil.backPage;
import static com.chinaoly.util.StringUtil.getBigDecimal;


/**
 * 鞋子账单服务接口实现类
 *
 * @author liuq
 * @date 2019-11-07 17:09:05
 */
@Service
@Transactional
public class BillShoesServiceImpl extends AbstractService<BillShoes> implements BillShoesService {

    @Resource
    private BillShoesMapper billShoesMapper;

    @Override
    public void save(BillShoes model) {
        // 设置初始数据
        model.setEffectFlag(StatusEnum.NORMAL);
        model.setCreateTime(new Date());
        model.setUpdateTime(new Date());
        model.setShoesId(MD5Util.getMD5(""));
        if (model.getSellStatus() == 1) {
            // 利润 = 总售价 - 进货总价 - 运费 - 手续费 - 押金
            BigDecimal price = getBigDecimal(model.getTotalSalesPrice() + ",")[0];//总售价
            // 初始化
            BigDecimal[] priceList; // 进货总价 运费 押金
            // 判断押金状态 0未扣-不参与计算 1已扣-参与计算 后续修改为 不考虑押金状态
            priceList = getBigDecimal(model.getTotalBuyPrice() + "," + model.getFreight() + "," + model.getServiceCharge() + "," + model.getDeposit());
            /*if (model.getDeduct() == 1) {
                priceList = getBigDecimal(model.getTotalBuyPrice() + "," + model.getFreight() + "," + model.getServiceCharge() + "," + model.getDeposit());
            } else {
                priceList = getBigDecimal(model.getTotalBuyPrice() + "," + model.getFreight() + "," + model.getServiceCharge());
            }*/
            model.setProfit(NumberArithmeticUtils.safeSubtract(price, priceList));
        } else {
            model.setProfit(new BigDecimal(0.00));
        }
        super.save(model);
    }

    @Override
    public int deleteByIds(String ids) {
        List<String> id = Arrays.asList(ids.split(","));
        Example example = new Example(BillShoes.class);
        Criteria criteria = example.createCriteria();
        criteria.andIn("shoesId", id);
        BillShoes shoes = new BillShoes();
        shoes.setEffectFlag(StatusEnum.DELETE);
        shoes.setUpdateTime(new Date());
        return billShoesMapper.updateByExampleSelective(shoes, example);
    }

    @Override
    public void update(BillShoes model) {
        model.setUpdateTime(new Date());
        if (model.getSellStatus() == 1) {
            // 利润 = 总售价 - 进货总价 - 运费 - 手续费 - 押金   判断押金状态 0未扣-不参与计算 1已扣-参与计算
            BigDecimal price = getBigDecimal(model.getTotalSalesPrice() + ",")[0];
            // 初始化
            BigDecimal[] priceList;
            priceList = getBigDecimal(model.getTotalBuyPrice() + "," + model.getFreight() + "," + model.getServiceCharge() + "," + model.getDeposit());
            /*if (model.getDeduct() == 1) {
                priceList = getBigDecimal(model.getTotalBuyPrice() + "," + model.getFreight() + "," + model.getServiceCharge() + "," + model.getDeposit());
            } else {
                priceList = getBigDecimal(model.getTotalBuyPrice() + "," + model.getFreight() + "," + model.getServiceCharge());
            }*/
            model.setProfit(NumberArithmeticUtils.safeSubtract(price, priceList));
        } else {
            model.setProfit(new BigDecimal(0.00));
        }
        super.update(model);
    }


    @Override
    public PageUtil findAll(Integer brandType, String startTime, String endTime, Integer sellStatus, Integer page, Integer limit) {
        Example example = new Example(BillShoes.class);
        Criteria criteria = example.createCriteria();
        if (!(CommonUtil.isNull(startTime) && CommonUtil.isNull(endTime))) {
            criteria.andEqualTo("brandType", brandType).andGreaterThanOrEqualTo("createTime", startTime).andLessThanOrEqualTo("createTime", endTime).andEqualTo("effectFlag", StatusEnum.NORMAL);
        } else {
            criteria.andEqualTo("brandType", brandType).andEqualTo("sellStatus", sellStatus).andEqualTo("effectFlag", StatusEnum.NORMAL);
        }
        example.setOrderByClause("CREATE_TIME DESC");
        // 获取第page页，limit条内容，默认查询总数count
        PageHelper.startPage(page, limit);
        // 紧跟着的第一个select方法会被分页
        List<BillShoes> list = billShoesMapper.selectByExample(example);
        //用PageInfo对结果进行包装
        PageInfo pages = new PageInfo(list);
        int count = (int) pages.getTotal();
        if (list == null || list.size() == 0)
            return backPage(1, "当前没有数据", count, list);
        else
            return backPage(0, "SUCCEE", count, list);
    }

}