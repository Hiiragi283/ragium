package hiiragi283.ragium.common.block.entity.machine

import hiiragi283.ragium.api.RagiumAPI
import hiiragi283.ragium.api.network.HTNbtCodec
import hiiragi283.ragium.api.recipe.RagiumRecipeTypes
import hiiragi283.ragium.api.recipe.base.HTInfusingRecipe
import hiiragi283.ragium.api.recipe.input.HTItemWithFluidRecipeInput
import hiiragi283.ragium.api.storage.fluid.HTFilteredFluidHandler
import hiiragi283.ragium.api.storage.fluid.HTFluidFilter
import hiiragi283.ragium.api.storage.item.HTFilteredItemHandler
import hiiragi283.ragium.api.storage.item.HTItemFilter
import hiiragi283.ragium.api.storage.item.HTItemHandler
import hiiragi283.ragium.api.util.RagiumConst
import hiiragi283.ragium.common.inventory.HTItemWithFluidToItemMenu
import hiiragi283.ragium.common.network.HTFluidSlotUpdatePacket
import hiiragi283.ragium.common.storage.fluid.HTFluidTank
import hiiragi283.ragium.common.storage.item.HTItemStackHandler
import hiiragi283.ragium.setup.RagiumBlockEntityTypes
import hiiragi283.ragium.setup.RagiumMenuTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler

class HTInfuserBlockEntity(pos: BlockPos, state: BlockState) :
    HTProcessorBlockEntity<HTItemWithFluidRecipeInput, HTInfusingRecipe>(
        RagiumRecipeTypes.INFUSING.get(),
        RagiumBlockEntityTypes.INFUSER,
        pos,
        state,
    ) {
    override val inventory: HTItemHandler = HTItemStackHandler(2, this::setChanged)
    private val tank = HTFluidTank(RagiumAPI.getConfig().getDefaultTankCapacity(), this::setChanged)
    override val energyUsage: Int get() = RagiumAPI.getConfig().getAdvancedMachineEnergyUsage()

    override fun writeNbt(writer: HTNbtCodec.Writer) {
        super.writeNbt(writer)
        writer.write(RagiumConst.TANK, tank)
    }

    override fun readNbt(reader: HTNbtCodec.Reader) {
        super.readNbt(reader)
        reader.read(RagiumConst.TANK, tank)
    }

    override fun sendUpdatePacket(serverLevel: ServerLevel, consumer: (CustomPacketPayload) -> Unit) {
        super.sendUpdatePacket(serverLevel, consumer)
        consumer(HTFluidSlotUpdatePacket(0, tank.fluid))
    }

    //    Ticking    //

    override fun createRecipeInput(level: ServerLevel, pos: BlockPos): HTItemWithFluidRecipeInput =
        HTItemWithFluidRecipeInput(inventory.getStackInSlot(0), tank.fluid)

    // アウトプットに搬出できるか判定する
    override fun canProgressRecipe(level: ServerLevel, input: HTItemWithFluidRecipeInput, recipe: HTInfusingRecipe): Boolean =
        insertToOutput(1..1, recipe.assemble(input, level.registryAccess()), true).isEmpty

    override fun serverTickPost(
        level: ServerLevel,
        pos: BlockPos,
        state: BlockState,
        input: HTItemWithFluidRecipeInput,
        recipe: HTInfusingRecipe,
    ) {
        // 周囲のエンチャントパワーを計算する
        /*val aroundCost: Float = EnchantingTableBlock.BOOKSHELF_OFFSETS
            .map { posIn: BlockPos ->
                if (EnchantingTableBlock.isValidBookShelf(level, blockPos, posIn)) {
                    val posTo: BlockPos = blockPos.offset(posIn)
                    level.getBlockState(posTo).getEnchantPowerBonus(level, posTo)
                } else {
                    0f
                }
            }.sum()*/
        // 実際にアウトプットに搬出する
        insertToOutput(1..1, recipe.assemble(input, level.registryAccess()), false)
        // インプットを減らす
        tank.drain(recipe.fluidIngredient, IFluidHandler.FluidAction.EXECUTE)
        inventory.consumeStackInSlot(0, recipe.itemIngredient, false)
        // サウンドを流す
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS)
    }

    override fun getItemHandler(direction: Direction?): IItemHandler? = HTFilteredItemHandler(
        inventory,
        object : HTItemFilter {
            override fun canInsert(handler: IItemHandler, slot: Int, stack: ItemStack): Boolean = slot == 0

            override fun canExtract(handler: IItemHandler, slot: Int, amount: Int): Boolean = slot == 1
        },
    )

    override fun getFluidHandler(direction: Direction?): HTFilteredFluidHandler =
        HTFilteredFluidHandler(listOf(tank), HTFluidFilter.FILL_ONLY)

    override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): HTItemWithFluidToItemMenu =
        HTItemWithFluidToItemMenu(
            RagiumMenuTypes.INFUSER,
            containerId,
            playerInventory,
            blockPos,
            createDefinition(inventory),
        )
}
