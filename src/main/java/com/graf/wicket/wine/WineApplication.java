package com.graf.wicket.wine;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.IResource;

public class WineApplication extends WebApplication
{
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return WineHome.class;
	}

    @Override
    public Session newSession(Request request, Response response){
        return new SignInSession(request);
    }

	@Override
	public void init()
	{
		super.init();

		// add your configuration here
        //Register the authorization strategy
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {
            @Override
            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
                //Check if the new Page requires authentication
                if(AuthenticatedWebPage.class.isAssignableFrom(componentClass)){
                    //Is user signed in?
                    if(((SignInSession)Session.get()).isSignedIn()){
                        return true;
                    }

                    //Stop the request, but remember target for later
                    //Invokes Component.continueToOriginalDestination() after successful logon to continue with target
                    throw new RestartResponseAtInterceptPageException(SignIn.class);
                }

                //Proceed
                return true;
            }

            @Override
            public boolean isActionAuthorized(Component component, Action action) {
                //authorize everything
                return true;
            }

            @Override
            public boolean isResourceAuthorized(IResource iResource, PageParameters pageParameters) {
                return false;
            }
        });
	}
}
