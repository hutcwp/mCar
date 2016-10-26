package com.hut.cwp.mcar.way.clazz;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hut.cwp.mcar.R;

import java.util.ArrayList;
import java.util.List;

public class MycarInfoAdapter extends ArrayAdapter<CarInfo>{
	private int resourceId ;
    //private List<MycarInfo> mData= new ArrayList<>();
    private List<CarInfo> mData = new ArrayList<>();

	public MycarInfoAdapter (Context context, int textViewResourceId,List<CarInfo> objects){
		super(context,textViewResourceId,objects);
		resourceId = textViewResourceId;
        mData = objects;

	}
	
	@Override
	public View getView (final int position, View convertView, ViewGroup parent){
		CarInfo mycarInfo =getItem(position);

		View view =LayoutInflater.from(getContext()).inflate(resourceId, null);

		TextView licensePlateInfo = (TextView) view.findViewById(R.id.way_itme_licensePlate);
		TextView engineInfo = (TextView) view.findViewById(R.id.way_itme_engine);
		TextView frameInfo = (TextView) view.findViewById(R.id.way_itme_frame);
		ImageView asd = (ImageView) view.findViewById(R.id.imageView2);
		if(asd == null){
			Log.d("TAG","asd为空");
		}else{
			Log.d("TAG","asd不为空");
		}

		licensePlateInfo.setText(mData.get(position).getLicensePlate().toString());
		engineInfo.setText(mData.get(position).getEngine().toString());
		frameInfo.setText(mData.get(position).getFrame().toString());
//		asd.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(getContext(),"点击了删除"+position+"@@"+mData.get(position).getObjectId(),Toast.LENGTH_LONG).show();
//			}
//		});

		//asd.setVisibility(View.VISIBLE);



		return view;
	}

}