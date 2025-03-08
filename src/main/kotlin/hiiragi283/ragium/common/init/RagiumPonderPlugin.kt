package hiiragi283.ragium.common.init

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.extension.asHolder
import hiiragi283.ragium.api.extension.idOrThrow
import hiiragi283.ragium.api.machine.HTMachineType
import net.createmod.ponder.api.registration.PonderPlugin
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper
import net.createmod.ponder.api.scene.SceneBuilder
import net.createmod.ponder.api.scene.SceneBuildingUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Blocks

/**
 * @sample [net.createmod.ponder.foundation.content.DebugScenes]
 */
object RagiumPonderPlugin : PonderPlugin {
    override fun getModId(): String = RagiumAPI.MOD_ID

    //    Scene    //

    override fun registerScenes(helper: PonderSceneRegistrationHelper<ResourceLocation>) {
        val itemHelper: PonderSceneRegistrationHelper<ItemLike> =
            helper.withKeyFunction { item: ItemLike -> item.asHolder().idOrThrow }

        itemHelper.addStoryBoard(
            HTMachineType.FISHER,
            "machine/fisher",
            { builder: SceneBuilder, util: SceneBuildingUtil ->
                builder.title("multiblock", "Coordinate Space")
                builder.showBasePlate()
                builder.idle(10)
                builder.world().showSection(util.select().layersFrom(1), Direction.DOWN)
                builder.idle(10)
                // Place Water
                val waterPos: BlockPos = util.grid().at(2, 1, 2)
                // val waterSelection: Selection = util.select().position(waterPos)
                builder.idle(20)
                builder.world().setBlock(
                    waterPos,
                    Blocks.WATER.defaultBlockState(),
                    false,
                )
                builder.idle(20)
                // Place Barrel
            },
            MACHINES,
        )
    }

    //     Tag    //

    @JvmField
    val MACHINES: ResourceLocation = RagiumAPI.id("machines")

    @JvmField
    val MULTIBLOCK: ResourceLocation = RagiumAPI.id("multiblock")

    override fun registerTags(helper: PonderTagRegistrationHelper<ResourceLocation>) {
        val itemHelper: PonderTagRegistrationHelper<ItemLike> =
            helper.withKeyFunction { item: ItemLike -> item.asHolder().idOrThrow }
    }
}
