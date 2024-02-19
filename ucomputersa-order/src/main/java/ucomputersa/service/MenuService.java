package ucomputersa.service;

import reactor.core.publisher.Mono;
import ucomputersa.domain.model.Menu2Model;

import java.util.List;
import java.util.Map;

public interface MenuService {


    Mono<Map<String, List<Menu2Model>>> getMenuJson();
}
