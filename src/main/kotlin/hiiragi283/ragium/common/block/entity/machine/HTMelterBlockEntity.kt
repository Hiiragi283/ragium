package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTItemToFluidRecipe
import hiiragi283.ragium.api.storage.HTContentListener
import hiiragi283.ragium.api.storage.HTStorageAccess
import hiiragi283.ragium.api.storage.fluid.HTFluidInteractable
import hiiragi283.ragium.api.storage.holder.HTFluidTankHolder
import hiiragi283.ragium.api.storage.holder.HTItemSlotHolder
import hiiragi283.ragium.api.storage.item.HTItemSlot
import hiiragi283.ragium.common.recipe.HTDynamicRecipeCache
import hiiragi283.ragium.common.recipe.HTDynamicRecipes
import hiiragi283.ragium.common.storage.fluid.HTVariableFluidStackTank
import hiiragi283.ragium.common.storage.holder.HTSimpleFluidTankHolder
import hiiragi283.ragium.common.storage.holder.HTSimpleItemSlotHolder
import hiiragi283.ragium.common.storage.item.slot.HTItemStackSlot
import hiiragi283.ragium.common.variant.HTMachineVariant
import hiiragi283.ragium.config.RagiumConfig
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HTMelterBlockEntity(pos: BlockPos, state: BlockState) :
    HTSingleItemInputBlockEntity<HTItemToFluidRecipe>(
        HTDynamicRecipeCache(RagiumRecipeTypes.MELTING.get(), HTDynamicRecipes::bucketEmptying),
        HTMachineVariant.MELTER,
        pos,
        state,
    ),
    HTFluidInteractable {
    private lateinit var outputSlot: HTItemSlot

    override fun initializeItemHandler(listener: HTContentListener): HTItemSlotHolder {
        // input
        inputSlot = HTItemStackSlot.input(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(0))
        // output
        outputSlot = HTItemStackSlot.output(listener, HTSlotHelper.getSlotPosX(2), HTSlotHelper.getSlotPosY(2))
        return HTSimpleItemSlotHolder(this, listOf(inputSlot), listOf(outputSlot))
    }

    private lateinit var tank: HTVariableFluidStackTank

    override fun initializeFluidHandler(listener: HTContentListener): HTFluidTankHolder {
        tank = HTVariableFluidStackTank.output(listener, RagiumConfig.CONFIG.melterTankCapacity)
        return HTSimpleFluidTankHolder.output(this, tank)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.MELTER.openMenu(player, title, this, ::writeExtraContainerData)

    //    Ticking    //

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: SingleRecipeInput, recipe: HTItemToFluidRecipe): Boolean =
        tank.insert(recipe.assembleFluid(input, level.registryAccess()), true, HTStorageAccess.INTERNAl).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: SingleRecipeInput,
        recipe: HTItemToFluidRecipe,
    ) {
        // 実際にアウトプットに搬出する
        tank.insert(recipe.assembleFluid(input, level.registryAccess()), false, HTStorageAccess.INTERNAl)
        val stack: ItemStack = input.item()
        if (stack.hasCraftingRemainingItem()) {
            outputSlot.insertItem(stack.craftingRemainingItem, false, HTStorageAccess.INTERNAl)
        }
        // インプットを減らす
        inputSlot.shrinkStack(recipe.ingredient, false)
        // SEを鳴らす
        level.playSound(null, pos, SoundEvents.WITCH_DRINK, SoundSource.BLOCKS, 1f, 0.5f)
    }

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)
}
