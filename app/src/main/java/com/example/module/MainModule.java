package com.example.module;

import java.lang.reflect.Method;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
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

        // --- ۱. دور زدن قفل دونیت (بدون تغییر) ---
        try {
            XposedHelpers.findAndHookMethod(
                    "uz.unnarsx.cherrygram.donates.DonatesManager",
                    lpparam.classLoader,
                    "checkAllDonatedAccountsForMarketplace",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            return true;
                        }
                    }
            );
            XposedBridge.log(TAG + ": ✅ قفل دونیت باز شد.");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": ❌ خطا در هوک دونیت: " + t.getMessage());
        }

        // --- ۲. هوک کردن داینامیک تنظیمات منوی بلور ---
        // چون کد مبهم‌سازی شده و از متدهای داینامیک استفاده می‌کند،
        // ما کلاس‌های مربوط به تنظیمات چری‌گرام را هدف قرار می‌دهیم.
        
        // لیستی از کلاس‌هایی که احتمال می‌دهیم تنظیمات آنجا باشد (بر اساس پکیج چری‌گرام)
        String[] classesToHook = {
                "uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig",
                "uz.unnarsx.cherrygram.core.configs.CherrygramCoreConfig",
                "uz.unnarsx.cherrygram.preferences.MessageMenuPreferencesEntry"
        };

        for (String className : classesToHook) {
            try {
                Class<?> clazz = XposedHelpers.findClass(className, lpparam.classLoader);
                // تمام متدهای این کلاس را می‌گردیم
                for (Method method : clazz.getDeclaredMethods()) {
                    // اگر نام متد شامل Blur بود (مثل getBlurMessageMenuBackground)
                    if (method.getName().toLowerCase().contains("blur")) {
                        XposedBridge.hookMethod(method, new XC_MethodReplacement() {
                            @Override
                            protected Object replaceHookedMethod(MethodHookParam param) {
                                XposedBridge.log(TAG + ": ✅ متد " + param.method.getName() + " اجرا شد -> بازگشت True");
                                return true;
                            }
                        });
                        XposedBridge.log(TAG + ": ✅ متد " + method.getName() + " با موفقیت هوک شد.");
                    }
                }
            } catch (Throwable t) {
                // اگر کلاسی پیدا نشد، فقط لاگ می‌دهیم و ادامه می‌دهیم
                XposedBridge.log(TAG + ": ⚠️ کلاس پیدا نشد: " + className);
            }
        }

        // --- ۳. روش جایگزین: هوک کردن خروجی کلاس داینامیک Lg06 ---
        try {
            // بر اساس عکس smali، کلاس Lg06 مسئول بررسی تنظیمات است
            // ما به جای تغییر متد تنظیمات، به این کلاس می‌گوییم همیشه True برگرداند
            XposedHelpers.findAndHookMethod(
                    "g06", // نام کلاس بدون L و ;
                    lpparam.classLoader,
                    "getBoolean", // نام متد فرضی (ممکن است متفاوت باشد)
                    String.class, boolean.class,
                    new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            // اگر کلید درخواستی مربوط به بلور بود
                            if (param.args[0].toString().contains("blurMessageMenuBackground")) {
                                param.setResult(true);
                                XposedBridge.log(TAG + ": ✅ کلید بلور در کلاس داینامیک True شد.");
                            }
                        }
                    }
            );
        } catch (Throwable ignored) {}

    }
}
