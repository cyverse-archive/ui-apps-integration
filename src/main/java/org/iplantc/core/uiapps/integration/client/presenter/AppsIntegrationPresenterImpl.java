package org.iplantc.core.uiapps.integration.client.presenter;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.iplantc.core.resources.client.messages.IplantDisplayStrings;
import org.iplantc.core.resources.client.uiapps.integration.AppIntegrationErrorMessages;
import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent;
import org.iplantc.core.uiapps.integration.client.dialogs.CommandLineOrderingPanel;
import org.iplantc.core.uiapps.integration.client.services.AppTemplateServices;
import org.iplantc.core.uiapps.integration.client.view.AppIntegrationToolbar;
import org.iplantc.core.uiapps.integration.client.view.AppsIntegrationView;
import org.iplantc.core.uiapps.widgets.client.dialog.DCListingDialog;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateSelectedEvent.AppTemplateSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.AppTemplateUpdatedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.Argument;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentGroup;
import org.iplantc.core.uiapps.widgets.client.models.ArgumentType;
import org.iplantc.core.uiapps.widgets.client.models.DataObject;
import org.iplantc.core.uiapps.widgets.client.models.DeployedComponent;
import org.iplantc.core.uiapps.widgets.client.models.util.AppTemplateUtils;
import org.iplantc.core.uiapps.widgets.client.presenter.AppWizardPresenterJsonAdapter;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard.IArgumentEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard.IArgumentGroupEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.IAppTemplateEditor;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IPlantDialog;
import org.iplantc.core.uicommons.client.views.gxt3.dialogs.IplantInfoBox;
import org.iplantc.de.client.UUIDService;
import org.iplantc.de.client.UUIDServiceAsync;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.info.DefaultInfoConfig;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * TODO JDS Need to control toolbar button visibility (namely, the delete button)
 * TODO JDS Need to integrate the command line preview service with changes made to the bound AppTemplate via the AppTemplateWizard 
 * Every time the bound AppTemplate is changed, the command line preview will potentially change. Simply stated, these changes
 * need to be coordinated.
 * @author jstroot
 *
 */
public class AppsIntegrationPresenterImpl implements AppsIntegrationView.Presenter {

    private final AppsIntegrationView view;
    private AppTemplate appTemplate;
    private final AppTemplateServices atService;
    private final AppIntegrationErrorMessages errorMessages;
    private final EventBus eventBus;
    private Object currentSelection;
    private final IplantDisplayStrings messages;
    private AppTemplate lastSave;
    private HandlerRegistration beforeHideHandlerRegistration;

    public AppsIntegrationPresenterImpl(final AppsIntegrationView view, final EventBus eventBus, final AppTemplateServices atService, final AppIntegrationErrorMessages errorMessages,
            final IplantDisplayStrings messages) {
        this.view = view;
        this.eventBus = eventBus;
        this.atService = atService;
        this.errorMessages = errorMessages;
        this.messages = messages;
        view.setPresenter(this);
        SelectionHandler handler = new SelectionHandler(view, view.getToolbar());
        eventBus.addHandler(ArgumentSelectedEvent.TYPE, handler);
        eventBus.addHandler(ArgumentGroupSelectedEvent.TYPE, handler);
        eventBus.addHandler(AppTemplateSelectedEvent.TYPE, handler);
        eventBus.addHandler(AppTemplateUpdatedEvent.TYPE, this);
    }

    @Override
    public void goLegacy(HasOneWidget container, Splittable legacyJson) {
        setAppTemplateFromLegacyJson(legacyJson);
        go(container);
    }

    @Override
    public void go(HasOneWidget container, AppTemplate appTemplate) {
        this.appTemplate = appTemplate;
        go(container);
    }

    @Override
    public void go(HasOneWidget container) {
        view.edit(appTemplate);
        /*
         * JDS Make a copy so we can check for differences on exit.
         * Flushing is necessary before copying because some values don't exist in the JSON when the
         * template is fetched. This will result in a false detection of changes in the BeforeHide
         * handler method
         */
        view.flush();
        this.lastSave = AppTemplateUtils.copyAppTemplate(appTemplate);
        container.setWidget(view);
    }

