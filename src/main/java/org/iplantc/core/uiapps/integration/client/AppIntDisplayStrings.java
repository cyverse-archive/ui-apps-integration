package org.iplantc.core.uiapps.integration.client;

import org.iplantc.core.uicommons.client.CommonUIDisplayStrings;

/**
 * Interface to represent the messages contained in resource bundle:
 * 	/Users/jstroot/git/libraries/ui-apps-integration/src/main/resources/org/iplantc/core/appsIntegration/client/AppIntDisplayStrings.properties'.
 */
public interface AppIntDisplayStrings extends CommonUIDisplayStrings {
  
  /**
   * Translated "Apps Integration Module".
   * 
   * @return translated "Apps Integration Module"
   */
  @DefaultMessage("Apps Integration Module")
  String appIntegrationModule();

    String attribution();

    String newToolReq();

    String searchEmptyText();

    String requestConfirmMsg();

    String newToolRequestError();

    String toolNameLabel();

    String srcBin();

    String srcLinkPrompt();

    String toolDesc();

    String version();

    String docLink();

    String upldTestData();

    String cmdLineRun();

    String addnlData();

    String comments();

    String contactTab();

    String toolTab();

    String otherTab();
    
    String submitRequest();

    String submitting();

    String inValidUrl();

    String isMultiThreaded();

    String toolAttributionLabel();

    String toolAttributionEmptyText();

    /**
     * Location of the bin
     * 
     * @return
     */
    String path();

}
