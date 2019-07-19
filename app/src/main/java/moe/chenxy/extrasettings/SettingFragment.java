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
            changeResolution("1440x2160","560");
            return false;
        });
        // resolution done
        // remove app
        findPreference("removeapp").setOnPreferenceClickListener(preference -> {
            removeApp("360PhoneAssist");
            removeApp("iFlyIME");
            removeApp("SogouBrowser");
            removeApp("SohuNews");
            removeApp("Weather");
            removeApp("360cledroid");
            final Snackbar snackbar = Snackbar.make(getView(), "Please restart your phone.", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.setAction("Reboot", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rebootAdvanced(null);
                }
            });
            snackbar.show();
            return true;
        });
        // donate
        findPreference("wechat").setOnPreferenceClickListener(preference -> {
            donateWeixin();
            return true;
        });
        findPreference("alipay").setOnPreferenceClickListener(preference -> {
            donateAlipay("FKX03133TLJFCY8UNXHC56");
            return true;
        });
        // donate done

        // advanced reboot
        findPreference("recovery").setOnPreferenceClickListener(preference -> {
            rebootAdvanced("recovery");
            return true;
        });
        findPreference("bootloader").setOnPreferenceClickListener(preference -> {
            rebootAdvanced("bootloader");
            return true;
        });
        // done
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
            View view = getView();
            final Snackbar snackbar = Snackbar.make(view, "Alipay not installed!(未安装支付宝哦~)", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
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

    /**
     * 提供一个高级重启的接口
     * @param arg 重启参数定义，arg为null时，普通重启。
     */
    public void rebootAdvanced(String arg) {
        String cmd = "reboot " + arg;
        Command command = new Command(0, cmd);
        View view = getView();
        try {
            RootTools.getShell(true).add(command);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            final Snackbar snackbar = Snackbar.make(view, "Get root timeout! Magisk may not run in background.", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
            final Snackbar snackbar = Snackbar.make(view, "Please allow me to be root!", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
        }
    }
    /**
     * 分辨率修改接口
     * @param resolution 分辨率 例如1440x2160
     * @param density dpi
     */
    public void changeResolution(String resolution, String density){
        View view = getView();
        String cmd1 = "wm size " + resolution;
        String cmd2 = "wm density " + density;
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
            final Snackbar snackbar = Snackbar.make(view, "Done!", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            final Snackbar snackbar = Snackbar.make(view, "Get root timeout! Magisk may not run in background.", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
            final Snackbar snackbar = Snackbar.make(view, "Please allow me to be root!", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
        }
    }
    /**
     * 移除预装
     * @param appname apk所在文件夹名字
     */
    public void removeApp(String appname){
        View view = getView();
        String cmd = "rm -rf /system/priv-app/" + appname;
        String rwmount = "mount -o rw,remount /system";
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
        Command command = new Command(0, cmd);
        Command remount = new Command(0, rwmount);
        try {
            if (appname == null || appname.equals("")){
                final Snackbar snackbar = Snackbar.make(view, "App Name is Null!", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
                ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
                snackbar.show();
            } else {
                RootTools.remount("/system","rw");
                RootTools.getShell(true).add(remount);
                RootTools.getShell(true).add(command);
                final Snackbar snackbar = Snackbar.make(view, "Done!", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
                ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
                snackbar.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            final Snackbar snackbar = Snackbar.make(view, "Get root timeout! Magisk may not run in background.", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
            final Snackbar snackbar = Snackbar.make(view, "Please allow me to be root!", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
            snackbar.show();
        }
    }
}

