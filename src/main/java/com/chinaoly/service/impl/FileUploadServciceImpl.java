package com.chinaoly.service.impl;

import com.chinaoly.Model.BillShoes;
import com.chinaoly.Model.BillTicket;
import com.chinaoly.dao.BillShoesMapper;
import com.chinaoly.dao.BillTicketMapper;
import com.chinaoly.service.FileUploadServcice;
import com.chinaoly.util.MD5Util;
import com.chinaoly.util.StatusEnum;
import com.chinaoly.util.XLSXCovertCSVReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.chinaoly.util.NumberArithmeticUtils.safeMultiply;
import static com.chinaoly.util.NumberArithmeticUtils.safeSubtract;
import static com.chinaoly.util.StringUtil.getBigDecimal;


@Service
@Transactional
public class FileUploadServciceImpl implements FileUploadServcice {

    @Resource
    private BillShoesMapper billShoesMapper;

    @Resource
    private BillTicketMapper billTicketMapper;

    public ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public int addFileListsByShoes(String file, Integer status) throws Exception {

        // 采用了Java多线程的Future模式，采用异步的方式最终来获取执行结果。
        List<Future<Integer>> resultList = new ArrayList<>();
        int willSaveAmount = 0;  // 将要插入的数据量
        // 文件路径  列总数
        List<String[]> listr = XLSXCovertCSVReader.readerExcel(file, 10);
        // LinkedList 加快插入效率
        List<BillShoes> lists = new LinkedList<>();
        for (int i = 0; i < listr.size(); i++) {
            BillShoes shoes = new BillShoes();
            shoes.setModel(listr.get(i)[0]);
            shoes.setBrandType(Integer.valueOf(listr.get(i)[1]));
            shoes.setShoeSize(listr.get(i)[2]);
            shoes.setNumber(Integer.valueOf(listr.get(i)[3]));
            shoes.setTotalBuyPrice(getBigDecimal(listr.get(i)[4])[0]); // 进货总价
            shoes.setTotalSalesPrice(getBigDecimal(listr.get(i)[5])[0]); // 总售价
            shoes.setFreight(getBigDecimal(listr.get(i)[6])[0]); // 运费
            shoes.setServiceCharge(getBigDecimal(listr.get(i)[7])[0]); // 手续费
            shoes.setDeposit(getBigDecimal(listr.get(i)[8])[0]); // 押金
//            shoes.setDeduct(Integer.valueOf(listr.get(i)[9])); // 押金状态
            shoes.setMemo(listr.get(i)[9]);
            // 设置初始数据
            shoes.setEffectFlag(StatusEnum.NORMAL);
            shoes.setCreateTime(new Date());
            shoes.setUpdateTime(new Date());
            shoes.setShoesId(MD5Util.getMD5(""));
            if (status == 0) {
                shoes.setSellStatus(0);
                shoes.setProfit(new BigDecimal(0.00));
            } else {
                shoes.setSellStatus(1);
                // 利润 = 总售价 - 进货总价 - 运费 - 手续费 - 押金
                BigDecimal price = shoes.getTotalSalesPrice();//总售价
                // 初始化
                BigDecimal[] priceList; // 进货总价 运费 押金
                // 判断押金状态 0未扣-不参与计算 1已扣-参与计算 后续修改为 不考虑押金状态
                priceList = getBigDecimal(shoes.getTotalBuyPrice() + "," + shoes.getFreight() + "," + shoes.getServiceCharge() + "," + shoes.getDeposit());
                /*if (shoes.getDeduct() == 1) {
                    priceList = getBigDecimal(shoes.getTotalBuyPrice() + "," + shoes.getFreight() + "," + shoes.getServiceCharge() + "," + shoes.getDeposit());
                } else {
                    priceList = getBigDecimal(shoes.getTotalBuyPrice() + "," + shoes.getFreight() + "," + shoes.getServiceCharge());
                }*/
                shoes.setProfit(safeSubtract(price, priceList));
            }
            lists.add(shoes);
            willSaveAmount++;
            /**
             * 判断即将插入的数据是否已经到达5000，如果到达5000，
             * 进行数据插入
             */
            if (willSaveAmount == 5000) {
                // 满足条件的数据
                List<BillShoes> centreLists = lists;
                Callable<Integer> callable = () -> {
                    int count = billShoesMapper.insertMysqlAndOthersList(centreLists);
                    return count;
                };
                willSaveAmount = 0;
                lists = new LinkedList<>();
                Future<Integer> future = executor.submit(callable);
                resultList.add(future);
            }
        }
        // 针对没有存入的数据进行处理
        if (willSaveAmount != 0) {
            // 剩余的数据
            List<BillShoes> surplusLists = lists;
            Callable<Integer> callable = () -> {
                int count = billShoesMapper.insertMysqlAndOthersList(surplusLists);
                return count;
            };
            Future<Integer> future = executor.submit(callable);
            resultList.add(future);
        }
        // 增加isShutdown()判断
        if (executor.isShutdown()) {
            executor.shutdown();
        }
        int total = 0;
        for (Future<Integer> future : resultList) {
            while (true) {
                if (future.isDone() && !future.isCancelled()) {
                    int sum = future.get();
                    total += sum;
                    break;
                } else {
                    Thread.sleep(100);
                }
            }
        }
        return total;
    }

