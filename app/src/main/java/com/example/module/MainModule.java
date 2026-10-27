package com.yourname.cherryhook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookMain implements IXposedHookLoadPackage {

    private static final String TAG = "CherryIOSMenuHook";
    private static final String TARGET_PACKAGE = "uz.unnarsx.cherrygram";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        // فقط روی پکیج اصلی Cherrygram کار کن
        if (!lpparam.packageName.equals(TARGET_PACKAGE)) {
            return;
        }

        XposedBridge.log(TAG + ": Loaded in Cherrygram, applying hooks...");

        // ۱. دور زدن قفل دونیت (برای فعال شدن آیتم‌های UI)
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
            XposedBridge.log(TAG + ": Donate check bypassed.");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": Failed to hook donate check: " + t.getMessage());
        }

        // ۲. اجبار منوی پیام iOS (Blur) به روشن بودن
        try {
            XposedHelpers.findAndHookMethod(
                    "uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig",
                    lpparam.classLoader,
                    "getBlurMessageMenuBackground",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            return true;
                        }
                    }
            );
            XposedBridge.log(TAG + ": BlurMessageMenuBackground forced to true.");
        } catch (Throwable t) {
            XposedBridge.log(TAG + ": Failed to hook getBlurMessageMenuBackground: " + t.getMessage());
        }

        // ۳. (اختیاری) اجبار Unified Scroll به true برای تجربه iOS کامل‌تر
        try {
            XposedHelpers.findAndHookMethod(
                    "uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig",
                    lpparam.classLoader,
                    "getMsgMenuUnifiedScroll",
                    new XC_MethodReplacement() {
                        @Override
                        protected Object replaceHookedMethod(MethodHookParam param) {
                            return true;
                        }
                    }
            );
        } catch (Throwable ignored) {}
    }
}
