package com.mr.telephonymanagersample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellInfo;
import android.telephony.CellInfoLte;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NetworkListener.NetworkStateChangedListener {
	TelephonyManager telephonyManager;
	private int MY_PERMISSION_REQUEST_ACCESS = 101;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
				ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// Request the missing permissions
			requestPermission();
			return;
		}
		initTelephonyManager();

	}
	protected void requestPermission() {
		ActivityCompat.requestPermissions(this,
				new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_ACCESS);
	}
	private void initTelephonyManager() {
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		NetworkListener networkListener = NetworkListener.getInstance(this);
		networkListener.addListener(this);
		telephonyManager.listen(networkListener,
				PhoneStateListener.LISTEN_CELL_INFO
						| PhoneStateListener.LISTEN_CELL_LOCATION
						| PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == MY_PERMISSION_REQUEST_ACCESS){
			// If request is cancelled, the result arrays are empty.
			if (grantResults.length > 1
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED
					&& grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
				// permission was granted!
				initTelephonyManager();
			} else {
				// permission denied
				Snackbar.make(this.getCurrentFocus().getRootView(),"Permission is required to continue",Snackbar.LENGTH_LONG)
						.setAction("RETRY", new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								requestPermission();
							}
						}).show();
			}
			return;
		}
	}

	@Override
	public void onNetworkStateChanged(Object data) {
		if(data instanceof CellLocation){
			TextView textView2 = findViewById(R.id.textView2);
			CellLocation cellLocation = (CellLocation) data;
			textView2.setText(cellLocation.toString());
		}else if(data instanceof SignalStrength){
			TextView textView3 = findViewById(R.id.textView3);
			SignalStrength signalStrength = (SignalStrength) data;
			textView3.setText(signalStrength.toString());
		}else{
			Toast.makeText(this,"Wrong data type",Toast.LENGTH_LONG).show();
		}

		TextView textView1 = findViewById(R.id.textView1);
		@SuppressLint("MissingPermission") List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();
		//telephonyManager.set
		CellInfoLte cellInfoLte = null;
		for(CellInfo cellInfo : cellInfoList){
			if(cellInfo instanceof CellInfoLte) {
				cellInfoLte = (CellInfoLte) cellInfo;
				break;
			}
		}
		if(cellInfoLte != null) {
			textView1.setText("LTE Signal strength:" + cellInfoLte.getCellSignalStrength().getLevel());
		}

	}
}
