package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.RegionService;
import kr.co.loosie.foody.domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RegionController {

    @Autowired
    private RegionService regionsService;

    @GetMapping("/regions")
    public List<Region> list(){
        List<Region> regions = regionsService.getRegions();
        return regions;
    }



}
