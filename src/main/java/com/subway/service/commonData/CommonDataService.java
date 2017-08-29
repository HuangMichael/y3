package com.subway.service.commonData;

import com.subway.dao.app.resource.ResourceRepository;
import com.subway.dao.equipments.EquipmentsClassificationRepository;
import com.subway.dao.equipments.EquipmentsRepository;
import com.subway.dao.equipments.VeqClassRepository;
import com.subway.dao.equipments.VequipmentsRepository;
import com.subway.dao.line.LineRepository;
import com.subway.dao.line.StationRepository;
import com.subway.dao.locations.LocationsRepository;
import com.subway.dao.locations.VlocationsRepository;
import com.subway.dao.outsourcingUnit.OutsourcingUnitRepository;
import com.subway.dao.person.PersonRepository;
import com.subway.dao.workOrder.WorkOrderReportCartRepository;
import com.subway.domain.app.resoure.Resource;
import com.subway.domain.equipments.EquipmentsClassification;
import com.subway.domain.equipments.VeqClass;
import com.subway.domain.equipments.Vequipments;
import com.subway.domain.line.Line;
import com.subway.domain.line.Station;
import com.subway.domain.locations.Locations;
import com.subway.domain.locations.Vlocations;
import com.subway.domain.person.Person;
import com.subway.domain.units.Units;
import com.subway.domain.user.User;
import com.subway.object.ListObject;
import com.subway.object.ReturnObject;
import com.subway.service.app.BaseService;
import com.subway.service.line.LineService;
import com.subway.service.line.StationService;
import com.subway.service.locations.LocationsService;
import com.subway.service.person.PersonService;
import com.subway.service.workOrder.WorkOrderReportCartService;
import com.subway.utils.CommonStatusType;
import com.subway.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by huangbin on 2016/3/24.
 */
@Service
@CacheConfig
public class CommonDataService extends BaseService {

    @Autowired
    WorkOrderReportCartRepository workOrderReportCartRepository;

    @Autowired
    LocationsRepository locationsRepository;

    @Autowired
    VlocationsRepository vlocationsRepository;

    @Autowired
    VeqClassRepository veqClassRepository;

    @Autowired
    EquipmentsClassificationRepository equipmentsClassificationRepository;

    @Autowired
    ResourceRepository resourceRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;


    @Autowired
    VequipmentsRepository vequipmentsRepository;

    @Autowired
    EquipmentsRepository equipmentsRepository;

    @Autowired
    LocationsService locationsService;

    @Autowired
    StationService stationService;

    @Autowired
    LineService lineService;

    @Autowired
    PersonService personService;

    @Autowired
    OutsourcingUnitRepository unitRepository;


    @Autowired
    WorkOrderReportCartService workOrderReportCartService;


    /**
     * @return 查询我的下属位置信息
     * 先从session中找  如果失败再做查询
     */
    @Cacheable(value = "locations", key = "'locations'")
    public List<Locations> findMyLocation() {
        return locationsRepository.findByLocationStartingWith("BJ");
    }


    /**
     * @return 查询我的下属位置信息
     * 先从session中找  如果失败再做查询
     */
    @Cacheable(value = "vLocations", key = "'vlocations'")
    public List<Vlocations> findMyVLocation() {
        return vlocationsRepository.findByLocationStartingWith("BJ");
    }


    /**
     * @return 查询我的下属位置设备信息
     * 先从session中找  如果失败再做查询
     */

    @Cacheable(value = "Vequipments", key = "'Vequipments'+#locCode")
    public List<Vequipments> findMyVeqs(String locCode) {
        return vequipmentsRepository.findByLocationStartingWith(locCode);
    }


    /**
     * @return 查询设备种类信息
     */
    @Cacheable(value = "equipmentsClassifications", key = "'equipmentsClassifications'")
    public List<EquipmentsClassification> findEquipmentsClassification() {
        return equipmentsClassificationRepository.findAll();
    }


    /**
     * @return 查询设备种类信息
     */
    @Cacheable(value = "veqClasses", key = "'veqClasses'")
    public List<VeqClass> findVeqClass() {
        return veqClassRepository.findAll();
    }

