package top.lihugang.mc.mod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.text.Text;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import top.lihugang.mc.mod.client.utils.SystemInfoCollector;
import top.lihugang.mc.mod.client.utils.defaultConfigGenerator;
import top.lihugang.mc.mod.client.utils.gameInfoCollector.fabricGameInfoCollector;
import top.lihugang.mc.mod.client.utils.outputGenerator;

public class mcfetchClient implements ClientModInitializer {

    public static final String MOD_ID = "mcfetch";
    public static final Logger logger = LoggerFactory.getLogger(MOD_ID);
    @Override
    public void onInitializeClient() {

        long minecraftStartTime = new Date().getTime();

        defaultConfigGenerator.generate(logger);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal(MOD_ID).executes(context -> {
                List<AbstractMap.SimpleEntry<String, String>> gameInfo = fabricGameInfoCollector.collect(minecraftStartTime);
                List<AbstractMap.SimpleEntry<String, String>> sysInfo = SystemInfoCollector.collect();

                List<List<AbstractMap.SimpleEntry<String, String>>> list = new ArrayList<>();
                list.add(gameInfo);
                list.add(sysInfo);

                String commandResponse = outputGenerator.generate("fabric", list);

                logger.info(commandResponse);

                context.getSource().sendFeedback(Text.literal(commandResponse));
                return 1;
            }));
        });
    }
}
