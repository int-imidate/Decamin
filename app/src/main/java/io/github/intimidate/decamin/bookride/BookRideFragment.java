package io.github.intimidate.decamin.bookride;


import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import io.github.intimidate.decamin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookRideFragment extends BottomSheetDialogFragment {
    TextView women;
    LottieAnimationView toggle;
    int flag = 0;
    TextView to,from;
    String address,fromlocation;
    CarouselPicker carouselPicker;
    Boolean WomenDriver=false;
    Button confirm;
    Boolean isBookedNow=false;
    WheelPicker wheelPicker;
    LottieAnimationView bookingConfirmed;
    ScrollView bookingLayout;

    public BookRideFragment(String from,String address,Boolean now) {
        // Required empty public constructor
        this.address=address;
        this.fromlocation=from;
        this.isBookedNow=now;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_ride, container, false);
        wheelPicker=v.findViewById(R.id.wheelpicker);
        bookingConfirmed=v.findViewById(R.id.confirmAnim);
        if(isBookedNow){
            wheelPicker.setVisibility(View.GONE);
        }
        women = v.findViewById(R.id.women);
        toggle = v.findViewById(R.id.toggleAnim);
        to=v.findViewById(R.id.toLocation);
        from=v.findViewById(R.id.fromLocation);
        bookingLayout=v.findViewById(R.id.bookinglayout);
        to.setText(address);
        from.setText(fromlocation);
        float speed = 2.5f;
        CarouselPicker.CarouselViewAdapter textAdapter;
        women.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag == 0) {
                    toggle.setMinAndMaxProgress(0f, 0.43f); //Here, calculation is done on the basis of start and stop frame divided by the total number of frames
                    toggle.playAnimation();
                    flag = 1;
                    WomenDriver=true;

                    //---- Your code here------
                } else {
                    toggle.setMinAndMaxProgress(0.5f, 1f);
                    toggle.playAnimation();
                    flag = 0;
                    WomenDriver=false;
                    setWheelPickerData();
                    //---- Your code here------
                }

            }
        });

       carouselPicker = (CarouselPicker) v.findViewById(R.id.carousel);
        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
//20 here represents the textSize in dp, change it to the value you want.
        textItems.add(new CarouselPicker.TextItem("1", 24));
        textItems.add(new CarouselPicker.TextItem("2", 24));
        textItems.add(new CarouselPicker.TextItem("3", 24));
        textItems.add(new CarouselPicker.TextItem("4", 24));

         textAdapter = new CarouselPicker.CarouselViewAdapter(getActivity(), textItems, 0);
        textAdapter.setTextColor(Color.WHITE);

        carouselPicker.setAdapter(textAdapter);

        carouselPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("carousel",String.valueOf(position));


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        confirm=v.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookDriver();
            }
        });
        return v;
    }
    private void setWheelPickerData(){
        Calendar rightNow = Calendar.getInstance();
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        List<Integer> data = new ArrayList<>();
        for(int i=hour+1;i<=hour+7;i++){
            data.add(i);

        }
        wheelPicker.setData(data);


    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);

            }
        });
        return dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
    }
    private void collapseLayout(BottomSheetDialog bottomSheetDialog){
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();


                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return ((3*displayMetrics.heightPixels)/4);
    }
    private void bookDriver(){
        bookingLayout.setVisibility(View.GONE);
        bookingConfirmed.playAnimation();

    }
}
