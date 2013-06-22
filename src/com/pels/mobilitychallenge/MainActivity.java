package com.pels.mobilitychallenge;

import java.util.Date;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.pels.mobilitychallenge.common.SamplingMode;
import com.pels.mobilitychallenge.common.Session;

public class MainActivity extends Activity {

	private static Intent serviceIntent;
	private SamplingService samplingService;
	private SamplingMode selectedMode;

	private TextView totalReadingsCount;
	private TextView lastReadingTimestamp;
	private TextView samplingDuration;
	private RadioGroup rg;
	private Date currentDate;
	private ToggleButton startStop;

	/**
	 * Provides a connection to the GPS Sampling Service
	 */
	private final ServiceConnection samplingServiceConnection = new ServiceConnection() {

		public void onServiceDisconnected(ComponentName name) {
			samplingService = null;
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			samplingService = ((SamplingService.SamplingServiceBinder) service)
					.getService();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		serviceIntent = new Intent(this, SamplingService.class);
		startAndBindService();
		selectedMode = SamplingMode.DEFAULT;

		totalReadingsCount = (TextView) findViewById(R.id.totalReading);
		samplingDuration = (TextView) findViewById(R.id.samplingDuration);
		lastReadingTimestamp = (TextView) findViewById(R.id.lastReadingTimestamp);

		rg = (RadioGroup) findViewById(R.id.radioGroup);

		startStop = (ToggleButton) findViewById(R.id.startStop);

		currentDate = new Date(System.currentTimeMillis());
		Session.setDate(currentDate);

	}

	/**
	 * Starts the service and binds the activity to it.
	 */
	private void startAndBindService() {
		startService(serviceIntent);
		bindService(serviceIntent, samplingServiceConnection,
				Context.BIND_AUTO_CREATE);
		// samplingService.startSampling();
	}

	public void onStartStopClicked(View view) {
		boolean on = ((ToggleButton) view).isChecked();
		if (on) {
			startSamplingService();
		} else {
			stopSamplingService();
		}

	}

	private void startSamplingService() {
		Session.setCurrentSamplingMode(selectedMode);
		boolean success = samplingService.startSampling();
		if (success) {
			Session.resetSummary();
			Session.setSamplingStartTime(System.currentTimeMillis());
			clearSummaryTable();
			rg.setEnabled(false);
		} else {
			startStop.toggle();
		}

	}

	private void stopSamplingService() {
		samplingService.stopSampling();
		Session.setSamplingStopTime(System.currentTimeMillis());
		rg.setEnabled(true);
		populateSummaryTable();
	}

	private void clearSummaryTable() {
		totalReadingsCount.setText("--");
		lastReadingTimestamp.setText("--");
		samplingDuration.setText("--");
	}

	private void populateSummaryTable() {
		totalReadingsCount.setText("" + Session.getReadingsCount());
		lastReadingTimestamp.setText(Session.getLatestTimeStampLabel());
		samplingDuration.setText(Session.getSamplingDurationLabel());
	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.radioBike:
			if (checked)
				selectedMode = SamplingMode.BIKE;
			break;
		case R.id.radioDefault:
			if (checked)
				selectedMode = SamplingMode.DEFAULT;
			break;
		}
		Session.setCurrentSamplingMode(selectedMode);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
