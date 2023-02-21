package com.example.djshichaoren.googleocrtest2.util.dark;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class BaseLiveData<T> extends MutableLiveData<T> {
    public BaseLiveData() {
    }

    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        super.observe(owner, observer);
    }

    public void observeForever(@NonNull Observer<? super T> observer) {
        super.observeForever(observer);
    }

    public void removeObserver(@NonNull Observer<? super T> observer) {
        super.removeObserver(observer);
    }

    public void removeObservers(@NonNull LifecycleOwner owner) {
        super.removeObservers(owner);
    }

    public void postValue(T value) {
        if (value != null && value instanceof IValue) {
            ((IValue)value).setLiveData(this);
        }

        super.postValue(value);
    }

    public void setValue(T value) {
        if (value != null && value instanceof IValue) {
            ((IValue)value).setLiveData(this);
        }

        super.setValue(value);
    }

    @Nullable
    public T getValue() {
        return super.getValue();
    }

    public boolean hasObservers() {
        return super.hasObservers();
    }

    public boolean hasActiveObservers() {
        return super.hasActiveObservers();
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }
}
