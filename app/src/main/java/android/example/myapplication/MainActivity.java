package android.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Async_Res async_res;
    private Async_Log async_log;
    private Button btn_toLogin;
    private Button btn_Register;
    private Button btn_toRegister;
    private Button btn_Login;
    private Button btn_Logout;
    private TextView tex_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        //setContentView(R.layout.layout_login);
        //setContentView(R.layout.activity_main);
        preferences = getSharedPreferences("LoginMessage", Context.MODE_PRIVATE);
        editor = preferences.edit();
        btn_toLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        btn_toLogin.setOnClickListener(lsn_toLogin);
        btn_Register = (Button) findViewById(R.id.btnRegister);
        btn_Register.setOnClickListener(lsn_Register);
        //editor.clear();
        //editor.commit();
    }

    private class Async_Res extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... texts) {
            String name = texts[0];
            String password = texts[1];
            String email = texts[2];

            if (!preferences.getString(email, "").equals("")) {
                return null;//username exist
            } else {
                editor.putString(email, password);
                editor.putString(email + "_name", name);
                editor.commit();
                return name;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                LayoutInflater layoutInflater = LayoutInflater.from(btn_Register.getContext());
                View v_login = layoutInflater.inflate(R.layout.layout_login, null);
                setContentView(v_login);
                btn_toRegister = (Button) v_login.findViewById(R.id.btnLinkToRegisterScreen);
                btn_Login = (Button) v_login.findViewById(R.id.btnLogin);
                btn_toRegister.setOnClickListener(lsn_toRegister);
                btn_Login.setOnClickListener(lsn_Login);

            } else {
                Toast.makeText(getApplicationContext(), "email is signed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class Async_Log extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... texts) {
            String password = texts[0];
            String email = texts[1];
            String password_s = preferences.getString(email, "");
            if (password_s.equals("")) {
                return null;//not registered
            } else {
                if(password_s.equals(password)) {
                    String name = preferences.getString(email+"_name","unknown");
                    return name;
                }else {
                    return "";
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                if(!result.equals("")) {
                    LayoutInflater layoutInflater = LayoutInflater.from(btn_Login.getContext());
                    View v_main = layoutInflater.inflate(R.layout.activity_main, null);
                    setContentView(v_main);
                    String show_text = "Welcome!\n"+result;
                    tex_welcome = (TextView) v_main.findViewById(R.id.user_name);
                    btn_Logout = (Button) v_main.findViewById(R.id.btnLogout);
                    tex_welcome.setText(show_text);
                    btn_Logout.setOnClickListener(lsn_Logout);
                }else {
                    Toast.makeText(getApplicationContext(), "password is wrong!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "email not signed.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public View.OnClickListener lsn_Register = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String texts[] = new String[3];
            EditText text_reg_name = (EditText) findViewById(R.id.name);
            EditText text_reg_password = (EditText) findViewById(R.id.password);
            EditText text_reg_email = (EditText) findViewById(R.id.email);
            texts[0] = text_reg_name.getText().toString();
            texts[1] = text_reg_password.getText().toString();
            texts[2] = text_reg_email.getText().toString();
            if (texts[0].equals("") || texts[1].equals("") || texts[2].equals("")) {
                Toast.makeText(getApplicationContext(), "Input can't be null!", Toast.LENGTH_SHORT).show();
            } else {
                async_res = new Async_Res();
                async_res.execute(texts);
            }
        }
    };

    public View.OnClickListener lsn_toLogin = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
            View v_login = layoutInflater.inflate(R.layout.layout_login, null);
            setContentView(v_login);
            btn_toRegister = (Button) v_login.findViewById(R.id.btnLinkToRegisterScreen);
            btn_Login = (Button) v_login.findViewById(R.id.btnLogin);
            btn_toRegister.setOnClickListener(lsn_toRegister);
            btn_Login.setOnClickListener(lsn_Login);
        }
    };

    public View.OnClickListener lsn_toRegister = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
            View v_register = layoutInflater.inflate(R.layout.layout_register, null);
            setContentView(v_register);
            btn_toLogin = (Button) v_register.findViewById(R.id.btnLinkToLoginScreen);
            btn_Register = (Button) v_register.findViewById(R.id.btnRegister);
            btn_toLogin.setOnClickListener(lsn_toLogin);
            btn_Register.setOnClickListener(lsn_Register);
        }
    };
    public View.OnClickListener lsn_Login = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String texts[] = new String[2];
            EditText text_reg_password = (EditText) findViewById(R.id.password);
            EditText text_reg_email = (EditText) findViewById(R.id.email);
            texts[0] = text_reg_password.getText().toString();
            texts[1] = text_reg_email.getText().toString();
            if (texts[0].equals("") || texts[1].equals("")) {
                Toast.makeText(getApplicationContext(), "Input can't be null!", Toast.LENGTH_SHORT).show();
            } else {
                async_log = new Async_Log();
                async_log.execute(texts);
            }
        }
    };
    public View.OnClickListener lsn_Logout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LayoutInflater layoutInflater = LayoutInflater.from(v.getContext());
            View v_login = layoutInflater.inflate(R.layout.layout_login, null);
            setContentView(v_login);
            btn_toRegister = (Button) v_login.findViewById(R.id.btnLinkToRegisterScreen);
            btn_Login = (Button) v_login.findViewById(R.id.btnLogin);
            btn_toRegister.setOnClickListener(lsn_toRegister);
            btn_Login.setOnClickListener(lsn_Login);
        }
    };
}