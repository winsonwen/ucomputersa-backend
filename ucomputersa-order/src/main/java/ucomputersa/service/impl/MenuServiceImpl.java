package ucomputersa.service.impl;

import com.ucomputersa.common.config.HibernateService;
import com.ucomputersa.common.security.JwtService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ucomputersa.constant.ShowStatusEnum;
import ucomputersa.domain.entity.MenuEntity;
import ucomputersa.repository.MenuRepository;
import ucomputersa.service.MenuService;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class MenuServiceImpl implements MenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    private MenuRepository menuRepository;

    private HibernateService hibernateService;

    private JwtService jwtService;


    //TODO Is caching successful? check
    @Cacheable(value = "menu", key = "#root.method.name", sync = true)  //sync = true:local lock
    @Override
    public Mono<Map<String, List<Menu2Model>>> getMenuJson() {
        return hibernateService.synchronizeSessionReactive(() -> {
            List<MenuEntity> selectList = menuRepository.findByShowStatus(ShowStatusEnum.SHOWED);

            //1. find all level 1 menu
            List<MenuEntity> level1Categorys = getParent_cid(selectList, 0);
            return level1Categorys.stream().collect(Collectors.toMap(k -> k.getMenuId().toString(), v -> {
                List<MenuEntity> categoryEntities = getParent_cid(selectList, v.getParentMenuId());
                List<Menu2Model> menu2Models = new LinkedList<>();
                if (categoryEntities != null) {
                    menu2Models = categoryEntities.stream().map(l2 -> {
                        Menu2Model menu2Model = new Menu2Model(v.getMenuId().toString(), null, l2.getMenuId().toString(), l2.getName());

                        List<MenuEntity> level3Catelog = getParent_cid(selectList, l2.getMenuId());
                        if (level3Catelog != null) {
                            List<Menu2Model.Menu3Model> collect = level3Catelog.stream().map(l3 -> {
                                //2.封装成指定格式
                                return new Menu2Model.Menu3Model(l2.getMenuId().toString(), l3.getMenuId().toString(), l3.getName());
                            }).collect(Collectors.toList());
                            menu2Model.setMenu3List(collect);
                        }
                        return menu2Model;
                    }).collect(Collectors.toList());
                }
                return menu2Models;
            }));

        });
    }

    private List<MenuEntity> getParent_cid(List<MenuEntity> selectList, int parentId) {
        return selectList.stream().filter(item -> item.getParentMenuId() == parentId).collect(Collectors.toList());
    }
}
