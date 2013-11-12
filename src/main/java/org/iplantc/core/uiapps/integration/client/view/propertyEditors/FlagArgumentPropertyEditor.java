package org.iplantc.core.uiapps.integration.client.view.propertyEditors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;

import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.resources.client.uiapps.widgets.argumentTypes.CheckboxInputLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToBooleanConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.widgets.CheckBoxAdapter;

public class FlagArgumentPropertyEditor extends AbstractArgumentPropertyEditor {

    interface EditorDriver extends SimpleBeanEditorDriver<Argument, FlagArgumentPropertyEditor> {}
    interface FlagArgumentPropertyEditorUiBinder extends UiBinder<Widget, FlagArgumentPropertyEditor> {}

    private static FlagArgumentPropertyEditorUiBinder uiBinder = GWT.create(FlagArgumentPropertyEditorUiBinder.class);

    @UiField(provided = true)
    AppsWidgetsPropertyPanelLabels appLabels;

    @UiField
    FieldLabel argLabelLabel;
    @UiField(provided = true)
    CheckboxInputLabels checkBoxLabels;

    @UiField(provided = true)
    ArgumentEditorConverter<Boolean> defaultValueEditor;
    @UiField
    @Path("visible")
    CheckBoxAdapter doNotDisplay;
    @UiField
    TextField label;

    @UiField
    @Path("description")
    TextField toolTipEditor;
    @UiField
    FieldLabel toolTipLabel;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    public FlagArgumentPropertyEditor(AppTemplateWizardAppearance appearance, AppsWidgetsPropertyPanelLabels appLabels, AppsWidgetsContextualHelpMessages help) {
        super(appearance);
        this.appLabels = appLabels;
        this.checkBoxLabels = appLabels;

        CheckBoxAdapter checkBoxAdapter = new CheckBoxAdapter();
        checkBoxAdapter.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").appendEscaped(checkBoxLabels.checkboxDefaultLabel()).toSafeHtml());
        defaultValueEditor = new ArgumentEditorConverter<Boolean>(checkBoxAdapter, new SplittableToBooleanConverter());

        initWidget(uiBinder.createAndBindUi(this));
        toolTipLabel.setHTML(appearance.createContextualHelpLabel(appLabels.toolTipText(), help.toolTip()));
        doNotDisplay.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appLabels.doNotDisplay()).toSafeHtml());

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
    @Ignore
    protected LeafValueEditor<Splittable> getDefaultValueEditor() {
        return defaultValueEditor;
    }

    @UiHandler("defaultValueEditor")
    void onDefaultValueChange(ValueChangeEvent<Splittable> event) {
        // Forward defaultValue onto value.
        model.setValue(event.getValue());
    }

}