package hello.elan.jpa.freight;

import hello.elan.jpa.freight.entity.Waybill;
import hello.elan.jpa.freight.entity.WaybillAddress;
import hello.elan.jpa.freight.entity.Wrap;
import hello.elan.jpa.freight.repo.WaybillAddressRepo;
import hello.elan.jpa.freight.repo.WaybillRepo;
import hello.elan.jpa.freight.repo.WrapRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class FreightService {

    private final WaybillRepo waybillRepo;
    private final WaybillAddressRepo waybillAddressRepo;
    private final WrapRepo wrapRepo;

    public FreightService(WaybillRepo waybillRepo, WaybillAddressRepo waybillAddressRepo, WrapRepo wrapRepo) {
        this.waybillRepo = waybillRepo;
        this.waybillAddressRepo = waybillAddressRepo;
        this.wrapRepo = wrapRepo;
    }

    @GetMapping("list")
    public List<Waybill> list(){
        return waybillRepo.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveWaybill(Waybill req) {
        req.setId(null);
        req.setWaybillNum(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss")));
        List<WaybillAddress> waybillAddressSet = req.getWaybillAddressSet();
        Set<Wrap> wraps = new HashSet<>(waybillAddressSet.size() << 8);
        waybillAddressSet.forEach(waybillAddress -> wraps.addAll(waybillAddress.getWrapSet()));
        req.setWrapSet(wraps);
        req = waybillRepo.saveAndFlush(req);
        for (WaybillAddress waybillAddress : waybillAddressSet) {
            waybillAddress.setWaybill(req);
        }
        waybillAddressSet = waybillAddressRepo.saveAll(waybillAddressSet);
        return true;
    }
}
