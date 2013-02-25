/**
 * 
 */
package org.iplantc.core.appsIntegration.client.view;

import java.util.List;

import org.iplantc.core.appsIntegration.client.models.DeployedComponent;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.widget.client.TextButton;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * A grid that displays list of available deployed components (bin/tools) in Condor
 * 
 * @author sriram
 * 
 */
public class DeployedComponentsListingViewImpl implements DeployedComponentsListingView {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("DeployedComponentsListingView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DeployedComponentsListingViewImpl> {
    }

    
    @UiField(provided = true)
    ListStore<DeployedComponent> listStore;
    @UiField(provided = true)
    ColumnModel<DeployedComponent> cm;
    
    private final Widget widget;


    private Presenter presenter;

    @UiField
    TextField<String> searchField;

    @UiField
    TextButton searchBtn;

    @UiField
    Grid<DeployedComponent> grid;

    public DeployedComponentsListingViewImpl(ListStore<DeployedComponent> listStore,
            ColumnModel<DeployedComponent> cm) {
        this.listStore = listStore;
        this.cm = cm;
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;

    }

    @Override
    public void loadDC(List<DeployedComponent> list) {
        listStore.clear();
        listStore.addAll(list);
    }

}
