package org.iplantc.core.appsIntegration.client.view;

import org.iplantc.core.widgets.client.appWizard.models.AppTemplate;
import org.iplantc.core.widgets.client.appWizard.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.widgets.client.appWizard.view.AppWizardView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

public class AppsIntegrationViewImpl extends Composite {

    private static AppsIntegrationViewImplUiBinder BINDER = GWT.create(AppsIntegrationViewImplUiBinder.class);

    interface AppsIntegrationViewImplUiBinder extends UiBinder<Widget, AppsIntegrationViewImpl> {
    }

    @UiField
    ContentPanel centerPanel;
    /**
     * This panel is a card layout so that 'cards' can be created which are bound to
     * and individual property.
     */
    @UiField
    CardLayoutContainer eastPanel;

    /**
     * @param appWizardPresenter
     * @param appTemplate the app for the center panel. If this is a blank appTemplate, it must be
     *            created with {@link AppTemplateAutoBeanFactory}.
     */
    public AppsIntegrationViewImpl(AppWizardView.Presenter appWizardPresenter, AppTemplate appTemplate) {
        initWidget(BINDER.createAndBindUi(this));

        appWizardPresenter.go(centerPanel, appTemplate);
    }

}
