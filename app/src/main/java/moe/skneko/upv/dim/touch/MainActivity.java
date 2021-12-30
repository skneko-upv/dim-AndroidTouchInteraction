package moe.skneko.upv.dim.touch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToEx1(View view) {
        startActivity(new Intent(this, Ex1Activity.class));
    }

    public void goToEx2(View view) {
        startActivity(new Intent(this, Ex2Activity.class));
    }

    public void goToEx3(View view) {
        startActivity(new Intent(this, Ex3Activity.class));
    }

    public void goToEx4(View view) {
        startActivity(new Intent(this, Ex4Activity.class));
    }

    public void goToEx5(View view) {
        startActivity(new Intent(this, Ex5Activity.class));
    }
}