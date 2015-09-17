package com.graf.wicket.wine;

import org.apache.wicket.extensions.markup.html.repeater.data.sort.SortOrder;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Max on 09.09.2015.
 */
public class WineProvider extends SortableDataProvider {

    private List<Wine> list = new ArrayList<Wine>();
    private SortableDataProviderComparator comparator = new SortableDataProviderComparator();

    public WineProvider() {
        // The default sorting
        setSort("name", SortOrder.ASCENDING);

        list = WineHome.wineList;
    }

    public Iterator<Wine> iterator(final long first, final long count) {

        // Get the data
        List<Wine> newList = new ArrayList<Wine>(list);

        // Sort the data
        Collections.sort(newList, comparator);

        // Return the data for the current page - this can be determined only after sorting
        return newList.subList((int)first, (int)first + (int)count).iterator();
    }

    public IModel<Wine> model(final Object object) {
        return new AbstractReadOnlyModel<Wine>() {
            @Override
            public Wine getObject() {
                return (Wine) object;
            }
        };
    }

    public long size() {
        return list.size();
    }

    class SortableDataProviderComparator implements Comparator<Wine>, Serializable {
        public int compare(final Wine o1, final Wine o2) {
            PropertyModel<Comparable> model1 = new PropertyModel<Comparable>(o1,getSort().getProperty().toString());
            PropertyModel<Comparable> model2 = new PropertyModel<Comparable>(o2, getSort().getProperty().toString());

            int result = model1.getObject().compareTo(model2.getObject());

            if (!getSort().isAscending()) {
                result = -result;
            }

            return result;
        }

    }

}
