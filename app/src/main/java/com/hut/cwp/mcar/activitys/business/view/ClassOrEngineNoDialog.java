package com.hut.cwp.mcar.activitys.business.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hut.cwp.mcar.R;

import me.shaohui.bottomdialog.BaseBottomDialog;

/**
 * Created by Just on 2016/10/18.
 */

public class ClassOrEngineNoDialog extends BaseBottomDialog {
    private Button confirm,cancel;
    private EditText etInput;

    private int mTag;
    private Context mContext;

    private ICompleted mCompletedListener;

    private TextView mTvPrompt;

    public static int TAG_FOR_CLASS=1;//车架号
    public static int TAG_FOR_ENGINE=2;//发动机号

    public ClassOrEngineNoDialog(Context context, int tag) {
        super();
        mContext=context;
        mTag=tag;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.z_layout_class_or_engine_dialog;
    }

    @Override
    public void bindView(View v) {
        mTvPrompt= (TextView) v.findViewById(R.id.tv_car_info_dialog_prompt);
        if (mTag==TAG_FOR_CLASS) {//车架号
            mTvPrompt.setText("请输入车架号");
        } else if(mTag==TAG_FOR_ENGINE) {
            mTvPrompt.setText("请输入发动机号");
        }
        etInput= (EditText) v.findViewById(R.id.et_car_info_dialog_input);
        etInput.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm =
                        (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etInput, 0);
            }
        });
        etInput.setText("");
        confirm= (Button) v.findViewById(R.id.bt_car_info_dialog_confirm);
        cancel= (Button) v.findViewById(R.id.bt_car_info_dialog_cancel);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result=etInput.getText().toString();
                if (mTag==TAG_FOR_CLASS) {//车架号
                    if (result.length()!=17) {
                        Toast.makeText(mContext, "车架号不符合规范!", Toast.LENGTH_SHORT).show();
                    } else {
                        mCompletedListener.afterCompleted(result);
                        dismiss();
                    }
                } else if(mTag==TAG_FOR_ENGINE) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(mContext, "发动机号不符合规范!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        mCompletedListener.afterCompleted(result);
                        dismiss();
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public interface ICompleted {
        void afterCompleted(String result);
    }

    public void setCompletedListener(ICompleted listener) {
        mCompletedListener=listener;
    }
}
