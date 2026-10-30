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
            // نام کلاس در جاوا با کاراکترهای یونیکد نوشته می‌شود
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

        // --- ۲. هک کردن متد اصلی بررسی دونیت در صفحه تنظیمات ---
        try {
            String entryClassName = "uz.unnarsx.cherrygram.preferences.DonatesPreferencesEntry";
            Class<?> entryClass = XposedHelpers.findClass(entryClassName, lpparam.classLoader);

            // ما به برنامه می‌گوییم هر بار که خواست لیست را بسازد (fillItems)، فرض کند کاربر دونیت کرده است
            XposedHelpers.findAndHookMethod(
                    entryClass,
                    "fillItems",
                    java.util.ArrayList.class,
                    org.telegram.ui.Components.UniversalAdapter.class, // یا کلاس مشابه
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            // اینجا کاری نمی‌کنیم، فقط لاگ می‌دهیم که متد اجرا شده
                            XposedBridge.log(TAG + ": ✅ متد fillItems در صفحه دونیت اجرا شد.");
                            return null; // اجازه می‌دهیم کد اصلی اجرا شود اما چون دونیت true است، لیست نمایش داده می‌شود
                        }
                    }
            );
        } catch (Throwable ignored) {}

        // --- ۳. هک کردن منوی پیام iOS (برای اطمینان) ---
        try {
            // در فایل MessageMenuPreferencesEntry، یک متغیر وجود دارد که تعیین می‌کند بلور فعال باشد یا نه
            // ما مستقیماً متد getBlurMessageMenuBackground را در کلاس تنظیمات هک می‌کنیم
            String configClassName = "uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig";
            XposedHelpers.findAndHookMethod(
                    configClassName,
                    lpparam.classLoader,
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
