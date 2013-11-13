package org.iplantc.core.uiapps.integration.client.view.propertyEditors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.resources.client.uiapps.widgets.argumentTypes.TreeSelectionLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.widgets.CheckBoxAdapter;

public class TreeSelectionPropertyEditor extends AbstractArgumentPropertyEditor {

    interface EditorDriver extends SimpleBeanEditorDriver<Argument, TreeSelectionPropertyEditor> {}
    interface TreeSelectionPropertyEditorUiBinder extends UiBinder<Widget, TreeSelectionPropertyEditor> {}

    private static TreeSelectionPropertyEditorUiBinder uiBinder = GWT.create(TreeSelectionPropertyEditorUiBinder.class);

    @UiField(provided = true)
    AppsWidgetsPropertyPanelLabels appLabels;

    @UiField
    @Path("name")
    TextField argumentOptionEditor;
    @UiField
    @Path("visible")
    CheckBoxAdapter doNotDisplay;

    @UiField
    TextField label;

    @UiField
    CheckBoxAdapter omitIfBlank, requiredEditor;
    @UiField
    @Path("description")
    TextField toolTipEditor;

    @UiField
    FieldLabel toolTipTextLabel, argumentOptionLabel;
    @UiField(provided = true)
    TreeSelectionLabels treeSelectionLabels;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    public TreeSelectionPropertyEditor(AppTemplateWizardAppearance appearance, AppsWidgetsPropertyPanelLabels appLabels, AppsWidgetsContextualHelpMessages help) {
        super(appearance);
        this.appLabels = appLabels;
        this.treeSelectionLabels = appLabels;
        initWidget(uiBinder.createAndBindUi(this));

        toolTipTextLabel.setHTML(appearance.createContextualHelpLabel(appLabels.toolTipText(), help.toolTip()));
        argumentOptionLabel.setHTML(appearance.createContextualHelpLabel(appLabels.argumentOption(), help.argumentOption()));
        doNotDisplay.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appLabels.doNotDisplay()).toSafeHtml());

        requiredEditor.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appLabels.isRequired()).toSafeHtml());
        omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                .append(appearance.createContextualHelpLabelNoFloat(appLabels.excludeWhenEmpty(), help.singleSelectExcludeArgument())).toSafeHtml());

        editorDriver.initialize(this);
        editorDriver.accept(new InitializeTwoWayBinding(this));
    }

    @Override
    public void edit(Argument argument) {
        super.edit(argument);
        editorDriver.edit(argument);
    }

    @Override
    public com.google.gwt.editor.client.EditorDriver<Argument> getEditorDriver() {
        return editorDriver;
    }

    @Override
    protected void initLabelOnlyEditMode(boolean isLabelOnlyEditMode) {
        argumentOptionEditor.setEnabled(!isLabelOnlyEditMode);
        doNotDisplay.setEnabled(!isLabelOnlyEditMode);
        omitIfBlank.setEnabled(!isLabelOnlyEditMode);
        requiredEditor.setEnabled(!isLabelOnlyEditMode);
    }


}