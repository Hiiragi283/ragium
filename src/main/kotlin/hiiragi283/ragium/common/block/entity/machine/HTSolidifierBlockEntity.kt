package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.block.entity.HTFluidInteractable
import hiiragi283.ragium.api.inventory.HTSlotHelper
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTFluidWithCatalystToItemRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.storage.fluid.HTFluidStackTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumMenuTypes
import hiiragi283.ragium.util.variant.HTMachineVariant
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class HTSolidifierBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTItemWithFluidRecipeInput, HTFluidWithCatalystToItemRecipe>(
        RagiumRecipeTypes.SOLIDIFYING.get(),
        HTMachineVariant.SOLIDIFIER,
        pos,
        state,
    ),
    HTFluidInteractable {
    override val inventory: HTItemHandler = HTItemStackHandler
        .Builder(2)
        .addInput(0)
        .addOutput(1)
        .build(this)
    private val tank = HTFluidStackTank(variant.tankCapacity, this)

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader.read(RagiumConst.TANK, tank)
    }

    override fun openGui(player: Player, title: Component): InteractionResult =
        RagiumMenuTypes.SOLIDIFIER.openMenu(player, title, this, ::writeExtraContainerData)

    override fun createSound(random: RandomSource, pos: BlockPos): SoundInstance = createSound(SoundEvents.FIRE_EXTINGUISH, random, pos)

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inventory.getStackInSlot(0), tank.fluid)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(
        level: ServerLevel,
        input: HTItemWithFluidRecipeInput,
        recipe: HTFluidWithCatalystToItemRecipe,
    ): Boolean = insertToOutput(recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun completeRecipe(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTFluidWithCatalystToItemRecipe,
    ) {
        // 実際にアウトプットに搬出する
        insertToOutput(recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        tank.drain(recipe.ingredient, false)
    }

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler = HTFilteredFluidHandler(tank, HTFluidFilter.FILL_ONLY)

    //    HTFluidInteractable    //

    override fun interactWith(level: Level, player: Player, hand: InteractionHand): ItemInteractionResult = interactWith(player, hand, tank)

    //    Slot    //

    override fun addInputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 0, HTSlotHelper.getSlotPosX(3), HTSlotHelper.getSlotPosY(1))
    }

    override fun addOutputSlot(consumer: (handler: IItemHandler, index: Int, x: Int, y: Int) -> Unit) {
        consumer(inventory, 1, HTSlotHelper.getSlotPosX(6.5), HTSlotHelper.getSlotPosY(1))
    }
}
