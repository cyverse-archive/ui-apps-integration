package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Composite;

/**
 * This is a ui component which contains draggable images of the different supported argument types in
 * the App Integration view.
 * 
 * @author jstroot
 * 
 */
class AppIntegrationPalette extends Composite {

    private static AppIntegrationPaletteUiBinder uiBinder = GWT.create(AppIntegrationPaletteUiBinder.class);
    interface AppIntegrationPaletteUiBinder extends UiBinder<Widget, AppIntegrationPalette> {}

    private final AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);

    @UiField
    Image flag, environmentVariable, multiFileSelector, fileInput, group, integer, treeSelection, multiSelect, singleSelect, multiLineText, text;

    // Expose group drag source for special case handling in AppsIntegrationViewImpl
    DragSource grpDragSource;

    public AppIntegrationPalette() {
        initWidget(uiBinder.createAndBindUi(this));

        // Add dragSource objects to each button
        DragSource ds1 = new DragSource(environmentVariable);
        ds1.setData(createNewArgument(ArgumentType.EnvironmentVariable));

        DragSource ds2 = new DragSource(fileInput);
        ds2.setData(createNewArgument(ArgumentType.FileInput));

        DragSource ds3 = new DragSource(flag);
        ds3.setData(createNewArgument(ArgumentType.Flag));

        grpDragSource = new DragSource(group);
        grpDragSource.setData(createNewArgumentGroup());

        DragSource ds5 = new DragSource(integer);
        ds5.setData(createNewArgument(ArgumentType.Integer));

        DragSource ds6 = new DragSource(multiFileSelector);
        ds6.setData(createNewArgument(ArgumentType.MultiFileSelector));

        DragSource ds7 = new DragSource(multiLineText);
        ds7.setData(createNewArgument(ArgumentType.MultiLineText));

        DragSource ds8 = new DragSource(multiSelect);
        ds8.setData(createNewArgument(ArgumentType.TextSelection));

        DragSource ds9 = new DragSource(text);
        ds9.setData(createNewArgument(ArgumentType.Text));

        DragSource ds10 = new DragSource(singleSelect);
        ds10.setData(createNewArgument(ArgumentType.TextSelection));

        DragSource ds11 = new DragSource(treeSelection);
        ds11.setData(createNewArgument(ArgumentType.TreeSelection));

    }

    private ArgumentGroup createNewArgumentGroup() {
        ArgumentGroup ag = factory.argumentGroup().as();
        ag.setArguments(Lists.<Argument> newArrayList());
        ag.setLabel("DEFAULT");
        return ag;
    }

    private Argument createNewArgument(ArgumentType type) {
        Argument argument = factory.argument().as();
        argument.setLabel("DEFAULT");
        argument.setDescription("DEFAULT");
        argument.setType(type);
        return argument;
    }

}
