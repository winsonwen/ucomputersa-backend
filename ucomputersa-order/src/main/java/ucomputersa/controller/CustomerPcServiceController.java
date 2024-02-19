package ucomputersa.controller;


import com.ucomputersa.common.constant.CustomerConstant;
import com.ucomputersa.common.model.AddressModel;
import com.ucomputersa.common.utils.R;
import com.ucomputersa.common.utils.TimeUtil;
import io.jsonwebtoken.lang.Collections;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ucomputersa.constant.ServiceTypeEnum;
import ucomputersa.domain.beans.CreatePcServiceRequest;
import ucomputersa.service.CustomerPcService;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/customer/pcService")
public class CustomerPcServiceController {


    private final Validator validator;

    private final CustomerPcService customerPCService;


    CustomerPcServiceController(Validator validator, CustomerPcService customerPCService) {
        this.validator = validator;
        this.customerPCService = customerPCService;
    }


    @PostMapping("/create")
    public Mono<ResponseEntity<R>> createPcService(ServerWebExchange exchange, @RequestBody CreatePcServiceRequest createPCServiceRequest) {

        Set<ConstraintViolation<CreatePcServiceRequest>> constraintViolations = validator.validate(createPCServiceRequest);
        if (!Collections.isEmpty(constraintViolations)) {
            return Mono.just(ResponseEntity.badRequest().body(R.error(constraintViolations.toString())));
        }

        // validating start time and end time
        if (!TimeUtil.validateGreatThanCurrentTime(createPCServiceRequest.getStartTime(), createPCServiceRequest.getEndTime())) {
            return Mono.just(ResponseEntity.badRequest().body(R.error("please enter a validated start time or end time")));
        }

        if (ServiceTypeEnum.DOOR_TO_DOOR.name().equals(createPCServiceRequest.getServiceType())) {
            if (Objects.isNull(createPCServiceRequest.getAddress())) {
                return Mono.just(ResponseEntity.badRequest().body(R.error("please enter a validated address")));
            }
            Set<ConstraintViolation<AddressModel>> addressBeanValidator = validator.validate(createPCServiceRequest.getAddress());
            if (!Collections.isEmpty(addressBeanValidator)) {
                return Mono.just(ResponseEntity.badRequest().body(R.error(addressBeanValidator.toString())));
            }
        } else if (ServiceTypeEnum.TO_STORE.name().equals(createPCServiceRequest.getServiceType())) {
            if (Objects.nonNull(createPCServiceRequest.getAddress()))
                return Mono.just(ResponseEntity.badRequest().body(R.error("Address should be null when your service is ToStore")));
        } else {
            return Mono.just(ResponseEntity.badRequest().body(R.error("Service Type is correct not as expected")));
        }

        return customerPCService.createPcService(createPCServiceRequest, exchange.getAttributes().get(CustomerConstant.CUSTOMER_ID).toString()).map(response -> ResponseEntity.ok(R.ok(response)));
    }


}