    @Override
    public void setAppTemplateFromLegacyJson(Splittable legacyJson) {
        Splittable appTemplateSplit = AppWizardPresenterJsonAdapter.adaptAppTemplateJsonString(legacyJson);

        AppTemplateAutoBeanFactory factory = GWT.create(AppTemplateAutoBeanFactory.class);
        AutoBean<AppTemplate> appTemplateAb = AutoBeanCodex.decode(factory, AppTemplate.class, appTemplateSplit);

        this.appTemplate = appTemplateAb.as();
    }

    @Override
    public AppTemplate getAppTemplate() {
        return appTemplate;
    }

    @Override
    public void onSaveClicked() {
        // The flushed AppTemplate is the same object when editing was first started.
        AppTemplate savedAppTemplate = view.flush();

        // Update the AppTemplate's edited and published date.
        Date currentTime = new Date();
        savedAppTemplate.setEditedDate(currentTime);
        savedAppTemplate.setPublishedDate(currentTime);

        UUIDServiceAsync uuidService = GWT.create(UUIDService.class);

        final List<Argument> argNeedUuid = Lists.newArrayList();
        // First loop over AppTemplate and look for UUIDs which need to be applied
        for (ArgumentGroup ag : savedAppTemplate.getArgumentGroups()) {
            for (Argument arg : ag.getArguments()) {
                if (Strings.isNullOrEmpty(arg.getId())) {
                    argNeedUuid.add(arg);
                }
            }
        }

        // Check if we have anything which needs a UUID
        if (argNeedUuid.size() > 0) {
            uuidService.getUUIDs(argNeedUuid.size(), new GetUuidCallback(argNeedUuid));
        } else {
            doSave();
        }
    }

    @Override
    public void onPreviewUiClicked() {
        // TODO JDS Take current AppTemplate and launch a view with the Apps Widget presenter. "Launch" button should only perform validations.
    }

    @Override
    public void onPreviewJsonClicked() {
        Splittable split = AutoBeanCodex.encode(AutoBeanUtils.getAutoBean(appTemplate));
        TextArea ta = new TextArea();
        ta.setReadOnly(true);
        ta.setValue(prettyPrint(split.getPayload(), null, 4));
        IPlantDialog dlg = new IPlantDialog();
        dlg.setHeadingText(messages.previewJSON());
        dlg.setPredefinedButtons(PredefinedButton.OK);
        dlg.add(ta);
        dlg.setSize("500", "350");
        dlg.setResizable(true);

        dlg.show();
    }

    @Override
    public void onArgumentOrderClicked() {
        IPlantDialog dlg = new IPlantDialog();
        dlg.setPredefinedButtons(PredefinedButton.OK);
        dlg.setHeadingText(messages.commandLineOrder());
        dlg.setModal(true);
        dlg.setOkButtonText(messages.done());
        dlg.setAutoHide(false);
    
        CommandLineOrderingPanel clop = new CommandLineOrderingPanel(getAllTemplateArguments(view.flush()), this, messages);
        clop.setSize("640", "480");
        dlg.add(clop);
        dlg.addHideHandler(new HideHandler() {
            @Override
            public void onHide(HideEvent event) {
                // JDS Tell view to refresh since we may have changed values.
                view.onAppTemplateChanged();
            }
        });
        dlg.show();
    }

    @Override
    public void onDeleteButtonClicked() {
    
        // When delete is clicked, we might want to have the reference of what we are deleting.
        if (currentSelection != null) {
            // TODO JDS Fire an event to delete current selection.
        }
    }

    @Override
    public void onSelectToolClicked() {
        final DCListingDialog dialog = new DCListingDialog();
        dialog.addHideHandler(new HideHandler() {
    
            @Override
            public void onHide(HideEvent event) {
                DeployedComponent dc = dialog.getSelectedComponent();
                // Set the deployed component in the AppTemplate
                if ((dc != null) && (appTemplate != null)) {
                    appTemplate.setDeployedComponent(dc);
                    onAppTemplateChanged();
                }
            }
        });
        dialog.show();
    }

