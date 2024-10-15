package elysia.file;

import elysia.ElysiaPotion;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class FileListener {

    private static final long POLL_INTERVAL = TimeUnit.SECONDS.toMillis(1);
    // 获取所有文件 只有初始化的时候需要用到 这个 commons io 监听精确到每一个文件
    public static void startWatching(File rootDir) {
        FileAlterationObserver observer = new FileAlterationObserver(rootDir);
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                ElysiaPotion.getInstance().getLogger().info("检测到文件变更，自动重载");
                ElysiaPotion.getPotionManager().loadPotionData();
                ElysiaPotion.getConfigManager().loadConfig();
            }

            @Override
            public void onFileChange(File file) {
                ElysiaPotion.getInstance().getLogger().info("检测到文件变更，自动重载");
                ElysiaPotion.getPotionManager().loadPotionData();
                ElysiaPotion.getConfigManager().loadConfig();
            }

            @Override
            public void onFileDelete(File file) {
                ElysiaPotion.getInstance().getLogger().info("检测到文件变更，自动重载");
                ElysiaPotion.getPotionManager().loadPotionData();
                ElysiaPotion.getConfigManager().loadConfig();
            }

            @Override
            public void onDirectoryCreate(File directory) {
                ElysiaPotion.getInstance().getLogger().info("检测到文件变更，自动重载");
                ElysiaPotion.getPotionManager().loadPotionData();
                ElysiaPotion.getConfigManager().loadConfig();
            }

            @Override
            public void onDirectoryChange(File directory) {
                ElysiaPotion.getInstance().getLogger().info("检测到文件变更，自动重载");
                ElysiaPotion.getPotionManager().loadPotionData();
                ElysiaPotion.getConfigManager().loadConfig();
            }

            @Override
            public void onDirectoryDelete(File directory) {
                ElysiaPotion.getInstance().getLogger().info("检测到文件变更，自动重载");
                ElysiaPotion.getPotionManager().loadPotionData();
                ElysiaPotion.getConfigManager().loadConfig();
            }
        });

        FileAlterationMonitor monitor = new FileAlterationMonitor(POLL_INTERVAL, observer);
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
