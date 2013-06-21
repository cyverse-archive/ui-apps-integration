package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent.AppTemplateUpdatedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
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

    @UiField
    AppTemplateWizard wizard;

    @UiField
    AppIntegrationToolbar toolbar;

    @UiField
    AppIntegrationPalette palette;

    @UiField
    HTML cmdLinePreview;

    public AppsIntegrationViewImpl() {
        initWidget(BINDER.createAndBindUi(this));

        /*
         * JDS - Add handling to collapse all argument groups on drag start. To understand why, comment
         * out the handler below, and drag a new argument group to the app wizard. The behaviour is
         * abrasive and jarring to the user.
         */
        palette.grpDragSource.addDragStartHandler(new DndDragStartHandler() {

            @Override
            public void onDragStart(DndDragStartEvent event) {
                wizard.collapseAllArgumentGroups();
            }
        });
    }

    @UiFactory
    AppTemplateWizard createAppTemplateWizard() {
        return new AppTemplateWizard(true);
    }

    @Override
    public void setPresenter(AppsIntegrationView.Presenter presenter) {
        toolbar.setPresenter(presenter);
    }

    @Override
    public void setEastWidget(IsWidget widget) {
        eastPanel.setActiveWidget(widget);
    }

    @Override
    public void edit(AppTemplate appTemplate) {
        wizard.edit(appTemplate);
    }

    @Override
    public AppTemplate flush() {
        return wizard.flushAppTemplate();
    }

    @Override
    public AppIntegrationToolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void onAppTemplateChanged() {
        wizard.onArgumentPropertyValueChange();
    }

    @Override
    public void setCmdLinePreview(String preview) {
        cmdLinePreview.setText(preview);
    }

    @Override
    public void addAppTemplateSelectedEventHandler(AppTemplateSelectedEventHandler handler) {
        wizard.addAppTemplateSelectedEventHandler(handler);
    }

    @Override
    public void addAppTemplateUpdatedEventHandler(AppTemplateUpdatedEventHandler handler) {
        wizard.addAppTemplateUpdatedEventHandler(handler);
    }

    @Override
    public void addArgumentSelectedEventHandler(ArgumentSelectedEventHandler handler) {
        wizard.addArgumentSelectedEventHandler(handler);
    }

    @Override
    public void addArgumentGroupSelectedEventHandler(ArgumentGroupSelectedEventHandler handler) {
        wizard.addArgumentGroupSelectedEventHandler(handler);
    }

    @Override
    public void updateAppTemplateId(String id) {
        wizard.updateAppTemplateId(id);
    }

}
