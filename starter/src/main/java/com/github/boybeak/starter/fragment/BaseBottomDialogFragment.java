package com.github.boybeak.starter.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;

import com.github.boybeak.starter.ILife;

public class BaseBottomDialogFragment extends BottomSheetDialogFragment implements ILife {

    private boolean isAlive;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isAlive = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isAlive = false;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void setAlive(boolean b) {
        isAlive = b;
    }
}
