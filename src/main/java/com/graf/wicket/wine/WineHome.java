package com.graf.wicket.wine;

import com.googlecode.wicket.jquery.ui.form.button.*;
import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import com.googlecode.wicket.jquery.ui.markup.html.link.AjaxLink;
import org.apache.wicket.*;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.*;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.*;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WineHome extends WebPage implements AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    //Global list of all wines from all users
    public static final List<Wine> wineList = Collections.synchronizedList(new ArrayList<Wine>());
    //Form for adding wines
    private WineForm wineForm;
    //Container for wine form
    private WebMarkupContainer wineFormContainer;
    //Container used to update wines
    public static WebMarkupContainer weinkeller = null;
    //Label for displaying if wine list is empty
    private Label noWine;
    //Static Label for display above wineForm
    private Label addWineText;
    //Wine for checking if wine values are empty
    private Wine myWine,modelWine = new Wine();
    //Fields for form input
    private TextField nameField,ortField,typeField,agingPrivateField,abHofPriceField,yearField;
    private CheckBox bestellbarCheckBox;
    //Modal window for displaying wine information
    public static ModalWindow wineWindow;
    //Pagereference for wineWindow
    public static PageReference pageRef;
    //Behavior for completetly hiding components via CSS
    AttributeModifier hidden = new AttributeModifier("style","display:none;");
    //Behavior for blurring body when needed eg. adding wine, modal window
    public static AttributeAppender blur = new AttributeAppender("style",
            "    -webkit-filter: blur(14px);\n" +
            "    -moz-filter: blur(14px);\n" +
            "    filter: blur(14px);"
            );
    //Webmarkupcontainer for blurring body
    public static WebMarkupContainer bodyContainer;
    //Columns for datatable
    public static List<IColumn<Wine,String>> columns;

    public WineHome(final PageParameters parameters) {
        super(parameters);
        //Get page reference
        pageRef = getPageReference();

        //Initialize ModalWindwow
        add(wineWindow = new ModalWindow("wineWindow"));
        wineWindow.setCookieName("wine-window");

        //Add Label for static display above wine form
        addWineText = new Label("addWineText",Model.of("Add wine"));

        //Add form for adding wine
        wineForm = new WineForm("wineForm");

        //Initialize wineFormContainer
        wineFormContainer = new WebMarkupContainer("wineFormContainer"){};
        wineFormContainer.setOutputMarkupId(true);
        wineFormContainer.add(wineForm);
        wineFormContainer.add(addWineText);
        wineFormContainer.add(hidden);

        //Add label which displays if winelist is empty
        noWine = new Label("noWine","Your wine list is empty!");
        noWine.setOutputMarkupId(true);

        //Hide form if winelist != empty
        //Hide label if wines were already added
        if(wineList.size() > 0){
            noWine.add(hidden);
            add(noWine);
            add(wineFormContainer);
        }
        else {
            add(noWine);
            //Show wineForm only when user clicks on +Wine
            add(wineFormContainer);
        }

        //Prepare DataTable
        columns = new ArrayList<IColumn<Wine,String>>();
        columns.add(new PropertyColumn(new Model<String>("Name"),"name","name"));
        columns.add(new PropertyColumn(new Model<String>("Ort"),"ort","ort"));
        columns.add(new PropertyColumn(new Model<String>("Typ"),"type","type"));
        columns.add(new PropertyColumn(new Model<String>("Jahr"),"year","year"));
        columns.add(new PropertyColumn(new Model<String>("Preis"),"abHofPrice","abHofPrice"));
        columns.add(new PropertyColumn(new Model<String>("Bestellbar"),"bestellbar","bestellbar"));
        columns.add(new PropertyColumn(new Model<String>("Art der Reife"), "agingPrivate", "agingPrivate"));

        //Add datatable container
        weinkeller = new WebMarkupContainer("weinkeller");
        //Add winetable to weinkeller
        weinkeller.add(new MyDataTable("wineTable", columns, new WineProvider(), 10));
        weinkeller.setOutputMarkupId(true);
        add(weinkeller);

        bodyContainer = new WebMarkupContainer("bodyContainer");
        bodyContainer.add(wineWindow);
        bodyContainer.add(//Add link to add wine
                new AjaxLink("addWine") {
                    @Override
                    public void onClick(AjaxRequestTarget ajaxRequestTarget) {
                        try {
                            //Blur body
                            bodyContainer.add(blur);
                            ajaxRequestTarget.add(bodyContainer);
                            wineFormContainer.remove(hidden);
                            ajaxRequestTarget.add(wineFormContainer);
                            ajaxRequestTarget.appendJavaScript("document.getElementById('textname').focus();");
                        }
                        //Form is visible
                        catch (IllegalStateException e) {
                            ajaxRequestTarget.appendJavaScript("document.getElementById('textname').focus();");
                        }
                    }
                });
        bodyContainer.add(noWine);
        bodyContainer.add(weinkeller);
        bodyContainer.setOutputMarkupId(true);
        add(bodyContainer);
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
                    nameField.clearInput();
                    ortField.clearInput();
                    typeField.clearInput();
                    yearField.clearInput();
                    abHofPriceField.clearInput();
                    agingPrivateField.clearInput();
                    bestellbarCheckBox.clearInput();

                    target.add(nameField);
                    target.add(ortField);
                    target.add(typeField);
                    target.add(yearField);
                    target.add(abHofPriceField);
                    target.add(agingPrivateField);
                    target.add(bestellbarCheckBox);

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
            }.setDefaultFormProcessing(false));
            add(new AjaxButton("cancel",wineForm) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    setResponsePage(WineHome.class);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    setResponsePage(WineHome.class);
                }
            });
            add(new AjaxFormSubmitBehavior(wineForm, "submit") {
                @Override
                public final void onSubmit(AjaxRequestTarget target) {
                    if (!nameField.getValue().isEmpty() && !ortField.getValue().isEmpty() && !typeField.getValue().isEmpty() &&
                            !agingPrivateField.getValue().isEmpty() && !abHofPriceField.getValue().isEmpty() &&
                            !yearField.getValue().isEmpty()) {
                        Wine wine = wineForm.getModelObject();
                        myWine = new Wine(wine);

                        //Add wine to list
                        wineList.add(myWine);
                        //Hide label
                        noWine.add(new AttributeModifier("style", "display:none;"));
                        target.add(noWine);
                        //Update datatable
                        target.add(weinkeller);
                        //Hide form
                        wineFormContainer.add(hidden);

                        //Clear
                        myWine = new Wine();
                        wine.setName("");
                        wine.setOrt("");
                        wine.setType("");
                        wine.setAgingPrivate("");
                        wine.setAbHofPrice((float) 0);
                        wine.setYear(0);
                        wine.setBestellbar(false);

                        //Unblur
                        bodyContainer.remove(blur);
                    } else {
                        target.appendJavaScript("alert('Please fill out the wine form completely.');");
                    }
                }

                @Override
                protected void onError(AjaxRequestTarget target) {
                    target.appendJavaScript("alert('AJAX error! Please check form for invalid input.');");
                }
            });
        }
    }

    public static void clear(){
        wineList.clear();
    }
}
