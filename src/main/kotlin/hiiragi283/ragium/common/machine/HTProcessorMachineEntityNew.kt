package hiiragi283.ragium.common.machine

import hiiragi283.ragium.api.extension.component1
import hiiragi283.ragium.api.extension.component2
import hiiragi283.ragium.api.inventory.HTSimpleInventory
import hiiragi283.ragium.api.inventory.HTStorageBuilder
import hiiragi283.ragium.api.inventory.HTStorageIO
import hiiragi283.ragium.api.inventory.HTStorageSide
import hiiragi283.ragium.api.machine.HTMachineConvertible
import hiiragi283.ragium.api.machine.HTMachineEntityNew
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.recipe.machines.HTGrinderRecipe
import hiiragi283.ragium.api.recipe.machines.HTMachineRecipeProcessor
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.screen.HTProcessorScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.recipe.input.SingleStackRecipeInput
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

open class HTProcessorMachineEntityNew(type: HTMachineConvertible, tier: HTMachineTier) : HTMachineEntityNew(type, tier) {
    private val recipeCache: HTRecipeCache<SingleStackRecipeInput, HTGrinderRecipe> =
        HTRecipeCache(RagiumRecipeTypes.GRINDER)

    override fun tickSecond(world: World, pos: BlockPos, state: BlockState) {
        recipeCache
            .getFirstMatch(
                SingleStackRecipeInput(getStack(0)),
                world,
            ).ifPresent { (id: Identifier, recipe: HTGrinderRecipe) ->
                HTMachineRecipeProcessor.process(
                    world,
                    pos,
                    recipe,
                    definition,
                    parent,
                )
            }
    }

    final override val parent: HTSimpleInventory = HTStorageBuilder(5)
        .set(0, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSide.ANY)
        .set(2, HTStorageIO.INTERNAL, HTStorageSide.NONE)
        .set(3, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .set(4, HTStorageIO.OUTPUT, HTStorageSide.ANY)
        .buildSimple()

    final override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTProcessorScreenHandler(
            syncId,
            playerInventory,
            packet,
            parentBE.ifPresentWorld { world: World ->
                ScreenHandlerContext.create(world, parentBE.pos)
            } ?: ScreenHandlerContext.EMPTY,
        )
}
