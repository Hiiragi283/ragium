package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.RagiumRegistries
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.machine.HTSimpleMultiblockComponent
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMultiblockComponentTypes {
    @JvmField
    val REGISTRY: DeferredRegister<HTMultiblockComponent.Type<*>> =
        DeferredRegister.create(RagiumRegistries.Keys.MULTIBLOCK_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    // val AXIS: HTMultiblockComponent.Type<HTAxisMultiblockComponent> = register("axis")

    @JvmField
    val SIMPLE: DeferredHolder<HTMultiblockComponent.Type<*>, out HTMultiblockComponent.Type<HTSimpleMultiblockComponent>> =
        register<HTSimpleMultiblockComponent>("simple")

    // val TAG: HTMultiblockComponent.Type<HTTagMultiblockComponent> = register("tag")

    // val TIER: HTMultiblockComponent.Type<HTTieredMultiblockComponent> = register("tier")

    @JvmStatic
    private fun <T : HTMultiblockComponent> register(
        name: String,
    ): DeferredHolder<HTMultiblockComponent.Type<*>, out HTMultiblockComponent.Type<T>> = REGISTRY.register(name) { id: ResourceLocation ->
        object : HTMultiblockComponent.Type<T> {
            override fun toString(): String = id.toString()
        }
    }
}
