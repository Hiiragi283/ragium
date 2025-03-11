package hiiragi283.ragium.api.event

import hiiragi283.ragium.api.block.entity.HTMachineBlockEntity

/**
 * 機械が処理を行う度に呼び出されるイベント
 * [HTMachineBlockEntity.tickOnServer]中にフックされています。
 */
sealed class HTMachineProcessEvent(machine: HTMachineBlockEntity) : HTMachineEvent(machine) {
    /**
     * 機械が処理に成功した場合のイベント
     */
    class Success(machine: HTMachineBlockEntity) : HTMachineProcessEvent(machine)

    /**
     * 機械が処理に失敗した場合のイベント
     * @param throwable 失敗時のエラーメッセージ
     */
    class Failed(machine: HTMachineBlockEntity, val throwable: Throwable) : HTMachineProcessEvent(machine)
}
