package com.graf.wicket.wine;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.util.crypt.StringUtils;
import org.apache.wicket.util.value.ValueMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WineHome extends WebPage implements AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    //List that holds our current wines
    private static final List<Wine> wineList = Collections.synchronizedList(new ArrayList<Wine>());

    public WineHome(final PageParameters parameters) {
		super(parameters);

        //add Form for adding wine
        add(new WineForm("wineForm"));
        //Add listview of existing wines
        add(new PropertyListView<Wine>("wines",wineList) {
            @Override
            public void populateItem(final ListItem<Wine> listItem){
                listItem.add(new Label("name"));
                listItem.add(new Label("ort"));
                listItem.add(new Label("type"));
                listItem.add(new Label("year"));
                listItem.add(new Label("abHofPrice"));
                listItem.add(new Label("bestellbar"));
                listItem.add(new Label("agingPrivate"));
            }
        }).setVersioned(false);
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
            add(new TextArea<String>("bestellbar").setType(Boolean.class));
        }

        @Override
        public final void onSubmit(){
            ValueMap values = getModelObject();
            //check if full
            if(StringUtils.isNotBlank((String)values.get("name"))){
                error("Wine must have a name!");
                return;
            }
        }
    }
}
