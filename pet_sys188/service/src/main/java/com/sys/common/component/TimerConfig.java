package com.sys.common.component;

import com.sys.common.DateUtil;
import com.sys.dao.*;
import com.sys.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class TimerConfig {
    @Autowired
    private SysPetShowerMapper sysPetShowerMapper;
    @Autowired
    private SysPetShowerTimeMapper sysPetShowerTimeMapper;
    @Autowired
    private SysPetMapper sysPetMapper;
    @Autowired
    private SysNotifyMapper sysNotifyMapper;
    @Autowired
    private SysPetSterilizationConfigMapper sysPetSterilizationConfigMapper;
    @Autowired
    private SysPetSterilizationMapper sysPetSterilizationMapper;
    @Autowired
    private SysPetVaccineMapper sysPetVaccineMapper;
    @Autowired
    private SysPetVaccineConfigMapper sysPetVaccineConfigMapper;
    @Autowired
    private SysPetVaccineLogMapper sysPetVaccineLogMapper;

    /**
     * 每隔60秒执行一次
     */
    @Scheduled(fixedRate = 1000*60)
    public void timer(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        System.out.println("timer exec : "+format.format(new Date()));

        //查询洗澡配置
        List<SysPetShowerTime> sysPetShowerTimes = sysPetShowerTimeMapper.selectList("");
        //查询洗澡完成的宠物
        List<SysPetShower> sysPetShowers = sysPetShowerMapper.selectList("and is_over = 1 and pet_id in (select pet_id from sys_pet)");
        for (int i = 0;i<sysPetShowers.size();i++){
            //查询宠物
            SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetShowers.get(i).getPetId());
            if(sysPet != null){
                //判断宠物的体积
                for (int j = 0;j<sysPetShowerTimes.size();j++){
                    int petType = sysPet.getPetType().equals("猫") ? 0 : 1;
                    if(petType == sysPetShowerTimes.get(i).getPetType() && sysPet.getWeight() >= sysPetShowerTimes.get(j).getMinKg() && sysPet.getWeight() <= sysPetShowerTimes.get(j).getMaxKg()){
                        //查询到，判断是否到分钟
                        if(DateUtil.compareTo(DateUtil.addMinute(sysPet.getCreateTime(), sysPetShowerTimes.get(j).getTime()))){
                            //如果时间到
                            sysPetShowers.get(i).setIsOver(2);
                            sysPetShowerMapper.updateByPrimaryKeySelective(sysPetShowers.get(i));
                            //洗澡完成
                            SysNotify sysNotify = new SysNotify();
                            sysNotify.setKeyId(sysPetShowers.get(i).getPetShowerId());
                            sysNotify.setType(4);
                            sysNotify.setTitle("宠物洗澡通知");
                            sysNotify.setStatus(0);
                            sysNotify.setMemberId(sysPet.getMemberId());
                            sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成洗澡");
                            sysNotifyMapper.insertSelective(sysNotify);
                        }
                    }
                }
            }else {
                sysPetShowers.get(i).setIsOver(2);
                sysPetShowerMapper.updateByPrimaryKeySelective(sysPetShowers.get(i));
            }
        }

        //查询绝育的配置
        List<SysPetSterilizationConfig> sysPetSterilizationConfigs = sysPetSterilizationConfigMapper.selectList("");
        //查询正在绝育的宠物
        List<SysPetSterilization> sysPetSterilizations = sysPetSterilizationMapper.selectList(" and is_over = 1");
        for (int i = 0;i<sysPetSterilizations.size();i++){
            //查询宠物
            SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetSterilizations.get(i).getPetId());
            if(sysPet != null){
                //判断宠物的体积
                sysPet.setIsSterilization(1);
                sysPetMapper.updateByPrimaryKeySelective(sysPet);
                if(sysPet.getPetType().equals("猫")){
                    if(DateUtil.compareTo(DateUtil.addMinute(sysPet.getCreateTime(), sysPetSterilizationConfigs.get(0).getTime()))){
                        //如果时间到
                        sysPetSterilizations.get(i).setIsOver(2);
                        sysPetSterilizationMapper.updateByPrimaryKeySelective(sysPetSterilizations.get(i));
                        //洗澡完成
                        SysNotify sysNotify = new SysNotify();
                        sysNotify.setKeyId(sysPetSterilizations.get(i).getPetSterilizationId());
                        sysNotify.setType(5);
                        sysNotify.setTitle("宠物绝育通知");
                        sysNotify.setStatus(0);
                        sysNotify.setMemberId(sysPet.getMemberId());
                        sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成绝育");
                        sysNotifyMapper.insertSelective(sysNotify);
                    }
                }else {
                    if(DateUtil.compareTo(DateUtil.addMinute(sysPet.getCreateTime(), sysPetSterilizationConfigs.get(1).getTime()))){
                        //如果时间到
                        sysPetSterilizations.get(i).setIsOver(2);
                        sysPetSterilizationMapper.updateByPrimaryKeySelective(sysPetSterilizations.get(i));
                        //洗澡完成
                        SysNotify sysNotify = new SysNotify();
                        sysNotify.setKeyId(sysPetSterilizations.get(i).getPetSterilizationId());
                        sysNotify.setType(5);
                        sysNotify.setTitle("宠物绝育通知");
                        sysNotify.setStatus(0);
                        sysNotify.setMemberId(sysPet.getMemberId());
                        sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成绝育");
                        sysNotifyMapper.insertSelective(sysNotify);
                    }
                }
            }else {
                sysPetSterilizations.get(i).setIsOver(2);
                sysPetShowerMapper.updateByPrimaryKeySelective(sysPetShowers.get(i));
            }
        }

        //查询正在疫苗的宠物
        List<SysPetVaccine> sysPetVaccines = sysPetVaccineMapper.selectList(" and is_over = 1");
        for (int i = 0;i<sysPetVaccines.size();i++){
            //查询宠物
            SysPet sysPet = sysPetMapper.selectByPrimaryKey(sysPetVaccines.get(i).getPetId());
            if(sysPet != null){
                //增加疫苗记录
                SysPetVaccineConfig sysPetVaccineConfig = sysPetVaccineConfigMapper.selectByPrimaryKey(sysPetVaccines.get(i).getPetVaccineConfigId());
                if(DateUtil.compareTo(DateUtil.addMinute(sysPet.getCreateTime(), sysPetVaccineConfig.getTime()))){
                    //如果时间到
                    sysPetVaccines.get(i).setIsOver(2);
                    sysPetVaccineMapper.updateByPrimaryKeySelective(sysPetVaccines.get(i));
                    //增加日志
                    SysPetVaccineLog sysPetVaccineLog = new SysPetVaccineLog();
                    sysPetVaccineLog.setPetId(sysPet.getPetId());
                    sysPetVaccineLog.setPetVaccineConfigId(sysPetVaccines.get(i).getPetVaccineConfigId());
                    sysPetVaccineLog.setCreateTime(new Date());
                    sysPetVaccineLogMapper.insertSelective(sysPetVaccineLog);
                    //接种完成
                    SysNotify sysNotify = new SysNotify();
                    sysNotify.setKeyId(sysPetVaccines.get(i).getPetVaccineId());
                    sysNotify.setType(6);
                    sysNotify.setTitle("宠物疫苗通知");
                    sysNotify.setStatus(0);
                    sysNotify.setMemberId(sysPet.getMemberId());
                    sysNotify.setMessage("您的宠物【"+sysPet.getPetName()+"】已完成疫苗接种");
                    sysNotifyMapper.insertSelective(sysNotify);
                }
            }else {
                sysPetVaccines.get(i).setIsOver(2);
                sysPetVaccineMapper.updateByPrimaryKeySelective(sysPetVaccines.get(i));
            }
        }
    }

    /**
     * 5分钟执行
     */
    @Scheduled(fixedRate = 5000*60)
    public void shower(){
        //查询未绝育的
        List<SysPet> sysPets = sysPetMapper.selectList(" and is_sterilization = 0");
        for (int i = 0;i<sysPets.size();i++){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPets.get(i).getPetId());
            sysNotify.setType(6);
            sysNotify.setTitle("宠物绝育通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPets.get(i).getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPets.get(i).getPetName()+"】需要绝育，请尽快预约");
            sysNotifyMapper.insertSelective(sysNotify);
        }
        //查询未接种疫苗的
        List<SysPet> sysPets1 = sysPetMapper.selectNoneList();
        for (int i = 0;i<sysPets1.size();i++){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPets1.get(i).getPetId());
            sysNotify.setType(6);
            sysNotify.setTitle("宠物接种通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPets1.get(i).getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPets1.get(i).getPetName()+"】需要接种疫苗，请尽快预约");
            sysNotifyMapper.insertSelective(sysNotify);
        }
    }

    //每月的1号执行，体检信息
    @Scheduled(fixedRate = 5000*60)
    public void test(){
        //宠物需要体检
        List<SysPet> sysPets = sysPetMapper.selectList(" and pet_id not in (select distinct pet_id from sys_pet_test where DATE_SUB(CURDATE(), INTERVAL 30 DAY) <= date(create_time))");
        for (int i = 0;i<sysPets.size();i++){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPets.get(i).getPetId());
            sysNotify.setType(6);
            sysNotify.setTitle("宠物体检通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPets.get(i).getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPets.get(i).getPetName()+"】需要体检，请尽快预约");
            sysNotifyMapper.insertSelective(sysNotify);
        }
        //需要体检
        List<SysPet> sysPets1 = sysPetMapper.selectList(" and pet_id not in (select distinct pet_id from sys_pet_test where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(create_time))");
        for (int i = 0;i<sysPets1.size();i++){
            SysNotify sysNotify = new SysNotify();
            sysNotify.setKeyId(sysPets1.get(i).getPetId());
            sysNotify.setType(7);
            sysNotify.setTitle("宠物洗澡通知");
            sysNotify.setStatus(0);
            sysNotify.setMemberId(sysPets1.get(i).getMemberId());
            sysNotify.setMessage("您的宠物【"+sysPets1.get(i).getPetName()+"】需要洗澡，请尽快预约");
            sysNotifyMapper.insertSelective(sysNotify);
        }
    }
}
