package com.lec.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lec.spring.config.PrincipalDetails;
import com.lec.spring.domain.*;
import com.lec.spring.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/Search/{id}")
    public String searchResult(@RequestParam("Query") String query, @RequestParam("regionQuery") Long regionQuery
            , @RequestParam("sigunguQuery") Long sigunguQuery, @RequestParam("searchQuery") String searchQuery, @PathVariable("id") long id, Model model
            , @RequestParam("offset") int offset, @RequestParam("limit") int limit) {

        List<TravelPost> travelPostList;
        List<TravelPost> totalTravelPostList;
        List<TravelClassDetail> travelClassDetailList;
        Areacode areacode;
        Sigungucode sigungucode = null;
        List<Sigungucode> sigungucodes = null;

        TravelType travelType = travelTypeService.selectById(id);

        String type = null;

        if (query.equals("99")) {
            travelClassDetailList = travelClassDetailService.selectedByTravelTypeList(travelType);
        } else {
            travelClassDetailList = travelClassDetailService.selectedTravelTypeByCodeList(travelType, query);
        }

        if (regionQuery != 99) {
            sigungucodes = sigungucodeService.selectedByAreacode(regionQuery);
        }

        if (sigunguQuery != 99) {
            areacode = areacodeService.selectedByAreacode(regionQuery);
            sigungucode = sigungucodeService.selectedAreacodeBySigungucode(areacode, sigunguQuery);
        }

        if (regionQuery == 99 && sigunguQuery == 99 && searchQuery.equals("99")) {

            travelPostList = travelPostService.selectedByTravelTypeList(travelClassDetailList, travelType.getId(), limit, offset);
            totalTravelPostList = travelPostService.selectedByTravelTypeTotalList(travelClassDetailList, travelType.getId());

        } else if (regionQuery == 99 && sigunguQuery == 99) {

            travelPostList = travelPostService.selectedByTravelTypAndSearchList(travelClassDetailList, travelType.getId(), searchQuery, limit, offset);
            totalTravelPostList = travelPostService.selectedByTravelTypAndSearchTotalList(travelClassDetailList, travelType.getId(), searchQuery);

        } else if (sigunguQuery == 99 && searchQuery.equals("99")) {

            travelPostList = travelPostService.selectedTravelTypeByAreacodeList(travelClassDetailList, sigungucodes, travelType.getId(), limit, offset);
            totalTravelPostList = travelPostService.selectedTravelTypeByAreacodeTotalList(travelClassDetailList, sigungucodes, travelType.getId());

        } else if (sigunguQuery == 99) {

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSearchList(travelClassDetailList, sigungucodes, travelType.getId(), searchQuery, limit, offset);
            totalTravelPostList = travelPostService.selectedTravelTypeByAreacodeAndSearchTotalList(travelClassDetailList, sigungucodes, travelType.getId(), searchQuery);

        } else if (searchQuery.equals("99")) {

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeList(travelClassDetailList, sigungucode, travelType.getId(), limit, offset);
            totalTravelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeTotalList(travelClassDetailList, sigungucode, travelType.getId());

        } else {

            travelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeAndSearchList(travelClassDetailList, sigungucode, travelType.getId(), searchQuery, limit, offset);
            totalTravelPostList = travelPostService.selectedTravelTypeByAreacodeAndSigungucodeAndSearchTotalList(travelClassDetailList, sigungucode, travelType.getId(), searchQuery);

        }

        if (travelType.getId() == 12) {
            type = "tour";
        } else if (travelType.getId() == 15) {
            type = "festival";
            travelPostList.forEach(TravelPost::preparaData);
        }

        travelPostList.forEach(TravelPost::defaultImageData);

        model.addAttribute(type, travelPostList);

        if (offset == 0) {
            model.addAttribute("count", totalTravelPostList);
            return "fragment/results :: results";
        } else {
            return "fragment/appendResults :: appendResults";
        }

    }

    @GetMapping("/categoryTable/{id}")
    public String list(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {

        if (userDetails != null) {
            User loggedUser = ((PrincipalDetails) userDetails).getUser();
            model.addAttribute("loggedUser", loggedUser);
        } else {
            model.addAttribute("loggedUser", null);
        }

        if (travelTypeService.selectById(id) == null) {
            return "redirect:/";
        }

        List<Areacode> areacodes = areacodeService.list();
        model.addAttribute("areacode", areacodes);

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

            if (!checkedDecodes.containsKey(decode)) {
                checkedDecodes.put(decode, true);
                List<TravelClassDetail> travelClassDetails = travelClassDetailService.selectedTravelTypeIdByDecode(travelType, decode);
                String first = "travelClassDetail_" + decode;
                model.addAttribute(first, travelClassDetails);
                String second = "decode_" + decode;
                model.addAttribute(second, decode);

            }

        }

        model.addAttribute("decodes", checkedDecodes.keySet());

        return "categoryTable";

    }

}
