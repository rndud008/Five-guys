package com.lec.spring.controller;


import com.lec.spring.domain.*;
import com.lec.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/")
public class CategoryTableController {

    @Autowired
    AreacodeService areacodeService;
    @Autowired
    SigungucodeService sigungucodeService;
    @Autowired
    TravelClassDetailService travelClassDetailService;
    @Autowired
    TravelTypeService travelTypeService;
    @Autowired
    TravelPostService travelPostService;



    @PostMapping("/testSearch")
    public String searchResult(@RequestParam("Query") String query, @RequestParam("regionQuery") Long regionQuery
            , @RequestParam("sigunguQuery") Long sigunguQuery ,Model model){

        List<TravelPost> travelPostList = null;

        if (query.equals("99") && sigunguQuery == 99){

            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(regionQuery);

            TravelType travelType = travelTypeService.selectById(12l);
            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeList(travelClassDetailList,sigungucodes);
            System.out.println(travelPostList.size());

        }
        else if (query.equals("99")){

            Areacode areacode = areacodeService.selectedByAreacode(regionQuery);
            Sigungucode sigungucode = sigungucodeService.selectedAreacodeBySigungucode(areacode, sigunguQuery);

            TravelType travelType = travelTypeService.selectById(12l);
            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeList(travelClassDetailList,sigungucode);
            System.out.println(travelPostList.size());

        }
        else if(regionQuery == 99 && sigunguQuery==99){
            TravelType travelType = travelTypeService.selectById(12l);
            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType,query);

            travelPostList = travelPostService.selectedByTravelTypeList(travelClassDetailList);
            System.out.println(travelPostList.size());

        }
        else if(sigunguQuery==99){
            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(regionQuery);

            TravelType travelType = travelTypeService.selectById(12l);
            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType,query);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeList(travelClassDetailList,sigungucodes);
            System.out.println(travelPostList.size());

        }
        else {
            Areacode areacode = areacodeService.selectedByAreacode(regionQuery);
            Sigungucode sigungucode = sigungucodeService.selectedAreacodeBySigungucode(areacode, sigunguQuery);

            TravelType travelType = travelTypeService.selectById(12l);
            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType,query);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeList(travelClassDetailList,sigungucode);
            System.out.println(travelPostList.size());

        }

        model.addAttribute("travelPostList",travelPostList);

        return "fragment/results :: results";
    }


    /*
        디폴트 여행정보 전체 출력

        TravelType travelType = travelTypeService.selectById(12l);
        List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);

        List<TravelPost> travelPostList = travelPostService.selectedByTravelTypeList(travelClassDetailList);
        System.out.println(travelPostList.size());
        model.addAttribute("travelPostList",travelPostList);

     */


    @GetMapping("/kategoryTable")
    public String list(
            Model model
//            ,@PathVariable Long id
    ) {
        model.addAttribute("areacode", areacodeService.list());
        List<Areacode> areacodes = areacodeService.list();
        for (Areacode areacode : areacodes) {
            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(areacode.getAreacode());
            model.addAttribute("sigungucodes_" + areacode.getAreacode(), sigungucodes);
        }

        TravelType travelType = travelTypeService.selectById(12L);
        List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);
        Map<String, Boolean> checkedDecodes = new HashMap<>();
        for (TravelClassDetail travelClassDetail : travelClassDetailList) {
            String decode = travelClassDetail.getDecode();
            System.out.println("Decode: " + decode);  // 디버그 출력

            if (!checkedDecodes.containsKey(decode)) {
                checkedDecodes.put(decode, true);
                List<TravelClassDetail> travelClassDetails =
                        travelClassDetailService.selectedTravelTypeIdByDecode(travelType, decode);
                String first = "travelClassDetail_" + decode;
                model.addAttribute(first, travelClassDetails);
                String second = "decode_" + decode;
                model.addAttribute(second, decode);
                System.out.println("Adding attributes: " + first + ", " + second);  // 디버그 출력

            }
        }

        model.addAttribute("decodes", checkedDecodes.keySet());

        return "kategoryTable";

    }
    @GetMapping("/travelSearch")
    public void search(){}

    @PostMapping("/travelSearch")
    public String searchList(@RequestParam("searchQuery") String searchQuery, Model model){

        TravelType travelType = travelTypeService.selectById(12l);
        List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);
        List<TravelPost> travelPostList = null;

        for (TravelClassDetail travelClassDetail : travelClassDetailList){
            travelPostList = travelPostService.selectedTravelTypeByTitleList(travelClassDetail, searchQuery);
            System.out.println(travelPostList.size());
        }

        model.addAttribute("travelPostList",travelPostList);

        return "fragment/results :: results";
    }


}
