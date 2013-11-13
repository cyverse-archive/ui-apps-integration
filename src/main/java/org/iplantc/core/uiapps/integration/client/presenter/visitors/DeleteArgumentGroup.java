package org.iplantc.core.uiapps.integration.client.presenter.visitors;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.EditorVisitor;
import com.google.gwt.editor.client.adapters.ListEditor;

import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

import java.util.LinkedList;

/**
 * This class is responsible for completely removing a given ArgumentGroup from an editor hierarchy.
 * 
 * First, the ArgumentGroup's associated handlers (its associated editor) will be un-registered.
 * 
 * Next, the absolute path of the given argument will be determined. The ArgumentGroup's parent list
 * editor's
 * path will be derived from the ArgumentGroup's path.
 * 
 * Finally, the parent's path will be used to locate the list editor, and the ArgumentGroup's associated
 * editor will be removed.
 * 
 * @author jstroot
 * 
 */
public class DeleteArgumentGroup extends EditorVisitor {

    private String absolutePath;
    private final AppTemplateWizardAppearance appearance;
    private AppTemplateForm appTemplateForm;
    private final ArgumentGroup argGrp;
    private String parentAbsPath;

    public DeleteArgumentGroup(ArgumentGroup argGrp, AppTemplateWizardAppearance appearance) {
        this.argGrp = argGrp;
        this.appearance = appearance;
    }

    @Override
    public <T> void endVisit(EditorContext<T> ctx) {
        // Remove from parent list editor
        if (ctx.getAbsolutePath().equals(parentAbsPath)) {
            if (ctx.getEditor() instanceof ListEditor<?, ?>) {
                @SuppressWarnings("unchecked")
                ListEditor<ArgumentGroup, AppTemplateForm.ArgumentGroupEditor> listEditor = (ListEditor<ArgumentGroup, AppTemplateForm.ArgumentGroupEditor>)ctx.getEditor();
                int indexRemoved = listEditor.getList().indexOf(argGrp);
                listEditor.getList().remove(argGrp);

                if (listEditor.getList().isEmpty()) {
                    appTemplateForm.asWidget().fireEvent(new ArgumentGroupSelectedEvent(null));
                } else {
                    int index = (indexRemoved > 0) ? indexRemoved - 1 : 0;
                    AppTemplateForm.ArgumentGroupEditor toBeSelected = listEditor.getEditors().get(index);
                    appTemplateForm.asWidget().fireEvent(new ArgumentGroupSelectedEvent(argGrp));
                    toBeSelected.asWidget().addStyleName(appearance.getStyle().appHeaderSelect());
                }
            }
        }
    }

    @Override
    public <T> boolean visit(EditorContext<T> ctx) {
        if (ctx.getEditor() instanceof AppTemplateForm) {
            this.appTemplateForm = (AppTemplateForm)ctx.getEditor();
        }
        // Find ArgumentGroup's absolute path.
        if (ctx.getFromModel() == argGrp) {
            absolutePath = ctx.getAbsolutePath();
            LinkedList<String> newLinkedList = Lists.newLinkedList(Splitter.on(".").split(absolutePath));
            newLinkedList.removeLast();
            parentAbsPath = Joiner.on(".").join(newLinkedList);
        }
        return ctx.asLeafValueEditor() == null;
    }

}
