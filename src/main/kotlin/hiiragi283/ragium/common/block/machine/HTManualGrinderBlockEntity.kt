package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.getOrNull
import hiiragi283.ragium.api.extension.replaceBlockState
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.RagiumBlockEntityTypes
import hiiragi283.ragium.common.init.RagiumBlockProperties
import hiiragi283.ragium.common.init.RagiumMachineKeys
import hiiragi283.ragium.common.init.RagiumRecipeTypes
import hiiragi283.ragium.common.recipe.HTMachineRecipe
import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class HTManualGrinderBlockEntity(pos: BlockPos, state: BlockState) :
    HTBlockEntityBase(RagiumBlockEntityTypes.MANUAL_GRINDER, pos, state) {
    private val inventory: HTMachineInventory = HTMachineInventory.Builder(1).input(0).build()

    override fun asInventory(): SidedInventory = inventory

    override fun onRightClicked(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        val step: Int = state.get(RagiumBlockProperties.LEVEL_7)
        if (step == 7) {
            process(player)
        }
        if (!world.isClient) {
            world.replaceBlockState(pos) { stateIn: BlockState ->
                stateIn.with(RagiumBlockProperties.LEVEL_7, (step + 1) % 8)
            }
        }
        return ActionResult.success(world.isClient)
    }

    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache(RagiumRecipeTypes.MACHINE)

    private fun process(player: PlayerEntity) {
        val world: World = world ?: return
        val stackIn: ItemStack = inventory.getStack(0)
        val recipe: HTMachineRecipe =
            recipeCache
                .getFirstMatch(
                    HTMachineInput.create(
                        RagiumMachineKeys.GRINDER,
                        HTMachineTier.PRIMITIVE,
                    ) { add(stackIn) },
                    world,
                ).getOrNull() ?: return
        dropStackAt(player, recipe.getResult(world.registryManager))
        stackIn.decrement(recipe.getItemIngredient(0)?.count ?: 0)
        RagiumMachineKeys.GRINDER.getEntryOrNull()?.ifPresent(HTMachinePropertyKeys.SOUND) {
            world.playSound(null, pos, it, SoundCategory.BLOCKS)
        }
    }
}
