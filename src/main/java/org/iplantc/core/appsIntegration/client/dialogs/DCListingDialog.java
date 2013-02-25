/**
 * 
 */
package org.iplantc.core.appsIntegration.client.dialogs;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.appsIntegration.client.models.DCProperties;
import org.iplantc.core.appsIntegration.client.models.DeployedComponent;
import org.iplantc.core.appsIntegration.client.presenter.DeployedComponentPresenterImpl;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter;
import org.iplantc.core.appsIntegration.client.view.cells.DCNameHyperlinkCell;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingViewImpl;

import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.core.client.IdentityValueProvider;
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
        setHeadingText("Installed Tools");
        ListStore<DeployedComponent> listStore = new ListStore<DeployedComponent>(new DCKeyProvider());
        DeployedComponentsListingView view = new DeployedComponentsListingViewImpl(listStore);
        Presenter p = new DeployedComponentPresenterImpl(view);
        p.go(this);

    }



    class DCKeyProvider implements ModelKeyProvider<DeployedComponent> {

        @Override
        public String getKey(DeployedComponent item) {
            return item.getId();
        }

    }

}
