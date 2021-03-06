// Generated by view binder compiler. Do not edit!
package com.example.trabajoindividual_1.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.trabajoindividual_1.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListviewPeliculasBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button buttonDescripcion;

  @NonNull
  public final Button buttonReview;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final RatingBar ratingBarListView;

  @NonNull
  public final TextView textViewTituloPeli;

  private ListviewPeliculasBinding(@NonNull ConstraintLayout rootView,
      @NonNull Button buttonDescripcion, @NonNull Button buttonReview, @NonNull ImageView imageView,
      @NonNull RatingBar ratingBarListView, @NonNull TextView textViewTituloPeli) {
    this.rootView = rootView;
    this.buttonDescripcion = buttonDescripcion;
    this.buttonReview = buttonReview;
    this.imageView = imageView;
    this.ratingBarListView = ratingBarListView;
    this.textViewTituloPeli = textViewTituloPeli;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListviewPeliculasBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListviewPeliculasBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.listview_peliculas, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListviewPeliculasBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.button_descripcion;
      Button buttonDescripcion = ViewBindings.findChildViewById(rootView, id);
      if (buttonDescripcion == null) {
        break missingId;
      }

      id = R.id.button_review;
      Button buttonReview = ViewBindings.findChildViewById(rootView, id);
      if (buttonReview == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.ratingBar_listView;
      RatingBar ratingBarListView = ViewBindings.findChildViewById(rootView, id);
      if (ratingBarListView == null) {
        break missingId;
      }

      id = R.id.textViewTituloPeli;
      TextView textViewTituloPeli = ViewBindings.findChildViewById(rootView, id);
      if (textViewTituloPeli == null) {
        break missingId;
      }

      return new ListviewPeliculasBinding((ConstraintLayout) rootView, buttonDescripcion,
          buttonReview, imageView, ratingBarListView, textViewTituloPeli);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
