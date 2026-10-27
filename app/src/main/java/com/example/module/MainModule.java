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

        XposedBridge.log(TAG + ": 🚀 ماژول با موفقیت در چری‌گرام لود شد.");

        // --- ۱. هک کردن DonateManager ---
        try {
            XposedHelpers.findAndHookMethod(
                    "uz.unnarsx.cherrygram.donates.DonatesManager",
                    lpparam.classLoader,
                    "checkAllDonatedAccountsForMarketplace",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + ": ✅ متد checkAllDonatedAccountsForMarketplace صدا زده شد -> بازگشت True (دور زدن دونیت)");
                            return true;
                        }
                    }
            );
            XposedBridge.log(TAG + ": ✅ هوک DonatesManager با موفقیت اعمال شد.");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": ❌ خطا در هوک DonatesManager: " + t.getMessage());
        }

        // --- ۲. هک کردن تنظیمات منوی بلور (iOS) ---
        try {
            XposedHelpers.findAndHookMethod(
                    "uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig",
                    lpparam.classLoader,
                    "getBlurMessageMenuBackground",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + ": ✅ متد getBlurMessageMenuBackground صدا زده شد -> بازگشت True");
                            return true;
                        }
                    }
            );
            XposedBridge.log(TAG + ": ✅ هوک CherrygramMessagesConfig (Blur) با موفقیت اعمال شد.");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": ❌ خطا در هوک getBlurMessageMenuBackground: " + t.getMessage());
        }

        // --- ۳. هک کردن اسکرول یکپارچه ---
        try {
            XposedHelpers.findAndHookMethod(
                    "uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig",
                    lpparam.classLoader,
                    "getMsgMenuUnifiedScroll",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            XposedBridge.log(TAG + ": ✅ متد getMsgMenuUnifiedScroll صدا زده شد -> بازگشت True");
                            return true;
                        }
                    }
            );
            XposedBridge.log(TAG + ": ✅ هوک CherrygramMessagesConfig (UnifiedScroll) اعمال شد.");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": ❌ خطا در هوک getMsgMenuUnifiedScroll: " + t.getMessage());
        }
    }
}
