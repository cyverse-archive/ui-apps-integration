package org.iplantc.core.uiapps.integration.client.dialogs;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentProperties;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

/**
 * TODO JDS Update grid re-ordering on drag drop
 * 
 * @author jstroot
 * 
 */
public class CommandLineOrderingPanel extends Composite {

    private static CommandLineOrderingPanelUiBinder BINDER = GWT.create(CommandLineOrderingPanelUiBinder.class);
    interface CommandLineOrderingPanelUiBinder extends UiBinder<Widget, CommandLineOrderingPanel> {}

    @UiField(provided = true)
    ColumnModel<Argument> cm1, cm2;

    @UiField
    Grid<Argument> unorderedGrid;

    @UiField
    Grid<Argument> orderedGrid;

    public CommandLineOrderingPanel(List<Argument> arguments) {
        initColumnModels();
        initWidget(BINDER.createAndBindUi(this));

        unorderedGrid.getView().setEmptyText(I18N.DISPLAY.noParameters());
        orderedGrid.getView().setEmptyText(I18N.DISPLAY.noParameters());
        new GridDragSource<Argument>(unorderedGrid);
        new GridDragSource<Argument>(orderedGrid);
        GridDropTarget<Argument> unOrdDropTarget = new GridDropTarget<Argument>(unorderedGrid);
        GridDropTarget<Argument> ordDropTarget = new GridDropTarget<Argument>(orderedGrid);

        DropHandler dropHandler = new DropHandler();
        unOrdDropTarget.addDropHandler(dropHandler);
        ordDropTarget.addDropHandler(dropHandler);
    }

    private void initColumnModels() {
        ArgumentProperties props = GWT.create(ArgumentProperties.class);
        ColumnConfig<Argument, String> name = new ColumnConfig<Argument, String>(props.name(), 170);

        // cm1
        List<ColumnConfig<Argument, ?>> cm1List = Lists.newArrayList();
        cm1List.add(name);
        cm1 = new ColumnModel<Argument>(cm1List);

        // cm2
        ColumnConfig<Argument, String> ordName = new ColumnConfig<Argument, String>(props.name(), 140);
        ColumnConfig<Argument, Integer> order = new ColumnConfig<Argument, Integer>(props.order(), 30);
        List<ColumnConfig<Argument, ?>> cm2List = Lists.newArrayList();
        cm2List.add(order);
        cm2List.add(ordName);
        cm2 = new ColumnModel<Argument>(cm2List);
    }

    /**
     * Updates the ordering of the given list
     */
    private void updateArgumentOrdering() {
        for (Argument arg : orderedGrid.getStore().getAll()) {
            arg.setOrder(orderedGrid.getStore().indexOf(arg) + 1);
        }
        // TODO JDS Need to fire event so Cmd line prev can be updated, etc.

    }

    private final class DropHandler implements DndDropHandler {
        @Override
        public void onDrop(DndDropEvent event) {
            updateArgumentOrdering();

            if (event.getTarget() == unorderedGrid) {
                if (event.getData() instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Argument> args = (List<Argument>)event.getData();
                    for (Argument arg : args) {
                        arg.setOrder(-1);
                    }
                }
            }
        }
    }

}
