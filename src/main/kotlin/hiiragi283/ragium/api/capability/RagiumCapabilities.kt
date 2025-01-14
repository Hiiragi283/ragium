package hiiragi283.ragium.api.capability

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.multiblock.HTControllerHolder
import net.minecraft.core.Direction
import net.neoforged.neoforge.capabilities.BlockCapability

object RagiumCapabilities {
    //    Block    //

    @JvmField
    val CONTROLLER_HOLDER: BlockCapability<HTControllerHolder, Direction?> =
        BlockCapability.createSided(RagiumAPI.Companion.id("controller_holder"), HTControllerHolder::class.java)

    @JvmField
    val MACHINE_TIER: BlockCapability<HTMachineTier, Void?> =
        BlockCapability.createVoid(RagiumAPI.Companion.id("machine_tier"), HTMachineTier::class.java)
}
