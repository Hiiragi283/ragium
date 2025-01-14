package hiiragi283.ragium.data

import com.mojang.logging.LogUtils
import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.data.client.RagiumBlockStateProvider
import hiiragi283.ragium.data.client.RagiumEnglishProvider
import hiiragi283.ragium.data.client.RagiumJapaneseProvider
import hiiragi283.ragium.data.client.RagiumModelProvider
import hiiragi283.ragium.data.server.RagiumFluidTagProvider
import hiiragi283.ragium.data.server.RagiumItemTagProvider
import hiiragi283.ragium.data.server.RagiumRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.DataGenerator
import net.minecraft.data.PackOutput
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.neoforged.neoforge.data.event.GatherDataEvent
import org.slf4j.Logger
import java.util.concurrent.CompletableFuture

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = RagiumAPI.MOD_ID)
object RagiumData {
    @JvmField
    val LOGGER: Logger = LogUtils.getLogger()

    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator: DataGenerator = event.generator
        val output: PackOutput = generator.packOutput
        val helper: ExistingFileHelper = event.existingFileHelper
        val provider: CompletableFuture<HolderLookup.Provider> = event.lookupProvider
        // server
        generator.addProvider(event.includeServer(), RagiumRecipeProvider(output, provider))

        generator.addProvider(event.includeServer(), RagiumFluidTagProvider(output, provider, helper))
        generator.addProvider(event.includeServer(), RagiumItemTagProvider(output, provider, helper))
        // client
        generator.addProvider(event.includeClient(), ::RagiumEnglishProvider)
        generator.addProvider(event.includeClient(), ::RagiumJapaneseProvider)

        generator.addProvider(event.includeClient(), RagiumBlockStateProvider(output, helper))
        generator.addProvider(event.includeClient(), RagiumModelProvider(output, helper))

        LOGGER.info("Gathered client resources!")
    }
}
