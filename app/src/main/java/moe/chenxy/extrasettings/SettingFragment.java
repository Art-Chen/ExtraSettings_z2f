package moe.chenxy.extrasettings;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;

import androidx.preference.ListPreference;
import androidx.preference.Preference;
//import android.preference.PreferenceFragment;
import androidx.preference.PreferenceFragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.stericson.RootShell.exceptions.RootDeniedException;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        if(!Build.USER.equals("Art_Chen")){
            Toast.makeText(getContext(),"你使用的不是琛琛的Rom哦~\n请关注琛琛微博来获得正版Rom",Toast.LENGTH_LONG).show();
        }
        // u-touch
        ListPreference touch = (ListPreference) getPreferenceManager().findPreference("touch");
        ListPreference longtouch = (ListPreference) getPreferenceManager().findPreference("long_touch");
        ListPreference swipeleft = (ListPreference) getPreferenceManager().findPreference("swipe_left");
        ListPreference swiperight = (ListPreference) getPreferenceManager().findPreference("swipe_right");
        // init
            if (readkeyProcess("616") == null ||
                    readkeyProcess("617") == null ||
                    readkeyProcess("620") == null ||
                    readkeyProcess("621") == null ){
                // reset
                fingerprintGestureModify_slient("616","BACK");
                fingerprintGestureModify_slient("617","HOME");
                fingerprintGestureModify_slient("620","BACK");
                fingerprintGestureModify_slient("621","APP_SWITCH");
                Toast.makeText(getContext(),"检测到指纹配置按键值异常，已经还原为默认值",Toast.LENGTH_LONG).show();
            }
        // touch
        Log.i("Art_Chen","keyCode: 616, readkeyProcess:"+readkeyProcess("616"));
        if (readkeyProcess("616").equals("BACK")){
            touch.setValueIndex(0);
        } else if (readkeyProcess("616").equals("MENU")){
            touch.setValueIndex(1);
        } else if (readkeyProcess("616").equals("APP_SWITCH")){
            touch.setValueIndex(2);
        } else if (readkeyProcess("616").equals("VOICE_ASSIST")){
            touch.setValueIndex(3);
        } else if (readkeyProcess("616").equals("HOME")){
            touch.setValueIndex(4);
        }
        touch.setSummary(touch.getEntry());
        touch.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals("0")){
                fingerprintGestureModify("616","BACK");
            } else if (newValue.equals("1")){
                fingerprintGestureModify("616","MENU");
            } else if (newValue.equals("2")){
                fingerprintGestureModify("616","APP_SWITCH");
            } else if (newValue.equals("3")){
                fingerprintGestureModify("616","VOICE_ASSIST");
            } else if (newValue.equals("4")){
                fingerprintGestureModify("616","HOME");
            }
            CharSequence[] entries=touch.getEntries();
            int index=touch.findIndexOfValue((String)newValue);
            touch.setSummary(entries[index]);
            return true;
        });
        // long touch
        Log.i("Art_Chen","keyCode: 617, readkeyProcess:"+readkeyProcess("617"));
        if (readkeyProcess("617").equals("BACK")){
            longtouch.setValueIndex(0);
        } else if (readkeyProcess("617").equals("MENU")){
            longtouch.setValueIndex(1);
        } else if (readkeyProcess("617").equals("APP_SWITCH")){
            longtouch.setValueIndex(2);
        } else if (readkeyProcess("617").equals("VOICE_ASSIST")){
            longtouch.setValueIndex(3);
        } else if (readkeyProcess("617").equals("HOME")){
            longtouch.setValueIndex(4);
        }
        longtouch.setSummary(longtouch.getEntry());
        longtouch.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals("0")){
                fingerprintGestureModify("617","BACK");
            } else if (newValue.equals("1")){
                fingerprintGestureModify("617","MENU");
            } else if (newValue.equals("2")){
                fingerprintGestureModify("617","APP_SWITCH");
            } else if (newValue.equals("3")){
                fingerprintGestureModify("617","VOICE_ASSIST");
            } else if (newValue.equals("4")){
                fingerprintGestureModify("617","HOME");
            }
            CharSequence[] entries=longtouch.getEntries();
            int index=longtouch.findIndexOfValue((String)newValue);
            longtouch.setSummary(entries[index]);
            return true;
        });
        // swipe left
        Log.i("Art_Chen","keyCode: 620, readkeyProcess:"+readkeyProcess("620"));
        if (readkeyProcess("620").equals("BACK")){
            swipeleft.setValueIndex(0);
        } else if (readkeyProcess("620").equals("MENU")){
            swipeleft.setValueIndex(1);
        } else if (readkeyProcess("620").equals("APP_SWITCH")){
            swipeleft.setValueIndex(2);
        } else if (readkeyProcess("620").equals("VOICE_ASSIST")){
            swipeleft.setValueIndex(3);
        } else if (readkeyProcess("620").equals("HOME")){
            swipeleft.setValueIndex(4);
        }
        swipeleft.setSummary(swipeleft.getEntry());
        swipeleft.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals("0")){
                fingerprintGestureModify("620","BACK");
            } else if (newValue.equals("1")){
                fingerprintGestureModify("620","MENU");
            } else if (newValue.equals("2")){
                fingerprintGestureModify("620","APP_SWITCH");
            } else if (newValue.equals("3")){
                fingerprintGestureModify("620","VOICE_ASSIST");
            } else if (newValue.equals("4")){
                fingerprintGestureModify("620","HOME");
            }
            CharSequence[] entries=swipeleft.getEntries();
            int index=swipeleft.findIndexOfValue((String)newValue);
            swipeleft.setSummary(entries[index]);
            return true;
        });
        // swipe right
        Log.i("Art_Chen","keyCode: 621, readkeyProcess:"+readkeyProcess("621"));
        if (readkeyProcess("621").equals("BACK")){
            swiperight.setValueIndex(0);
        } else if (readkeyProcess("621").equals("MENU")){
            swiperight.setValueIndex(1);
        } else if (readkeyProcess("621").equals("APP_SWITCH")){
            swiperight.setValueIndex(2);
        } else if (readkeyProcess("621").equals("VOICE_ASSIST")){
            swiperight.setValueIndex(3);
        } else if (readkeyProcess("621").equals("HOME")){
            swiperight.setValueIndex(4);
        }
        swiperight.setSummary(swiperight.getEntry());
        swiperight.setOnPreferenceChangeListener((preference, newValue) -> {
            if (newValue.equals("0")){
                fingerprintGestureModify("621","BACK");
            } else if (newValue.equals("1")){
                fingerprintGestureModify("621","MENU");
            } else if (newValue.equals("2")){
                fingerprintGestureModify("621","APP_SWITCH");
            } else if (newValue.equals("3")){
                fingerprintGestureModify("621","VOICE_ASSIST");
            } else if (newValue.equals("4")){
                fingerprintGestureModify("621","HOME");
            }
            CharSequence[] entries=swiperight.getEntries();
            int index=swiperight.findIndexOfValue((String)newValue);
            swiperight.setSummary(entries[index]);
            return true;
        });
        // u-touch done
        //resolution restore
        Preference resolution = getPreferenceManager()
                .findPreference("resolution");
        resolution.setOnPreferenceClickListener(preference -> {
            changeResolution("1440x2560","560");
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
            snackbar.setAction("Reboot", v -> rebootAdvanced(null));
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
            View view = getView();
            final Snackbar snackbar = Snackbar.make(view, "Alipay not installed!(未安装支付宝哦~)", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.CYAN);
            snackbar.setAction("安装",v -> {
                Intent it = new Intent();
                it.setAction(Intent.ACTION_VIEW);
                it.setData(Uri.parse("https://t.alipayobjects.com/L1/71/100/and/alipay_wap_main.apk"));
                startActivity(it);
            });
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
    /**
     * 指纹手势文件修改
     * @param keyCode 键值
     * @param process 操作
     */
    public void fingerprintGestureModify(String keyCode, String process) {
        View view = getView();
        String rm = "sed -i -e '/key " + keyCode + "/d' /vendor/usr/keylayout/uinput-fpc.kl";
        String add = "echo key " + keyCode + "   " + process + "  VIRTUAL >> /vendor/usr/keylayout/uinput-fpc.kl";
        String rwmount = "mount -o rw,remount /vendor";
        String chmod = "chmod 644 /vendor/usr/keylayout/uinput-fpc.kl";
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
        Command remove = new Command(0, rm);
        Command Add = new Command(0, add);
        Command remount = new Command(0, rwmount);
        Command chmod644 = new Command(0, chmod);
        try {
            //RootTools.remount("/vendor","rw");
            RootTools.getShell(true).add(remount);
            RootTools.getShell(true).add(remove);
            RootTools.getShell(true).add(Add);
            RootTools.getShell(true).add(chmod644);
            final Snackbar snackbar = Snackbar.make(view, "更改将在重启后生效", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
            ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.CYAN);
            snackbar.setAction("重启",v -> {rebootAdvanced(null);});
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
    public void fingerprintGestureModify_slient(String keyCode, String process) {
        String rm = "sed -i -e '/key " + keyCode + "/d' /vendor/usr/keylayout/uinput-fpc.kl";
        String add = "echo key " + keyCode + "   " + process + "  VIRTUAL >> /vendor/usr/keylayout/uinput-fpc.kl";
        String rwmount = "mount -o rw,remount /vendor";
        String chmod = "chmod 644 /vendor/usr/keylayout/uinput-fpc.kl";

        Command remove = new Command(0, rm);
        Command Add = new Command(0, add);
        Command remount = new Command(0, rwmount);
        Command chmod644 = new Command(0, chmod);
        try {
            //RootTools.remount("/vendor","rw");
            RootTools.getShell(true).add(remount);
            RootTools.getShell(true).add(remove);
            RootTools.getShell(true).add(Add);
            RootTools.getShell(true).add(chmod644);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (RootDeniedException e) {
            e.printStackTrace();
        }
    }
    /**
     *
     * @param keyCode 键值
     * @return 返回对应键值的操作值
     */
    public String readkeyProcess(String keyCode) {
        View view = getView();
        try {
            File file = new File("/vendor/usr/keylayout/uinput-fpc.kl");
            if (file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(
                        new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;

                while ((lineTxt = br.readLine()) != null) {
                    if (lineTxt.startsWith("#") || lineTxt.equals("")){
                        continue;
                    } else {
                        String[] arrStrings = lineTxt.split("\\s+"); // 用于把一个字符串分割成字符串数组

                        if (!arrStrings[1].equals(keyCode)) {
                            continue;
                        } else {
                            return arrStrings[2];
                        }
                    }
                }
                br.close();

            } else {
                final Snackbar snackbar = Snackbar.make(view, "Fingerprint key layout not found!", Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                ((TextView) snackbarView.findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
                ((TextView) snackbarView.findViewById(R.id.snackbar_action)).setTextColor(Color.BLUE);
                snackbar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

