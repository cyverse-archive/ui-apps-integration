package org.iplantc.core.uiapps.integration.client.presenter.visitors;

import com.google.gwt.editor.client.EditorContext;
import com.google.gwt.editor.client.EditorVisitor;

import org.iplantc.core.uiapps.widgets.client.view.HasLabelOnlyEditMode;

public class InitLabelOnlyEditMode extends EditorVisitor {

    private final boolean onlyLabelEditMode;

    public InitLabelOnlyEditMode(boolean onlyLabelEditMode) {
        this.onlyLabelEditMode = onlyLabelEditMode;
    }

    @Override
    public <T> void endVisit(EditorContext<T> ctx) {
        if (ctx.getEditor() instanceof HasLabelOnlyEditMode) {
            ((HasLabelOnlyEditMode)ctx.getEditor()).setLabelOnlyEditMode(onlyLabelEditMode);
        }
    }

}
