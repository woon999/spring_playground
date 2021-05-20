package kr.co.loosie.foody.interfaces;

import kr.co.loosie.foody.application.RegionService;
import kr.co.loosie.foody.domain.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
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

    @PostMapping("/regions")
    public ResponseEntity<?> create(
            @RequestBody Region resource
    ) throws URISyntaxException {
        String name = resource.getName();

        Region region = regionsService.addRegion(name);

        String url = "/regions/" + region.getId();
        return ResponseEntity.created(new URI(url)).body("{}");
    }

}
