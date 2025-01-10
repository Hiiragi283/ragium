package hiiragi283.ragium.api.multiblock

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTMutablePropertyHolder
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.block.BlockState
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos

/**
 * マルチブロックを構成する要素を表すインターフェース
 */
interface HTMultiblockComponent {
    val type: Type<*>

    fun getBlockName(controller: HTControllerDefinition): Text

    fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean

    fun getPlacementState(controller: HTControllerDefinition): BlockState?

    fun collectData(controller: HTControllerDefinition, pos: BlockPos, holder: HTMutablePropertyHolder) {}

    //    Type    //

    interface Type<T : HTMultiblockComponent> {
        companion object {
            @JvmField
            val REGISTRY_KEY: RegistryKey<Registry<Type<*>>> =
                RegistryKey.ofRegistry<Type<*>>(RagiumAPI.id("multiblock_component_type"))

            @JvmField
            val REGISTRY: Registry<Type<*>> = FabricRegistryBuilder
                .createSimple(REGISTRY_KEY)
                .attribute(RegistryAttribute.SYNCED)
                .buildAndRegister()
        }
    }
}
