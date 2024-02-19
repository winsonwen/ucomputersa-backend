package ucomputersa.service;

import reactor.core.publisher.Mono;
import ucomputersa.domain.beans.CreatePcServiceRequest;

public interface CustomerPcService {

    Mono<String> createPcService(CreatePcServiceRequest createPCServiceRequest, String customerId);
}
