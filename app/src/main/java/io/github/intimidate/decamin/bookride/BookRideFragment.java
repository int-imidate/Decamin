package io.github.intimidate.decamin.bookride;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.aigestudio.wheelpicker.WheelPicker;
import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import io.github.intimidate.decamin.BookRideActivity;
import io.github.intimidate.decamin.BookRideImpl;
import io.github.intimidate.decamin.R;
import io.github.intimidate.decamin.User;
import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.BookingBody;
import io.github.intimidate.decamin.remote.DriverBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookRideFragment extends BottomSheetDialogFragment {
    TextView women;
    LottieAnimationView toggle;
    int flag = 0;
    TextView to, from;
    String address, fromlocation;
    CarouselPicker carouselPicker;
    Boolean WomenDriver = false;
    Button confirm;
    Boolean isBookedNow = false;
    WheelPicker wheelPicker;
    LottieAnimationView bookingConfirmed;
    ScrollView bookingLayout;
    BookRideImpl bookRide;
    LatLng location,destination;
    int numberOfseats;
    int token;
    BookRideActivity bookRideActivity;

    public BookRideFragment(LatLng location, String address, Boolean now, BookRideImpl bookRide,LatLng destination,int token,BookRideActivity bookRideActivity) {
        // Required empty public constructor
        this.bookRide = bookRide;
        this.address = address;
        this.fromlocation = location.toString();
        this.isBookedNow = now;
        this.location=location;
        this.destination=destination;
        this.token=token;
        this.bookRideActivity=bookRideActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_book_ride, container, false);
        bookingConfirmed = v.findViewById(R.id.confirmAnim);
        View bottomSheet = v.findViewById(R.id.bottom_sheet);

        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        if (isBookedNow) {
        }
        women = v.findViewById(R.id.women);
        toggle = v.findViewById(R.id.toggleAnim);
        to = v.findViewById(R.id.toLocation);
        from = v.findViewById(R.id.fromLocation);
        bookingLayout = v.findViewById(R.id.bookinglayout);
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
                    WomenDriver = true;

                    //---- Your code here------
                } else {
                    toggle.setMinAndMaxProgress(0.5f, 1f);
                    toggle.playAnimation();
                    flag = 0;
                    WomenDriver = false;
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

                numberOfseats=position+1;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        confirm = v.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                requestBook();
            }
        });
        return v;
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
        bottomSheet.setBackground(null);
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

    private void collapseLayout(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();


        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return ((3 * displayMetrics.heightPixels) / 4);
    }

    private void bookDriver() {


    }
    private void requestBook(){
        String time =Calendar.getInstance().getTime().toString();
        bookingConfirmed.playAnimation();
       // bookRideActivity.close_dialog();
        /*Call<BookingBody> call = ApiManager.api.bookDriver(token, User.email,location.latitude,location.longitude,destination.latitude,destination.longitude,numberOfseats,time);
        call.enqueue(new Callback<BookingBody>() {
            @Override
            public void onResponse(Call<BookingBody> call, Response<BookingBody> response) {
                bookingLayout.setVisibility(View.GONE);
                bookingConfirmed.playAnimation();
                if(response.body().getStatus()==1){
                    bookingLayout.setVisibility(View.GONE);
                    bookingConfirmed.playAnimation();
                }
            }

            @Override
            public void onFailure(Call<BookingBody> call, Throwable t) {
                Log.d("TAGi", call.toString());
                t.printStackTrace();
            }
        });*/
    }
}
