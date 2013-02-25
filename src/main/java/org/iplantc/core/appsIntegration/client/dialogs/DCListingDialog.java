/**
 * 
 */
package org.iplantc.core.appsIntegration.client.dialogs;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.appsIntegration.client.models.DCProperties;
import org.iplantc.core.appsIntegration.client.models.DeployedComponent;
import org.iplantc.core.appsIntegration.client.presenter.DeployedComponentPresenterImpl;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingViewImpl;

import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

/**
 * @author sriram
 *
 */
public class DCListingDialog extends Dialog {

    
    public DCListingDialog() {
        setHideOnButtonClick(true);
        setPixelSize(600, 500);
        setResizable(false);
        setModal(true);
        ListStore<DeployedComponent> listStore = new ListStore<DeployedComponent>(new DCKeyProvider());
        DeployedComponentsListingView view = new DeployedComponentsListingViewImpl(listStore,
                buildColumnModel());
        Presenter p = new DeployedComponentPresenterImpl(view);
        p.go(this);

    }

    @SuppressWarnings("unchecked")
    private ColumnModel<DeployedComponent> buildColumnModel() {
        DCProperties properties = GWT.create(DCProperties.class);
        List<ColumnConfig<DeployedComponent, ?>> configs = new LinkedList<ColumnConfig<DeployedComponent, ?>>();

        ColumnConfig<DeployedComponent, String> name = new ColumnConfig<DeployedComponent, String>(
                properties.name(), 100);
        name.setHeader("Name");
        configs.add(name);
        name.setMenuDisabled(true);

        ColumnConfig<DeployedComponent, String> version = new ColumnConfig<DeployedComponent, String>(
                properties.version(), 100);
        name.setHeader("Version");
        configs.add(version);
        version.setMenuDisabled(true);

        ColumnConfig<DeployedComponent, String> path = new ColumnConfig<DeployedComponent, String>(
                properties.location(), 100);
        name.setHeader("Path");
        configs.add(path);
        path.setMenuDisabled(true);

        configs.addAll(Arrays.asList(name, version, path));

        return new ColumnModel<DeployedComponent>(configs);

    }

    class DCKeyProvider implements ModelKeyProvider<DeployedComponent> {

        @Override
        public String getKey(DeployedComponent item) {
            return item.getId();
        }

    }

}
