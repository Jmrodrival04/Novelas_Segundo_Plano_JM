package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class NovelViewModel extends ViewModel {

    private MutableLiveData<List<Novel>> novels;

    public NovelViewModel() {
        novels = new MutableLiveData<>();
        loadNovels();  // Cargar novelas de ejemplo
    }

    public LiveData<List<Novel>> getAllNovels() {
        return novels;
    }

    private void loadNovels() {
        // Agrega datos de ejemplo
        List<Novel> novelList = new ArrayList<>();
        novelList.add(new Novel("1984", "George Orwell"));
        novelList.add(new Novel("To Kill a Mockingbird", "Harper Lee"));
        novels.setValue(novelList);
    }
}
