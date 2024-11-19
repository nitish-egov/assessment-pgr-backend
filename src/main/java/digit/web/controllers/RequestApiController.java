package digit.web.controllers;


import digit.service.PGRService;
import digit.util.PGRConstants;
import digit.util.ResponseInfoFactory;
import digit.web.models.CountResponse;
import digit.web.models.ErrorRes;
import digit.web.models.RequestInfoWrapper;
import digit.web.models.RequestSearchCriteria;
import digit.web.models.ServiceRequest;
import digit.web.models.ServiceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.web.models.ServiceWrapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import org.egov.common.contract.response.ResponseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.IOException;
import java.util.*;

import jakarta.validation.constraints.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

@jakarta.annotation.Generated(value = "org.egov.codegen.SpringBootCodegen", date = "2024-11-18T11:04:31.362244598+05:30[Asia/Kolkata]")
@RestController
@RequestMapping("")
public class RequestApiController {

  private final ObjectMapper objectMapper;
  private PGRService pgrService;


  private final HttpServletRequest request;

  private ResponseInfoFactory responseInfoFactory;

  @Autowired
  public RequestApiController(ObjectMapper objectMapper, PGRService pgrService,
      HttpServletRequest request, ResponseInfoFactory responseInfoFactory) {
    this.objectMapper = objectMapper;
    this.pgrService = pgrService;
    this.request = request;
    this.responseInfoFactory = responseInfoFactory;
  }


  @RequestMapping(value = "/request1/_create", method = RequestMethod.POST)
  public ResponseEntity<ServiceResponse> requestsCreatePost(
      @Valid @RequestBody ServiceRequest request) throws IOException {
    ServiceRequest enrichedReq = pgrService.create(request);
    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(
        request.getRequestInfo(), true);
    ServiceWrapper serviceWrapper = ServiceWrapper.builder().service(enrichedReq.getService())
        .workflow(enrichedReq.getWorkflow()).build();
    ServiceResponse response = ServiceResponse.builder().responseInfo(responseInfo)
        .serviceWrappers(Collections.singletonList(serviceWrapper)).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @RequestMapping(value = "/request1/_search", method = RequestMethod.POST)
  public ResponseEntity<ServiceResponse> requestsSearchPost(
      @javax.validation.Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
      @javax.validation.Valid @ModelAttribute RequestSearchCriteria criteria) {

    String tenantId = criteria.getTenantId();
    List<ServiceWrapper> serviceWrappers = pgrService.search(requestInfoWrapper.getRequestInfo(),
        criteria);
//    Map<String, Integer> dynamicData = pgrService.getDynamicData(tenantId);
//
//    int complaintsResolved = dynamicData.get(PGRConstants.COMPLAINTS_RESOLVED);
//    int averageResolutionTime = dynamicData.get(PGRConstants.AVERAGE_RESOLUTION_TIME);
//    int complaintTypes = pgrService.getComplaintTypes();

    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(
        requestInfoWrapper.getRequestInfo(), true);
//    ServiceResponse response = ServiceResponse.builder().responseInfo(responseInfo)
//        .serviceWrappers(serviceWrappers).complaintsResolved(complaintsResolved)
//        .averageResolutionTime(averageResolutionTime).complaintTypes(complaintTypes).build();
    ServiceResponse response = ServiceResponse.builder().responseInfo(responseInfo).serviceWrappers(serviceWrappers).build();
    return new ResponseEntity<>(response, HttpStatus.OK);

  }


  // update
  @RequestMapping(value="/request1/_update", method = RequestMethod.POST)
  public ResponseEntity<ServiceResponse> requestsUpdatePost(@Valid @RequestBody ServiceRequest request) throws IOException {
    ServiceRequest enrichedReq = pgrService.update(request);
    ServiceWrapper serviceWrapper = ServiceWrapper.builder().service(enrichedReq.getService()).workflow(enrichedReq.getWorkflow()).build();
    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(request.getRequestInfo(), true);
    ServiceResponse response = ServiceResponse.builder().responseInfo(responseInfo).serviceWrappers(Collections.singletonList(serviceWrapper)).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @RequestMapping(value="/request1/_count", method = RequestMethod.POST)
  public ResponseEntity<CountResponse> requestsCountPost(@Valid @RequestBody RequestInfoWrapper requestInfoWrapper,
      @Valid @ModelAttribute RequestSearchCriteria criteria) {
    Integer count = pgrService.count(requestInfoWrapper.getRequestInfo(), criteria);
    ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfoWrapper.getRequestInfo(), true);
    CountResponse response = CountResponse.builder().responseInfo(responseInfo).count(count).build();
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
