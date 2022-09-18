package com.github.weaksloth.dolphins.process;

import com.github.weaksloth.dolphins.core.AbstractOperator;
import com.github.weaksloth.dolphins.core.DolphinException;
import com.github.weaksloth.dolphins.remote.DolphinsRestTemplate;
import com.github.weaksloth.dolphins.remote.HttpRestResult;
import com.github.weaksloth.dolphins.remote.Query;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProcessOperator extends AbstractOperator {

  public ProcessOperator(
      String dolphinAddress, String token, DolphinsRestTemplate dolphinsRestTemplate) {
    super(dolphinAddress, token, dolphinsRestTemplate);
  }

  /**
   * create dolphin scheduler process api:
   * /dolphinscheduler/projects/{projectCode}/process-definition
   *
   * @param projectCode project code
   * @param processDefineParam create process param
   * @return create response
   */
  public ProcessDefineResp create(Long projectCode, ProcessDefineParam processDefineParam) {
    String url = dolphinAddress + "/projects/" + projectCode + "/process-definition";
    log.info("create process definition,url:{}", url);
    try {
      HttpRestResult<ProcessDefineResp> restResult =
          dolphinsRestTemplate.postForm(
              url, getHeader(), processDefineParam, ProcessDefineResp.class);
      if (restResult.getSuccess()) {
        return restResult.getData();
      } else {
        log.error("{},response:{}", DolphinException.CREATE_WORKFLOW_ERROR, restResult);
        throw new DolphinException(DolphinException.CREATE_WORKFLOW_ERROR);
      }
    } catch (Exception e) {
      log.error(DolphinException.CREATE_WORKFLOW_ERROR, e);
      throw new DolphinException(DolphinException.CREATE_WORKFLOW_ERROR);
    }
  }

  /**
   * update dolphin scheduler workflow
   *
   * <p>api:/dolphinscheduler/projects/{projectCode}/process-definition/{process-definition-code}
   *
   * @param processDefineParam update process def param
   * @param processCode workflow code
   * @return update response json
   */
  public ProcessDefineResp update(
      Long projectCode, ProcessDefineParam processDefineParam, Long processCode) {
    String url = dolphinAddress + "/projects/" + projectCode + "/process-definition/" + processCode;
    log.info("update process definition,url:{}", url);
    try {
      HttpRestResult<ProcessDefineResp> restResult =
          dolphinsRestTemplate.putForm(
              url, getHeader(), processDefineParam, ProcessDefineResp.class);
      if (restResult.getSuccess()) {
        return restResult.getData();
      } else {
        log.error("{},response:{}", DolphinException.UPDATE_WORKFLOW_ERROR, restResult);
        throw new DolphinException(DolphinException.UPDATE_WORKFLOW_ERROR);
      }
    } catch (Exception e) {
      log.error(DolphinException.UPDATE_WORKFLOW_ERROR, e);
      throw new DolphinException(DolphinException.UPDATE_WORKFLOW_ERROR);
    }
  }

  /**
   * delete process
   *
   * @param projectCode project code
   * @param processCode process code
   * @return true for success,otherwise false
   */
  public Boolean delete(Long projectCode, Long processCode) {
    String url = dolphinAddress + "/projects/" + projectCode + "/process-definition/" + processCode;
    try {
      HttpRestResult<String> restResult =
          dolphinsRestTemplate.delete(url, getHeader(), null, String.class);
      return restResult.getSuccess();
    } catch (Exception e) {
      log.error(DolphinException.DELETE_WORKFLOW_ERROR, e);
      throw new DolphinException(DolphinException.DELETE_WORKFLOW_ERROR);
    }
  }

  /**
   * release, api: /dolphinscheduler/projects/{projectCode}/process-definition/{code}/release
   *
   * @param projectCode project code
   * @param code workflow id
   * @param processReleaseParam param
   * @return true for success,otherwise false
   */
  public Boolean release(Long projectCode, Long code, ProcessReleaseParam processReleaseParam) {
    String url =
        dolphinAddress + "/projects/" + projectCode + "/process-definition/" + code + "/release";
    log.info("release process definition,url:{}", url);
    try {
      HttpRestResult<String> restResult =
          dolphinsRestTemplate.postForm(url, getHeader(), processReleaseParam, String.class);
      return restResult.getSuccess();
    } catch (Exception e) {
      log.error(
          "{},current release param:{}",
          DolphinException.RELEASE_WORKFLOW_ERROR,
          processReleaseParam,
          e);
      throw new DolphinException(DolphinException.RELEASE_WORKFLOW_ERROR);
    }
  }

  /**
   * generate task code
   *
   * @param projectCode project's code
   * @param codeNumber the number of task code
   * @return task code list
   */
  public List<Long> generateTaskCode(Long projectCode, int codeNumber) {
    String url = dolphinAddress + "/projects/" + projectCode + "/task-definition/gen-task-codes";
    Query query = new Query();
    query.addParam("genNum", String.valueOf(codeNumber));
    try {
      HttpRestResult<List> restResult =
          dolphinsRestTemplate.get(url, getHeader(), query, List.class);
      return (List<Long>) restResult.getData();
    } catch (Exception e) {
      log.error(DolphinException.GENERATE_TASK_CODE_ERROR, e);
      throw new DolphinException(DolphinException.GENERATE_TASK_CODE_ERROR);
    }
  }
}
