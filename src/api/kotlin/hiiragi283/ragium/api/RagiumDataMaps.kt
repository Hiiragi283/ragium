package hiiragi283.ragium.api

import hiiragi283.ragium.api.data.HTTreeTap
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

/**
 * @see [net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps]
 */
object RagiumDataMaps {
    @JvmField
    val TREE_TAP: DataMapType<Fluid, HTTreeTap> =
        DataMapType
            .builder(RagiumAPI.id("tree_tap"), Registries.FLUID, HTTreeTap.CODEC)
            .synced(HTTreeTap.CODEC, false)
            .build()
}
