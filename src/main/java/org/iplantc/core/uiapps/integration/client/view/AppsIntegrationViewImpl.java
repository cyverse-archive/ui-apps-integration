package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.integration.client.dialogs.DCListingDialog;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentGroupListEditor;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
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

    interface Driver extends SimpleBeanEditorDriver<AppTemplate, AppsIntegrationViewImpl> {}

    private final Driver driver = GWT.create(Driver.class);
    
    @UiField
    BorderLayoutContainer borderLayoutContainer;

    /**
     * This panel is a card layout so that 'cards' can be created which are bound to
     * an individual Argument.
     */
    @UiField
    CardLayoutContainer eastPanel;

    @UiField
    ArgumentGroupListEditor argumentGroupsEditor;

    @Ignore
    @UiField
    TextButton toolSelector;

    private AppsIntegrationView.Presenter presenter;
    private final EventBus eventBus;

    public AppsIntegrationViewImpl(final EventBus eventBus) {
        this.eventBus = eventBus;
        initWidget(BINDER.createAndBindUi(this));
        driver.initialize(this);
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
    ArgumentGroupListEditor createArgumentGroupListEditor() {
        return new ArgumentGroupListEditor(eventBus);
    }

    @Override
    public void setPresenter(AppsIntegrationView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public SimpleBeanEditorDriver<AppTemplate, ? extends Editor<AppTemplate>> getEditorDriver() {
        return driver;
    }

    @Override
    public void setEastWidget(IsWidget widget) {
        eastPanel.setActiveWidget(widget);
    }

}
