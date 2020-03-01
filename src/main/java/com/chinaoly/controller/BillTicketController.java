package com.chinaoly.controller;

import com.chinaoly.Model.BillTicket;
import com.chinaoly.frm.common.utils.CommonUtil;
import com.chinaoly.frm.core.entity.Result;
import com.chinaoly.frm.core.entity.ResultGenerator;
import com.chinaoly.service.BillTicketService;
import com.chinaoly.util.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuq
 * @date 2019-11-07 17:09:53
 */
@RestController
@RequestMapping(value = "/api/billTicket")
@Api(value = "billTicket", tags = "门票类API", description = "门票类信息详细描述包含增删改查")
public class BillTicketController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BillTicketController.class);

    @Autowired
    private BillTicketService billTicketService;


    /**
     * 添加数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:53
     */
    @PostMapping(value = "/save")
    @ApiOperation(value = "save", notes = "添加数据")
    public Result save(BillTicket billTicket) {
        billTicketService.save(billTicket);
        return ResultGenerator.genSuccessResult();
    }


    /**
     * 添加数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:53
     */
    @PostMapping(value = "/delete")
    @ApiOperation(value = "delete", notes = "添加数据")
    public Result delete(String id) {
        if (!CommonUtil.isNull(id)) {
            int i = billTicketService.deleteByIds(id);
            return ResultGenerator.genSuccessResult(i);
        } else {
            return ResultGenerator.genFailResult("参数不正确!");
        }
    }

    /**
     * 修改数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:53
     */
    @PostMapping(value = "/update")
    @ApiOperation(value = "update", notes = "修改数据")
    public Result update(BillTicket billTicket) {
        billTicketService.update(billTicket);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据id获取数据
     *
     * @author:liuq
     * @date 2019-11-07 17:09:53
     */
    @PostMapping(value = "/detail")
    @ApiOperation(value = "detail", notes = "根据id获取数据")
    public Result detail(String id) {
        if (!CommonUtil.isNull(id)) {
            BillTicket billTicket = billTicketService.findById(id);
            if (!CommonUtil.isNull(billTicket))
                return ResultGenerator.genSuccessResult(billTicket);
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
     * @date 2019-11-07 17:09:53
     * String ticketName; //门票名称  String startTime; //开始时间  String endTime; //结束时间
     */
    @PostMapping("/queryPage")
    @ApiOperation(value = "queryPage", notes = "分页查询")
    public PageUtil queryPage(String ticketName, String startTime, String endTime, Integer sellStatus, Integer page, Integer limit) {
        return billTicketService.findAll(ticketName, startTime, endTime, sellStatus, page, limit);
    }

}
