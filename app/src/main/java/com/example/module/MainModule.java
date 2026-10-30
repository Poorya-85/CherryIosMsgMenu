package com.example.module;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainModule implements IXposedHookLoadPackage {

    private static final String TAG = "CherryHook_Debug";
    private static final String TARGET_PACKAGE = "uz.unnarsx.cherrygram";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals(TARGET_PACKAGE)) {
            return;
        }

        XposedBridge.log(TAG + ": 🚀 ماژول در چری‌گرام لود شد.");

        // --- ۱. دور زدن سیستم دونیت (کلاس 㥿) ---
        try {
            // نام کلاس دقیقاً از فایل Smali گرفته شده است
            String donateClassName = "uz.unnarsx.cherrygram.donates.㥿"; 
            Class<?> donateClass = XposedHelpers.findClass(donateClassName, lpparam.classLoader);

            // هوک کردن متد ώ (دونیت سطح پایه)
            XposedHelpers.findAndHookMethod(
                    donateClass,
                    "ώ",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + ": ✅ متد ώ() صدا زده شد -> بازگشت True");
                            return true;
                        }
                    }
            );

            // هوک کردن متد 㝸 (دونیت VIP)
            XposedHelpers.findAndHookMethod(
                    donateClass,
                    "㝸",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + ": ✅ متد 㝸() صدا زده شد -> بازگشت True");
                            return true;
                        }
                    }
            );
            XposedBridge.log(TAG + ": ✅ سیستم دونیت با موفقیت هک شد.");

        } catch (Throwable t) {
            XposedBridge.log(TAG + ": ❌ خطا در هک کردن کلاس دونیت: " + t.getMessage());
        }

        // --- ۲. هک کردن تنظیمات منوی iOS (در صورت نیاز) ---
        // (اگر با هک دونیت، منو باز شد، به این بخش نیازی نیست، اما برای اطمینان می‌گذاریم)
        try {
            String configClassName = "uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig";
            Class<?> configClass = XposedHelpers.findClass(configClassName, lpparam.classLoader);
            
            XposedHelpers.findAndHookMethod(
                    configClass,
                    "getBlurMessageMenuBackground",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            return true;
                        }
                    }
            );
            XposedBridge.log(TAG + ": ✅ تنظیمات منوی iOS به زور روشن شد.");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": ⚠️ خطا در هک منوی iOS: " + t.getMessage());
        }
    }
}
