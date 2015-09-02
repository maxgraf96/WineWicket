package com.graf.wicket.wine;

import org.apache.wicket.authroles.authentication.panel.SignInPanel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.value.ValueMap;

/**
 * Created by Max on 01.09.2015.
 */
public final class SignIn extends WebPage{

    public SignIn(){
        this(null);
    }

    public SignIn(final PageParameters parameters){
        //Standard auth-role module
        add(new SignInPanel("signInPanel",false));
    }
}