    @Override
    public boolean orderingRequired(Argument arg) {
        if (arg == null) {
            return false;
        }
        ArgumentType type = arg.getType();
        if (type.equals(ArgumentType.Info) || type.equals(ArgumentType.EnvironmentVariable)) {
            return false;
        }
    
        DataObject dataObject = arg.getDataObject();
        if ((dataObject != null) && dataObject.isImplicit() && type.equals(ArgumentType.Output)) {
            return false;
        }
        return true;
    }

    @Override
    public void onAppTemplateChanged() {
        view.onAppTemplateChanged();
    }

    @Override
    public void onAppTemplateUpdate(AppTemplateUpdatedEvent event) {
        updateCommandLinePreview(view.flush());
    }

    @Override
    public void onBeforeHide(final BeforeHideEvent event) {
        // Determine if there are any changes, variables are broken out for readability
        AutoBean<AppTemplate> lastSaveAb = AutoBeanUtils.getAutoBean(lastSave);
        AutoBean<AppTemplate> currentAb = AutoBeanUtils.getAutoBean(AppTemplateUtils.copyAppTemplate(view.flush()));
        String lastSavePayload = AutoBeanCodex.encode(lastSaveAb).getPayload();
        String currentPayload = AutoBeanCodex.encode(currentAb).getPayload();
        boolean areEqual = lastSavePayload.equals(currentPayload);
    
        if (!areEqual) {
            event.setCancelled(true);
            final Component component = event.getSource();
            // JDS There are differences, so prompt user to save.
            final IplantInfoBox dlg = new IplantInfoBox(messages.save(), messages.unsavedChanges()) {
                @Override
                protected void onButtonPressed(TextButton button) {
                    if (button == getButtonBar().getItemByItemId(PredefinedButton.YES.name())) {
                        // JDS Do save and let window close
                        beforeHideHandlerRegistration.removeHandler();
                        onSaveClicked();
                        component.hide();
                    } else if (button == getButtonBar().getItemByItemId(PredefinedButton.NO.name())) {
                        // JDS Just let window close
                        beforeHideHandlerRegistration.removeHandler();
                        component.hide();
                    } else if (button == getButtonBar().getItemByItemId(PredefinedButton.CANCEL.name())) {
                        // JDS Do not hide the window

                    }
                    hide();
                }
            };
            dlg.setIcon(MessageBox.ICONS.question());
            dlg.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
            dlg.show();
        }
    }

    private void doSave() {
        // JDS Make a copy so we can check for differences on exit
        lastSave = AppTemplateUtils.copyAppTemplate(view.flush());
        atService.saveAndPublishAppTemplate(lastSave, new AsyncCallback<String>() {
    
            @Override
            public void onSuccess(String result) {
                eventBus.fireEvent(new AppGroupCountUpdateEvent(true, null));
                // TODO JDS The user feedback provided by the TempInfoWidget needs to be replaced pending completion of new notification widget (waiting on CORE-4126, CORE-4170)
                Info infoThing = new TempInfoWidget();
                infoThing.show(new DefaultInfoConfig("Success", "App Sucessfully Saved"));
            }
    
            @Override
            public void onFailure(Throwable caught) {
                String failureMsg = errorMessages.publishFailureDefaultMessage();
                // TODO JDS Notify user of failure via new notification widget (waiting on CORE-4126, CORE-4170)
                Info infoThing = new TempInfoWidget();
                infoThing.show(new DefaultInfoConfig("Oops!", failureMsg));
            }
        });
    
    }

    /**
     * 
     * A native method that calls java script method to pretty print json.
     * 
     * @param json the json to pretty print
     * @param replacer
     * @param space the char to used for formatting
     * @return the pretty print version of json
     */
    private native String prettyPrint(String json, String replacer, int space) /*-{
		return $wnd.JSON.stringify($wnd.JSON.parse(json), replacer, space);
    }-*/;

