package org.iplantc.core.uiapps.integration.client.presenter.visitors;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.editor.client.adapters.ListEditor;

import static com.sencha.gxt.dnd.core.client.DND.Feedback.BOTH;

import com.sencha.gxt.core.client.dom.XElement;

import org.iplantc.core.uiapps.integration.client.presenter.dnd.ArgGrpListDragSource;
import org.iplantc.core.uiapps.integration.client.presenter.dnd.ArgGrpListEditorDropTarget;
import org.iplantc.core.uiapps.integration.client.presenter.dnd.ArgListEditorDragSource;
import org.iplantc.core.uiapps.integration.client.presenter.dnd.ArgListEditorDropTarget;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm.ArgumentEditorFactory;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm.ArgumentGroupEditor;


/**
 * @author jstroot
 * 
 */
public class InitializeDragAndDrop extends EditorVisitor {

    private final boolean labelOnlyEditMode;
    private XElement scrollElement = null;

    public InitializeDragAndDrop(boolean labelOnlyEditMode) {
        this.labelOnlyEditMode = labelOnlyEditMode;
    }

    @Override
    public <T> boolean visit(EditorContext<T> ctx) {

        Editor<T> editor = ctx.getEditor();
        if (editor instanceof AppTemplateForm) {
            AppTemplateForm appTemplateForm = (AppTemplateForm)editor;
            scrollElement = appTemplateForm.getDndContainer().getElement();
            ListEditor<ArgumentGroup, ArgumentGroupEditor> listEditor = appTemplateForm.argumentGroups();
            new ArgGrpListDragSource(appTemplateForm.getDndContainer(), listEditor);
            ArgGrpListEditorDropTarget argGrpListEditorDropTarget = new ArgGrpListEditorDropTarget(labelOnlyEditMode, appTemplateForm.getDndContainer(), listEditor);
            argGrpListEditorDropTarget.setFeedback(BOTH);
            argGrpListEditorDropTarget.setAllowSelfAsSource(true);
        } else if (editor instanceof AppTemplateForm.ArgumentGroupEditor) {
            AppTemplateForm.ArgumentGroupEditor argumentListEditor = (AppTemplateForm.ArgumentGroupEditor)editor;
            ListEditor<Argument, ArgumentEditorFactory> asEditor = argumentListEditor.argumentsEditor();
            new ArgListEditorDragSource(argumentListEditor.getDndContainer(), asEditor);
            ArgListEditorDropTarget argListEditorDropTarget = new ArgListEditorDropTarget(labelOnlyEditMode, argumentListEditor.getDndContainer(), asEditor, scrollElement);
            argListEditorDropTarget.setFeedback(BOTH);
            argListEditorDropTarget.setAllowSelfAsSource(true);
        }
        return ctx.asLeafValueEditor() == null;
    }


}
