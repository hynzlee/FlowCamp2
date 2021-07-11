package com.example.flowcamp2;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestFragment extends Fragment  {

    private View view;
    private Context context;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://192.249.18.163:80";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        context = container.getContext();

        return view;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.fragment_tab1);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        getActivity().findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoginDialog();
            }
        });
        getActivity().findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignupDialog();
            }
        });
        getActivity().findViewById(R.id.todoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleToDoDiaLog();
            }
        });
    }

    private void handleToDoDiaLog(){
        View view = getLayoutInflater().inflate(R.layout.tododialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view).show();

    }

    private void handleLoginDialog() {
        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view).show();
        Button loginBtn = view.findViewById(R.id.login);
        final EditText emailEdit = view.findViewById(R.id.loginIdEdit);
        final EditText nameEdit = view.findViewById(R.id.loginNameEdit);
        final EditText numberEdit = view.findViewById(R.id.loginNumberEdit);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터를 보낼 해쉬 맵 생성
                HashMap<String, String> map = new HashMap<>();
                map.put("_id", emailEdit.getText().toString());
                map.put("Name", nameEdit.getText().toString());
                map.put("Number", numberEdit.getText().toString());
                Call<LoginResult> call = retrofitInterface.executeLogin(map);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        //call 다시 오는 내용값 send()에 들어가는 , response status() 안에 들어가는 값
                        if (response.code() == 200) {
                            LoginResult result = response.body();
                            Toast.makeText(context, "로그인 성공",
                                    Toast.LENGTH_LONG).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(context, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void handleSignupDialog() {
        View view = getLayoutInflater().inflate(R.layout.signup_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view).show();
        Button signupBtn = view.findViewById(R.id.signup);
        final EditText idEdit = view.findViewById(R.id.idEdit);
        final EditText nameEdit = view.findViewById(R.id.nameEdit);
        final EditText numberEdit = view.findViewById(R.id.numberEdit);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, String> map = new HashMap<>();
                map.put("_id", idEdit.getText().toString());
                map.put("Name", nameEdit.getText().toString());
                map.put("Number", numberEdit.getText().toString());
                Call<Void> call = retrofitInterface.executeSignup(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(context,
                                    "Signed up successfully", Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(context,
                                    "Already registered", Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
