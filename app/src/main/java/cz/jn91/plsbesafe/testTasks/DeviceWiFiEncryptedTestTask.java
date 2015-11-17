package cz.jn91.plsbesafe.testTasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import java.util.List;

import cz.jn91.plsbesafe.R;
import cz.jn91.plsbesafe.TestResult;
import cz.jn91.plsbesafe.adapters.TestTasksAdapter;
import cz.jn91.plsbesafe.fragments.TestsFragment;

/**
 * Created by jn91 on 12.11.2015.
 */
public class DeviceWiFiEncryptedTestTask extends BaseTestAsyncTask {
    public DeviceWiFiEncryptedTestTask(TestsFragment fragment, TestTasksAdapter adapter, int position) {
        super(fragment, adapter, position);
    }

    @Override
    protected String getExplanation() {
        return context.getString(R.string.testWifiSecurText);
    }

    @Override
    protected String getName() {
        return context.getString(R.string.testWifiSecurName);
    }

    @Override
    protected TestResult.TestResolver getResolver() {
        return new TestResult.TestResolver() {
            @Override
            public void resolveProblem(final Activity activity) {
                openSettingsDialog(Settings.ACTION_WIFI_SETTINGS, activity, getMenuIcon(activity));
            }

            @Override
            public Drawable getMenuIcon(Activity activity) {
                return activity.getResources().getDrawable(R.drawable.ic_wifi_lock_white_48dp);
            }
        };
    }

    @Override
    protected TestResult.Result doInBackground(Void... params) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> networkList = wifi.getScanResults();

        WifiInfo wi = wifi.getConnectionInfo();
        String currentSSID = wi.getSSID();
        currentSSID = currentSSID.replaceAll("\"","");

        if (networkList != null) {
            for (ScanResult network : networkList)
            {
                String ssid = network.SSID.replaceAll("\"","");
                if (currentSSID.equals(ssid)){
                    String Capabilities =  network.capabilities;
                    if(Capabilities.contains("WPA2") || Capabilities.contains("WPA") || Capabilities.contains("WEP")){
                        return TestResult.Result.OK;
                    } else {
                        return TestResult.Result.FAIL;
                    }
                }
            }
        }
        return TestResult.Result.NOT_TESTED;
    }
}
