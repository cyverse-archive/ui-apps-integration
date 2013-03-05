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

    String version();

    String searchEmptyText();

    /**
     * Location of the bin
     * 
     * @return
     */
    String path();

    String newToolReq();

}
