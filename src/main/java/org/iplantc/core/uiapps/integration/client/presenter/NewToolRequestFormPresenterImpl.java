/**
 * 
 */
package org.iplantc.core.uiapps.integration.client.presenter;



import org.iplantc.core.uiapps.integration.client.view.NewToolRequestFormView;
import org.iplantc.core.uiapps.integration.client.view.NewToolRequestFormView.Presenter;

import com.google.gwt.user.client.ui.HasOneWidget;

/**
 * @author sriram
 *
 */
public class NewToolRequestFormPresenterImpl implements Presenter {

    private NewToolRequestFormView view;

    public NewToolRequestFormPresenterImpl(NewToolRequestFormView view) {
        this.view = view;
        view.setPresenter(this);
    }

    /* (non-Javadoc)
     * @see org.iplantc.core.uicommons.client.presenter.Presenter#go(com.google.gwt.user.client.ui.HasOneWidget)
     */
    @Override
    public void go(HasOneWidget container) {
        container.setWidget(view);
    }

}
