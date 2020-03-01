package com.chinaoly.service;

import com.chinaoly.Model.BillShoes;
import com.chinaoly.frm.core.service.Service;
import com.chinaoly.util.PageUtil;

/**
 * 鞋子账单服务接口
 *
 * @author liuq
 * @date 2019-11-07 17:09:05
 */
public interface BillShoesService extends Service<BillShoes> {

    PageUtil findAll(Integer brandType, String startTime, String endTime, Integer sellStatus, Integer page, Integer limit);

}