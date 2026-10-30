package com.example.module;

import io.github.libxposed.api.XposedInterface;
import io.github.libxposed.api.XposedModule;
import io.github.libxposed.api.XposedModuleInterface;
import java.lang.reflect.Executable;

public class MainModule extends XposedModule {
    private static final String TAG = "CherryHook_Debug";

    @Override
    public void onPackageLoaded(XposedModuleInterface.PackageLoadedParam param) {
        // فقط روی پکیج چری‌گرام کار کن
        if (!param.getPackageName().equals("uz.unnarsx.cherrygram")) {
            return;
        }

        log(4, TAG, "🚀 ماژول در چری‌گرام لود شد (API 102).");

        // --- ۱. دور زدن سیستم دونیت (کلاس 㥿) ---
        try {
            Class<?> donateClass = param.getClassLoader().loadClass("uz.unnarsx.cherrygram.donates.㥿");

            // هوک کردن متد ώ (دونیت سطح پایه)
            Executable method1 = donateClass.getDeclaredMethod("ώ");
            hook(method1).setExceptionMode(XposedInterface.ExceptionMode.PROTECTIVE).intercept(chain -> {
                log(4, TAG, "✅ متد ώ() صدا زده شد -> بازگشت True");
                return true;
            });

            // هوک کردن متد 㝸 (دونیت VIP)
            Executable method2 = donateClass.getDeclaredMethod("㝸");
            hook(method2).setExceptionMode(XposedInterface.ExceptionMode.PROTECTIVE).intercept(chain -> {
                log(4, TAG, "✅ متد 㝸() صدا زده شد -> بازگشت True");
                return true;
            });

            log(4, TAG, "✅ سیستم دونیت با موفقیت هک شد.");
        } catch (Throwable t) {
            log(4, TAG, "❌ خطا در هک کردن کلاس دونیت: " + t.getMessage());
        }

        // --- ۲. هک کردن تنظیمات منوی iOS (برای اطمینان) ---
        try {
            Class<?> configClass = param.getClassLoader().loadClass("uz.unnarsx.cherrygram.core.configs.CherrygramMessagesConfig");
            Executable method3 = configClass.getDeclaredMethod("getBlurMessageMenuBackground");
            hook(method3).setExceptionMode(XposedInterface.ExceptionMode.PROTECTIVE).intercept(chain -> {
                log(4, TAG, "✅ متد Blur صدا زده شد -> بازگشت True");
                return true;
            });
            log(4, TAG, "✅ تنظیمات منوی iOS به زور روشن شد.");
        } catch (Throwable t) {
            log(4, TAG, "⚠️ خطا در هک منوی iOS: " + t.getMessage());
        }
    }
}
