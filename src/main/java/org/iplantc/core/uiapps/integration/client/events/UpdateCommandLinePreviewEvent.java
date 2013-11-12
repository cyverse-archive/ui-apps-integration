package org.iplantc.core.uiapps.integration.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import org.iplantc.core.uiapps.integration.client.events.UpdateCommandLinePreviewEvent.UpdateCommandLinePreviewEventHandler;

public class UpdateCommandLinePreviewEvent extends GwtEvent<UpdateCommandLinePreviewEventHandler> {

    public static interface HasUpdateCommandLinePreviewEventHandlers {
        HandlerRegistration addUpdateCommandLinePreviewEventHandler(UpdateCommandLinePreviewEventHandler handler);
    }

    public interface UpdateCommandLinePreviewEventHandler extends EventHandler {
        void onUpdateCommandLinePreview(UpdateCommandLinePreviewEvent event);
    }

    public static final GwtEvent.Type<UpdateCommandLinePreviewEventHandler> TYPE = new GwtEvent.Type<UpdateCommandLinePreviewEventHandler>();

    @Override
    public GwtEvent.Type<UpdateCommandLinePreviewEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateCommandLinePreviewEventHandler handler) {
        handler.onUpdateCommandLinePreview(this);
    }
}