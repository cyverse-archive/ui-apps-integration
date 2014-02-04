package org.iplantc.de.apps.integration.client.view;

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.iplantc.de.apps.integration.client.view.widgets.AppGroupContentPanelAppearance;
import org.iplantc.de.apps.widgets.client.view.AppTemplateForm;
import org.iplantc.de.apps.widgets.client.view.editors.ArgumentGroupEditorImpl;
import org.iplantc.de.apps.widgets.client.view.editors.style.AppTemplateWizardAppearance;

public class ArgumentGroupEditorAppEditorImpl extends ArgumentGroupEditorImpl {

    @Inject
    public ArgumentGroupEditorAppEditorImpl(AppGroupContentPanelAppearance cpAppearance, AppTemplateWizardAppearance appearance, Provider<AppTemplateForm.ArgumentEditorFactory> argumentEditorProvider) {
        super(cpAppearance, appearance, argumentEditorProvider);
    }

}
