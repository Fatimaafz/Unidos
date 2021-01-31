package com.example.unidos.report;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.unidos.ElementoObservable;
import com.example.unidos.repository.Description;
import com.example.unidos.repository.DescriptionRepository;
import com.example.unidos.repository.Field;
import com.example.unidos.repository.ReportedPerson;
import com.example.unidos.repository.ReportedPersonRepository;

import java.util.Observable;
import java.util.Observer;

public class PersonDetailsViewModel extends ViewModel {
    private ReportedPerson person;
    private Description description;
    private DescriptionRepository descriptionRepository;
    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> sex = new MutableLiveData<>();
    public MutableLiveData<String> age = new MutableLiveData<>();
    public MutableLiveData<String> nat = new MutableLiveData<>();
    public MutableLiveData<String> complexion = new MutableLiveData<>();
    public MutableLiveData<String> skin = new MutableLiveData<>();
    public MutableLiveData<String> lips = new MutableLiveData<>();
    public MutableLiveData<String> eyeColor = new MutableLiveData<>();
    public MutableLiveData<String> eyeShape = new MutableLiveData<>();
    public MutableLiveData<String> hairColor = new MutableLiveData<>();
    public MutableLiveData<String> hairShape = new MutableLiveData<>();
    public MutableLiveData<String> status = new MutableLiveData<>();
    public MutableLiveData<String> height = new MutableLiveData<>();
    public MutableLiveData<String> face = new MutableLiveData<>();
    public MutableLiveData<String> nose = new MutableLiveData<>();
    public MutableLiveData<String> features = new MutableLiveData<>();
    private MutableLiveData<Integer> opResult = new MutableLiveData<>();

    public PersonDetailsViewModel(){
        descriptionRepository = new DescriptionRepository();
    }

    public void setPerson(ReportedPerson person){
        this.person = person;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public MutableLiveData<Integer> getOpResult() {
        return opResult;
    }

    public void showInfo(){
        descriptionRepository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                if((boolean) ((ElementoObservable) o).getElemento()) {
                    description = descriptionRepository.getDescription();
                    setFieldValue();
                }else
                    opResult.setValue(-2);
            }
        });

        if(person.getDescID() != null)
            descriptionRepository.findDescription(person.getDescID());
        else
            setFieldValue();
    }

    public void setFieldValue(){
        Field field = new Field();

        name.setValue(person.getFullName());
        sex.setValue(field.intToSex(person.getSex()));
        age.setValue(person.getAge());
        nat.setValue(person.getNat());
        status.setValue(field.boolToStatus(person.isFound()));

        complexion.setValue(field.compToString(description.getComp()));
        skin.setValue(field.skinToString(description.getSkin()));
        face.setValue(field.faceToString(description.getFace()));
        lips.setValue(field.lipsToString(description.getLips()));
        nose.setValue(field.noseToString(description.getNose()));
        eyeColor.setValue(field.eyeColorToString(description.getEyeColor()));
        eyeShape.setValue(field.eyeShapeToString(description.getEyeShape()));
        hairColor.setValue(field.hairColorToString(description.getHairColor()));
        hairShape.setValue(field.hairShapeToString(description.getHairShape()));
        features.setValue(description.getFeatures());

        if(!(description.getHeight() == (float) 0))
            height.setValue(String.valueOf(description.getHeight()));
    }


    public void getUpdatedPersonInfo(){
        ReportedPersonRepository repository = new ReportedPersonRepository();
        repository.getOpResult().addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                switch ((int) ((ElementoObservable) o).getElemento()){
                    case 1:
                        setFieldValue();
                        break;
                    case -1:
                        opResult.setValue(-1);
                        break;
                    case -2:
                        opResult.setValue(-2);
                        break;
                }
            }
        });
    }
}
