package com.lec.spring.controller;


import com.lec.spring.domain.Areacode;
import com.lec.spring.domain.Sigungucode;
import com.lec.spring.domain.TravelClassDetail;
import com.lec.spring.domain.TravelType;
import com.lec.spring.service.AreacodeService;
import com.lec.spring.service.SigungucodeService;
import com.lec.spring.service.TravelClassDetailService;
import com.lec.spring.service.TravelTypeService;
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


}
