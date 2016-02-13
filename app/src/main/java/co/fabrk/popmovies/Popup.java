package co.fabrk.popmovies;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import co.fabrk.popmovies.transitions.FabDialogMorphSetup;
import co.fabrk.popmovies.R;


public class Popup extends Activity {

    boolean isDismissing = false;
    private ViewGroup container;
    private TextView message;
    private Button login;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        container = (ViewGroup) findViewById(R.id.container);
        FabDialogMorphSetup.setupSharedElementTransitions(this, container,
                getResources().getDimensionPixelSize(R.dimen.dialog_corners));

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        checkAuthCallback(intent);
    }

    public void doLogin(View view) {
        showLoading();
//        dribbblePrefs.login(DribbbleLogin.this);
    }

    public void dismiss(View view) {
        isDismissing = true;
        setResult(Activity.RESULT_CANCELED);
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    private void showLoading() {
        TransitionManager.beginDelayedTransition(container);
        message.setVisibility(View.GONE);
        login.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    private void showLogin() {
        TransitionManager.beginDelayedTransition(container);
        message.setVisibility(View.VISIBLE);
        login.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }


    private void forceSharedElementLayout() {
        int widthSpec = View.MeasureSpec.makeMeasureSpec(container.getWidth(),
                View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(container.getHeight(),
                View.MeasureSpec.EXACTLY);
        container.measure(widthSpec, heightSpec);
        container.layout(container.getLeft(), container.getTop(), container.getRight(), container
                .getBottom());
    }

    private SharedElementCallback sharedElementEnterCallback = new SharedElementCallback() {
        @Override
        public View onCreateSnapshotView(Context context, Parcelable snapshot) {
            // grab the saved fab snapshot and pass it to the below via a View
            View view = new View(context);
            final Bitmap snapshotBitmap = getSnapshot(snapshot);
            if (snapshotBitmap != null) {
                view.setBackground(new BitmapDrawable(context.getResources(), snapshotBitmap));
            }
            return view;
        }

        @Override
        public void onSharedElementStart(List<String> sharedElementNames,
                                         List<View> sharedElements,
                                         List<View> sharedElementSnapshots) {
            // grab the fab snapshot and fade it out/in (depending on if we are entering or exiting)
            for (int i = 0; i < sharedElements.size(); i++) {
                if (sharedElements.get(i) == container) {
                    View snapshot = sharedElementSnapshots.get(i);
                    BitmapDrawable fabSnapshot = (BitmapDrawable) snapshot.getBackground();
                    fabSnapshot.setBounds(0, 0, snapshot.getWidth(), snapshot.getHeight());
                    container.getOverlay().clear();
                    container.getOverlay().add(fabSnapshot);
                    if (!isDismissing) {
                        // fab -> login: fade out the fab snapshot
                        ObjectAnimator.ofInt(fabSnapshot, "alpha", 0).setDuration(100).start();
                    } else {
                        // login -> fab: fade in the fab snapshot toward the end of the transition
                        fabSnapshot.setAlpha(0);
                        ObjectAnimator fadeIn = ObjectAnimator.ofInt(fabSnapshot, "alpha", 255)
                                .setDuration(150);
                        fadeIn.setStartDelay(150);
                        fadeIn.start();
                    }
                    forceSharedElementLayout();
                    break;
                }
            }
        }

        private Bitmap getSnapshot(Parcelable parcel) {
            if (parcel instanceof Bitmap) {
                return (Bitmap) parcel;
            } else if (parcel instanceof Bundle) {
                Bundle bundle = (Bundle) parcel;
                // see SharedElementCallback#onCaptureSharedElementSnapshot
                return (Bitmap) bundle.getParcelable("sharedElement:snapshot:bitmap");
            }
            return null;
        }
    };


}
