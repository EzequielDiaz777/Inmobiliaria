package com.ezediaz.inmobiliaria.ui.inquilino;

import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.ezediaz.inmobiliaria.R;
import com.ezediaz.inmobiliaria.model.Inquilino;

public class InquilinoFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<Inquilino> mInquilino;

    public InquilinoFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<Inquilino> getMInquilino() {
        if (mInquilino == null) {
            mInquilino = new MutableLiveData<>();
        }
        return mInquilino;
    }

    public void cargarInquilino(Bundle arguments) {
        Inquilino inquilino = (Inquilino) arguments.getSerializable("inquilino");
        mInquilino.setValue(inquilino);
    }
}

