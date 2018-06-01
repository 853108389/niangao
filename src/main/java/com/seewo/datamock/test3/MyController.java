package com.seewo.datamock.test3;

import com.seewo.datamock.common.weaving.beans.AnnEntity;
import com.seewo.datamock.common.weaving.beans.AnnEntry;
import com.seewo.datamock.common.weaving.beans.EnumEntry;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author NianGao
 * @Date 2018/5/8.
 * @description
 */
public class MyController {

    @RequestMapping("/showView")
    public ModelAndView showView(@RequestParam Model model, @PathVariable String a, String b, String c, Integer d) {
        ModelAndView modelAndView = new ModelAndView();
//        ControllerWeavingUtils.printAnnInfo(annEntities);
//        ControllerWeavingUtils.printAnnInfo();
//        String a = "aaa";
//        modelAndView.setViewName("viewName");
//        modelAndView.addObject(" 需要放到 model 中的属性名称 ", " 对应的属性值，它是一个对象 ");
        return modelAndView;
    }

    @RequestMapping(value = "testMethod2", method = {RequestMethod.GET, RequestMethod.DELETE})
    public String testMethod(String name) {
        System.out.println("000000000000000000000000");
        //        LinkedList<AnnEntity> annEntities = creatMock();
//        LinkedList<AnnEntity> annEntities1 = creatObject(annEntities);
//        ControllerWeavingUtils.printAnnInfo(annEntities1);
//        ControllerWeavingUtils.printAnnInfo(annEntities);
        return "method";
    }

    @PostMapping({"/DSRoom/"})
    public static String testMethod3(String name) {

        return "method";
    }

 /*   */

    /**
     * 创建目标对象
     *
     * @return
     *//*
    private LinkedList<AnnEntity> creatObject(LinkedList<AnnEntity> annList) {
        LinkedList<AnnEntity> retlist = new LinkedList<AnnEntity>();
        for (AnnEntity annEntity : annList) {
            List<AnnEntry> l1 = new ArrayList<AnnEntry>();
            for (AnnEntry annEntry : annEntity.getEntryList()) {
                List<EnumEntry> l2 = new LinkedList<EnumEntry>();
                for (EnumEntry enumEntry : annEntry.getEnumEntrylist()) {
                    EnumEntry e = new EnumEntry("cccc", "ddddd");
                    l2.add(e);
                }
                AnnEntry entry = new AnnEntry("annEntry.getKey()", false, l2);
                l1.add(entry);
            }
            AnnEntity entity = new AnnEntity(annEntity.getAnnDesc(), l1);
            retlist.add(entity);
            ControllerWeavingUtils.printAnnInfo(retlist);
        }
        return retlist;
    }*/
    private LinkedList<AnnEntity> creatMock() {
        //=========一个键值对
        EnumEntry enumEntry = new EnumEntry("", "testMethod2");
        List<EnumEntry> list1 = new LinkedList<>();
        list1.add(enumEntry);
        AnnEntry annEntry = new AnnEntry("value", false, list1);
//        //=========一个键值对
        EnumEntry enumEntry2 = new EnumEntry("Lorg.springframework.web.bind.annotation.RequestMethod;", "GET");
        EnumEntry enumEntry3 = new EnumEntry("Lorg.springframework.web.bind.annotation.RequestMethod;", "DELETE");
        List<EnumEntry> list2 = new LinkedList<>();
        list2.add(enumEntry2);
        list2.add(enumEntry3);
        AnnEntry annEntry2 = new AnnEntry("method", true, list2);
        //=========键值对集合
        List<AnnEntry> list = new ArrayList<>();
        list.add(annEntry);
        list.add(annEntry2);
        //==========一个注解
        AnnEntity annEntity = new AnnEntity("Lorg.springframework.web.bind.annotation.RequestMapping;", list);
        //==========许多个注解
        LinkedList<AnnEntity> annList = new LinkedList<>();
        annList.add(annEntity);
        return annList;
    }
}