package org.iplantc.core.uiapps.integration.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AppIntegrationPalette extends Composite {

    private static AppIntegrationPaletteUiBinder uiBinder = GWT.create(AppIntegrationPaletteUiBinder.class);

    interface AppIntegrationPaletteUiBinder extends UiBinder<Widget, AppIntegrationPalette> {
    }

    public AppIntegrationPalette() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
