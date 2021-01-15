package com.tecmanic.gogrocer;

import android.view.View;

public interface Categorygridquantity {
  void  onClick(View view, int position, String ccId, String id);
  void  onCartItemAddOrMinus();
  void onProductDetials(int position);
}
