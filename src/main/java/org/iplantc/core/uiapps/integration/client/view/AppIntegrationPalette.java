package org.iplantc.core.uiapps.integration.client.view;

import java.util.Map;

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
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uicommons.client.widgets.ContextualHelpPopup;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
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
    Image info, folderInput, integerSelection, doubleSelection, doubleInput, fileOutput, folderOutput, multiFileOutput, referenceGenome, referenceSequence, referenceAnnotation;

    @UiField
    ToolButton fileFolderCategoryHelpBtn, listsCategoryHelpBtn, textNumericalInputCategoryHelpBtn, outputCategoryHelpBtn, referenceGenomeCategoryHelpBtn;

    // Expose group drag source for special case handling in AppsIntegrationViewImpl
    DragSource grpDragSource;

    private final Map<ArgumentType, DragSource> dragSourceMap = Maps.newHashMap();

    private boolean onlyLabelEditMode;

    private final AppTemplateWizardAppearance appearance = GWT.create(AppTemplateWizardAppearance.class);


    public AppIntegrationPalette() {
        initWidget(uiBinder.createAndBindUi(this));

        grpDragSource = new DragSource(group);
        grpDragSource.addDragStartHandler(new DndDragStartHandler() {

            @Override
            public void onDragStart(DndDragStartEvent event) {
                if (onlyLabelEditMode) {
                    event.getStatusProxy().setStatus(false);
                    event.getStatusProxy().update("Groups cannot be added to a published app.");
                    return;
                }

                event.getStatusProxy().setStatus(true);
                event.getStatusProxy().update(group.getElement().getString());

            }
        });
        grpDragSource.setData(createNewArgumentGroup());
        dragSourceMap.put(ArgumentType.Group, grpDragSource);

        // Add dragSource objects to each button
        createDragSource(environmentVariable, ArgumentType.EnvironmentVariable);
        createDragSource(fileInput, ArgumentType.FileInput);
        createDragSource(flag, ArgumentType.Flag);
        createDragSource(integerInput, ArgumentType.Integer);
        createDragSource(multiFileSelector, ArgumentType.MultiFileSelector);
        createDragSource(multiLineText, ArgumentType.MultiLineText);
        createDragSource(text, ArgumentType.Text);
        createDragSource(singleSelect, ArgumentType.TextSelection);
        createDragSource(treeSelection, ArgumentType.TreeSelection);
        createDragSource(info, ArgumentType.Info);
        createDragSource(folderInput, ArgumentType.FolderInput);
        createDragSource(integerSelection, ArgumentType.IntegerSelection);
        createDragSource(doubleSelection, ArgumentType.DoubleSelection);
        createDragSource(doubleInput, ArgumentType.Double);
        createDragSource(fileOutput, ArgumentType.FileOutput);
        createDragSource(folderOutput, ArgumentType.FolderOutput);
        createDragSource(multiFileOutput, ArgumentType.MultiFileOutput);
        createDragSource(referenceGenome, ArgumentType.ReferenceGenome);
        createDragSource(referenceAnnotation, ArgumentType.ReferenceAnnotation);
        createDragSource(referenceSequence, ArgumentType.ReferenceSequence);

    }

    @UiFactory
    ToolButton createToolButton() {
        return new ToolButton(ToolButton.QUESTION);
    }

    @UiHandler({"fileFolderCategoryHelpBtn", "listsCategoryHelpBtn", "textNumericalInputCategoryHelpBtn", "outputCategoryHelpBtn", "referenceGenomeCategoryHelpBtn"})
    void onSelect(SelectEvent event) {
        if (!(event.getSource() instanceof ToolButton)) {
            return;
        }
        ToolButton btn = (ToolButton)event.getSource();
        ContextualHelpPopup popup = new ContextualHelpPopup();
        popup.setWidth(450);
        popup.add(new HTML(getCategoryContextHelp(btn)));
        popup.showAt(btn.getAbsoluteLeft(), btn.getAbsoluteTop() + 15);
    }

    private SafeHtml getCategoryContextHelp(ToolButton btn) {
        SafeHtml ret = null;
        if (btn == fileFolderCategoryHelpBtn) {
            ret = appearance.getContextHelpMessages().appCategoryFileInput();
        } else if (btn == listsCategoryHelpBtn) {
            ret = appearance.getContextHelpMessages().appCategoryLists();
        } else if (btn == textNumericalInputCategoryHelpBtn) {
            ret = appearance.getContextHelpMessages().appCategoryTextInput();
        } else if (btn == outputCategoryHelpBtn) {
            ret = appearance.getContextHelpMessages().appCategoryOutput();
        } else if (btn == referenceGenomeCategoryHelpBtn) {
            ret = appearance.getContextHelpMessages().appCategoryReferenceGenome();
        }
        return ret;
    }

    private void createDragSource(final Image widget, final ArgumentType type) {
        DragSource ds = new DragSource(widget);
        ds.addDragStartHandler(new DndDragStartHandler() {

            @Override
            public void onDragStart(DndDragStartEvent event) {
                if (onlyLabelEditMode && !type.equals(ArgumentType.Info)) {
                    event.getStatusProxy().setStatus(false);
                    event.getStatusProxy().update("This item cannot be added to a published app.");
                    return;
                }

                event.getStatusProxy().update(widget.getElement().getString());
            }
        });
        ds.setData(createNewArgument(type));
        dragSourceMap.put(type, ds);
        if (GXT.isGecko()) {
            widget.addMouseDownHandler(new MouseDownHandler() {
                @Override
                public void onMouseDown(MouseDownEvent event) {
                    widget.addStyleName(appearance.getStyle().grabbing());
                }
            });
            widget.addMouseUpHandler(new MouseUpHandler() {
                @Override
                public void onMouseUp(MouseUpEvent event) {
                    widget.removeStyleName(appearance.getStyle().grabbing());
                }
            });
        }
    }

    private ArgumentGroup createNewArgumentGroup() {
        AutoBean<ArgumentGroup> argGrpAb = factory.argumentGroup();
        // JDS Annotate as a newly created autobean
        argGrpAb.setTag(ArgumentGroup.IS_NEW, "--");

        ArgumentGroup ag = argGrpAb.as();
        ag.setArguments(Lists.<Argument> newArrayList());
        ag.setLabel("DEFAULT");
        return ag;
    }

    private Argument createNewArgument(ArgumentType type) {
        AutoBean<Argument> argAb = factory.argument();
        // JDS Annotate as a newly created autobean.
        argAb.setTag(Argument.IS_NEW, "--");

        Argument argument = argAb.as();
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

    public void setOnlyLabelEditMode(boolean onlyLabelEditMode) {
        this.onlyLabelEditMode = onlyLabelEditMode;
    }

}
