package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.machine.HTAxisMultiblockComponent
import hiiragi283.ragium.common.machine.HTSimpleMultiblockComponent
import hiiragi283.ragium.common.machine.HTTagMultiblockComponent
import hiiragi283.ragium.common.machine.HTTieredMultiblockComponent
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RagiumMultiblockComponentTypes {
    @JvmField
    val REGISTER: DeferredRegister<HTMultiblockComponent.Type<*>> =
        DeferredRegister.create(RagiumAPI.RegistryKeys.MULTIBLOCK_COMPONENT_TYPE, RagiumAPI.MOD_ID)

    val AXIS: DeferredHolder<HTMultiblockComponent.Type<*>, out HTMultiblockComponent.Type<HTAxisMultiblockComponent>> =
        register("axis")

    @JvmField
    val SIMPLE: DeferredHolder<HTMultiblockComponent.Type<*>, out HTMultiblockComponent.Type<HTSimpleMultiblockComponent>> =
        register("simple")

    val TAG: DeferredHolder<HTMultiblockComponent.Type<*>, out HTMultiblockComponent.Type<HTTagMultiblockComponent>> =
        register("tag")

    val TIER: DeferredHolder<HTMultiblockComponent.Type<*>, out HTMultiblockComponent.Type<HTTieredMultiblockComponent>> =
        register("tier")

    @JvmStatic
    private fun <T : HTMultiblockComponent> register(
        name: String,
    ): DeferredHolder<HTMultiblockComponent.Type<*>, out HTMultiblockComponent.Type<T>> = REGISTER.register(name) { id: ResourceLocation ->
        object : HTMultiblockComponent.Type<T> {
            override fun toString(): String = id.toString()
        }
    }
}
