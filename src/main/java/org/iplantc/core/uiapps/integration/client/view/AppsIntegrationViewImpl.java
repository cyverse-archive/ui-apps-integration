package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.integration.client.dialogs.DCListingDialog;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widget.client.TextButton;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

public class AppsIntegrationViewImpl extends Composite implements AppsIntegrationView {

    private static AppsIntegrationViewImplUiBinder BINDER = GWT.create(AppsIntegrationViewImplUiBinder.class);

    @UiTemplate("AppsIntegrationView.ui.xml")
    interface AppsIntegrationViewImplUiBinder extends UiBinder<Widget, AppsIntegrationViewImpl> {}

    @UiField
    BorderLayoutContainer borderLayoutContainer;

    /**
     * This panel is a card layout so that 'cards' can be created which are bound to
     * an individual Argument.
     */
    @UiField
    CardLayoutContainer eastPanel;

    @Path("")
    @UiField
    AppTemplateWizard wizard;

    @Ignore
    @UiField
    TextButton toolSelector;

    private final EventBus eventBus;

    public AppsIntegrationViewImpl(final EventBus eventBus) {
        this.eventBus = eventBus;
        initWidget(BINDER.createAndBindUi(this));
        toolSelector.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                DCListingDialog dialog = new DCListingDialog();
                dialog.show();
            }
        });
    }

    @Ignore
    @UiFactory
    AppTemplateWizard createAppTemplateWizard() {
        return new AppTemplateWizard(eventBus, true);
    }

    @Override
    public void setPresenter(AppsIntegrationView.Presenter presenter) {
        /* Do Nothing */
    }

    @Override
    public void setEastWidget(IsWidget widget) {
        eastPanel.setActiveWidget(widget);
    }

    @Override
    public void edit(AppTemplate appTemplate) {
        wizard.edit(appTemplate);
    }

}
