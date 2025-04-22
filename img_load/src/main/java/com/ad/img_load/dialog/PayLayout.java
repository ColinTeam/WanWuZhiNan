package com.ad.img_load.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.comm.img_load.R;


public class PayLayout extends LinearLayout {

    private Context mContext;

    ImageView ivClose;
    TextView tvTitle;
    TextView tvContent;

    LinearLayout layoutPrice;
    TextView tvPrice;
    ImageView ivPrice;

    PwdView etPassword;
    TextView tvLeft;
    TextView tvCenter;
    TextView tvRight;
    GridView gvBtn;

    Layoutlistener mListener;


    //下面三个是继承LinearLayout后必须添加的三个重载方法
    public PayLayout(Context context) {
        super(context);
        initLayout(context);
    }
    public PayLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }
    public PayLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    public void setmListener(Layoutlistener mListener) {
        this.mListener = mListener;
    }

    public void initLayout(Context context){
        mContext = context;
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_pay, this);
        ivClose = mView.findViewById(R.id.iv_close);
        tvTitle = mView.findViewById(R.id.tv_title);
        tvContent = mView.findViewById(R.id.tv_content);
        tvPrice= mView.findViewById(R.id.tv_price);
        ivPrice= mView.findViewById(R.id.iv_price);
        layoutPrice= mView.findViewById(R.id.price_layout);
        etPassword = mView.findViewById(R.id.et_content);
        tvLeft = mView.findViewById(R.id.tv_left);
        tvCenter = mView.findViewById(R.id.tv_center);
        tvRight = mView.findViewById(R.id.tv_right);
        gvBtn = mView.findViewById(R.id.gv_btn);
        
        setGridView();
        setListeners();
    }

    //设置gridView的参数，显示按钮
    private void setGridView() {
        gvBtn.setNumColumns(3); //设成三列

        char[] list = new char[12];
        {
            list[0] = '1';
            list[1] = '2';
            list[2] = '3';
            list[3] = '4';
            list[4] = '5';
            list[5] = '6';
            list[6] = '7';
            list[7] = '8';
            list[8] = '9';
            list[9] = ' ';
            list[10] = '0';
            list[11] = '/';
        }

        ButtonAdapter buttonAdapter = new ButtonAdapter(mContext, list);
        buttonAdapter.setmListener(new ButtonAdapter.ButtonListener() {
            @Override
            public void getContent(char c) {
                editPwd(c);
            }
        });
        gvBtn.setAdapter(buttonAdapter);
    }

    //监听事件，并且暴露给外部Dialog使用
    private void setListeners() {
        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.closeBtn();
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mListener != null)  mListener.fillContent(etPassword.getText().toString());
            }
        });


        tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.leftBtn();
            }
        });

        tvCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.centerBtn();
            }
        });

        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.rightBtn();
            }
        });

    }

    //点击按钮后内容的变化
    private void editPwd(char c) {
        switch (c){
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case '0':
                etPassword.setText(etPassword.getText().toString() + c);
                break;
            case '/':
                int length = etPassword.getText().toString().length();
                if (length > 0)
                    etPassword.setText(etPassword.getText().toString().substring(0, length-1));
                break;
        }
    }

    public void setDialogTitle(String title){
        if (!isStringEmpty(title)){
            tvTitle.setText(title);
        }
    }

    //设置文本
    public void setText(String title, String content, String left, String center, String right) {
        if (!isStringEmpty(title))
            tvTitle.setText(title);
        if (!isStringEmpty(content)){
            tvContent.setVisibility(VISIBLE);
            tvContent.setText(content);
        }
        if (!isStringEmpty(left)){
            tvLeft.setVisibility(VISIBLE);
            tvLeft.setText(left);
        }
        if (!isStringEmpty(content)){
            tvCenter.setVisibility(VISIBLE);
            tvCenter.setText(center);
        }
        if (!isStringEmpty(content)){
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(right);
        }
//        if (priceIcon > 0 && !isStringEmpty(price)){
//            layoutPrice.setVisibility(VISIBLE);
//            tvPrice.setText(String.format("需支付:%s",price));
//            ivPrice.setImageResource(priceIcon);
//        }
    }

    public static boolean isStringEmpty(String str) {
        return str == null || str.length() <= 0;
    }


    public interface Layoutlistener{
        void closeBtn();

        void fillContent(String content);

        void leftBtn();

        void centerBtn();

        void rightBtn();
    }


}
