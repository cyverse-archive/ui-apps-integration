package org.iplantc.core.appsIntegration.client.presenter;

import org.iplantc.core.appsIntegration.client.view.AppsIntegrationView;
import org.iplantc.core.widgets.client.appWizard.models.AppTemplate;

import com.google.gwt.user.client.ui.HasOneWidget;

/**
 * @author jstroot
 *
 */
public class AppsIntegrationPresenterImpl implements AppsIntegrationView.Presenter {

    private final AppsIntegrationView view;

    public AppsIntegrationPresenterImpl(AppsIntegrationView view) {
        this.view = view;
    }

    @Override
    public void go(HasOneWidget container) {
        view.setPresenter(this);
        container.setWidget(view);
    }

    @Override
    public void go(HasOneWidget container, AppTemplate appTemplate) {
        view.getEditorDriver().edit(appTemplate);
        go(container);
    }

}