    private List<Argument> getAllTemplateArguments(AppTemplate at) {
        if (at == null) {
            return Collections.emptyList();
        }
        List<Argument> args = Lists.newArrayList();
        for (ArgumentGroup ag : at.getArgumentGroups()) {
             args.addAll(ag.getArguments());
        }
        return args;
    }

    private void setCurrentSelection(Object currentSelection) {
        this.currentSelection = currentSelection;
    }

    private void updateCommandLinePreview(AppTemplate flush) {
        // TODO JDS CORE-4190, Waiting on the creation of an endpoint which would assemble the CLI prev
        // of the given AppTemplate
    }

    private final class TempInfoWidget extends Info {
        @Override
        protected Point position() {
            int left = view.asWidget().getAbsoluteLeft() + view.asWidget().getOffsetWidth() - config.getWidth();

            int top = view.asWidget().getAbsoluteTop() + 10;

            return new Point(left, top);
        }
    }

    private final class GetUuidCallback implements AsyncCallback<List<String>> {
        private final List<Argument> argNeedUuid;

        private GetUuidCallback(List<Argument> argNeedUuid) {
            this.argNeedUuid = argNeedUuid;
        }

        @Override
        public void onSuccess(List<String> result) {
            if ((result == null) || (result.size() != argNeedUuid.size())) {
                return;
            }
            // JDS Apply UUIDs
            for (Argument arg : argNeedUuid) {
                arg.setId(result.remove(0));
            }
            doSave();
        }

        @Override
        public void onFailure(Throwable caught) {
            // TODO JDS Notify user of failure via new notification widget (waiting on CORE-4126, CORE-4170)
            ErrorHandler.post(caught);
        }
    }

    /**
     * 
     * @author jstroot
     * 
     */
    private final class SelectionHandler implements ArgumentSelectedEventHandler, ArgumentGroupSelectedEventHandler, AppTemplateSelectedEventHandler {
        private final AppsIntegrationView view;
        private final AppIntegrationToolbar toolbar;
    
        private SelectionHandler(AppsIntegrationView view, AppIntegrationToolbar toolbar) {
            this.view = view;
            this.toolbar = toolbar;
        }
    
        @Override
        public void onArgumentSelected(ArgumentSelectedEvent event) {
            if ((event.getSource() instanceof IArgumentEditor) 
                    && (((IArgumentEditor)event.getSource()).getArgumentPropertyEditor() != null)) {
                IsWidget argumentPropertyEditor = ((IArgumentEditor)event.getSource()).getArgumentPropertyEditor();
                view.setEastWidget(argumentPropertyEditor);
                toolbar.setDeleteButtonEnabled(true);
                setCurrentSelection(((IArgumentEditor)event.getSource()).getCurrentArgument());
            }
        }
    
        @Override
        public void onArgumentGroupSelected(ArgumentGroupSelectedEvent event) {
            if ((event.getSource() instanceof IArgumentGroupEditor) 
                    && (((IArgumentGroupEditor)event.getSource()).getArgumentGroupPropertyEditor() != null)) {
                IsWidget argumentGrpPropertyEditor = ((IArgumentGroupEditor)event.getSource()).getArgumentGroupPropertyEditor();
                view.setEastWidget(argumentGrpPropertyEditor);
                toolbar.setDeleteButtonEnabled(true);
                setCurrentSelection(((IArgumentGroupEditor)event.getSource()).getCurrentArgumentGroup());
            }
        }
    
        @Override
        public void onAppTemplateSelected(AppTemplateSelectedEvent event) {
            if ((event.getSource() instanceof IAppTemplateEditor) 
                    && (((IAppTemplateEditor)event.getSource()).getAppTemplatePropertyEditor() != null)) {
                IsWidget appTemplatePropertyEditor = ((IAppTemplateEditor)event.getSource()).getAppTemplatePropertyEditor();
                view.setEastWidget(appTemplatePropertyEditor);
                toolbar.setDeleteButtonEnabled(true);
                setCurrentSelection(appTemplate);
            }
        }
    }

    @Override
    public void setBeforeHideHandlerRegistration(HandlerRegistration hr) {
        this.beforeHideHandlerRegistration = hr;
    }

}
