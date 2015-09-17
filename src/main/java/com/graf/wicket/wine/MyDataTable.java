package com.graf.wicket.wine;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

import java.util.List;

/**
 * Created by Max on 17.09.2015.
 */
public class MyDataTable<T,S> extends DefaultDataTable<T,S> {
    private static final long serialVersionUID = 1L;

    public MyDataTable(String id, List<? extends IColumn<T, S>> columns, ISortableDataProvider<T, S> dataProvider, int rowsPerPage) {
        super(id, columns, dataProvider, rowsPerPage);
        this.addTopToolbar(new NavigationToolbar(this));
        this.addTopToolbar(new HeadersToolbar(this, dataProvider));
        this.addBottomToolbar(new NoRecordsToolbar(this));
    }

    @Override
    protected Item<T> newRowItem(String id, int index, final IModel<T> model){
        Item<T> rowItem = new Item<T>(id,index,model);
        rowItem.add(new AjaxEventBehavior("onclick") {
            private static final long serialVersionUID = 6720512493017210281L;
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                //My stuff

            }
        });
        return rowItem;
    }
}
