package org.iplantc.core.appsIntegration.client.view;

import org.iplantc.core.widgets.client.appWizard.models.AppTemplate;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author jstroot
 *
 */
public interface AppsIntegrationView extends IsWidget, Editor<AppTemplate> {
    
    /**
     * @author jstroot
     *
     */
    public interface Presenter extends org.iplantc.core.uicommons.client.presenter.Presenter{
        void go(HasOneWidget container, AppTemplate appTemplate);
    }

    void setPresenter(Presenter presenter);

    SimpleBeanEditorDriver<AppTemplate, ? extends Editor<AppTemplate>> getEditorDriver();

}