    @Override
    public int addFileListsByTicket(String file, Integer status) throws Exception {
        // 采用了Java多线程的Future模式，采用异步的方式最终来获取执行结果。
        List<Future<Integer>> resultList = new ArrayList<>();
        int willSaveAmount = 0;  // 将要插入的数据量
        // 文件路径  列总数
        List<String[]> listr = XLSXCovertCSVReader.readerExcel(file, 8);
        // LinkedList 加快插入效率
        List<BillTicket> lists = new LinkedList<>();
        for (int i = 0; i < listr.size(); i++) {
            BillTicket ticket = new BillTicket();
            ticket.setTicketName(listr.get(i)[0]);
            ticket.setTicketDate(listr.get(i)[1]);
            ticket.setTicketPrice(getBigDecimal(listr.get(i)[2])[0]); // 门票价格
            ticket.setNumber(Integer.valueOf(listr.get(i)[3])); // 数量
            ticket.setBuyTicketPrice(getBigDecimal(listr.get(i)[4])[0]); // 进票价
            ticket.setSellTicketPrice(getBigDecimal(listr.get(i)[5])[0]); // 卖票价
            ticket.setFreight(getBigDecimal(listr.get(i)[6])[0]); // 运费
            ticket.setMemo(listr.get(i)[7]);
            // 设置初始数据
            ticket.setEffectFlag(StatusEnum.NORMAL);
            ticket.setCreateTime(new Date());
            ticket.setUpdateTime(new Date());
            ticket.setTicketId(MD5Util.getMD5(""));
            if (status == 0) {
                ticket.setSellStatus(0);
                ticket.setProfit(new BigDecimal(0.00));
            } else {
                ticket.setSellStatus(1);
                // 利润 = （卖票价 - 进票价 - 运费）* 数量
                BigDecimal price = getBigDecimal(ticket.getSellTicketPrice() + ",")[0];
                // 初始化
                BigDecimal[] priceList = getBigDecimal(ticket.getBuyTicketPrice() + "," + ticket.getFreight());
                ticket.setProfit(safeMultiply(safeSubtract(price, priceList), ticket.getNumber()));
            }
            lists.add(ticket);
            willSaveAmount++;
            /**
             * 判断即将插入的数据是否已经到达5000，如果到达5000，
             * 进行数据插入
             */
            if (willSaveAmount == 5000) {
                // 满足条件的数据
                List<BillTicket> centreLists = lists;
                Callable<Integer> callable = () -> {
                    int count = billTicketMapper.insertMysqlAndOthersList(centreLists);
                    return count;
                };
                willSaveAmount = 0;
                lists = new LinkedList<>();
                Future<Integer> future = executor.submit(callable);
                resultList.add(future);
            }
        }
        // 针对没有存入的数据进行处理
        if (willSaveAmount != 0) {
            // 剩余的数据
            List<BillTicket> surplusLists = lists;
            Callable<Integer> callable = () -> {
                int count = billTicketMapper.insertMysqlAndOthersList(surplusLists);
                return count;
            };
            Future<Integer> future = executor.submit(callable);
            resultList.add(future);
        }
        // 增加isShutdown()判断
        if (executor.isShutdown()) {
            executor.shutdown();
        }
        int total = 0;
        for (Future<Integer> future : resultList) {
            while (true) {
                if (future.isDone() && !future.isCancelled()) {
                    int sum = future.get();
                    total += sum;
                    break;
                } else {
                    Thread.sleep(100);
                }
            }
        }
        return total;
    }
}
