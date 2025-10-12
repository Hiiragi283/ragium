package hiiragi283.ragium.api.addon

import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.material.HTMaterialVariant
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModList
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import java.util.function.BiConsumer

/**
 * Ragiumのアドオン向けのインターフェース
 * これを実装したクラスは[HTAddon]アノテーションをつける必要があります。
 */
interface RagiumAddon {
    fun priority(): Int = 0

    //    Initialization    //

    /**
     * modのコンストラクタで呼び出されます。
     */
    fun onModConstruct(eventBus: IEventBus, dist: Dist) {}

    /**
     * [FMLCommonSetupEvent]中に呼び出されます。
     */
    fun onCommonSetup(event: FMLCommonSetupEvent) {}

    /**
     * [FMLDedicatedServerSetupEvent]中に呼び出されます。
     */
    fun onServerSetup(event: FMLDedicatedServerSetupEvent) {}

    /**
     * [FMLClientSetupEvent]中に呼び出されます。
     */

    fun onClientSetup(event: FMLClientSetupEvent) {}

    //    Extension    //

    fun registerMaterial(consumer: BiConsumer<HTMaterialType, HTMaterialVariant.ItemTag>) {}

    //    Provider    //

    /**
     * これを実装したクラスは[HTAddon]アノテーションをつける必要があります。
     */
    fun interface Provider {
        fun getAddons(modList: ModList): List<RagiumAddon>
    }
}
