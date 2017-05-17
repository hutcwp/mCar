package com.hut.cwp.mcar.activitys.business.view;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hut.cwp.mcar.R;

import me.shaohui.bottomdialog.BaseBottomDialog;


/**
 * BaseBottomDialog BottomDialog
 * 是一个通过 DialogFragment
 * 实现的底部弹窗布局，并且支持弹出动画，支持任意布局
 */
public class LsnumDialog extends BaseBottomDialog {

    private Context mContext;

    private KeyboardView keyboardView;
    private Keyboard k1;// 省份简称键盘
    private Keyboard k2;// 数字、字母键盘
    private EditText[] edits = new EditText[7];
    private Button confirm, cancel;

    private ICompleted mCompletedListener;

    private int currentEditText = 0;//默认当前光标在第一个EditText

    private static String[] provinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
            , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
            , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
            , "青", "琼", "新", "港", "澳", "台", "宁"};

    private static String[] letterAndDigit = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
            , "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
            , "A", "S", "D", "F", "G", "H", "J", "K", "L"
            , "Z", "X", "C", "V", "B", "N", "M"};

    public LsnumDialog(Context context) {
        super();
        this.mContext = context;
        k1 = new Keyboard(mContext, R.xml.province_short_keyboard);
        k2 = new Keyboard(mContext, R.xml.lettersanddigit_keyboard);
    }


    @Override
    public int getLayoutRes() {
        return R.layout.z_layout_lsnum_dialog;
    }

    @Override
    public void bindView(View v) {

        edits[0] = (EditText) v.findViewById(R.id.et_car_license_inputbox1);
        edits[1] = (EditText) v.findViewById(R.id.et_car_license_inputbox2);
        edits[2] = (EditText) v.findViewById(R.id.et_car_license_inputbox3);
        edits[3] = (EditText) v.findViewById(R.id.et_car_license_inputbox4);
        edits[4] = (EditText) v.findViewById(R.id.et_car_license_inputbox5);
        edits[5] = (EditText) v.findViewById(R.id.et_car_license_inputbox6);
        edits[6] = (EditText) v.findViewById(R.id.et_car_license_inputbox7);
        keyboardView = (KeyboardView) v.findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(true);
        keyboardView.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {

            }

            @Override
            public void onRelease(int primaryCode) {

            }

            @Override
            public void onKey(final int primaryCode, int[] keyCodes) {
                if (primaryCode == 112) { //xml中定义的删除键值为112
                    currentEditText--;
                    if (currentEditText < 1) {
                        //切换为省份简称键盘
                        keyboardView.setKeyboard(k1);
                    }
                    if (currentEditText < 0) {
                        currentEditText = 0;
                    }
                    edits[currentEditText].setText("");
                } else { //其它字符按键
                    if (currentEditText == 0) {
                        //如果currentEditText==0代表当前为省份键盘,
                        // 按下一个按键后,设置相应的EditText的值
                        // 然后切换为字母数字键盘
                        //currentEditText+1
                        edits[0].setText(provinceShort[primaryCode]);
                        currentEditText = 1;
                        //切换为字母数字键盘
                        keyboardView.setKeyboard(k2);
                    } else {
                        //第二位必须大写字母
                        if (currentEditText == 1 && !letterAndDigit[primaryCode].matches("[A-Z]{1}") || currentEditText == 7) {
                            return;
                        }
                        edits[currentEditText].setText(letterAndDigit[primaryCode]);
                        currentEditText++;
                        if (currentEditText > 7) {
                            currentEditText = 7;
                        }
                    }
                }
            }

            @Override
            public void onText(CharSequence text) {

            }

            @Override
            public void swipeLeft() {

            }

            @Override
            public void swipeRight() {

            }

            @Override
            public void swipeDown() {

            }

            @Override
            public void swipeUp() {

            }
        });

        confirm = (Button) v.findViewById(R.id.bt_car_info_dialog_confirm);
        cancel = (Button) v.findViewById(R.id.bt_car_info_dialog_cancel);

        confirm.setOnClickListener(v1 -> {
            if (currentEditText != 7) {
                Toast.makeText(mContext, "车牌号码不完善!", Toast.LENGTH_SHORT).show();
            } else {
                StringBuilder result = new StringBuilder();
                for (EditText edit : edits) {
                    result.append(edit.getText());
                }
                mCompletedListener.afterCompleted(result.toString());
                dismiss();
            }
        });
        cancel.setOnClickListener(v12 -> dismiss());
    }

    public interface ICompleted {
        void afterCompleted(String result);
    }

    public void setCompletedListener(ICompleted listener) {
        mCompletedListener = listener;
    }
}