    /**
     * @param type 位置类型  1为段区  2为站区
     * @return 查询设备种类信息
     */
    public List<VeqClass> findVeqClassByType(String type) {
        List<VeqClass> eqClassList = veqClassRepository.findByClassType(type);
        return eqClassList;
    }

    /**
     * @return 查询设备种类信息
     */
    @Cacheable(value = "modules", keyGenerator = "'modules'")
    public List<Resource> findMenus() {
        return resourceRepository.findByResourceLevel(1L);
    }


    /**
     * @return (0:停用 1:投用 2报废)
     */
    @Cacheable(value = "eqStatus", key = "'eqStatus'")
    public List<ListObject> getEqStatus() {
        List<ListObject> eqStatusList = new ArrayList<ListObject>();
        eqStatusList.add(new ListObject("0", "维修"));
        eqStatusList.add(new ListObject("1", "投用"));
        eqStatusList.add(new ListObject("2", "报废"));
        return eqStatusList;
    }


    /**
     * @return 获取运行状态 (0:停止 1:运行)
     */
    @Cacheable(value = "runningStatus", key = "'runningStatus'")
    public List<ListObject> getRunningStatus() {
        List<ListObject> eqRunStatusList = new ArrayList<ListObject>();
        eqRunStatusList.add(new ListObject("0", "停止"));
        eqRunStatusList.add(new ListObject("1", "运行"));
        return eqRunStatusList;

    }


    /**
     * @return 查询系统信息人员信息
     */

    public List<Person> findActivePerson() {
        return personService.findActivePerson();
    }


    /**
     * @return 查询外委单位信息
     */
    @Cacheable(value = "unitList", key = "'unitList'")
    public List<Units> findUnits() {
        return unitRepository.findByStatus(CommonStatusType.STATUS_YES);
    }

    /**
     * @return 获取服务器当前时间
     */
    public String getServerDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }


    /**
     * @param result      返回结果
     * @param successDesc 执行成功后描述
     * @param failureDesc 执行失败时描述
     * @return
     */
    public ReturnObject getReturnType(Boolean result, String successDesc, String failureDesc) {
        ReturnObject returnObject = new ReturnObject();
        String resultDesc = result ? successDesc : failureDesc;
        returnObject.setResult(result);
        returnObject.setResultDesc(resultDesc);
        return returnObject;
    }


    /**
     * @param id 位置编号
     * @return 查询位置下对应的设备分类
     */
    public List<VeqClass> findEqClassesByLocationId(Long id) {
        return equipmentsRepository.findEqClassesByLocationId(id);
    }


    /**
     * @param lid
     * @param cid
     * @return 查询位置下对应的设备分类
     */
    public List<Vequipments> findEqByLocIdAndEqcId(Long lid, Long cid) {
        return equipmentsRepository.findEqByLocIdAndEqcId(lid, cid);
    }


    /**
     * @param session 会话
     * @return 重载session值
     */
    public Boolean reload(User currentUser, HttpSession session) {

        List<Line> lineList = lineService.findByStatus("1");
        List<Station> stationList = stationService.findByStatus("1");
        List<Vlocations> locationsList = locationsService.findByLocationStartingWithAndStatus(currentUser.getVlocations().getLocation());
        List<Locations> locList = locationsService.findByLocationStartingWithAndStatus(currentUser.getVlocations().getLocation(), "1");
        List<VeqClass> veqClassList = veqClassRepository.findAll();
        List<Resource> menusList = findMenus();
        session.setAttribute("locationsList", locationsList);
        session.setAttribute("locList", locList);
        session.setAttribute("veqClassList", veqClassList);
        session.setAttribute("lineList", lineList);
        session.setAttribute("stationList", stationList);
        session.setAttribute("menusList", menusList);
        return true;
    }


    /**
     * @return 根据序号生成
     */
    public String genWorkOrderLineNo() {
        //先查出年月的
        String startNo = DateUtils.convertDate2Str(new Date(), "yyMMdd");
        startNo += workOrderReportCartRepository.getNextOrderNo();
        //再查出序号 xxxx
        return startNo;
    }
}
