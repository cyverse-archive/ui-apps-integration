package org.iplantc.core.uiapps.integration.client.view.propertyEditors;

import com.google.gwt.core.client.GWT;
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
import org.iplantc.core.resources.client.uiapps.widgets.argumentTypes.ReferenceSelectorLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.metadata.ReferenceGenome;
import org.iplantc.core.uiapps.widgets.client.services.AppMetadataServiceFacade;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToReferenceGenomeConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.widgets.CheckBoxAdapter;

public class ReferenceSequencePropertyEditor extends AbstractArgumentPropertyEditor {

    interface EditorDriver extends SimpleBeanEditorDriver<Argument, ReferenceSequencePropertyEditor> {}
    interface ReferenceSequencePropertyEditorUiBinder extends UiBinder<Widget, ReferenceSequencePropertyEditor> {}

    private static ReferenceSequencePropertyEditorUiBinder uiBinder = GWT.create(ReferenceSequencePropertyEditorUiBinder.class);

    @UiField(provided = true)
    AppsWidgetsPropertyPanelLabels appLabels;

    @UiField
    @Path("name")
    TextField argumentOptionEditor;
    @UiField(provided = true)
    ArgumentEditorConverter<ReferenceGenome> defaultValueEditor;
    @UiField
    @Path("visible")
    CheckBoxAdapter doNotDisplay;
    @UiField
    TextField label;
    @UiField
    CheckBoxAdapter omitIfBlank, requiredEditor;

    @UiField(provided = true)
    ReferenceSelectorLabels referenceSequenceSelectorLabels;

    @UiField
    @Path("description")
    TextField toolTipEditor;
    @UiField
    FieldLabel toolTipLabel, argumentOptionLabel, selectionItemDefaultValueLabel;

    private final EditorDriver editorDriver = GWT.create(EditorDriver.class);

    @Inject
    public ReferenceSequencePropertyEditor(AppTemplateWizardAppearance appearance, AppsWidgetsPropertyPanelLabels appLabels, AppsWidgetsContextualHelpMessages help,
            AppMetadataServiceFacade appMetadataService) {
        super(appearance);
        this.appLabels = appLabels;
        this.referenceSequenceSelectorLabels = appLabels;

        defaultValueEditor = new ArgumentEditorConverter<ReferenceGenome>(createReferenceGenomeStore(appMetadataService), new SplittableToReferenceGenomeConverter());

        initWidget(uiBinder.createAndBindUi(this));

        selectionItemDefaultValueLabel.setHTML(appearance.createContextualHelpLabel(appLabels.singleSelectionDefaultValue(), help.singleSelectDefaultItem()));

        toolTipLabel.setHTML(appearance.createContextualHelpLabel(appLabels.toolTipText(), help.toolTip()));
        argumentOptionLabel.setHTML(appearance.createContextualHelpLabel(appLabels.argumentOption(), help.argumentOption()));
        doNotDisplay.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appLabels.doNotDisplay()).toSafeHtml());

        requiredEditor.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;").append(appLabels.isRequired()).toSafeHtml());

        omitIfBlank.setHTML(new SafeHtmlBuilder().appendHtmlConstant("&nbsp;")
                .append(appearance.createContextualHelpLabelNoFloat(appLabels.excludeWhenEmpty(), help.excludeReference()))
                .toSafeHtml());
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

    @UiHandler("defaultValueEditor")
    void onDefaultValueChange(ValueChangeEvent<Splittable> event) {
        // Forward defaultValue onto value.
        model.setValue(event.getValue());
    }

}