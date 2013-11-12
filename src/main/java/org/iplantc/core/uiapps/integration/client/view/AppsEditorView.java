package org.iplantc.core.uiapps.integration.client.view;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;

import com.sencha.gxt.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;

import org.iplantc.core.uiapps.integration.client.events.UpdateCommandLinePreviewEvent.UpdateCommandLinePreviewEventHandler;
import org.iplantc.core.uiapps.integration.client.view.widgets.AppTemplatePropertyEditor;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.view.AppLaunchView.RenameWindowHeaderCommand;
import org.iplantc.core.uiapps.widgets.client.view.AppTemplateForm;

/**
 * @author jstroot
 *
 */
public interface AppsEditorView extends IsWidget, Editor<AppTemplate>, ArgumentSelectedEventHandler, ArgumentGroupSelectedEventHandler, AppTemplateSelectedEventHandler {
    
    interface EditorDriver extends SimpleBeanEditorDriver<AppTemplate, AppsEditorView> {
    }

    public interface Presenter extends org.iplantc.core.uiapps.widgets.client.view.AppLaunchView.BasePresenter, AppEditorToolbar.Presenter, BeforeHideHandler, UpdateCommandLinePreviewEventHandler {

        /**
         * This constant is used to key into an Autobean's tag map
         */
        String HANDLERS = "autobean_handlers_tag_key";
        void go(final HasOneWidget container, final AppTemplate appTemplate, final RenameWindowHeaderCommand renameCmd);

        boolean isEditorDirty();

        boolean isOnlyLabelEditMode();

        /**
         * Checks if the given argument should be ordered in order to be used by an App at launch.
         * 
         * @param arg
         * @return true if the property can be used at analysis execution but needs an order.
         */
        boolean orderingRequired(Argument arg);

        void setBeforeHideHandlerRegistration(HandlerRegistration hr);

        void setOnlyLabelEditMode(boolean onlyLabelEditMode);

    }

    /**
     * Exposed to satisfy Editor contract
     * 
     * @return
     */
    @Path("")
    AppTemplateForm getAppTemplateForm();

    /**
     * Exposed to satisfy Editor contract
     * 
     * @return
     */
    @Path("")
    AppTemplatePropertyEditor getAppTemplatePropertyEditor();

    EditorDriver getEditorDriver();

    AppEditorToolbar getToolbar();

    void setCmdLinePreview(String cmdLinePreview);

    void setEastWidget(IsWidget widget);

    void setOnlyLabelEditMode(boolean onlyLabelEditMode);

    void setPresenter(Presenter presenter);

}