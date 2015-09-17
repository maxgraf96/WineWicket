package com.graf.wicket.wine;

import com.googlecode.wicket.jquery.ui.form.button.*;
import com.googlecode.wicket.jquery.ui.form.button.Button;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButtons;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogIcon;
import com.googlecode.wicket.jquery.ui.widget.dialog.MessageDialog;
import com.sun.deploy.panel.PropertyTreeModel;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxFallbackButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.crypt.StringUtils;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WineHome extends WebPage implements AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    //Global list of all wines from all users
    public static final List<Wine> wineList = Collections.synchronizedList(new ArrayList<Wine>());
    //Form for adding wines
    private WineForm wineForm;
    //Container used to update wines
    private final WebMarkupContainer weinkeller;
    //Label for displaying if wine list is empty
    private Label noWine;
    //Wine for checking if wine values are empty
    private Wine myWine,modelWine = new Wine();
    //Fields for form input
    private TextField nameField,ortField,typeField,agingPrivateField,abHofPriceField,yearField;
    private CheckBox bestellbarCheckBox;

    public WineHome(final PageParameters parameters) {
        super(parameters);

        //Add debug bar
        add(new DebugBar("debug"));
        //Initialize modelWine - must happen before form is added to page
        //modelWine = new Wine();
        //Add form for adding wine
        wineForm = new WineForm("wineForm");
        add(wineForm);

        //Prepare DataTable
        List<IColumn<Wine,String>> columns = new ArrayList<IColumn<Wine,String>>();
        columns.add(new PropertyColumn(new Model<String>("Name"),"name","name"));
        columns.add(new PropertyColumn(new Model<String>("Ort"),"ort","ort"));
        columns.add(new PropertyColumn(new Model<String>("Typ"),"type","type"));
        columns.add(new PropertyColumn(new Model<String>("Jahr"),"year","year"));
        columns.add(new PropertyColumn(new Model<String>("Preis"),"abHofPrice","abHofPrice"));
        columns.add(new PropertyColumn(new Model<String>("Bestellbar"),"bestellbar","bestellbar"));
        columns.add(new PropertyColumn(new Model<String>("Art der Reife"), "agingPrivate", "agingPrivate"));

        //Add datatable container
        weinkeller = new WebMarkupContainer("weinkeller");
        add(weinkeller.setOutputMarkupId(true));

        //Add winetable to weinkeller
        weinkeller.add(new MyDataTable("wineTable",columns,new WineProvider(),10));

        wineForm.add(new AjaxFormSubmitBehavior(wineForm, "submit") {
            @Override
            public final void onSubmit(AjaxRequestTarget target) {
                if(!nameField.getValue().isEmpty() && !ortField.getValue().isEmpty() && !typeField.getValue().isEmpty() &&
                        !agingPrivateField.getValue().isEmpty() && !abHofPriceField.getValue().isEmpty() &&
                        !yearField.getValue().isEmpty()) {
                    Wine wine = wineForm.getModelObject();
                    myWine = new Wine(wine);

                    //Add wine to list
                    wineList.add(myWine);
                    //Hide label
                    noWine.add(new AttributeModifier("class", "hidden"));
                    target.add(noWine);
                    //Update datatable
                    target.add(weinkeller);

                    //Clear
                    myWine = new Wine();
                    wine.setName("");
                    wine.setOrt("");
                    wine.setType("");
                    wine.setAgingPrivate("");
                    wine.setAbHofPrice((float) 0);
                    wine.setYear(0);
                    wine.setBestellbar(false);
                }
                else{
                    target.appendJavaScript("alert('Please fill out the wine form completely.');");
                }
           }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.appendJavaScript("alert('AJAX error!');");
            }
        });

        //Add label which displays if winelist is empty
        noWine = new Label("noWine","Your wine list is empty!");
        noWine.setOutputMarkupId(true);

        //Hide label if wines were already added
        if(wineList.size() > 0){
            noWine.add(new AttributeModifier("class", "hidden"));
            add(noWine);
        }
        else {
            add(noWine);
        }
    }

    //Form for adding wine
    public final class WineForm extends Form<Wine>{
        public WineForm(final String id){
            super(id, new CompoundPropertyModel<Wine>(modelWine));
            setMarkupId("wineForm");

            //Add entry fields
            nameField = new TextField<String>("name");
            nameField.setType(String.class);
            nameField.setOutputMarkupId(true);
            add(nameField);

            ortField = new TextField<String>("ort");
            ortField.setType(String.class);
            ortField.setOutputMarkupId(true);
            add(ortField);

            typeField = new TextField<String>("type");
            typeField.setType(String.class);
            typeField.setOutputMarkupId(true);
            add(typeField);

            agingPrivateField = new TextField<String>("agingPrivate");
            agingPrivateField.setType(String.class);
            agingPrivateField.setOutputMarkupId(true);
            add(agingPrivateField);

            abHofPriceField = new TextField<Float>("abHofPrice");
            abHofPriceField.setType(Float.class);
            abHofPriceField.setOutputMarkupId(true);
            add(abHofPriceField);

            yearField = new TextField<Integer>("year");
            yearField.setType(Integer.class);
            yearField.setOutputMarkupId(true);
            add(yearField);

            bestellbarCheckBox = new CheckBox("bestellbar");
            bestellbarCheckBox.setType(Boolean.class);
            bestellbarCheckBox.setOutputMarkupId(true);
            add(bestellbarCheckBox);

            add(new AjaxButton("fill",wineForm) {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    modelWine.setName("Muskat");
                    modelWine.setOrt("Fels\u0151-Magyarorsz\u00e1g");
                    modelWine.setType("Muskat-Ottonel");
                    modelWine.setYear(2013);
                    modelWine.setAbHofPrice((float) 6.99);
                    modelWine.setAgingPrivate("Fass");
                    modelWine.setBestellbar(true);

                    target.add(nameField);
                    target.add(ortField);
                    target.add(typeField);
                    target.add(yearField);
                    target.add(abHofPriceField);
                    target.add(agingPrivateField);
                    target.add(bestellbarCheckBox);
                }
            });
        }
    }

    public static void clear(){
        wineList.clear();
    }

}
