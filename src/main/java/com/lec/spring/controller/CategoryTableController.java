package com.lec.spring.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lec.spring.domain.*;
import com.lec.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

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


    @PostMapping("/testSearch/{id}")
    public String searchResult(@RequestParam("Query") String query, @RequestParam("regionQuery") Long regionQuery
            , @RequestParam("sigunguQuery") Long sigunguQuery, @RequestParam("searchQuery") String searchQuery, @PathVariable("id") long id, Model model
            , @RequestParam("offset") int offset, @RequestParam("limit") int limit) throws JsonProcessingException {

        List<TravelPost> travelPostList = null;
        TravelType travelType = travelTypeService.selectById(id);

        String type = null;

        if (travelType.getId() == 12) {
            type = "tour";
        } else if (travelType.getId() == 15) {
            type = "festival";
        }

        if (query.equals("99") && sigunguQuery == 99 && regionQuery == 99 && searchQuery.equals("99")) {

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);

            travelPostList = travelPostService.selectedByTravelTypeList(
                    travelClassDetailList, travelType.getId(),limit, offset);
            System.out.println(travelPostList.size());

        } else if (query.equals("99") && sigunguQuery == 99 && regionQuery == 99) {

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);

            travelPostList = travelPostService.selectedByTravelTypAndSearchList(
                    travelClassDetailList, travelType.getId(), searchQuery,limit, offset);
            System.out.println(travelPostList.size());

        } else if (query.equals("99") && searchQuery.equals("99") && sigunguQuery == 99) {

            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(regionQuery);


            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeList(
                    travelClassDetailList, sigungucodes, travelType.getId(),limit, offset);
            System.out.println(travelPostList.size());

        } else if (query.equals("99") && sigunguQuery == 99) {

            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(regionQuery);


            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSearchList(
                    travelClassDetailList, sigungucodes, travelType.getId(), searchQuery,limit, offset);
            System.out.println(travelPostList.size());

        } else if (query.equals("99") && searchQuery.equals("99")) {

            Areacode areacode = areacodeService.selectedByAreacode(regionQuery);
            Sigungucode sigungucode = sigungucodeService.selectedAreacodeBySigungucode(areacode, sigunguQuery);
            System.out.println(sigungucode.getSigungucode() + "시군구 코드");
            System.out.println(sigungucode.getAreacode().getAreacode() + "지역코드");

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);
            System.out.println(travelClassDetailList.size() + "size");

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeList(
                    travelClassDetailList, sigungucode, travelType.getId(),limit, offset);
            System.out.println(travelPostList.size());

        } else if (query.equals("99")) {

            Areacode areacode = areacodeService.selectedByAreacode(regionQuery);
            Sigungucode sigungucode = sigungucodeService.selectedAreacodeBySigungucode(areacode, sigunguQuery);
            System.out.println(sigungucode.getSigungucode() + "시군구 코드");
            System.out.println(sigungucode.getAreacode().getAreacode() + "지역코드");

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);
            System.out.println(travelClassDetailList.size() + "size");

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeAndSearchList(
                    travelClassDetailList, sigungucode, travelType.getId(), searchQuery, limit, offset);
            System.out.println(travelPostList.size());

        } else if (regionQuery == 99 && sigunguQuery == 99 && searchQuery.equals("99")) {

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType, query);

            travelPostList = travelPostService.selectedByTravelTypeList(
                    travelClassDetailList, travelType.getId(),limit, offset);
            System.out.println(travelPostList.size());

        } else if (regionQuery == 99 && sigunguQuery == 99) {

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType, query);

            travelPostList = travelPostService.selectedByTravelTypAndSearchList(
                    travelClassDetailList, travelType.getId(), searchQuery,limit, offset);
            System.out.println(travelPostList.size());

        } else if (sigunguQuery == 99 && searchQuery.equals("99")) {
            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(regionQuery);

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType, query);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeList(
                    travelClassDetailList, sigungucodes, travelType.getId(),limit, offset);
            System.out.println(travelPostList.size());

        } else if (sigunguQuery == 99) {
            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(regionQuery);

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType, query);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSearchList(
                    travelClassDetailList, sigungucodes, travelType.getId(), searchQuery, limit, offset);
            System.out.println(travelPostList.size());

        } else if (searchQuery.equals("99")) {
            Areacode areacode = areacodeService.selectedByAreacode(regionQuery);
            Sigungucode sigungucode = sigungucodeService.selectedAreacodeBySigungucode(areacode, sigunguQuery);

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType, query);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeList(
                    travelClassDetailList, sigungucode, travelType.getId(), limit, offset);
            System.out.println(travelPostList.size());
        } else {
            Areacode areacode = areacodeService.selectedByAreacode(regionQuery);
            Sigungucode sigungucode = sigungucodeService.selectedAreacodeBySigungucode(areacode, sigunguQuery);

            List<TravelClassDetail> travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType, query);

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeAndSearchList(
                    travelClassDetailList, sigungucode, travelType.getId(), searchQuery, limit, offset);
            System.out.println(travelPostList.size());

        }

        model.addAttribute(type, travelPostList);


        return "fragment/results :: results";
    }


    @GetMapping("/categoryTable/{id}")
    public String list(Model model, @PathVariable("id") Long id) throws JsonProcessingException {
        model.addAttribute("areacode", areacodeService.list());
        List<Areacode> areacodes = areacodeService.list();
        for (Areacode areacode : areacodes) {
            List<Sigungucode> sigungucodes = sigungucodeService.selectedByAreacode(areacode.getAreacode());
            model.addAttribute("sigungucodes_" + areacode.getAreacode(), sigungucodes);
        }

        TravelType travelType = travelTypeService.selectById(id);

        model.addAttribute("categoryId", travelType.getId());

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


        return "categoryTable";

    }


}
