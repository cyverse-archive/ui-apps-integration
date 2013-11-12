package org.iplantc.core.uiapps.integration.client.gin;

import org.iplantc.core.uiapps.integration.client.view.AppsEditorView;
import org.iplantc.core.uiapps.widgets.client.view.AppLaunchPreviewViewImpl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(AppEditorGinModule.class)
public interface AppsEditorInjector extends Ginjector {

    public static final AppsEditorInjector INSTANCE = GWT.create(AppsEditorInjector.class);

    AppsEditorView.Presenter getAppEditorPresenter();

    AppLaunchPreviewViewImpl getAppLaunchPreviewView();

}
