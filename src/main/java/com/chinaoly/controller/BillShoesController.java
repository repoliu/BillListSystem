package com.chinaoly.controller;

import com.chinaoly.Model.BillShoes;
import com.chinaoly.frm.common.utils.CommonUtil;
import com.chinaoly.frm.core.entity.Result;
import com.chinaoly.frm.core.entity.ResultGenerator;
import com.chinaoly.frm.core.web.BaseController;
import com.chinaoly.service.BillShoesService;
import com.chinaoly.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.NumberFormat;


/**
 * @author liuq
 * @date 2019-11-07 17:09:05
 */
@RestController
@RequestMapping(value = "/api/billShoes")
@Api(value = "billShoes", tags = "鞋类API", description = "鞋类信息详细描述包含增删改查")
public class BillShoesController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BillShoesController.class);

    @Autowired
    private BillShoesService billShoesService;

    /**
     * 添加数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:05
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "save", notes = "添加数据")
    public Result save(BillShoes billShoes) {
        if (!(CommonUtil.isNull(billShoes.getTotalSalesPrice()) && CommonUtil.isNull(billShoes.getTotalBuyPrice()) && CommonUtil.isNull(billShoes.getFreight()) && CommonUtil.isNull(billShoes.getServiceCharge()) && CommonUtil.isNull(billShoes.getDeposit()))) {
            billShoesService.save(billShoes);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("价格是必填项!");
        }
    }


    /**
     * 删除数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:05
     */
    @PostMapping(value = "/delete")
    @ApiOperation(value = "delete", notes = "删除数据")
    public Result delete(String id) {
        if (!CommonUtil.isNull(id)) {
            int i = billShoesService.deleteByIds(id);
            return ResultGenerator.genSuccessResult(i);
        } else {
            return ResultGenerator.genFailResult("参数不正确!");
        }
    }

    /**
     * 修改数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:05
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "update", notes = "修改数据")
    public Result update(BillShoes billShoes) {
        if (!(CommonUtil.isNull(billShoes.getTotalSalesPrice()) && CommonUtil.isNull(billShoes.getTotalBuyPrice()) && CommonUtil.isNull(billShoes.getFreight()) && CommonUtil.isNull(billShoes.getServiceCharge()) && CommonUtil.isNull(billShoes.getDeposit()))) {
            billShoesService.update(billShoes);
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("价格是必填项!");
        }
    }

    /**
     * 根据id获取数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:05
     */
    @PostMapping(value = "/detail")
    @ApiOperation(value = "detail", notes = "根据id获取数据")
    public Result detail(String id) {
        if (!CommonUtil.isNull(id)) {
            BillShoes billShoes = billShoesService.findById(id);
            if (!CommonUtil.isNull(billShoes))
                return ResultGenerator.genSuccessResult(billShoes);
            else
                return ResultGenerator.genFailResult("未获取到信息!");
        } else {
            return ResultGenerator.genFailResult("未获取到信息!");
        }
    }

    /**
     * 分页查询
     *
     * @author:liuq
     * @date 2019-11-07 17:09:05
     * String brandType; //品牌类型  String startTime; //开始时间  String endTime; //结束时间
     */
    @PostMapping("/queryPage")
    @ApiOperation(value = "queryPage", notes = "分页查询")
    public PageUtil queryPage(Integer brandType, String startTime, String endTime, Integer sellStatus, Integer page, Integer limit) {
        return billShoesService.findAll(brandType, startTime, endTime, sellStatus, page, limit);
    }

    public static void main(String[] args) {
        double d = 6.135;  // 总售价
        // 货币表示
        String format = NumberFormat.getCurrencyInstance().format(d);

        System.out.println();
    }
}
