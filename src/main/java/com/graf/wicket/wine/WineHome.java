package com.graf.wicket.wine;

import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.crypt.StringUtils;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WineHome extends WebPage implements AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    //Global list of all wines from all users
    private static final List<Wine> wineList = Collections.synchronizedList(new ArrayList<Wine>());
    private WineForm wineForm;
    private PropertyListView<Wine> wineView;

    public WineHome(final PageParameters parameters) {
		super(parameters);

        //Add form for adding wine
        wineForm = new WineForm("wineForm");

        //Add listview of existing wines
        wineView = new PropertyListView<Wine>("wines",wineList) {
            @Override
            public void populateItem(final ListItem<Wine> listItem) {
                listItem.add(new Label("name"));
                listItem.add(new Label("ort"));
                listItem.add(new Label("type"));
                listItem.add(new Label("year"));
                listItem.add(new Label("abHofPrice"));
                listItem.add(new Label("bestellbar"));
                listItem.add(new Label("agingPrivate"));
            }
        };

        //Add things
        add(wineForm);
        add(wineView);

        //Check if user already added wines
        if(wineList.size() > 0){
            wineView.setVisible(true);
        }
        else{
            //Hide listview and show form to add wine
            wineView.setVisible(false);
            wineForm.setVisible(true);
        }


    }

    //Form for adding wine
    public final class WineForm extends Form<ValueMap>{
        public WineForm(final String id){
            super(id, new CompoundPropertyModel<ValueMap>(new ValueMap()));
            setMarkupId("wineForm");

            //Add entry fields
            add(new TextArea<String>("name").setType(String.class));
            add(new TextArea<String>("ort").setType(String.class));
            add(new TextArea<String>("type").setType(String.class));
            add(new TextArea<String>("agingPrivate").setType(String.class));
            add(new TextArea<String>("abHofPrice").setType(Float.class));
            add(new TextArea<String>("year").setType(Integer.class));
            add(new CheckBox("bestellbar").setType(Boolean.class));
        }

        @Override
        public final void onSubmit(){
            ValueMap values = getModelObject();
            //check user entered data
            if(values.get("name") == null || values.get("name").toString().isEmpty()){
                error("Wine must have a name!");
                return;
            }

            //Create a copy of the created wine
            Wine wine = new Wine();
            wine.setName((String) values.get("name"));
            wine.setOrt((String) values.get("ort"));
            wine.setType((String) values.get("type"));
            wine.setAgingPrivate((String) values.get("agingPrivate"));
            wine.setAbHofPrice((Float) values.get("abHofPrice"));
            wine.setYear((Integer) values.get("year"));
            wine.setBestellbar((Boolean) values.get("bestellbar"));

            //Add to list
            wineList.add(0, wine);

            //Clear the components
            values.put("name", "");
            values.put("ort", "");
            values.put("type", "");
            values.put("agingPrivate", "");
            values.put("abHofPrice", "");
            values.put("year", "");
            values.put("bestellbar", "");

            //Make listview visible and form invisible
            wineView.setVisible(true);
            wineForm.setVisible(false);

        }
    }

    public static void clear(){
        wineList.clear();
    }

}
