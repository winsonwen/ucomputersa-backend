package ucomputersa.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ucomputersa.service.MenuService;
import ucomputersa.domain.model.Menu2Model;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class MenuController {

    private MenuService menuService;


    public Mono<ResponseEntity<Map<String, List<Menu2Model>>>> getMenu() {
        return menuService.getMenuJson().map(ResponseEntity::ok);
    }


}