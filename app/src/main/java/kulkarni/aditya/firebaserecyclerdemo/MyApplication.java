package kulkarni.aditya.firebaserecyclerdemo;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by adicool on 13/11/17.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
