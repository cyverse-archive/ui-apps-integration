package org.iplantc.core.appsIntegration.client.presenter;

import org.iplantc.core.appsIntegration.client.view.AppsIntegrationView;
import org.iplantc.core.widgets.client.appWizard.models.AppTemplate;
import org.iplantc.core.widgets.client.appWizard.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.widgets.client.appWizard.presenter.AppWizardPresenterJsonAdapter;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;

/**
 * @author jstroot
 *
 */
public class AppsIntegrationPresenterImpl implements AppsIntegrationView.Presenter {

    private final AppsIntegrationView view;
    private AppTemplate appTemplate;

    public AppsIntegrationPresenterImpl(AppsIntegrationView view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void goLegacy(HasOneWidget container, Splittable legacyJson) {
        setAppTemplateFromLegacyJson(legacyJson);
        go(container);
    }

    @Override
    public void go(HasOneWidget container, AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
        go(container);
    }

    @Override
    public void go(HasOneWidget container) {
        view.getEditorDriver().edit(appTemplate);
        container.setWidget(view);
    }

    @Override
    public void setAppTemplateFromLegacyJson(Splittable legacyJson) {
        Splittable appTemplateSplit = AppWizardPresenterJsonAdapter.adaptAppTemplateJsonString(legacyJson);

        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        AutoBean<AppTemplate> appTemplateAb = AutoBeanCodex.decode(factory, AppTemplate.class, appTemplateSplit);

        this.appTemplate = appTemplateAb.as();
    }

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

}
