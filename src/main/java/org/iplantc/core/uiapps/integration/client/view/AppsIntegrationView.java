package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;

import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author jstroot
 *
 */
public interface AppsIntegrationView extends IsWidget {
    
    public interface Presenter extends org.iplantc.core.uiapps.widgets.client.view.AppWizardView.BasePresenter, AppIntegrationToolbar.Presenter {
    }

    void setPresenter(Presenter presenter);

    void setEastWidget(IsWidget widget);

    void edit(AppTemplate appTemplate);

    AppTemplate flush();

}
