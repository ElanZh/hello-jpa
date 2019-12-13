package hello.elan.jpa.freight;

import hello.elan.jpa.freight.entity.Waybill;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("freight/waybill")
public class WaybillCtrl {
    private final FreightService freightService;

    public WaybillCtrl(FreightService freightService) {
        this.freightService = freightService;
    }

    @PostMapping("add")
    public boolean add(@RequestBody Waybill req) {
        return freightService.saveWaybill(req);
    }
}
