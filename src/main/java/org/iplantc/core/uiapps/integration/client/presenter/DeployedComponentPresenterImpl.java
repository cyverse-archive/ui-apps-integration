/**
 * 
 */
package org.iplantc.core.uiapps.integration.client.presenter;

import java.util.List;

import org.iplantc.core.uiapps.integration.client.models.DeployedComponent;
import org.iplantc.core.uiapps.integration.client.models.DeployedComponentAutoBeanFactory;
import org.iplantc.core.uiapps.integration.client.models.DeployedComponentList;
import org.iplantc.core.uiapps.integration.client.services.EnumerationServices;
import org.iplantc.core.uiapps.integration.client.view.DeployedComponentsListingView;
import org.iplantc.core.uicommons.client.ErrorHandler;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

/**
 * @author sriram
 *
 */
public class DeployedComponentPresenterImpl implements DeployedComponentsListingView.Presenter {

    DeployedComponentsListingView view;
    DeployedComponentAutoBeanFactory factory = GWT.create(DeployedComponentAutoBeanFactory.class);

    public DeployedComponentPresenterImpl(DeployedComponentsListingView view) {
        this.view = view;
        getDeployedComponents();
    }

    /* (non-Javadoc)
     * @see org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter#onDCSelection(org.iplantc.core.appsIntegration.client.models.DeployedComponent)
     */
    @Override
    public void onDCSelection(DeployedComponent dc) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter#getSelectedDC()
     */
    @Override
    public DeployedComponent getSelectedDC() {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.iplantc.core.appsIntegration.client.view.DeployedComponentsListingView.Presenter#searchDC(java.lang.String)
     */
    @Override
    public void searchDC(String filter) {
        if (filter != null && !filter.isEmpty()) {
            if (filter.length() >= 3) {
                view.mask();
                EnumerationServices services = new EnumerationServices();
                services.searchDeployedComponents(filter, new AsyncCallback<String>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        ErrorHandler.post(caught);
                        view.unmask();
                    }

                    @Override
                    public void onSuccess(String result) {
                        view.loadDC(parseResult(result));
                        view.unmask();
                    }
                });
            }
        } else {
            getDeployedComponents();
        }

    }

    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view.asWidget());
    }

    
    private void getDeployedComponents() {
        EnumerationServices services = new EnumerationServices();
        view.mask();
        services.getDeployedComponents(new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
             //   setCurrentCompSelection(currentSelection);

                view.loadDC(parseResult(result));
                view.unmask();

            }

            @Override
            public void onFailure(Throwable caught) {
                view.unmask();
                ErrorHandler.post(caught);
            }
        });
    }

    private List<DeployedComponent> parseResult(String result) {
        AutoBean<DeployedComponentList> autoBean = AutoBeanCodex.decode(factory,
                DeployedComponentList.class, result);
        List<DeployedComponent> items = autoBean.as().getDCList();
        return items;

    }
    
}
