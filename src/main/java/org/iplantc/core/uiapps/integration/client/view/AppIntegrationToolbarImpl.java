package org.iplantc.core.uiapps.integration.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

public class AppIntegrationToolbarImpl implements AppIntegrationToolbar {

    @UiTemplate("AppIntegrationToolbar.ui.xml")
    interface AppIntegrationToolBarUiBinder extends UiBinder<Widget, AppIntegrationToolbarImpl> {}

    private static AppIntegrationToolBarUiBinder BINDER = GWT.create(AppIntegrationToolBarUiBinder.class);

    @UiField
    TextButton saveButton;

    @UiField
    TextButton selectToolButton;

    @UiField
    MenuItem previewUiMenuItem, previewJsonMenuItem;

    @UiField
    TextButton argumentOrderButton;

    @UiField
    TextButton deleteButton;

    private AppIntegrationToolbar.Presenter presenter;

    private final Widget widget;

    public AppIntegrationToolbarImpl() {
        widget = BINDER.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(AppIntegrationToolbar.Presenter presenter) {
        this.presenter = presenter;
    }

    @UiHandler("saveButton")
    void onSaveButtonClicked(SelectEvent event) {
        presenter.onSaveClicked();
    }

    @UiHandler("selectToolButton")
    void onSelectToolClicked(SelectEvent event) {
        presenter.onSelectToolClicked();
    }

    @UiHandler("previewUiMenuItem")
    void onPreviewUiClicked(SelectionEvent<Item> event) {
        presenter.onPreviewUiClicked();
    }

    @UiHandler("previewJsonMenuItem")
    void onPreviewJsonClicked(SelectionEvent<Item> event) {
        presenter.onPreviewJsonClicked();
    }

    @UiHandler("argumentOrderButton")
    void onArgumentOrderButtonClicked(SelectEvent event) {
        presenter.onArgumentOrderClicked();
    }

    @UiHandler("deleteButton")
    void onDeleteButtonClicked(SelectEvent event) {
        presenter.onDeleteButtonClicked();
    }

}
