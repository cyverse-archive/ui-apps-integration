package org.iplantc.core.uiapps.integration.client.services;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uicommons.client.models.HasId;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppTemplateServices {

    /**
     * Retrieves an <code>AppTemplate</code> from the database for editing.
     * 
     * @param appId the <code>App</code> id.
     * @param callback
     */
    void getAppTemplate(HasId appId, AsyncCallback<AppTemplate> callback);

    /**
     * Performs an initial publishing of new AppTemplates to the database, or updates of existing ones
     * after they've been updated.
     * 
     * @param at the <code>AppTemplate</code> to be saved/published.
     * @param callback
     */
    void saveAndPublishAppTemplate(AppTemplate at, AsyncCallback<String> callback);

    /**
     * Retrieves a UI preview of the given <code>AppTemplate</code>.
     * 
     * @param at the <code>AppTemplate</code> for which the preview should be produced.
     * @param callback
     */
    void getAppTemplatePreview(AppTemplate at, AsyncCallback<AppTemplate> callback);

    /**
     * Retrieves an <code>AppTemplate</code> with all of the values from the given analysisId.
     * 
     * @param analysisId the ID of the analysis for which the <code>AppTemplate</code> should be fetched.
     * @param callback
     */
    void rerunAnalysis(HasId analysisId, AsyncCallback<AppTemplate> callback);
}
