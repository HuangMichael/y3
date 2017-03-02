package com.subway.service.budge;

import com.subway.dao.budget.EcBudgetBillRepository;
import com.subway.dao.budget.VecbudgetBillRepository;
import com.subway.dao.locations.VlocationsRepository;
import com.subway.domain.EcBudget.EcBudgetBill;
import com.subway.domain.EcBudget.VEcBudgetBill;
import com.subway.service.app.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by huangbin on 2016/5/4.
 * 设备台账业务类
 */
@Service
public class EcBudgeService extends BaseService {


    @Autowired
    EcBudgetBillRepository ecBudgetBillRepository;


    @Autowired
    VecbudgetBillRepository vecbudgetBillRepository;


    @Autowired
    VlocationsRepository vlocationsRepository;


    /**
     * @param pageable
     * @return 分页查询
     */
    public Page<VEcBudgetBill> findAllV(Pageable pageable) {

        return vecbudgetBillRepository.findAll(pageable);
    }


    /**
     * @param ecname   易耗品名称
     * @param pageable
     * @return 分页查询 根据易耗品名称去查询
     */
    public Page<VEcBudgetBill> findByEcnameContains(String ecname, Pageable pageable) {

        return vecbudgetBillRepository.findByEcnameContains(ecname, pageable);
    }


    /**
     * @param ecname   易耗品名称
     * @return 分页查询 根据易耗品名称去查询
     */
    public List<VEcBudgetBill> findByEcnameContains(String ecname) {

        return vecbudgetBillRepository.findByEcnameContains(ecname);
    }

    /**
     * @param pageable
     * @return 分页查询
     */
    public Page<EcBudgetBill> findAll(Pageable pageable) {
        return ecBudgetBillRepository.findAll(pageable);
    }

    /**
     * @return 查询所有
     */
    public List<EcBudgetBill> findAll() {
        return ecBudgetBillRepository.findAll();
    }


    /**
     * @return 查询所有
     */
    public EcBudgetBill findById(Long id) {
        return ecBudgetBillRepository.findById(id);
    }


    /**
     * @return 查询所有
     */
    public List<VEcBudgetBill> findAllV() {
        return vecbudgetBillRepository.findAll();
    }


    /**
     * @return 查询所有
     */
    public EcBudgetBill save(EcBudgetBill ecBudgetBill) {
        //  ecBudgetBill.setApplyDate(new Date());
        return ecBudgetBillRepository.save(ecBudgetBill);
    }


    /**
     * @param id 根据id删除 删除成功返回true
     * @return
     */
    public boolean delete(Long id) {
        if (id != null) {
            ecBudgetBillRepository.delete(id);
        }
        return ecBudgetBillRepository.findById(id) == null;
    }


    /**
     * @return 查询所有的id
     */
    public List<Long> findAllIds() {
        List<Long> ids = ecBudgetBillRepository.findAllIds();
        return ids;
    }


}
