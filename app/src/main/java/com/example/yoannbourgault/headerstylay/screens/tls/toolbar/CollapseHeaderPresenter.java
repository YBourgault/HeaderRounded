package com.example.yoannbourgault.headerstylay.screens.tls.toolbar;

class CollapseHeaderPresenter implements CollapseHeaderContract.Presenter {

    @SuppressWarnings("FieldCanBeLocal")
    private final CollapseHeaderContract.View mView;

    CollapseHeaderPresenter(CollapseHeaderContract.View view) {
        mView = view;
    }
}
