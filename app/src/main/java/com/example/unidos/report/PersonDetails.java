package com.example.unidos.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.unidos.Connection;
import com.example.unidos.Messages;
import com.example.unidos.R;
import com.example.unidos.databinding.FragmentShowPersonInfoBinding;
import com.example.unidos.repository.Description;
import com.example.unidos.repository.ReportedPerson;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PersonDetails extends Fragment {
    private PersonDetailsViewModel myvm;
    private ReportSharedViewModel sharedViewModel;
    private Connection connection;
    private ImageView ivPerson;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myvm = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PersonDetailsViewModel.class);
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        FragmentShowPersonInfoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_show_person_info, container, false);
        View view = binding.getRoot();
        binding.setLifecycleOwner(this);
        binding.setVmPersonDetails(myvm);

        ivPerson = view.findViewById(R.id.ivPerson);

        connection = new Connection(getContext());

        sharedViewModel = new ViewModelProvider(requireActivity()).get(ReportSharedViewModel.class);
        sharedViewModel.getPersonReceived().observe(getViewLifecycleOwner(), new Observer<ReportedPerson>() {
            @Override
            public void onChanged(ReportedPerson person) {
                myvm.setPerson(person);
                showPersonPhoto(person);
                if(person.getDescID()!= null) {
                    if (connection.isConnected()) {
                        myvm.showInfo();
                    } else
                        showToast(Messages.ERR_MSSG_00);
                }else {
                    myvm.setDescription(new Description());
                    myvm.setFieldValue();
                }
            }
        });

        myvm.getOpResult().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer result) {
                switch (result) {
                    case -1:
                        showToast(Messages.ERR_MSSG_01);
                        break;
                    case -2:
                        showToast(Messages.ERR_MSSG_02);
                }
            }
        });

        return view;
    }

    private void showPersonPhoto(ReportedPerson reportedPerson){
        if(reportedPerson.getPhotoPath() != null) {
            if (!reportedPerson.getPhotoPath().isEmpty()) {
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(reportedPerson.getPhotoPath());
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageReference)
                        .fitCenter()
                        .into(ivPerson);
            }
        }else
            ivPerson.setImageResource(R.drawable.image_not_available);
    }

    private void showToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
