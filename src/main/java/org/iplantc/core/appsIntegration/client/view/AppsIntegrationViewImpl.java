package org.iplantc.core.appsIntegration.client.view;

import org.iplantc.core.widgets.client.appWizard.models.AppTemplate;
import org.iplantc.core.widgets.client.appWizard.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.widgets.client.appWizard.view.AppWizardPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

public class AppsIntegrationViewImpl extends Composite implements AppsIntegrationView {

    private static AppsIntegrationViewImplUiBinder BINDER = GWT.create(AppsIntegrationViewImplUiBinder.class);
    interface AppsIntegrationViewImplUiBinder extends UiBinder<Widget, AppsIntegrationViewImpl> {}

    interface Driver extends SimpleBeanEditorDriver<AppTemplate, AppsIntegrationViewImpl> {}

    private final Driver driver = GWT.create(Driver.class);
    
    /**
     * This panel is a card layout so that 'cards' can be created which are bound to
     * and individual property.
     */
    @UiField
    CardLayoutContainer eastPanel;

    private AppsIntegrationView.Presenter presenter;

    @Path("")
    @UiField
    AppWizardPanel centerPanel;

    /**
     * @param appWizardPresenter
     * @param appTemplate the app for the center panel. If this is a blank appTemplate, it must be
     *            created with {@link AppTemplateAutoBeanFactory}.
     */
    public AppsIntegrationViewImpl() {
        initWidget(BINDER.createAndBindUi(this));
    }

    @Override
    public void setPresenter(AppsIntegrationView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public SimpleBeanEditorDriver<AppTemplate, ? extends Editor<AppTemplate>> getEditorDriver() {
        return driver;
    }

}
