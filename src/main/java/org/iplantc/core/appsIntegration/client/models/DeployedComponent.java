package org.iplantc.core.appsIntegration.client.models;

import com.google.gwt.user.client.ui.HasName;
import com.google.web.bindery.autobean.shared.AutoBean.PropertyName;

import org.iplantc.core.uicommons.client.models.HasDescription;
import org.iplantc.core.uicommons.client.models.HasId;

public interface DeployedComponent extends HasId, HasDescription, HasName {

    @PropertyName("hid")
    String getHid();
    
    @PropertyName("hid")
    void setHid(String hid);

    @PropertyName("location")
    String location();

    @PropertyName("location")
    void setLocation(String location);

    @PropertyName("type")
    void setType(String type);

    @PropertyName("type")
    String getType();   
    
    @PropertyName("attribution")
    String getAttribution();
    
    @PropertyName("attribution")
    void setAttribution(String attribution);
    
    @PropertyName("version")
    void setVersion(String version);
    
    @PropertyName("version")
    String getVersion();
    
}
