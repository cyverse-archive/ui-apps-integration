package org.iplantc.core.uiapps.integration.client.services;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppTemplateServices {

    void saveAndPublishAppTemplate(AppTemplate at, AsyncCallback<String> callback);
}
