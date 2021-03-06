// Generated by view binder compiler. Do not edit!
package com.example.trabajoindividual_1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.trabajoindividual_1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button buttonLogin;

  @NonNull
  public final Button buttonLogup;

  @NonNull
  public final EditText editTextContrasena;

  @NonNull
  public final EditText editTextNombreUsuario;

  @NonNull
  public final TextView textView2;

  @NonNull
  public final Toolbar toolbar;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button buttonLogin,
      @NonNull Button buttonLogup, @NonNull EditText editTextContrasena,
      @NonNull EditText editTextNombreUsuario, @NonNull TextView textView2,
      @NonNull Toolbar toolbar) {
    this.rootView = rootView;
    this.buttonLogin = buttonLogin;
    this.buttonLogup = buttonLogup;
    this.editTextContrasena = editTextContrasena;
    this.editTextNombreUsuario = editTextNombreUsuario;
    this.textView2 = textView2;
    this.toolbar = toolbar;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button_login;
      Button buttonLogin = ViewBindings.findChildViewById(rootView, id);
      if (buttonLogin == null) {
        break missingId;
      }

      id = R.id.button_logup;
      Button buttonLogup = ViewBindings.findChildViewById(rootView, id);
      if (buttonLogup == null) {
        break missingId;
      }

      id = R.id.editText_contrasena;
      EditText editTextContrasena = ViewBindings.findChildViewById(rootView, id);
      if (editTextContrasena == null) {
        break missingId;
      }

      id = R.id.editText_nombreUsuario;
      EditText editTextNombreUsuario = ViewBindings.findChildViewById(rootView, id);
      if (editTextNombreUsuario == null) {
        break missingId;
      }

      id = R.id.textView2;
      TextView textView2 = ViewBindings.findChildViewById(rootView, id);
      if (textView2 == null) {
        break missingId;
      }

      id = R.id.toolbar;
      Toolbar toolbar = ViewBindings.findChildViewById(rootView, id);
      if (toolbar == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, buttonLogin, buttonLogup,
          editTextContrasena, editTextNombreUsuario, textView2, toolbar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
