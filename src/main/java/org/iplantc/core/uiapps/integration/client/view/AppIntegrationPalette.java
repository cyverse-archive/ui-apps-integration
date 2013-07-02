package org.iplantc.core.uiapps.integration.client.view;

import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataObject;
import org.iplantc.core.uiapps.widgets.client.models.metadata.DataSourceEnum;
import org.iplantc.core.uiapps.widgets.client.models.metadata.FileInfoTypeEnum;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItem;
import org.iplantc.core.uiapps.widgets.client.models.selection.SelectionItemGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

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
    Image flag, environmentVariable, multiFileSelector, fileInput, group, integerInput, treeSelection, singleSelect, multiLineText, text;

    @UiField
    Label info, folderInput, integerSelection, doubleSelection, doubleInput, fileOutput, folderOutput, multiFileOutput, referenceGenome, referenceSequence, referenceAnnotation;

    // Expose group drag source for special case handling in AppsIntegrationViewImpl
    DragSource grpDragSource;

    public AppIntegrationPalette() {
        initWidget(uiBinder.createAndBindUi(this));
        new ToolTip(environmentVariable, new ToolTipConfig("An environment variable which is set before running a job."));
        new ToolTip(doubleInput, new ToolTipConfig("A textbox that checks for valid decimal input."));
        new ToolTip(integerInput, new ToolTipConfig("A textbox that checks for valid integer input."));
        new ToolTip(doubleSelection, new ToolTipConfig("A list for selecting a decimal value."));
        new ToolTip(integerSelection, new ToolTipConfig("A list for selecting an integer value."));
        new ToolTip(singleSelect, new ToolTipConfig("A list for selecting a choice."));
        new ToolTip(treeSelection, new ToolTipConfig("A hierarchical list for selecting a choice."));

        // Add dragSource objects to each button
        DragSource ds1 = new DragSource(environmentVariable);
        ds1.setData(createNewArgument(ArgumentType.EnvironmentVariable));

        DragSource ds2 = new DragSource(fileInput);
        ds2.setData(createNewArgument(ArgumentType.FileInput));

        DragSource ds3 = new DragSource(flag);
        ds3.setData(createNewArgument(ArgumentType.Flag));

        grpDragSource = new DragSource(group);
        grpDragSource.setData(createNewArgumentGroup());

        DragSource ds5 = new DragSource(integerInput);
        ds5.setData(createNewArgument(ArgumentType.Integer));

        DragSource ds6 = new DragSource(multiFileSelector);
        ds6.setData(createNewArgument(ArgumentType.MultiFileSelector));

        DragSource ds7 = new DragSource(multiLineText);
        ds7.setData(createNewArgument(ArgumentType.MultiLineText));

        DragSource ds9 = new DragSource(text);
        ds9.setData(createNewArgument(ArgumentType.Text));

        DragSource ds10 = new DragSource(singleSelect);
        ds10.setData(createNewArgument(ArgumentType.TextSelection));

        DragSource ds11 = new DragSource(treeSelection);
        ds11.setData(createNewArgument(ArgumentType.TreeSelection));

        DragSource ds12 = new DragSource(info);
        ds12.setData(createNewArgument(ArgumentType.Info));

        DragSource ds13 = new DragSource(folderInput);
        ds13.setData(createNewArgument(ArgumentType.FolderInput));

        DragSource ds14 = new DragSource(integerSelection);
        ds14.setData(createNewArgument(ArgumentType.IntegerSelection));

        DragSource ds15 = new DragSource(doubleSelection);
        ds15.setData(createNewArgument(ArgumentType.DoubleSelection));

        DragSource ds16 = new DragSource(doubleInput);
        ds16.setData(createNewArgument(ArgumentType.Double));

        DragSource ds17 = new DragSource(fileOutput);
        ds17.setData(createNewArgument(ArgumentType.FileOutput));

        DragSource ds18 = new DragSource(folderOutput);
        ds18.setData(createNewArgument(ArgumentType.FolderOutput));

        DragSource ds19 = new DragSource(multiFileOutput);
        ds19.setData(createNewArgument(ArgumentType.MultiFileOutput));

        DragSource ds20 = new DragSource(referenceGenome);
        ds20.setData(createNewArgument(ArgumentType.ReferenceGenome));

        DragSource ds21 = new DragSource(referenceAnnotation);
        ds21.setData(createNewArgument(ArgumentType.ReferenceAnnotation));

        DragSource ds22 = new DragSource(referenceSequence);
        ds22.setData(createNewArgument(ArgumentType.ReferenceSequence));
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
        argument.setDescription("");
        argument.setType(type);
        argument.setName("");
        argument.setVisible(true);

        if (AppTemplateUtils.isSimpleSelectionArgumentType(type)) {
            argument.setSelectionItems(Lists.<SelectionItem> newArrayList());
        } else if (type.equals(ArgumentType.TreeSelection)) {
            SelectionItemGroup sig = factory.selectionItemGroup().as();
            sig.setSingleSelect(false);
            sig.setSelectionCascade(CheckCascade.CHILDREN);
            sig.setArguments(Lists.<SelectionItem> newArrayList());
            sig.setGroups(Lists.<SelectionItemGroup> newArrayList());
            argument.setSelectionItems(Lists.<SelectionItem> newArrayList(sig));

        } else if (AppTemplateUtils.isDiskResourceArgumentType(type)) {
            DataObject dataObj = factory.dataObject().as();
            dataObj.setFormat("Unspecified");
            dataObj.setDataSource(DataSourceEnum.file);
            dataObj.setCmdSwitch("");
            dataObj.setFileInfoType(FileInfoTypeEnum.File);
            argument.setDataObject(dataObj);

        }
        // Special handling to initialize new arguments, for specific ArgumentTypes.
        switch (type) {
            case Selection:
            case TextSelection:
                argument.setLabel("Text Selection");
                break;
            case IntegerSelection:
                argument.setLabel("Integer Selection");
                break;
            case ValueSelection:
            case DoubleSelection:
                argument.setLabel("Double Selection");
                break;

            case TreeSelection:
                argument.setLabel("Tree Selection");
                break;

            case FileInput:
                argument.setLabel("File Selector");
                break;

            case FolderInput:
                argument.setLabel("Folder Selector");
                break;

            case MultiFileSelector:
                argument.setLabel("Multi-file Selector");
                break;

            case Flag:
                argument.setLabel("CheckBox");
                break;

            case Text:
                argument.setLabel("Text Input");
                break;

            case MultiLineText:
                argument.setLabel("Multi-line Text Input");
                break;

            case EnvironmentVariable:
                argument.setLabel("Environment Variable");
                break;

            case Integer:
                argument.setLabel("Integer Input");
                break;

            case Double:
                argument.setLabel("Double Input");
                break;

            case FileOutput:
                argument.setLabel("File Output");
                break;

            case FolderOutput:
                argument.setLabel("Folder Output");
                break;

            case MultiFileOutput:
                argument.setLabel("Multi-file Output");
                break;

            case ReferenceAnnotation:
                argument.setLabel("Reference Annotation");
                break;

            case ReferenceGenome:
                argument.setLabel("Reference Genome");
                break;

            case ReferenceSequence:
                argument.setLabel("Reference Sequence");
                break;

            default:
                argument.setLabel("Default Label");
                break;
        }
        return argument;
    }

}
