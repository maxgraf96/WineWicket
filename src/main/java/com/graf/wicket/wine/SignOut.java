package com.graf.wicket.wine;

import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Created by Max on 07.09.2015.
 */
public class SignOut extends WebPage{
    public SignOut(final PageParameters parameters){
        //End session
        getSession().invalidate();
        //Redirect to login page
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(
                new RenderPageRequestHandler(new PageProvider(SignIn.class,null),
                RenderPageRequestHandler.RedirectPolicy.NEVER_REDIRECT)
        );
    }
}
