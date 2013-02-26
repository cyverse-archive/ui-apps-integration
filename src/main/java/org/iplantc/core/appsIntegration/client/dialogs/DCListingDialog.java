/**
 * 
 */
package org.iplantc.core.appsIntegration.client.dialogs;

import java.util.List;

import org.iplantc.core.appsIntegration.client.models.DeployedComponent;
import org.iplantc.core.appsIntegration.client.presenter.DeployedComponentPresenterImpl;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter;
import org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingViewImpl;

import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

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
        getButtonById(PredefinedButton.OK.toString()).setEnabled(false);

        ListStore<DeployedComponent> listStore = new ListStore<DeployedComponent>(new DCKeyProvider());
        DeployedComponentsListingView view = new DeployedComponentsListingViewImpl(listStore,
                new DCSelectionChangedHandler());
        Presenter p = new DeployedComponentPresenterImpl(view);
        p.go(this);

    }



    class DCKeyProvider implements ModelKeyProvider<DeployedComponent> {

        @Override
        public String getKey(DeployedComponent item) {
            return item.getId();
        }

    }

    class DCSelectionChangedHandler implements SelectionChangedHandler<DeployedComponent> {

        @Override
        public void onSelectionChanged(SelectionChangedEvent<DeployedComponent> event) {
            List<DeployedComponent> items = event.getSelection();
            if (items != null && items.size() > 0) {
                getButtonById(PredefinedButton.OK.toString()).setEnabled(true);
            } else {
                getButtonById(PredefinedButton.OK.toString()).setEnabled(false);
            }

        }

    }

}
