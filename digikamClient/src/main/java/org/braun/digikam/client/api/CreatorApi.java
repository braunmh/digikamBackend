/*
 * Digikam
 * RESTful Backend for Digikam
 *
 * The version of the OpenAPI document: 1.0
 * Contact: braun.h.michael@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package org.braun.digikam.client.api;

import com.fasterxml.jackson.core.type.TypeReference;

import org.braun.digikam.client.invoker.ApiException;
import org.braun.digikam.client.invoker.ApiClient;
import org.braun.digikam.client.invoker.Configuration;
import org.braun.digikam.client.model.*;
import org.braun.digikam.client.invoker.Pair;

import org.braun.digikam.client.model.Creator;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-05-05T16:52:31.595785912+02:00[Europe/Berlin]")
public class CreatorApi {


  private ApiClient apiClient;

  public CreatorApi() {
    this(Configuration.getDefaultApiClient());
  }

  public CreatorApi(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  public ApiClient getApiClient() {
    return apiClient;
  }

  public void setApiClient(ApiClient apiClient) {
    this.apiClient = apiClient;
  }

  /**
   * Find Creator of an Image by Name
   * 
   * @param name  (required)
   * @return List&lt;Creator&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Creator> findCreatorsByName(String name) throws ApiException {
    return this.findCreatorsByName(name, Collections.emptyMap());
  }


  /**
   * Find Creator of an Image by Name
   * 
   * @param name  (required)
   * @param additionalHeaders additionalHeaders for this call
   * @return List&lt;Creator&gt;
   * @throws ApiException if fails to make API call
   */
  public List<Creator> findCreatorsByName(String name, Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = null;
    
    // verify the required parameter 'name' is set
    if (name == null) {
      throw new ApiException(400, "Missing the required parameter 'name' when calling findCreatorsByName");
    }
    
    // create path and map variables
    String localVarPath = "/creator/findByName";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    localVarQueryParams.addAll(apiClient.parameterToPair("name", name));
    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      "application/json"
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    TypeReference<List<Creator>> localVarReturnType = new TypeReference<List<Creator>>() {};
    return apiClient.invokeAPI(
        localVarPath,
        "GET",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        localVarReturnType
    );
  }

  /**
   * 
   * 
   * @throws ApiException if fails to make API call
   */
  public void refreshCreatorCache() throws ApiException {
    this.refreshCreatorCache(Collections.emptyMap());
  }


  /**
   * 
   * 
   * @param additionalHeaders additionalHeaders for this call
   * @throws ApiException if fails to make API call
   */
  public void refreshCreatorCache(Map<String, String> additionalHeaders) throws ApiException {
    Object localVarPostBody = null;
    
    // create path and map variables
    String localVarPath = "/creator/refresh";

    StringJoiner localVarQueryStringJoiner = new StringJoiner("&");
    String localVarQueryParameterBaseName;
    List<Pair> localVarQueryParams = new ArrayList<Pair>();
    List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
    Map<String, String> localVarHeaderParams = new HashMap<String, String>();
    Map<String, String> localVarCookieParams = new HashMap<String, String>();
    Map<String, Object> localVarFormParams = new HashMap<String, Object>();

    
    localVarHeaderParams.putAll(additionalHeaders);

    
    
    final String[] localVarAccepts = {
      
    };
    final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);

    final String[] localVarContentTypes = {
      
    };
    final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

    String[] localVarAuthNames = new String[] {  };

    apiClient.invokeAPI(
        localVarPath,
        "GET",
        localVarQueryParams,
        localVarCollectionQueryParams,
        localVarQueryStringJoiner.toString(),
        localVarPostBody,
        localVarHeaderParams,
        localVarCookieParams,
        localVarFormParams,
        localVarAccept,
        localVarContentType,
        localVarAuthNames,
        null
    );
  }

}
