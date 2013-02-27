package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.widgets.client.appWizard.models.AppTemplate;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author jstroot
 *
 */
public interface AppsIntegrationView extends IsWidget, Editor<AppTemplate> {
    
    public interface Presenter extends org.iplantc.core.uiapps.widgets.client.appWizard.view.AppWizardView.BasePresenter {
    }

    void setPresenter(Presenter presenter);

    SimpleBeanEditorDriver<AppTemplate, ? extends Editor<AppTemplate>> getEditorDriver();

}
