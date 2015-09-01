package com.graf.wicket.wine;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.util.value.ValueMap;

/**
 * Created by Max on 01.09.2015.
 */
public final class SignIn extends WebPage{

    public SignIn(){
        add(new FeedbackPanel("feedback"));

        add(new SignInForm("signInForm"));
    }

    //LoginForm class
    public final class SignInForm extends Form<Void>
    {
        private static final String USERNAME = "user";
        private static final String PASSWORD = "password";

        private final ValueMap properties = new ValueMap();

        public SignInForm(final String id){
            super(id);

            //Attach textfield components that edit the "properties" value map
            add(new TextField<String>(USERNAME, new PropertyModel<String>(properties,USERNAME)));
            add(new PasswordTextField(PASSWORD, new PropertyModel<String>(properties,PASSWORD)));
        }

        @Override
        public final void onSubmit(){
            //get session info
            SignInSession session = getMySession();

            //Log the user in
            if(session.signIn(getUsername(), getPassword())){
                continueToOriginalDestination();
                setResponsePage(getApplication().getHomePage());
            }
            else{
                //Get error message from properties file associated w/ the component
                String errmsg = getString("loginError",null,"Unable to log you in");

                //Send error message to feedback panel
                error(errmsg);
            }
        }

        private String getUsername(){
            return properties.getString(USERNAME);
        }

        private String getPassword(){
            return properties.getString(PASSWORD);
        }

        private SignInSession getMySession(){
            return(SignInSession)getSession();
        }
    }
}
