/**
 * 
 */
package org.iplantc.core.uiapps.integration.client.view;

import java.util.List;

import org.iplantc.core.uiapps.integration.client.models.DeployedComponent;
import org.iplantc.core.uicommons.client.I18N;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * A grid that displays list of available deployed components (bin/tools) in Condor
 * 
 * @author sriram
 * 
 */
public class DeployedComponentsListingViewImpl implements DeployedComponentsListingView {

    private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

    @UiTemplate("DeployedComponentsListingView.ui.xml")
    interface MyUiBinder extends UiBinder<Widget, DeployedComponentsListingViewImpl> {
    }

    public interface DCDetailsRenderer extends XTemplates {
        @XTemplate(source = "DCDetails.html")
        public SafeHtml render();
    }

    
    @UiField(provided = true)
    ListStore<DeployedComponent> store;
    @UiField
    ColumnModel<DeployedComponent> cm;
    
    private final Widget widget;


    private Presenter presenter;

    @UiField
    TextField searchField;

    @UiField
    TextButton searchBtn;

    @UiField
    Grid<DeployedComponent> grid;

    @UiField
    VerticalLayoutContainer container;

    public DeployedComponentsListingViewImpl(ListStore<DeployedComponent> listStore,
            SelectionChangedHandler<DeployedComponent> handler) {
        this.store = listStore;
        widget = uiBinder.createAndBindUi(this);
        grid.getSelectionModel().addSelectionChangedHandler(handler);
        initSearchField();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private void initSearchField() {
        searchField.addKeyUpHandler(new KeyUpHandler() {
            
            @Override
            public void onKeyUp(KeyUpEvent event) {
                String currentValue = searchField.getCurrentValue();
                if(currentValue == null || currentValue.isEmpty()) {
                    presenter.loadDeployedComponents();
                }
            }
        });
        new KeyNav(searchField) {
            @Override
            public void onEnter(NativeEvent evt) {
                onSearchBtnClick(null);
            }
        };
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;

    }

    @UiFactory
    ColumnModel<DeployedComponent> createColumnModel() {
        return new DCColumnModel(this);
    }

    @Override
    public void loadDC(List<DeployedComponent> list) {
        store.clear();
        store.addAll(list);
    }

    @Override
    public void showInfo(DeployedComponent dc) {
        DCDetailsRenderer templates = GWT.create(DCDetailsRenderer.class);
        HtmlLayoutContainer c = new HtmlLayoutContainer(templates.render());
        c.add(new Label(I18N.DISPLAY.attribution() + ": "), new HtmlData(".cell1"));
        c.add(new Label(dc.getAttribution()), new HtmlData(".cell3"));
        c.add(new Label(I18N.DISPLAY.description() + ": "), new HtmlData(".cell5"));
        c.add(new Label(dc.getDescription()), new HtmlData(".cell7"));
        Dialog d = buildDetailsDialog(dc.getName());
        d.add(c);
        d.show();
    }

    private Dialog buildDetailsDialog(String heading) {
        Dialog d = new Dialog();
        d.getButtonBar().clear();
        d.setModal(true);
        d.setSize("300px", "200px");
        d.setHeadingText(heading);
        return d;
    }

    @Override
    public void mask() {
        container.mask(I18N.DISPLAY.loadingMask());

    }

    @Override
    public void unmask() {
        container.unmask();

    }

    @UiHandler({"searchBtn"})
    public void onSearchBtnClick(SelectEvent event) {
        String currentValue = searchField.getCurrentValue();
        if (currentValue == null || currentValue.isEmpty()) {
            presenter.loadDeployedComponents();
            return;
        }
        if (currentValue.length() >= 3) {
            presenter.searchDC(currentValue);
        } else {
            searchField.markInvalid(I18N.DISPLAY.searchEmptyText());
        }
    }

    @Override
    public DeployedComponent getSelectedDC() {
        return grid.getSelectionModel().getSelectedItem();
    }

}
