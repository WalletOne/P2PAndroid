package com.aronskiy_anton.p2pui;

/**
 * Created by aaronskiy on 07.09.2017.
 */

public interface BaseView<T extends BasePresenter> {
    void setPresenter(T presenter);
}