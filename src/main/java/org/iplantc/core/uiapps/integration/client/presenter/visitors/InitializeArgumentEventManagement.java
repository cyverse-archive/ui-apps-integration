package org.iplantc.core.uiapps.integration.client.presenter.visitors;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.EditorVisitor;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm.ArgumentEditor;
import org.iplantc.de.diskResource.client.views.widgets.DiskResourceSelector.HasDisableBrowseButtons;

/**
 * 
 * @author jstroot
 * 
 */
public class InitializeArgumentEventManagement extends EditorVisitor {

    private final ArgumentEditor argumentEditor;

    public InitializeArgumentEventManagement(ArgumentEditor argumentEditor) {
        this.argumentEditor = argumentEditor;
        if (argumentEditor != null) {
            // Disable ArgumentEditorValidations
            argumentEditor.disableValidations();
            argumentEditor.disableOnNotVisible();
        }
    }

    @Override
    public <T> boolean visit(EditorContext<T> ctx) {
        if (argumentEditor == null) {
            return ctx.asLeafValueEditor() == null;
        }
        Editor<T> editor = ctx.getEditor();
        if (editor instanceof ArgumentSelectedEventHandler) {
            // Add select handlers to the new ArgumentEditor
            argumentEditor.addArgumentSelectedEventHandler((ArgumentSelectedEventHandler)editor);
        }

        if (editor instanceof HasDisableBrowseButtons) {
            ((HasDisableBrowseButtons)editor).disableBrowseButtons();
        }

        // If this is not a LeafValueEditor, continue traversing
        return ctx.asLeafValueEditor() == null;
    }

}
