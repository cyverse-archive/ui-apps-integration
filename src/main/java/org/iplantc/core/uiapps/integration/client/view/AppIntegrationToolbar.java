package org.iplantc.core.uiapps.integration.client.view;

import com.google.gwt.user.client.ui.IsWidget;

public interface AppIntegrationToolbar extends IsWidget {

    public interface Presenter {

        /**
         * Submits the changed app to the server.
         */
        void onSaveClicked();

        void onPreviewUiClicked();

        void onPreviewJsonClicked();

        void onArgumentOrderClicked();

        void onSelectToolClicked();

    }

    void setPresenter(AppIntegrationToolbar.Presenter presenter);

}
