package ucomputersa.domain.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 耳机分类
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu2Model {

    private String MenuId;

    private List<Menu3Model> Menu3List;

    private String id;

    private String name;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Menu3Model {

        private String Menu2Id;

        private String id;

        private String name;

    }

}
