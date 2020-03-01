package com.chinaoly.service;

import com.chinaoly.Model.BillTicket;
import com.chinaoly.frm.core.service.Service;
import com.chinaoly.util.PageUtil;

/**
 * 门票账单服务接口
 *
 * @author liuq
 * @date 2019-11-07 17:09:53
 */
public interface BillTicketService extends Service<BillTicket> {

    PageUtil findAll(String ticketName, String startTime, String endTime, Integer sellStatus, Integer page, Integer limit);


}