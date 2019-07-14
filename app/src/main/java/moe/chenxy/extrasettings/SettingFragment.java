package moe.chenxy.extrasettings;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;

import androidx.preference.Preference;
//import android.preference.PreferenceFragment;
import androidx.preference.PreferenceFragment;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;

public class SettingFragment extends PreferenceFragment {
    boolean ifroot;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        getPreferenceManager().setSharedPreferencesName("Chen_Settings");
        addPreferencesFromResource(R.xml.perf);
        //resolution restore
        Preference resolution = getPreferenceManager()
                .findPreference("resolution");
        resolution.setOnPreferenceClickListener(preference -> {
            View view = getView();
            String cmd1 = "wm size 1440x2560";
            String cmd2 = "wm density 560";
            ifroot = RootTools.isRootAvailable();

            if (!ifroot) {
                Snackbar snackbar = Snackbar.make(view, "Need Root!", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
                ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
                //设置Snackbar的背景色
                //snackbarView.setBackgroundColor(Color.WHITE);
                snackbar.show();
            }
            Command command1 = new Command(0, cmd1);
            Command command2 = new Command(0, cmd2);
            try {
                RootTools.getShell(true).add(command1);
                RootTools.getShell(true).add(command2);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (RootDeniedException e) {
                e.printStackTrace();
                final Snackbar snackbar = Snackbar.make(view, "Please allow me to be root!", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
                ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
                snackbar.show();
            }
            return false;
        });
        // resolution done
        findPreference("wechat").setOnPreferenceClickListener(preference -> {
            donateWeixin();
            return true;
        });
        findPreference("alipay").setOnPreferenceClickListener(preference -> {
            donateAlipay("FKX03133TLJFCY8UNXHC56");
            return true;
        });
    }

    /**
     * 支付宝支付
     *
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 HTTPS://QR.ALIPAY.COM/FKX03133TLJFCY8UNXHC56 ，则
     *                payCode = FKX03133TLJFCY8UNXHC56
     *                注：不区分大小写
     */
    public void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(getContext());
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(getActivity(), payCode);
        }else{
            //TODO: Show message that Alipay is not installed
        }
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     */
    public void donateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.weixin);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonateSample" + File.separator +
                "weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(getActivity(), qrPath);
    }
}
