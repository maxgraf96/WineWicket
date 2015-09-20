package com.graf.wicket.wine;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
/**
 * Created by Max on 01.09.2015.
 */
public class SignInSession extends AuthenticatedWebSession {
    private String user;

    protected SignInSession(Request request){
        super(request);
    }

    //returns true if the username and password are correct
    @Override
    public final boolean authenticate(final String username, final String password){
        final String USER = "winzerkönig";
        final String PASS = "invinoveritas";

        if(user == null){
            if(USER.equalsIgnoreCase(username) && PASS.equalsIgnoreCase(password)){
                user = username;
            }
        }

        return user != null;
    }

    public String getUser(){
        return user;
    }

    @Override
    public Roles getRoles(){
        if(isSignedIn()){
            //Give user admin role
            return new Roles(Roles.ADMIN);
        }
        return null;
    }


}
