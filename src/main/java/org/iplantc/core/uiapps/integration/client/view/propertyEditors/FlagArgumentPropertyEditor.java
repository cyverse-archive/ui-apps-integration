package org.iplantc.core.uiapps.integration.client.view.propertyEditors;

import java.util.LinkedList;
import java.util.List;

import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsContextualHelpMessages;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.resources.client.uiapps.widgets.argumentTypes.CheckboxInputLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.ArgumentEditorConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.arguments.converters.SplittableToBooleanConverter;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;
import org.iplantc.core.uiapps.widgets.client.view.editors.widgets.CheckBoxAdapter;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;

public class FlagArgumentPropertyEditor extends AbstractArgumentPropertyEditor {

    public final class FlagArgumentOptionEditor implements ValueAwareEditor<String>, LeafValueEditor<String>, InvalidHandler {
        public final class ArgOptionsNotEmptyValidator implements Validator<String> {
            private final TakesValue<String> otherArgOption;

            public ArgOptionsNotEmptyValidator(TakesValue<String> otherArgOption) {
                this.otherArgOption = otherArgOption;
            }

            @Override
            public List<EditorError> validate(Editor<String> editor, String value) {
                if (Strings.isNullOrEmpty(value) && Strings.isNullOrEmpty(otherArgOption.getValue())) {
                    return Lists.<EditorError> newArrayList(new DefaultEditorError(editor, "An argument value cannot be defined without a corresponding argument option.", value));
                }
                return null;
            }
        }

        public final class HasArgumentOptionValidator implements Validator<String> {
            private final TakesValue<String> argOption;

            public HasArgumentOptionValidator(TakesValue<String> argOption) {
                this.argOption = argOption;
            }

            @Override
            public List<EditorError> validate(Editor<String> editor, String value) {
                if (!Strings.isNullOrEmpty(value) && Strings.isNullOrEmpty(argOption.getValue())) {
                    return Lists.<EditorError> newArrayList(new DefaultEditorError(editor, "At least one argument option must be defined.", value));
                }
                return null;
            }
        }

        private final TextField checkedValue1;
        private final TextField checkedArgOption1;
        private final TextField unCheckedArgOption1;
        private final TextField unCheckedValue1;
        private EditorDelegate<String> delegate;

        public FlagArgumentOptionEditor(TextField checkedArgOption, TextField checkedValue, TextField unCheckedArgOption, TextField unCheckedValue) {
            this.checkedArgOption1 = checkedArgOption;
            this.checkedValue1 = checkedValue;
            this.unCheckedArgOption1 = unCheckedArgOption;
            this.unCheckedValue1 = unCheckedValue;
            init();
        }

        private void init() {
            checkedValue1.addValidator(new HasArgumentOptionValidator(checkedArgOption1));
            unCheckedValue1.addValidator(new HasArgumentOptionValidator(unCheckedArgOption1));

            checkedArgOption1.addValidator(new ArgOptionsNotEmptyValidator(unCheckedArgOption1));
            unCheckedArgOption1.addValidator(new ArgOptionsNotEmptyValidator(checkedArgOption1));
        }

        @Override
        public void setValue(String value) {
            // Split value
            LinkedList<String> newLinkedList = Lists.newLinkedList(Splitter.on(",").omitEmptyStrings().trimResults().split(Strings.nullToEmpty(value)));
            if (newLinkedList.peek() != null) {
                setCheckedFields(newLinkedList.removeFirst());
                setUncheckedFields(newLinkedList.removeFirst());
            }
        }

        private void setUncheckedFields(String pop) {
            LinkedList<String> newLinkedList = Lists.newLinkedList(Splitter.on(" ").omitEmptyStrings().trimResults().split(pop));
            checkedArgOption1.setValue(newLinkedList.removeFirst());
            checkedValue1.setValue(newLinkedList.removeFirst());
        }

        private void setCheckedFields(String pop) {
            if (pop == null) {
                unCheckedArgOption1.clear();
                unCheckedValue1.clear();
                return;
            }
            LinkedList<String> newLinkedList = Lists.newLinkedList(Splitter.on(" ").omitEmptyStrings().trimResults().split(pop));
            unCheckedArgOption1.setValue(newLinkedList.removeFirst());
            unCheckedValue1.setValue(newLinkedList.removeFirst());
        }

        @Override
        public String getValue() {
            String checked = checkedArgOption1.getValue() + " " + checkedValue1.getValue();
            String unChecked = unCheckedArgOption1.getValue() + " " + unCheckedValue1.getValue();
            String ret = checked + ", " + unChecked;
            return ret;
        }

        @Override
        public void setDelegate(EditorDelegate<String> delegate) {
            this.delegate = delegate;
        }

        @Override
        public void flush() {
            checkedArgOption1.flush();
            checkedValue1.flush();
            unCheckedArgOption1.flush();
            unCheckedValue1.flush();
        }

        @Override
        public void onPropertyChange(String... paths) {/* Do Nothing */}

        @Override
        public void onInvalid(InvalidEvent event) {
            for (EditorError err : event.getErrors()) {
                delegate.recordError(err.getMessage(), err.getValue(), err.getUserData());
            }
        }
    }

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

    @Ignore
    @UiField
    TextField checkedArgOption, checkedValue, unCheckedArgOption, unCheckedValue;

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

        LeafValueEditor<String> argumentOptionEditor = new FlagArgumentOptionEditor(checkedArgOption, checkedValue, unCheckedArgOption, unCheckedValue);

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

    @Override
    protected void initLabelOnlyEditMode(boolean isLabelOnlyEditMode) {
        defaultValueEditor.setEnabled(!isLabelOnlyEditMode);
        doNotDisplay.setEnabled(!isLabelOnlyEditMode);
    }

    @UiHandler("defaultValueEditor")
    void onDefaultValueChange(ValueChangeEvent<Splittable> event) {
        // Forward defaultValue onto value.
        model.setValue(event.getValue());
    }

}