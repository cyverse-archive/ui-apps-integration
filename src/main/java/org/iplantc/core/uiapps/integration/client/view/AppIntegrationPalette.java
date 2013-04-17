package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.widgets.client.dnd.AppArgumentTypeDragSource;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;

/**
 * This is a ui component which contains draggable images of the different supported argument types in
 * the App Integration view.
 * 
 * @author jstroot
 * 
 */
public class AppIntegrationPalette extends Composite {

    private static AppIntegrationPaletteUiBinder uiBinder = GWT.create(AppIntegrationPaletteUiBinder.class);

    interface AppIntegrationPaletteUiBinder extends UiBinder<Widget, AppIntegrationPalette> {}

    @UiField
    Image flag, environmentVariable, multiFileSelector, fileInput, group, integer, treeSelection, multiSelect, singleSelect, multiLineText, text;

    public AppIntegrationPalette() {
        initWidget(uiBinder.createAndBindUi(this));

        // Add dragSource objects to each button
        new AppArgumentTypeDragSource(environmentVariable, ArgumentType.EnvironmentVariable);
        new AppArgumentTypeDragSource(fileInput, ArgumentType.FileInput);
        new AppArgumentTypeDragSource(flag, ArgumentType.Flag);
        new AppArgumentTypeDragSource(group, ArgumentType.Group);
        new AppArgumentTypeDragSource(integer, ArgumentType.Integer);
        new AppArgumentTypeDragSource(multiFileSelector, ArgumentType.MultiFileSelector);
        new AppArgumentTypeDragSource(multiLineText, ArgumentType.MultiLineText);
        new AppArgumentTypeDragSource(multiSelect, ArgumentType.TextSelection);
        new AppArgumentTypeDragSource(text, ArgumentType.Text);
        new AppArgumentTypeDragSource(singleSelect, ArgumentType.TextSelection);
        new AppArgumentTypeDragSource(treeSelection, ArgumentType.TreeSelection);
    }

}
