/**
 * 
 */
package org.iplantc.core.uiapps.integration.client.dialogs;

import org.iplantc.core.uiapps.integration.client.models.DeployedComponent;
import org.iplantc.core.uiapps.integration.client.presenter.DeployedComponentPresenterImpl;
import org.iplantc.core.uiapps.integration.client.view.DeployedComponentsListingView;
import org.iplantc.core.uiapps.integration.client.view.DeployedComponentsListingView.Presenter;
import org.iplantc.core.uiapps.integration.client.view.DeployedComponentsListingViewImpl;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;

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
