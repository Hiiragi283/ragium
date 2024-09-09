package hiiragi283.ragium.common.block.entity

import hiiragi283.ragium.common.inventory.*
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import hiiragi283.ragium.common.recipe.HTMachineType
import hiiragi283.ragium.common.recipe.HTRecipeInput
import hiiragi283.ragium.common.recipe.HTRecipeResult
import hiiragi283.ragium.common.screen.HTMachineScreenHandler
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeEntry
import net.minecraft.recipe.RecipeManager
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTSingleMachineBlockEntity(
    pos: BlockPos,
    state: BlockState,
    val type: HTMachineType = HTMachineType.GRINDER,
) : BlockEntity(HTMachineType.Variant.SINGLE.blockEntityType, pos, state), HTDelegatedInventory,
    NamedScreenHandlerFactory {

    companion object {
        @JvmField
        val TICKER: BlockEntityTicker<HTSingleMachineBlockEntity> =
            BlockEntityTicker { world: World, _: BlockPos, _: BlockState, tile: HTSingleMachineBlockEntity ->
                if (tile.ticks >= 20) {
                    tile.ticks = 0
                    val recipe: HTMachineRecipe = tile.matchGetter.getFirstMatch(
                        HTRecipeInput {
                            add(tile.getStack(0))
                            add(tile.getStack(1))
                            add(tile.getStack(2))
                        },
                        world
                    ).map(RecipeEntry<HTMachineRecipe>::value).getOrNull() ?: return@BlockEntityTicker
                    if (canAcceptOutputs(tile, recipe)) {

                    }
                } else {
                    tile.ticks++
                }
            }

        @JvmStatic
        private fun canAcceptOutputs(tile: HTSingleMachineBlockEntity, recipe: HTMachineRecipe): Boolean {
            recipe.outputs.forEachIndexed { index: Int, result: HTRecipeResult ->
                val stackIn: ItemStack = tile.getStack(index + 3)
                if (!result.canAccept(stackIn)) {
                    return false
                }
            }
            return true
        }
    }

    var ticks: Int = 0
        private set
    private val matchGetter: RecipeManager.MatchGetter<HTRecipeInput, HTMachineRecipe> =
        RecipeManager.createCachedMatchGetter(type)

    //    HTDelegatedInventory    //

    override val parent: HTSidedInventory = HTSidedStorageBuilder(7)
        .set(0, HTStorageIO.INPUT, HTStorageSides.ANY)
        .set(1, HTStorageIO.INPUT, HTStorageSides.ANY)
        .set(2, HTStorageIO.INPUT, HTStorageSides.ANY)
        .set(3, HTStorageIO.INTERNAL, HTStorageSides.NONE)
        .set(4, HTStorageIO.OUTPUT, HTStorageSides.ANY)
        .set(5, HTStorageIO.OUTPUT, HTStorageSides.ANY)
        .set(6, HTStorageIO.OUTPUT, HTStorageSides.ANY)
        .buildInventory()

    override fun markDirty() {
        super<BlockEntity>.markDirty()
    }

    //    NamedScreenHandlerFactory    //

    override fun createMenu(syncId: Int, playerInventory: PlayerInventory, player: PlayerEntity): ScreenHandler =
        HTMachineScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, pos))

    override fun getDisplayName(): Text = type.text

}