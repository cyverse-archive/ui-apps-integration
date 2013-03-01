package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.integration.client.dialogs.DCListingDialog;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.view.editors.ArgumentGroupListEditor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widget.client.TextButton;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;

public class AppsIntegrationViewImpl extends Composite implements AppsIntegrationView {

    private static AppsIntegrationViewImplUiBinder BINDER = GWT.create(AppsIntegrationViewImplUiBinder.class);

    @UiTemplate("AppsIntegrationView.ui.xml")
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

    @UiField
    ArgumentGroupListEditor argumentGroupsEditor;

    @Ignore
    @UiField
    TextButton toolSelector;
    /**
     * @param appWizardPresenter
     * @param appTemplate the app for the center panel. If this is a blank appTemplate, it must be
     *            created with {@link AppTemplateAutoBeanFactory}.
     */
    public AppsIntegrationViewImpl() {
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

    @Override
    public void setPresenter(AppsIntegrationView.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public SimpleBeanEditorDriver<AppTemplate, ? extends Editor<AppTemplate>> getEditorDriver() {
        return driver;
    }
    
   

}
