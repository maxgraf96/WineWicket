package com.graf.wicket.wine;

import com.googlecode.wicket.jquery.ui.form.button.AjaxButton;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * Created by Max on 18.09.2015.
 */
public class WinePage extends WebPage {
    //Wines for accessing/editing and deleting wine respectively
    public static Wine modelWine, closeWine;
    //Fields for form input
    public TextField<String> nameField, ortField, typeField, agingPrivateField;
    public TextField<Float> abHofPriceField;
    public TextField<Integer> yearField;
    private CheckBox bestellbarCheckBox;
    //Labels for wine data
    private Label ort, type, agingPrivate, year, abHofPrice, bestellbar, ortText, typeText, agingPrivateText,
            yearText, abHofPriceText, bestellbarText, bestellbarFix;
    //Containers for easy access
    private WebMarkupContainer labels, textfields;
    //Form for editing wine
    private Form<Wine> winePageForm;
    //Boolean for checking if user is viewing/editing the wine
    private boolean viewing = true;

    public WinePage(final PageReference wineWindowPage, Wine origWine) {
        modelWine = origWine;
        closeWine = modelWine;

        //region labels for displaying dynamic wine values
        //Put all labels into one Container for easy access
        labels = new WebMarkupContainer("labels");
        labels.setOutputMarkupId(true);
        labels.add(ort = new Label("ortLabel", new PropertyModel<Wine>(modelWine, "ort")));
        labels.add(type = new Label("typeLabel", new PropertyModel<Wine>(modelWine, "type")));
        labels.add(agingPrivate = new Label("agingPrivateLabel", new PropertyModel<Wine>(modelWine, "agingPrivate")));
        labels.add(year = new Label("yearLabel", new PropertyModel<Wine>(modelWine, "year")));
        labels.add(abHofPrice = new Label("abHofPriceLabel", new PropertyModel<Wine>(modelWine, "abHofPrice")));
        labels.add(bestellbar = new Label("bestellbarLabel", new PropertyModel<Wine>(modelWine, "bestellbar")));
        //endregion labels for displaying dynamic wine values

        //region labels for displaying static wine attribute descriptors
        labels.add(ortText = new Label("ortText", Model.of("Ort")));
        labels.add(typeText = new Label("typeText", Model.of("Typ")));
        labels.add(agingPrivateText = new Label("agingPrivateText", Model.of("Art der Reife")));
        labels.add(yearText = new Label("yearText", Model.of("Jahr")));
        labels.add(abHofPriceText = new Label("abHofPriceText", Model.of("Preis")));
        labels.add(bestellbarText = new Label("bestellbarText", Model.of("Bestellbar")));
        //endregion

        winePageForm = new WinePageForm("winePageForm");
        winePageForm.setOutputMarkupId(true);
        winePageForm.add(new AttributeAppender("style", "width:30%;margin-left:auto;margin-right:auto;margin-top:5vh;"));
        add(winePageForm);
        labels.add(new AttributeAppender("style", "width:30%;margin-left:auto;margin-right:auto;margin-top:5vh;"));
        add(labels);
    }

    //Form for editing wine
    public final class WinePageForm extends Form<Wine> {
        //Attribute modifier for hiding elements
        AttributeModifier hidden = new AttributeModifier("style", "display:none;");
        //String for submit button value
        String btnvalue = "Edit";

        public WinePageForm(final String id) {
            super(id, new CompoundPropertyModel(modelWine));
            setMarkupId("winePageForm");

            //region entryfields
            //Add entry fields
            nameField = new TextField("name");
            nameField.setType(String.class);
            nameField.setOutputMarkupId(true);

            ortField = new TextField("ort");
            ortField.setType(String.class);
            ortField.setOutputMarkupId(true);

            typeField = new TextField("type");
            typeField.setType(String.class);
            typeField.setOutputMarkupId(true);

            agingPrivateField = new TextField("agingPrivate");
            agingPrivateField.setType(String.class);
            agingPrivateField.setOutputMarkupId(true);

            abHofPriceField = new TextField("abHofPrice");
            abHofPriceField.setType(Float.class);
            abHofPriceField.setOutputMarkupId(true);

            yearField = new TextField("year");
            yearField.setType(Integer.class);
            yearField.setOutputMarkupId(true);

            bestellbarCheckBox = new CheckBox("bestellbar");
            bestellbarCheckBox.setType(Boolean.class);
            bestellbarCheckBox.setOutputMarkupId(true);

            //Add label which only show the text "Bestellbar" next to check box
            bestellbarFix = new Label("bestellbarFix", Model.of("Bestellbar"));
            bestellbarFix.setOutputMarkupId(true);
            //endregion

            //Put all form components into one Container for easy access
            textfields = new WebMarkupContainer("textFields");
            textfields.add(nameField);
            textfields.add(ortField);
            textfields.add(typeField);
            textfields.add(agingPrivateField);
            textfields.add(abHofPriceField);
            textfields.add(yearField);
            textfields.add(bestellbarCheckBox);
            textfields.add(bestellbarFix);

            //Make all textfields invisible, they'll be visible when user clicks editsave button
            textfields.setOutputMarkupId(true);
            textfields.add(hidden);
            add(textfields);

            //region editsave button
            add(new AjaxButton("editsave", new PropertyModel<String>(this, btnvalue), winePageForm) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    if (viewing) {
                        //Invisible
                        labels.add(hidden);
                        //Visible
                        textfields.remove(hidden);
                        //Change button value
                        btnvalue = "Save";

                        target.add(this);
                        target.add(labels);
                        target.add(textfields);

                        viewing = false;
                    } else {
                        //Visible
                        labels.remove(hidden);
                        //Invisible
                        textfields.add(hidden);
                        //Change button value
                        btnvalue = "Edit";

                        target.add(this);
                        target.add(labels);
                        target.add(textfields);

                        viewing = true;
                    }
                }
            });
            //endregion editsave button

            //region delete button
            add(new AjaxButton("delete", winePageForm) {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    WineHome.wineWindow.close(target);
                    WineHome.wineList.remove(closeWine);
                }
            });
            //endregion
        }

        //Getter for edit button
        public String getEdit() {
            return btnvalue;
        }
    }
}
