package org.iplantc.core.uiapps.integration.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class NewToolRequestFormView extends Composite {

    private static NewToolRequestFormViewUiBinder uiBinder = GWT
            .create(NewToolRequestFormViewUiBinder.class);

    interface NewToolRequestFormViewUiBinder extends UiBinder<Widget, NewToolRequestFormView> {
    }

    public NewToolRequestFormView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
