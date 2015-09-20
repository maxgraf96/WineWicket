package com.graf.wicket.wine;

import org.apache.wicket.Page;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.table.*;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

/**
 * Created by Max on 17.09.2015.
 */
public class MyDataTable<T,S> extends DefaultDataTable<T,S> {
    private static final long serialVersionUID = 1L;
    private ModalWindow ww = WineHome.wineWindow;
    private PageReference pr = WineHome.pageRef;

    public MyDataTable(String id, List<? extends IColumn<T, S>> columns, ISortableDataProvider<T, S> dataProvider, int rowsPerPage) {
        super(id, columns, dataProvider, rowsPerPage);
    }

    @Override
    protected Item<T> newRowItem(String id, int index, final IModel<T> model) {

        final Item<T> rowItem = new Item<T>(id, index, model);
        rowItem.add(new AjaxEventBehavior("onclick") {
            @Override
            protected void onEvent(AjaxRequestTarget target) {
                //Get wine
                final Wine wine = WineHome.wineList.get(WineHome.wineList.indexOf(rowItem.getModelObject()));
                ww.setPageCreator(new ModalWindow.PageCreator() {
                    @Override
                    public Page createPage() {
                        return new WinePage(pr,wine);
                    }
                });
                ww.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                    @Override
                    public void onClose(AjaxRequestTarget ajaxRequestTarget) {
                        ajaxRequestTarget.add(WineHome.weinkeller.remove("wineTable")
                                .add(new MyDataTable("wineTable", WineHome.columns, new WineProvider(), 10)));
                        setResponsePage(WineHome.class);
                    }
                });
                ww.setTitle(wine.getName());
                ww.show(target);
            }

        });
        return rowItem;
    }
}
