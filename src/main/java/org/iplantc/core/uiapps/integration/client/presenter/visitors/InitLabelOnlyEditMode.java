package org.iplantc.core.uiapps.integration.client.presenter.visitors;

import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.EditorVisitor;

import org.iplantc.core.uiapps.widgets.client.view.HasLabelOnlyEditMode;

public class InitLabelOnlyEditMode extends EditorVisitor {

    @Override
    public <T> boolean visit(EditorContext<T> ctx) {
        if (ctx.getEditor() instanceof HasLabelOnlyEditMode) {
            ((HasLabelOnlyEditMode)ctx.getEditor()).setLabelOnlyEditMode(true);
        }
        return ctx.asLeafValueEditor() == null;
    }

}
