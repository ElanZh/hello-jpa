package hello.elan.jpa.freight;

import hello.elan.jpa.freight.entity.Wrap;
import hello.elan.jpa.freight.repo.WrapRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("freight/wrap")
public class WrapCtrl {
    private final WrapRepo wrapRepo;

    public WrapCtrl(WrapRepo wrapRepo) {
        this.wrapRepo = wrapRepo;
    }

    @PostMapping("add")
    public List<Wrap> add(@RequestBody List<Wrap> req) {
        return wrapRepo.saveAll(req);
    }

    @GetMapping("list")
    public List<Wrap> get() {
        return wrapRepo.findAll();
    }
}
