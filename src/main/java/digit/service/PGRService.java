package digit.service;

import digit.config.PGRConfiguration;
import digit.kafka.Producer;
import digit.repository.PGRRepository;
import digit.util.MDMSUtils;
import digit.validator.ServiceRequestValidator;
import digit.web.models.RequestSearchCriteria;
import digit.web.models.ServiceRequest;
import digit.web.models.ServiceWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.egov.common.contract.request.RequestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class PGRService {


  private EnrichmentService enrichmentService;

  private UserService userService;

  private WorkflowService workflowService;

  private ServiceRequestValidator serviceRequestValidator;

  private ServiceRequestValidator validator;

  private Producer producer;

  private PGRConfiguration config;

  private PGRRepository repository;

  private MDMSUtils mdmsUtils;


  @Autowired
  public PGRService(EnrichmentService enrichmentService, UserService userService,
      WorkflowService workflowService,
      ServiceRequestValidator serviceRequestValidator, ServiceRequestValidator validator,
      Producer producer,
      PGRConfiguration config, PGRRepository repository, MDMSUtils mdmsUtils) {
    this.enrichmentService = enrichmentService;
    this.userService = userService;
    this.workflowService = workflowService;
    this.serviceRequestValidator = serviceRequestValidator;
    this.validator = validator;
    this.producer = producer;
    this.config = config;
    this.repository = repository;
    this.mdmsUtils = mdmsUtils;
  }


  /**
   * Creates a complaint in the system
   *
   * @param request The service request containg the complaint information
   * @return
   */
  public ServiceRequest create(ServiceRequest request) {
    Object mdmsData = mdmsUtils.mDMSCall(request);
    validator.validateCreate(request, mdmsData);
    enrichmentService.enrichCreateRequest(request);
    workflowService.updateWorkflowStatus(request);
    producer.push(config.getCreateTopic(), request);
    return request;
  }

  public List<ServiceWrapper> search(RequestInfo requestInfo, RequestSearchCriteria criteria) {
    validator.validateSearch(requestInfo, criteria);
    enrichmentService.enrichSearchRequest(requestInfo, criteria);

    if(criteria.getMobileNumber()!=null && CollectionUtils.isEmpty(criteria.getUserIds()))
      return new ArrayList<>();

    criteria.setIsPlainSearch(false);

    List<ServiceWrapper> serviceWrappers = repository.getServiceWrappers(criteria);

    if(CollectionUtils.isEmpty(serviceWrappers))
      return new ArrayList<>();;

    userService.enrichUsers(serviceWrappers);
    List<ServiceWrapper> enrichedServiceWrappers = workflowService.enrichWorkflow(requestInfo,serviceWrappers);
    Map<Long, List<ServiceWrapper>> sortedWrappers = new TreeMap<>(Collections.reverseOrder());
    for(ServiceWrapper svc : enrichedServiceWrappers){
      if(sortedWrappers.containsKey(svc.getService().getAuditDetails().getCreatedTime())){
        sortedWrappers.get(svc.getService().getAuditDetails().getCreatedTime()).add(svc);
      }else{
        List<ServiceWrapper> serviceWrapperList = new ArrayList<>();
        serviceWrapperList.add(svc);
        sortedWrappers.put(svc.getService().getAuditDetails().getCreatedTime(), serviceWrapperList);
      }
    }
    List<ServiceWrapper> sortedServiceWrappers = new ArrayList<>();
    for(Long createdTimeDesc : sortedWrappers.keySet()){
      sortedServiceWrappers.addAll(sortedWrappers.get(createdTimeDesc));
    }
    return sortedServiceWrappers;
  }


  public ServiceRequest update(ServiceRequest request){
    Object mdmsData = mdmsUtils.mDMSCall(request);
    validator.validateUpdate(request, mdmsData);
    enrichmentService.enrichUpdateRequest(request);
    workflowService.updateWorkflowStatus(request);
    producer.push(config.getUpdateTopic(),request);
    return request;
  }


  public Integer count(RequestInfo requestInfo, RequestSearchCriteria criteria){
    criteria.setIsPlainSearch(false);
    Integer count = repository.getCount(criteria);
    return count;
  }

}




