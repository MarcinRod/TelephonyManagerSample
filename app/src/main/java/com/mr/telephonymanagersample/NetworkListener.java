package com.mr.telephonymanagersample;

import android.content.Context;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

import java.util.List;

class NetworkListener extends PhoneStateListener {
	private static NetworkListener instance;
	Context context;
	NetworkStateChangedListener listener;
	private NetworkListener(Context context){
		this.context = context;
	}
	public static NetworkListener getInstance(Context context){
		if(instance == null){
			instance = new NetworkListener(context);
		}
		return instance;
	}
	public void addListener(NetworkStateChangedListener listener){
		this.listener = listener;
	}
	@Override
	public void onCellInfoChanged(List<CellInfo> cellInfo) {
		super.onCellInfoChanged(cellInfo);
		if(listener != null)
			listener.onNetworkStateChanged(cellInfo);
	}

	@Override
	public void onCellLocationChanged(CellLocation location) {
		super.onCellLocationChanged(location);
		if(listener != null)
			listener.onNetworkStateChanged(location);
	}

	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		super.onSignalStrengthsChanged(signalStrength);
		if(listener != null)
			listener.onNetworkStateChanged(signalStrength);
	}

	public interface NetworkStateChangedListener{
		void onNetworkStateChanged(Object data);
	}
}
