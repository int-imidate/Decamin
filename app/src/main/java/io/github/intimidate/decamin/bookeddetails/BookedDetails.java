package io.github.intimidate.decamin.bookeddetails;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import io.github.intimidate.decamin.BookRideActivity;
import io.github.intimidate.decamin.R;
import io.github.intimidate.decamin.qrcode.QrActivity;
import io.github.intimidate.decamin.remote.ApiManager;
import io.github.intimidate.decamin.remote.BookingBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookedDetails extends BottomSheetDialogFragment {

    TextView driverEmail,NumberOfSeats,CurrentStatus,Address;
    String driveremail,noOfSeats,address;
    int status;
    LottieAnimationView lottieAnimationView;
    BookRideActivity bookRideActivity;
    Button cancel;
    public BookedDetails(BookRideActivity bookRideActivity) {
        this.bookRideActivity=bookRideActivity;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_booked_details, container, false);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("token", -1);
        driveremail=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("driverEmail", null);
        int x=PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("noOfSeats",0);
        noOfSeats=String.valueOf(x);
        status=PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("status",0);
        address=PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("destination",null);
        driverEmail=v.findViewById(R.id.driverEmail);
        driverEmail.setText(driveremail);
        Address=v.findViewById(R.id.address);
        Address.setText(address);
        lottieAnimationView=v.findViewById(R.id.comingAnim);

        if (status==1){
            lottieAnimationView.playAnimation();
        }

        cancel=v.findViewById(R.id.cancelRide);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x= PreferenceManager.getDefaultSharedPreferences(bookRideActivity).getInt("bookingStatus",1);
                int y=PreferenceManager.getDefaultSharedPreferences(bookRideActivity).getInt("bookingId",1);
                int z=PreferenceManager.getDefaultSharedPreferences(bookRideActivity).getInt("token",-1);
                if(x==1){
                    x=2;
                }
                else{
                    x=3;
                }
                Log.d("TAGQ",String.valueOf(x+y+z));
                Call<BookingBody> call = ApiManager.api.updateBook(z,"f",4,y);
                int finalX = x;
                call.enqueue(new Callback<BookingBody>() {
                    @Override
                    public void onResponse(Call<BookingBody> call, Response<BookingBody> response) {
                        Log.d("TAGQ","Started ride");
                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt("bookingStatus", 4).apply();
                    }

                    @Override
                    public void onFailure(Call<BookingBody> call, Throwable t) {

                    }
                });                PreferenceManager
                        .getDefaultSharedPreferences(bookRideActivity)
                        .edit()
                        .putInt("isBooked",0)
                        .apply();
                bookRideActivity.changeBaseButtons(0);
                bookRideActivity.close_dialog();
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
        return ((3* displayMetrics.heightPixels) / 4);
    }

}
