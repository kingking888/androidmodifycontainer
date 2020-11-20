package com.gc.home;

import com.gc.abs.BasePresenter;
import com.gc.abs.BaseView;
import com.gc.home.models.AppInfo;

import java.util.List;

/**
 * @author Lody
 * @version 1.0
 */
/*package*/ class ListAppContract {
    interface ListAppView extends BaseView<ListAppPresenter> {

        void startLoading();

        void loadFinish(List<AppInfo> infoList);
    }

    interface ListAppPresenter extends BasePresenter {

    }
}
