package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.multiblock.HTMultiblockComponent
import hiiragi283.ragium.common.machine.*
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

object RagiumMultiblockComponentTypes {
    @JvmField
    val AXIS: HTMultiblockComponent.Type<HTAxisMultiblockComponent> = register("axis")

    @JvmField
    val MACHINE: HTMultiblockComponent.Type<HTMachineMultiblockComponent> = register("machine")

    @JvmField
    val SIMPLE: HTMultiblockComponent.Type<HTSimpleMultiblockComponent> = register("simple")

    @JvmField
    val TAG: HTMultiblockComponent.Type<HTTagMultiblockComponent> = register("tag")

    @JvmField
    val TIER: HTMultiblockComponent.Type<HTTieredMultiblockComponent> = register("tier")

    @JvmStatic
    private fun <T : HTMultiblockComponent> register(name: String): HTMultiblockComponent.Type<T> {
        val id: Identifier = RagiumAPI.id(name)
        return Registry.register(
            HTMultiblockComponent.Type.REGISTRY,
            id,
            object : HTMultiblockComponent.Type<T> {
                override fun toString(): String = id.toString()
            },
        )
    }
}
