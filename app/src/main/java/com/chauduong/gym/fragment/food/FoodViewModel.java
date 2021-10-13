package com.chauduong.gym.fragment.food;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class FoodViewModel  extends ViewModel {
    private MutableLiveData<String> stringMutableLiveData= new MutableLiveData<>();

    public MutableLiveData<String> getStringMutableLiveData() {
        return stringMutableLiveData;
    }
    public void gen(){
        stringMutableLiveData.setValue("abc"+ new Random().nextInt());
    }
}
