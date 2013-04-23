package org.iplantc.core.uiapps.integration.client.presenter;

import java.util.Date;

import org.iplantc.core.resources.client.uiapps.integration.AppIntegrationErrorMessages;
import org.iplantc.core.uiapps.client.events.AppGroupCountUpdateEvent;
import org.iplantc.core.uiapps.integration.client.dialogs.DCListingDialog;
import org.iplantc.core.uiapps.integration.client.services.AppTemplateServices;
import org.iplantc.core.uiapps.integration.client.view.AppsIntegrationView;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentGroupSelectedEvent.ArgumentGroupSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent;
import org.iplantc.core.uiapps.widgets.client.events.ArgumentSelectedEvent.ArgumentSelectedEventHandler;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplate;
import org.iplantc.core.uiapps.widgets.client.models.AppTemplateAutoBeanFactory;
import org.iplantc.core.uiapps.widgets.client.models.DeployedComponent;
import org.iplantc.core.uiapps.widgets.client.presenter.AppWizardPresenterJsonAdapter;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard.IArgumentEditor;
import org.iplantc.core.uiapps.widgets.client.view.editors.AppTemplateWizard.IArgumentGroupEditor;
import org.iplantc.core.uicommons.client.ErrorHandler;
import org.iplantc.core.uicommons.client.events.EventBus;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

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

    public AppsIntegrationPresenterImpl(final AppsIntegrationView view, final EventBus eventBus, final AppTemplateServices atService, final AppIntegrationErrorMessages errorMessages) {
        this.view = view;
        this.eventBus = eventBus;
        this.atService = atService;
        this.errorMessages = errorMessages;
        view.setPresenter(this);
        eventBus.addHandler(ArgumentSelectedEvent.TYPE, new ArgumentSelectedEventHandler() {

            @Override
            public void onArgumentSelected(ArgumentSelectedEvent event) {
                if ((event.getSource() instanceof IArgumentEditor) 
                        && (((IArgumentEditor)event.getSource()).getArgumentPropertyEditor() != null)) {
                    IsWidget argumentPropertyEditor = ((IArgumentEditor)event.getSource()).getArgumentPropertyEditor();
                    view.setEastWidget(argumentPropertyEditor);
                }
            }
        });
        eventBus.addHandler(ArgumentGroupSelectedEvent.TYPE, new ArgumentGroupSelectedEventHandler() {

            @Override
            public void onArgumentGroupSelected(ArgumentGroupSelectedEvent event) {
                if ((event.getSource() instanceof IArgumentGroupEditor) 
                        && (((IArgumentGroupEditor)event.getSource()).getArgumentGroupPropertyEditor() != null)) {
                    IsWidget argumentGrpPropertyEditor = ((IArgumentGroupEditor)event.getSource()).getArgumentGroupPropertyEditor();
                    view.setEastWidget(argumentGrpPropertyEditor);
                }
            }
        });
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

        atService.saveAndPublishAppTemplate(savedAppTemplate, new AsyncCallback<String>() {

            @Override
            public void onSuccess(String result) {
                eventBus.fireEvent(new AppGroupCountUpdateEvent(true, null));
                // TODO JDS Need to display feedback to user, letting them know that this succeeded.
                // Waiting on completion of new notification widget (waiting on CORE-4126, CORE-4170)
            }

            @Override
            public void onFailure(Throwable caught) {
                String failureMsg = errorMessages.publishFailureDefaultMessage();
                // TODO JDS Notify user of failure via new notification widget (waiting on CORE-4126, CORE-4170)
                ErrorHandler.post(failureMsg, caught);
            }
        });
    }

    @Override
    public void onPreviewUiClicked() {
        // TODO JDS Take current AppTemplate and launch a view with the Apps Widget presenter. "Launch" button should only perform validations.
    }

    @Override
    public void onPreviewJsonClicked() {
        // TODO JDS Need to implement JSON viewer, CORE-4215.

    }

    @Override
    public void onArgumentOrderClicked() {
        // TODO JDS CORE-4192
    }

    @Override
    public void onDeleteButtonClicked() {
        // TODO JDS This will depend on selections from the AppTemplateWizard

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
                }
            }
        });
        dialog.show();
    }

}
