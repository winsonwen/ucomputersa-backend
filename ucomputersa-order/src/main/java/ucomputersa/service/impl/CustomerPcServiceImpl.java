package ucomputersa.service.impl;

import com.ucomputersa.common.config.HibernateService;
import com.ucomputersa.common.utils.TimeUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ucomputersa.constant.ServiceTypeEnum;
import ucomputersa.domain.beans.CreatePcServiceRequest;
import ucomputersa.domain.entity.PcServiceEntity;
import ucomputersa.service.CustomerPcService;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class CustomerPcServiceImpl implements CustomerPcService {

    private HibernateService hibernateService;

    @Override
    public Mono<String> createPcService(CreatePcServiceRequest createPCServiceRequest, String customerId) {
        return hibernateService.synchronizeSessionReactive(() -> {
            PcServiceEntity pcServiceEntity = new PcServiceEntity();
            BeanUtils.copyProperties(createPCServiceRequest, pcServiceEntity);
            pcServiceEntity.setCustomerId(customerId);


            if (ServiceTypeEnum.TO_STORE.name().equals(createPCServiceRequest.getServiceType())) {
                pcServiceEntity.setServiceType(ServiceTypeEnum.TO_STORE);
            } else if (ServiceTypeEnum.DOOR_TO_DOOR.name().equals(createPCServiceRequest.getServiceType())) {
                pcServiceEntity.setServiceType(ServiceTypeEnum.DOOR_TO_DOOR);
            }

            LocalDateTime currentLocalDateTime = TimeUtil.getCurrentLocalDateTime();
            pcServiceEntity.setCreateDate(currentLocalDateTime);
            pcServiceEntity.setLastModificationDate(currentLocalDateTime);
            //pcServiceEntity.setServiceStatus();
            return null;

        });


    }
}

