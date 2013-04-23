/**
 * 
 */
package org.iplantc.core.uiapps.integration.client.models;

import java.util.List;

import org.iplantc.core.uiapps.widgets.client.models.DeployedComponent;

import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

/**
 * @author sriram
 *
 */
public interface DeployedComponentList {

    @PropertyName("components")
    List<DeployedComponent> getDCList();

}
