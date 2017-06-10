package com.gonext.live.gps.navigation.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.gonext.live.gps.navigation.R;
import com.pnikosis.materialishprogress.ProgressWheel;


/**
 * Created by Lenovo on 17-04-2017.
 */

public class PopUtils {
    private static Dialog mDialog;

    /**
     * Method to show alert dialog with two/single button
     *
     * @param context               context
     * @param message               Message of dialog
     * @param posButtonName         Name of positive button
     * @param nagButtonName         Name of negative button
     * @param onPositivButtonClick  call back method of positive button
     * @param onNagativeButtonClick call back method of negative butoon
     */
    public static void showCustomTwoButtonAlertDialog(final Context context, String title, String message, String posButtonName,
                                                      String nagButtonName, boolean changeButtonColor, boolean isOutSideCancelable,
                                                      final DialogInterface.OnClickListener onPositivButtonClick,
                                                      final DialogInterface.OnClickListener onNagativeButtonClick) {
        try {
            if (context != null) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context, R.style.DialogTheme);
                }
                builder.setMessage(message);
                if (TextUtils.isEmpty(title)) {
                    builder.setTitle(context.getResources().getString(R.string.app_name));
                } else {
                    builder.setTitle(title);
                }

                if (onPositivButtonClick != null) {
                    builder.setPositiveButton(posButtonName, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            onPositivButtonClick.onClick(dialog, id);
                        }
                    });
                }
                if (onNagativeButtonClick != null) {
                    builder.setNegativeButton(nagButtonName, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int id) {
                            onNagativeButtonClick.onClick(dialog, id);
                        }
                    });
                }

                builder.setCancelable(true);
                final AlertDialog alert = builder.create();
                alert.requestWindowFeature(Window.FEATURE_NO_TITLE);

                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor((context.getResources().getColor(R.color.colorPrimary)));
                    }
                });

                if (changeButtonColor) {

                    try {
                        alert.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor((context.getResources().getColor(R.color.colorPrimary)));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                alert.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor((context.getResources().getColor(R.color.colorPrimary)));
                    }
                });
                if (isOutSideCancelable) {
                    alert.setCanceledOnTouchOutside(true);
                } else {
                    alert.setCanceledOnTouchOutside(false);
                }
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayNetoworkDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_info);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * Method to show alertDialog with items array
     *
     * @param context            context
     * @param items              Array of data which are display in dialog
     * @param title              title of the alert dialog
     * @param onItemClickListner call back method of selected item
     */
    public static void showCustomAlertDialogWithItems(Context context, CharSequence[] items,
                                                      final String title,
                                                      final DialogInterface.OnClickListener onItemClickListner) {
        try {
            if (context != null) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle(title);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        onItemClickListner.onClick(dialog, item);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void showProgressDialog(final Context context) {
        try {
            hideProgressDialog();
            mDialog = new Dialog(context, R.style.AlertDialogCustom);
            mDialog.setContentView(R.layout.dialog_progress);
            ProgressWheel progressBar = (ProgressWheel) mDialog.findViewById(R.id.progressBar);
            progressBar.setBarColor(context.getResources().getColor(R.color.colorPrimary));
            // progressBar.getIndeterminateDrawable().setColorFilter(0xFF50d2c2, android.graphics.PorterDuff.Mode.MULTIPLY);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnKeyListener(new Dialog.OnKeyListener() {

                @Override
                public boolean onKey(DialogInterface arg0, int keyCode,
                                     KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mDialog.dismiss();
                    }
                    return true;
                }
            });
            mDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void hideProgressDialog() {
        try {
            if (mDialog != null && mDialog.isShowing())
                mDialog.dismiss();
            mDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
