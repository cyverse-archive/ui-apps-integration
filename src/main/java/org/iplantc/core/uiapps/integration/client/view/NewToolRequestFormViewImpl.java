package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.integration.client.view.AppsIntegrationView.Presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class NewToolRequestFormViewImpl extends Composite implements NewToolRequestFormView {

    private static NewToolRequestFormViewUiBinder uiBinder = GWT
            .create(NewToolRequestFormViewUiBinder.class);

    final private Widget widget;

    private Presenter presenter;

    @UiField(provided = true)
    SimpleComboBox<String> multiThreadCbo;

    @UiField
    VerticalLayoutContainer container;

    @UiTemplate("NewToolRequestFormView.ui.xml")
    interface NewToolRequestFormViewUiBinder extends UiBinder<Widget, NewToolRequestFormViewImpl> {
    }

    public NewToolRequestFormViewImpl() {
        multiThreadCbo = new SimpleComboBox<String>(new StringLabelProvider<String>());
        multiThreadCbo.add("Yes");
        multiThreadCbo.add("No");
        widget = uiBinder.createAndBindUi(this);
        container.setScrollMode(ScrollMode.AUTOY);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void setPresenter(Presenter p) {
        this.presenter = p;
    }

}
