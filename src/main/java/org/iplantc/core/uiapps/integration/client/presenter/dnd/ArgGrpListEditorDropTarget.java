package org.iplantc.core.uiapps.integration.client.presenter.dnd;

import java.util.List;

import org.iplantc.core.resources.client.messages.I18N;
import org.iplantc.core.resources.client.uiapps.widgets.AppsWidgetsPropertyPanelLabels;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm;
import org.iplantc.core.uiapps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;

/**
 * This <code>DropTarget</code> is responsible for handling
 * {@link org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup} additions to the
 * given {@link com.google.gwt.editor.client.adapters.ListEditor}, as well as handling the auto-expansion
 * of a child <code>ContentPanel</code>s when a drag move containing an
 * {@link org.iplantc.core.uiapps.widgets.client.models.Argument} is detected over a
 * <code>ContentPanel</code>'s header.
 * 
 * TODO JDS Handle DnD Argument additions when drop occurs on a ContentPanel header.
 * 
 * @author jstroot
 * 
 */
public final class ArgGrpListEditorDropTarget extends ContainerDropTarget<AccordionLayoutContainer> {
    private final AppTemplateWizardAppearance appearance = AppTemplateWizardAppearance.INSTANCE;
    private final AppsWidgetsPropertyPanelLabels appsWidgetsDisplay = I18N.APPS_LABELS;
    private int grpCountInt = 2;
    private Header header;
    private final boolean labelOnlyEditMode;
    private final ListEditor<ArgumentGroup, AppTemplateForm.ArgumentGroupEditor> listEditor;

    public ArgGrpListEditorDropTarget(boolean labelOnlyEditMode, AccordionLayoutContainer container, ListEditor<ArgumentGroup, AppTemplateForm.ArgumentGroupEditor> editor) {
        super(container);
        this.labelOnlyEditMode = labelOnlyEditMode;
        this.listEditor = editor;
    }

    @Override
    protected void onDragDrop(DndDropEvent event) {
        super.onDragDrop(event);
        List<ArgumentGroup> list = listEditor.getList();
        boolean isNewArgGrp = AutoBeanUtils.getAutoBean((ArgumentGroup) event.getData()).getTag(ArgumentGroup.IS_NEW) != null;
        ArgumentGroup newArgGrp = AppTemplateUtils.copyArgumentGroup((ArgumentGroup) event.getData());

        // Update new group label, if needed
        if (isNewArgGrp) {
            String defaultGroupLabel = appsWidgetsDisplay.groupDefaultLabel(grpCountInt++);
            newArgGrp.setLabel(defaultGroupLabel);
        }

        if (list != null) {
//            setFireSelectedOnAdd(true);
            list.add(insertIndex, newArgGrp);
            // presenter.onArgumentPropertyValueChange();
        }

        // Not sure if that should occur here, or elsewhere.
    }

    @Override
    protected boolean verifyDragData(Object dragData) {
        return (dragData instanceof ArgumentGroup) && super.verifyDragData(dragData) && !labelOnlyEditMode;
    }

    @Override
    protected boolean verifyDragMove(EventTarget target, Object dragData) {
        XElement conElement = container.getElement();
        Element as = Element.as(target);
        if (Element.is(target) && conElement.isOrHasChild(as)) {
            if (verifyDragData(dragData)) {
                header = null;
                return true;
            } else if (dragData instanceof Argument) {
                IsWidget findWidget = container.findWidget(as);
                if ((findWidget != null)) {
                    boolean isCp = findWidget instanceof ContentPanel;
                    if (isCp && ((ContentPanel)findWidget).getHeader().getElement().isOrHasChild(as)) {
                        final ContentPanel cp = (ContentPanel)findWidget;
                        if(cp.isCollapsed()){
                            header = cp.getHeader();
                            // JDS Kick off timer for autoExpand of ArgumentGroup content panel
                            Timer t = new Timer() {
                                @Override
                                public void run() {
                                    if ((cp.getHeader() == header) && cp.isCollapsed()) {
                                        container.setActiveWidget(cp);
                                    }
                                }
                            };
                            t.schedule(appearance.getAutoExpandOnHoverDelay());

                        }
                    } else {
                        header = null;
                    }
                } else {
                    header = null;
                }
            }
        }

        return super.verifyDragMove(target, dragData);
    }
}
