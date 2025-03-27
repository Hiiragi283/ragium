package hiiragi283.ragium.api.addon

import hiiragi283.ragium.api.material.HTMaterialKey
import hiiragi283.ragium.api.material.HTMaterialType
import hiiragi283.ragium.api.property.HTMutablePropertyMap
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import java.util.function.BiConsumer
import java.util.function.Function

/**
 * Ragiumのアドオン向けのインターフェース
 *
 * これを実装したクラスは[HTAddon]アノテーションをつける必要があります。
 */
interface RagiumAddon {
    /**
     * アドオンの優先度を返します。
     *
     * 値が大きいほど優先して読み込まれます。
     */
    val priority: Int

    /**
     * modのコンストラクタで呼び出されます。
     */
    fun onModConstruct(eventBus: IEventBus, dist: Dist) {}

    /**
     * [FMLConstructModEvent]中に呼び出されます。
     */
    fun onMaterialRegister(consumer: BiConsumer<HTMaterialKey, HTMaterialType>) {}

    /**
     * [onMaterialRegister]の後で呼び出されます。
     */
    fun onMaterialSetup(getter: Function<HTMaterialKey, HTMutablePropertyMap>) {}

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
}
