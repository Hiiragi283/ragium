package hiiragi283.ragium.common.block.machine

import hiiragi283.ragium.api.block.HTBlockEntityBase
import hiiragi283.ragium.api.extension.dropStackAt
import hiiragi283.ragium.api.extension.getStackInActiveHand
import hiiragi283.ragium.api.extension.isOf
import hiiragi283.ragium.api.extension.modifyStack
import hiiragi283.ragium.api.machine.HTMachinePropertyKeys
import hiiragi283.ragium.api.machine.HTMachineTier
import hiiragi283.ragium.api.recipe.HTMachineInput
import hiiragi283.ragium.api.recipe.HTMachineRecipe
import hiiragi283.ragium.api.recipe.HTRecipeCache
import hiiragi283.ragium.api.storage.HTMachineInventory
import hiiragi283.ragium.common.init.*
import net.minecraft.block.BlockState
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import kotlin.jvm.optionals.getOrNull

class HTManualForgeBlockEntity(pos: BlockPos, state: BlockState) : HTBlockEntityBase(RagiumBlockEntityTypes.MANUAL_FORGE, pos, state) {
    private val recipeCache: HTRecipeCache<HTMachineInput, HTMachineRecipe> =
        HTRecipeCache(RagiumRecipeTypes.MACHINE)

    private val inventory: HTMachineInventory = HTMachineInventory.Builder(1).generic(0).build()

    override fun asInventory(): SidedInventory = inventory

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hit: BlockHitResult,
    ): ActionResult {
        process(player)
        return ActionResult.success(world.isClient)
    }

    private fun process(player: PlayerEntity) {
        val world: World = world ?: return
        val stackMain: ItemStack = player.getStackInActiveHand()
        if (!stackMain.isOf(RagiumItemsNew.FORGE_HAMMER)) {
            inventory.modifyStack(0) { stack: ItemStack ->
                dropStackAt(player, stack)
                val copies: ItemStack = stackMain.copy()
                stackMain.count = 0
                copies
            }
            return
        }
        val invStack: ItemStack = inventory.getStack(0)
        val recipe: HTMachineRecipe =
            recipeCache
                .getFirstMatch(
                    HTMachineInput.create(
                        RagiumMachineKeys.COMPRESSOR,
                        HTMachineTier.PRIMITIVE,
                    ) {
                        add(invStack)
                        catalyst = ItemStack(RagiumItemsNew.PressMolds.PLATE)
                    },
                    world,
                ).result()
                .getOrNull()
                ?: return
        dropStackAt(player, recipe.getResult(world.registryManager))
        stackMain.damage(1, player, EquipmentSlot.MAINHAND)
        invStack.decrement(recipe.itemInputs.getOrNull(0)?.count ?: 0)
        RagiumMachineKeys.COMPRESSOR.entry.ifPresent(HTMachinePropertyKeys.SOUND) {
            world.playSound(null, pos, it, SoundCategory.BLOCKS)
        }
    }
}
