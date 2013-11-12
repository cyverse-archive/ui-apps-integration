package org.iplantc.core.uiapps.integration.client.presenter.dnd;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import org.iplantc.core.uiapps.integration.client.events.DeleteArgumentEvent;
import org.iplantc.core.uiapps.integration.client.events.DeleteArgumentEvent.DeleteArgumentEventHandler;
import org.iplantc.core.uiapps.integration.client.events.DeleteArgumentEvent.HasDeleteArgumentEventHandlers;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.HasArgumentSelectedEventHandlers;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

/**
 * A handler which controls the visibility, placement, and selection of a delete button over the
 * children of a <code>VerticalLayoutContainer</code>.
 *
 * @author jstroot
 *
 */
public final class ArgumentWYSIWYGDeleteHandler implements MouseOverHandler, MouseOutHandler, SelectEvent.SelectHandler, HasDeleteArgumentEventHandlers, HasArgumentSelectedEventHandlers {
    int currentItemIndex = -1;
    private final AppTemplateWizardAppearance appearance;
    private final IconButton button;
    private HandlerManager handlerManager;
    private final boolean labelOnlyEditMode;
    private final VerticalLayoutContainer layoutContainer;
    private final ListEditor<Argument, AppTemplateForm.ArgumentEditorFactory> listEditor;

    public ArgumentWYSIWYGDeleteHandler(AppTemplateWizardAppearance appearance, ListEditor<Argument, AppTemplateForm.ArgumentEditorFactory> listEditor, VerticalLayoutContainer layoutContainer,
            IconButton button, boolean labelOnlyEditMode) {
        this.appearance = appearance;
        this.listEditor = listEditor;
        this.layoutContainer = layoutContainer;
        this.button = button;
        this.labelOnlyEditMode = labelOnlyEditMode;
        layoutContainer.addDomHandler(this, MouseOverEvent.getType());
        layoutContainer.addDomHandler(this, MouseOutEvent.getType());
        layoutContainer.add(button);
        button.setVisible(false);
        button.addSelectHandler(this);
    }

    @Override
    public HandlerRegistration addArgumentSelectedEventHandler(ArgumentSelectedEventHandler handler) {
        return ensureHandlers().addHandler(ArgumentSelectedEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addDeleteArgumentEventHandler(DeleteArgumentEventHandler handler) {
        return ensureHandlers().addHandler(DeleteArgumentEvent.TYPE, handler);
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
        // event target gives us the thing we are leaving.

        EventTarget relatedTarget = event.getNativeEvent().getRelatedEventTarget();
        if (relatedTarget == null || Element.as(relatedTarget) == button.getElement()) {
            return;
        }
        currentItemIndex = -1;
        button.setVisible(false);
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
        int childCount = layoutContainer.getElement().getChildCount();
        for (int j = 0; j < childCount; j++) {
            Element child = Element.as(layoutContainer.getElement().getChild(j));
            Element target = Element.as(event.getNativeEvent().getEventTarget());
            if (child.isOrHasChild(target) && (target != button.getElement())) {
                // Determine if button needs to be placed,
                // if so, then place it.
                Argument arg = listEditor.getList().get(j);
                if (isOnlyLabelEditMode() && !arg.getType().equals(ArgumentType.Info)) {
                    break;
                }

                // JDS Do not display delete button for EmptyGroup argument.
                if (!Strings.isNullOrEmpty(arg.getId()) && arg.getId().equalsIgnoreCase(AppTemplateUtils.EMPTY_GROUP_ARG_ID)) {
                    break;
                }
                currentItemIndex = j;
                button.setVisible(true);
                button.setPagePosition(child.getAbsoluteRight() - (2 * button.getOffsetWidth()), child.getAbsoluteTop());

                break;
            }
        }
    }

    @Override
    public void onSelect(SelectEvent event) {
        if (currentItemIndex >= 0) {
            Argument arg = listEditor.getList().get(currentItemIndex);

            if (isOnlyLabelEditMode() && !arg.getType().equals(ArgumentType.Info)) {
                return;
            }
            ensureHandlers().fireEvent(new DeleteArgumentEvent(arg));
            listEditor.getList().remove(currentItemIndex);

            if (!listEditor.getList().isEmpty()) {
                int index = (currentItemIndex > 0) ? currentItemIndex - 1 : 0;
                AppTemplateForm.ArgumentEditor toBeSelected = listEditor.getEditors().get(index).getSubEditor();
                ensureHandlers().fireEvent(new ArgumentSelectedEvent(arg));
                toBeSelected.asWidget().addStyleName(appearance.getStyle().argumentSelect());
            } else {
                /*
                 * JDS If the ArgumentGroup is empty after performing remove, add the empty group
                 *
                 * JDS Fire ArgumentSelectedEvent with null parameter. This is to inform handlers
                 * that
                 * the selection should be cleared, or the previous argument selected, if possible. *
                 * argument.
                 */
                listEditor.getList().add(AppTemplateUtils.getEmptyGroupArgument());
                layoutContainer.forceLayout();
                ensureHandlers().fireEvent(new ArgumentSelectedEvent(null));
            }

        }
    }

    HandlerManager ensureHandlers() {
        return handlerManager == null ? handlerManager = createHandlerManager() : handlerManager;
    }

    private HandlerManager createHandlerManager() {
        return new HandlerManager(this);
    }

    private boolean isOnlyLabelEditMode() {
        return labelOnlyEditMode;
    }
}
