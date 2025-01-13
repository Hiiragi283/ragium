package hiiragi283.ragium.api.multiblock

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.property.HTPropertyHolderBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.state.BlockState

/**
 * マルチブロックを構成する要素を表すインターフェース
 */
interface HTMultiblockComponent {
    val type: Type<*>

    fun getBlockName(controller: HTControllerDefinition): Component

    fun checkState(controller: HTControllerDefinition, pos: BlockPos): Boolean

    fun getPlacementState(controller: HTControllerDefinition): BlockState?

    fun collectData(controller: HTControllerDefinition, pos: BlockPos, holder: HTPropertyHolderBuilder) {}

    //    Type    //

    interface Type<T : HTMultiblockComponent> {
        companion object {
            @JvmField
            val REGISTRY_KEY: ResourceKey<Registry<Type<*>>> =
                ResourceKey.createRegistryKey<Type<*>>(RagiumAPI.id("multiblock_component_type"))
        }
    }
}
